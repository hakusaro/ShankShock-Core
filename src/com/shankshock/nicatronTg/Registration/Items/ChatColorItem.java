package com.shankshock.nicatronTg.Registration.Items;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.entity.Player;

import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.SPlayer;
import com.shankshock.nicatronTg.Registration.Items.SilverManager.ShopType;

public class ChatColorItem extends Item {

	private String color = "";

	public ChatColorItem(ItemType type, String name, ShopType category,
			String description, int cost, String color) {
		super(type, name, category, false, description, cost);
		this.color = color;
	}

	@Override
	public void runExtraData(ConversationContext context,
			final Registration plugin) {
		final SPlayer sply = plugin.players.get(((Player) context.getForWhom()).getName());
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				sply.setChatColor(color);
			}
		});
	}

	@Override
	public void runPurchaseAnnoucement(ConversationContext context,
			Registration plugin) {
		boolean isFree = (Boolean) context.getSessionData("free");
		if (isFree) {
			context.getForWhom().sendRawMessage(ChatColor.AQUA + "Your chat color has been changed.");
		} else {
			context.getForWhom().sendRawMessage(ChatColor.AQUA + "Purchase complete! Your chat color has been changed.");
		}
	}

	public String getChatColor() {
		return color;
	}

}
