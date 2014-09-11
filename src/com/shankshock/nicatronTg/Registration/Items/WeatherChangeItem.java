package com.shankshock.nicatronTg.Registration.Items;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.entity.Player;

import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.SPlayer;
import com.shankshock.nicatronTg.Registration.Items.SilverManager.ShopType;

public class WeatherChangeItem extends Item {

	public WeatherChangeItem(ItemType type, String name, ShopType category,
			String description, int cost) {
		super(type, name, category, true, description, cost);
	}

	@Override
	public void runExtraData(ConversationContext context, Registration plugin) {
		SPlayer sply = plugin.players.get(((Player) context.getForWhom())
				.getName());
		Player ply = sply.getPlayer();
		boolean enableStorm = (Boolean) context.getSessionData("storm");
		ply.getWorld().setStorm(enableStorm);
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
								+ "Purchase complete! The weather will change shortly.");
		plugin.getServer().broadcast(
				ChatColor.GOLD + sply.getPlayer().getName()
						+ " just changed the weather!",
				Server.BROADCAST_CHANNEL_USERS);
	}

}
