package com.shankshock.nicatronTg.Registration.Listeners;

import com.shankshock.Redis.WorldConfig;
import com.shankshock.nicatronTg.Registration.Awards.AwardManager.AwardType;
import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.SPlayer;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.logging.Level;

public class BlockListener implements Listener {
	private final Registration plugin;

	public BlockListener(final Registration plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockBreak(BlockBreakEvent event) {
		SPlayer ply = plugin.players.get(event.getPlayer().getName());
		Block block = event.getBlock();
		Material blockType = block.getType();
		WorldConfig config = plugin.worlds.get(event.getBlock().getWorld()
				.getName());

		if (event.isCancelled()) {
			return;
		}
		
		//Tekkit player overrides
		if (event.getPlayer().getName().equalsIgnoreCase("[redpower]")
				|| event.getPlayer().getName().equalsIgnoreCase("[buildcraft]")
				|| event.getPlayer().getName().equalsIgnoreCase("[forestry]")) {
			event.setCancelled(false);
			return;
		}

		if (ply == null) {
			event.setCancelled(true);
			return;
		}

		if (event.isCancelled()) {
			return;
		}

		if (!ply.isActivated() || !ply.isRegistered()) {
			if (ply.isRegistered()) {
				ply.sendMessage(ChatColor.RED
						+ "Please activate your account to edit the world.");
			} else {
				ply.kickPlayer("Database Error: No registration ticket");
			}
			event.setCancelled(true);
			return;
		}

		if (plugin.zoneManager.checkForZoneGrief(block, event.getPlayer(), "B")) {
			event.setCancelled(true);
			return;
		}

		if (!config.canBuild()) {
			event.setCancelled(true);
			return;
		}

		if (!ply.getPlayer().hasPermission("shankshock.bedrock") && blockType == Material.BEDROCK) {
			event.setCancelled(true);
			return;
		}
		
		if (config.canEarnSilver()) {
			if (ply.getPlayer().getGameMode() != GameMode.SURVIVAL)
				return;

			int currencyGain = 0;

			if (plugin.silverManager.blockPayouts.containsKey(blockType)) {
				currencyGain = plugin.silverManager.blockPayouts.get(blockType);
			}

			if (plugin.silverManager.materialAwardsPrimary
					.containsKey(blockType)) {
				plugin.awardManager.updateAward(
						plugin.silverManager.materialAwardsPrimary
								.get(blockType), 1, ply.getPlayer());
			}

			if (plugin.silverManager.materialAwardsSecondary
					.containsKey(blockType)) {
				plugin.awardManager.updateAward(
						plugin.silverManager.materialAwardsSecondary
								.get(blockType), 1, ply.getPlayer());
			}

			if (ply.getChatPrefix().contains("Mining Corp")) {
				currencyGain = (int) (currencyGain + (.1 * currencyGain));
			}

			if (currencyGain > 0) {
				ply.addCurrency(currencyGain);
			}

			ply.tickBlocksDestroyed();
		}

	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockPlace(BlockPlaceEvent event) {
		SPlayer ply = plugin.players.get(event.getPlayer().getName());
		Block block = event.getBlock();
		Material blockType = block.getType();
		WorldConfig config = plugin.worlds.get(event.getBlock().getWorld()
				.getName());
		
		if (event.isCancelled()) {
			return;
		}

		if (event.getPlayer().getName().equalsIgnoreCase("[redpower]")
				|| event.getPlayer().getName().equalsIgnoreCase("[buildcraft]")
				|| event.getPlayer().getName().equalsIgnoreCase("[forestry]")) {
			event.setCancelled(false);
			return;
		}

		if (ply == null) {
			event.setCancelled(true);
			return;
		}

		if (!ply.isActivated() || !ply.isRegistered()) {
			if (ply.isRegistered()) {
				ply.sendMessage(ChatColor.RED
						+ "Please activate your account to edit the world.");
			} else {
				ply.kickPlayer("Database Error: No registration ticket");
			}
			event.setCancelled(true);
		}
		if (!ply.isRegistered()) {
			ply.sendMessage(ChatColor.RED + "Error:" + ChatColor.AQUA
					+ " Please type" + ChatColor.GREEN + " /register"
					+ ChatColor.AQUA
					+ " and activate or you will not be able to build.");
			event.setCancelled(true);
		} else if (plugin.zoneManager.checkForZoneGrief(event.getBlock(),
				event.getPlayer(), "P")) {
			event.setCancelled(true);
		}

		if (!config.canBuild()) {
			event.setCancelled(true);
			return;
		}
		
		if (!ply.getPlayer().hasPermission("shankshock.bedrock") && blockType == Material.BEDROCK) {
			event.setCancelled(true);
			return;
		}
        if (ply.getPlayer().getGameMode() != GameMode.SURVIVAL) {

            if (ply.getPlayer().getGameMode() == GameMode.CREATIVE) {
                plugin.awardManager.updateAward(AwardType.ARTIST, 1, event.getPlayer());
            }

            return;
        }
		if (config.canEarnSilver()) {

			int currencyLoss = 0;
			if (plugin.silverManager.blockPayouts.containsKey(blockType)) {
				currencyLoss = plugin.silverManager.blockPayouts.get(blockType);
			}

			if (block.getWorld().getName().contains("stock")) {
				currencyLoss = currencyLoss * 2;
			}

			if (ply.getChatPrefix().contains("Mining Corp")) {
				currencyLoss = (int) (currencyLoss + (.1 * currencyLoss));
			}

			if ((ply.getCurrency() - currencyLoss) < 0) {
				ply.sendError("Doing that would create something from nothing.");
				event.setCancelled(true);
				return;
			}

			if (currencyLoss > 0) {
				plugin.getServer()
						.getLogger()
						.log(Level.INFO,
								"Silver Transaction: "
										+ ply.getPlayer().getName()
										+ " lost currency for placing "
										+ event.getBlock().getType() + ".");
				ply.delCurrency(currencyLoss);
			}
		}

	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockForm(BlockFormEvent event) {
		if (event.getNewState().getType() == Material.ICE
				|| event.getNewState().getType() == Material.SNOW) {
			if (plugin.zoneManager.checkBlockIsInAntiGriefZone(event.getBlock()
					.getLocation())) {
				event.setCancelled(true);
			}
		}
	}
}
