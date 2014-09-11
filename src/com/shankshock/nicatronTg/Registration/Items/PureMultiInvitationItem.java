package com.shankshock.nicatronTg.Registration.Items;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.entity.Player;

import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.SPlayer;
import com.shankshock.nicatronTg.Registration.Items.SilverManager.ShopType;

public class PureMultiInvitationItem extends Item {

	public PureMultiInvitationItem(ItemType type, String name,
			ShopType category, String description, int cost) {
		super(type, name, category, false, description, cost);
	}

	@Override
	public void runExtraData(ConversationContext context, final Registration plugin) {
		final SPlayer sply = plugin.players.get(((Player) context.getForWhom())
				.getName());
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				try {
					if (sply.hasPureAccess()) {
						plugin.sqldb
								.executeSQL(
										"UPDATE `pure-invites` SET `invites` = `invites` + 4 WHERE `Username`=?",
										sply.getPlayer().getName());
						return;
					}
					plugin.sqldb.executeSQL("INSERT INTO `pure-invites` VALUES(?, ?)",
							sply.getPlayer().getName(), 3);
					plugin.pureEngine.cacheDatabase();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void runPurchaseAnnoucement(ConversationContext context,
			Registration plugin) {
		context.getForWhom()
				.sendRawMessage(
						ChatColor.GOLD
								+ "You now have invitations to Pure BlockShock. Type /pure to learn more and to invite friends.");
	}

}
