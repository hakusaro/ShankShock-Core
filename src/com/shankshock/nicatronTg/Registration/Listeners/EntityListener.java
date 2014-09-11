package com.shankshock.nicatronTg.Registration.Listeners;

import com.shankshock.nicatronTg.Registration.Awards.AwardManager.AwardType;
import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.SPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.logging.Level;

public class EntityListener implements Listener {
	private final Registration plugin;

	public EntityListener(final Registration plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		/**
		 * Block spawning monsters inside shadow zones
		 */
		if (plugin.zoneManager.checkBlockIsInAntiGriefZone(event.getLocation())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityDeath(EntityDeathEvent event) {
		Player killer = event.getEntity().getKiller();

		if (killer == null) {
			return;
		}

		SPlayer sply = plugin.players.get(killer.getName());

		int currencyGain = 0;
		if (plugin.silverManager.mobPayouts.containsKey(event.getEntityType())) {
			currencyGain = plugin.silverManager.mobPayouts.get(event.getEntityType());
		}

		if (plugin.silverManager.mobAwardsPrimary.containsKey(event.getEntityType())) {
			plugin.awardManager.updateAward(plugin.silverManager.mobAwardsPrimary.get(event.getEntityType()), 1, killer);

			if (killer.getInventory().getItemInHand().getType() == Material.BOW) {
				plugin.awardManager.updateAward(AwardType.KATNISS_EVERDEEN, 1, killer);
			}

			if (killer.getInventory().getItemInHand().getAmount() == 0) {
				plugin.awardManager.updateAward(AwardType.NINJA, 1, killer);
			}

			if (killer.getInventory().getItemInHand().getType() == Material.IRON_AXE) {
				plugin.awardManager.updateAward(AwardType.EXECUTIONER, 1, killer);
			}

			if (killer.getInventory().getItemInHand().getType() == Material.IRON_SWORD || killer.getInventory().getItemInHand().getType() == Material.DIAMOND_SWORD || killer.getInventory().getItemInHand().getType() == Material.STONE_SWORD) {
				plugin.awardManager.updateAward(AwardType.SWORDSMAN, 1, killer);
			}

		}

		if (plugin.silverManager.mobAwardsSecondary.containsKey(event.getEntityType())) {
			plugin.awardManager.updateAward(plugin.silverManager.mobAwardsSecondary.get(event.getEntityType()), 1, killer);
		}

		if (event.getEntityType() == EntityType.WOLF) {
			Wolf w = (Wolf) event.getEntity();

			if (w.isAngry()) {
				plugin.awardManager.updateAward(AwardType.LYCAN_HUNTER, 1, killer);
			}
		}

		if (event.getEntityType() == EntityType.VILLAGER) {
			plugin.awardManager.updateAward(AwardType.MARAUDER, 1, killer);
		}

		if (event.getEntityType() == EntityType.SKELETON) {
			Skeleton skeleton = (Skeleton) event.getEntity();

			if (skeleton.getSkeletonType() == SkeletonType.WITHER) {
				plugin.awardManager.updateAward(AwardType.THE_DAMNED, 1, killer);
			}
		}

		if (event.getEntityType() == EntityType.ENDER_DRAGON) {
			for (Player p : plugin.getServer().getOnlinePlayers()) {
				if (p.getWorld().getName().contains("end")) {
					plugin.awardManager.addAward(AwardType.VOID_KING, p);
					plugin.awardManager.updateAward(AwardType.DARK_ONE, 1, p);
				}
			}
		}

		if (currencyGain == 0) {
			return;
		}

		plugin.getServer().getLogger().log(Level.INFO, "Silver Transaction: " + sply.getPlayer().getName() + " gained currency for killing " + event.getEntityType().getName() + ".");

		sply.addCurrency(currencyGain);
		sply.tickMobsDestroyed();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntity().getWorld().getName().contains("stock")) {
			if (event instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent e2 = (EntityDamageByEntityEvent) event;

				if ((e2.getDamager() instanceof Player && e2.getEntity() instanceof Player)) {
					if (event.getEntity().getWorld().getTime() < 15000) {
						event.setCancelled(true);
					}
				}

				if ((e2.getDamager() instanceof Projectile && e2.getEntity() instanceof Player)) {

					Projectile p = (Projectile) e2.getDamager();
					if (p.getShooter() instanceof Player) {

						if (event.getEntity().getWorld().getTime() < 15000) {
							event.setCancelled(true);
						}
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerInteract(PlayerInteractEvent event) {

		if (!(event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			return;
		}

		if (!(event.getClickedBlock().getType() == Material.STONE_BUTTON || event.getClickedBlock().getType() == Material.WOOD_BUTTON)) {
			return;
		}

		Location vec1 = event.getClickedBlock().getLocation();
		final String loc = vec1.getX() + "," + vec1.getY() + "," + vec1.getZ();
		final String worldName = event.getClickedBlock().getWorld().getName();
		final String playerName = event.getPlayer().getName();

		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			public void run() {
				final String usetype = plugin.redisDatabase.getRedis().get("mc:server:" + plugin.worlds.get(worldName).getUniqueid() + ":" + worldName + ":block:" + loc + ":onuse");
				final String zone = plugin.redisDatabase.getRedis().get("mc:server:" + plugin.worlds.get(worldName).getUniqueid() + ":" + worldName + ":block:" + loc + ":zone");
				final String blocktype = plugin.redisDatabase.getRedis().get("mc:server:" + plugin.worlds.get(worldName).getUniqueid() + ":" + worldName + ":block:" + loc + ":typeid");
				final String blockData = plugin.redisDatabase.getRedis().get("mc:server:" + plugin.worlds.get(worldName).getUniqueid() + ":" + worldName + ":block" + loc + ":data");

				if (usetype == null) {
					return;
				}

				plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
					public void run() {
						Player ply = plugin.getServer().getPlayer(playerName);
						ply.sendMessage(ChatColor.AQUA + "Spleef arena reset!");
						if (blockData == null) {
							plugin.zoneManager.setZone(plugin.zoneManager.getZoneByName(zone), Integer.parseInt(blocktype));
						} else {
							plugin.zoneManager.setZone(plugin.zoneManager.getZoneByName(zone), Integer.parseInt(blocktype), Byte.parseByte(blockData));
						}
					}
				});
			}
		});
	}

}
