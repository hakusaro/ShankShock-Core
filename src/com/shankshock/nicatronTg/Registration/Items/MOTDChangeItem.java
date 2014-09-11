package com.shankshock.nicatronTg.Registration.Items;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;

import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.Items.SilverManager.ShopType;

public class MOTDChangeItem extends Item {

	public MOTDChangeItem(ItemType type, String name, ShopType category,
			boolean pureDisabled, String description, int cost) {
		super(type, name, category, pureDisabled, description, cost);
	}

	@Override
	public void runExtraData(ConversationContext context, final Registration plugin) {
		final String motd = (String) context.getSessionData("motd");
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

			@Override
			public void run() {
				plugin.globalConfig.setValue("motd", motd);
			}
			
		});
		
	}

	@Override
	public void runPurchaseAnnoucement(ConversationContext context,
			final Registration plugin) {
		context.getForWhom()
				.sendRawMessage(
						ChatColor.GOLD
								+ "> "
								+ ChatColor.AQUA
								+ "Purchase complete! The server MOTD has been changed.");
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

			@Override
			public void run() {
				plugin.redisDatabase.sendSettingsUpdate("motd");
			}
			
		});
	}

}
