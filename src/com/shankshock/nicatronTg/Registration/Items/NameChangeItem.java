package com.shankshock.nicatronTg.Registration.Items;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.entity.Player;

import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.SPlayer;
import com.shankshock.nicatronTg.Registration.Items.SilverManager.ShopType;

public class NameChangeItem extends Item {

	public NameChangeItem(ItemType type, String name, ShopType category,
			boolean pureDisabled, String description, int cost) {
		super(type, name, category, pureDisabled, description, cost);
	}

	@Override
	public void runExtraData(ConversationContext context,
			final Registration plugin) {
		final String name = (String) context.getSessionData("name");
		final SPlayer sply = plugin.players.get(((Player) context.getForWhom()).getName());
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				try {
					plugin.sqldb.executeSQL("UPDATE `data` SET `nickname`=? WHERE Username=?", name, sply.getPlayer().getName());
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
		SPlayer sply = plugin.players.get(((Player) context.getForWhom()).getName());
		context.getForWhom().sendRawMessage(ChatColor.GOLD + "> " + ChatColor.AQUA + "Purchase complete! Your display name has been changed.");
		plugin.getServer().broadcast(ChatColor.GOLD + sply.getPlayer().getName() + " changed their nickname.", Server.BROADCAST_CHANNEL_USERS);
	}
}
