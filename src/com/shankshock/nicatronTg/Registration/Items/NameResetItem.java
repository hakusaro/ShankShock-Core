package com.shankshock.nicatronTg.Registration.Items;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.entity.Player;

import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.SPlayer;
import com.shankshock.nicatronTg.Registration.Items.SilverManager.ShopType;

public class NameResetItem extends Item {

	public NameResetItem(ItemType id, String name, ShopType category,
			String description, int cost) {
		super(id, name, category, false, description, cost);
	}

	@Override
	public void runExtraData(final ConversationContext context, final Registration plugin) {
		final String name = ((Player) context.getForWhom()).getName();
		final SPlayer sply = plugin.players.get(((Player) context.getForWhom())
				.getName());
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				try {
					plugin.sqldb.executeSQL(
							"UPDATE `data` SET `nickname`=? WHERE Username=?", name,
							sply.getPlayer().getName());
					sply.getPlayer().setDisplayName(name);
					plugin.conn.setPlayerListName(sply.getPlayer(), true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void runPurchaseAnnoucement(ConversationContext context,
			Registration plugin) {
		SPlayer sply = plugin.players.get(((Player) context.getForWhom())
				.getName());
		context.getForWhom()
				.sendRawMessage(
						ChatColor.GOLD
								+ "> "
								+ ChatColor.AQUA
								+ "Purchase complete! Your display name has been changed.");
		plugin.getServer().broadcast(
				ChatColor.GOLD + sply.getPlayer().getName()
						+ " is now known as "
						+ sply.getPlayer().getName(),
				Server.BROADCAST_CHANNEL_USERS);
	}

}
