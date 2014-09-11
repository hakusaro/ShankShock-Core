package com.shankshock.nicatronTg.Registration.Items;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.entity.Player;

import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.SPlayer;
import com.shankshock.nicatronTg.Registration.Items.SilverManager.ShopType;

public class ChatTitleItem extends Item {

	String prefix;

	public ChatTitleItem(ItemType type, String name, ShopType category,
			String description, int cost, String prefix) {
		super(type, name, category, false, description, cost);
		this.prefix = prefix;
	}

	@Override
	public void runExtraData(final ConversationContext context,
			final Registration plugin) {
		final SPlayer sply = plugin.players.get(((Player) context.getForWhom()).getName());
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			@Override
			public void run() {
				sply.setChatPrefix(prefix);
				plugin.conn.cacheChatFormat(sply);
			}
		});

		if (prefix.contains("Spirit")) {
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				@Override
				public void run() {
					for (Player pl : plugin.getServer().getOnlinePlayers()) {
						pl.hidePlayer((Player) context.getForWhom());
					}
				}
			});
		} else {
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				@Override
				public void run() {
					for (Player pl : plugin.getServer().getOnlinePlayers()) {
						pl.showPlayer((Player) context.getForWhom());
					}
				}
			});
		}
	}

	@Override
	public void runPurchaseAnnoucement(ConversationContext context,
			Registration plugin) {
		boolean isFree = (Boolean) context.getSessionData("free");
		if (isFree) {
			context.getForWhom().sendRawMessage(ChatColor.AQUA + "Your chat title has been changed.");
		} else {
			context.getForWhom().sendRawMessage(ChatColor.AQUA + "Purchase complete! Your chat title has been changed.");
		}
	}

	public String getPrefix() {
		return prefix;
	}

}
