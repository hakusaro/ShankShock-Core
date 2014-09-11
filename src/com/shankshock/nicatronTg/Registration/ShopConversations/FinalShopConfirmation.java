package com.shankshock.nicatronTg.Registration.ShopConversations;

import java.text.NumberFormat;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.conversations.BooleanPrompt;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.SPlayer;
import com.shankshock.nicatronTg.Registration.Items.ChatColorItem;
import com.shankshock.nicatronTg.Registration.Items.ChatTitleItem;
import com.shankshock.nicatronTg.Registration.Items.Item;
import com.shankshock.nicatronTg.Registration.Items.MinecraftItem;

public class FinalShopConfirmation extends BooleanPrompt {

	private Item i;
	private final Registration plugin;
	private boolean isFree;
	double discount;

	public FinalShopConfirmation(Registration instance) {
		plugin = instance;
	}

	@Override
	public String getPromptText(ConversationContext context) {
		i = ((Item) context.getSessionData("item2"));
		if ((Boolean) context.getSessionData("usingdiscount")) {
			discount = (Double) context.getSessionData("discount");
		} else {
			discount = 0.00;
		}

		isFree = (Boolean) context.getSessionData("free");

		if (isFree) {
			return "Final Confirmation: Use already purchased item?";
		}

		if (i instanceof MinecraftItem) {
			return ChatColor.RED
					+ "Final confirmation:"
					+ ChatColor.AQUA
					+ " Pay "
					+ ChatColor.GRAY
					+ NumberFormat
							.getInstance()
							.format((int) ((i.getItemCost() * ((Integer) context
									.getSessionData("count"))) - (i
									.getItemCost()
									* ((Integer) context
											.getSessionData("count")) * discount)))
					+ " silver" + ChatColor.AQUA + " for the item '"
					+ i.getItemName() + "'?";
		}
		return ChatColor.RED
				+ "Final confirmation:"
				+ ChatColor.AQUA
				+ " Pay "
				+ ChatColor.GRAY
				+ NumberFormat
						.getInstance()
						.format((int) ((i.getItemCost() - (i.getItemCost() * discount))))
				+ " silver " + ChatColor.AQUA + "for the item '"
				+ i.getItemName() + "'?";
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context,
			boolean input) {
		if (input) {
			final SPlayer ply = plugin.players.get(((Player) context.getForWhom())
					.getName());
			if (!isFree) {
				if (i instanceof MinecraftItem) {
					int amount = (int) ((i.getItemCost() * ((Integer) context
							.getSessionData("count"))) - (i.getItemCost()
							* ((Integer) context.getSessionData("count")) * discount));
					ply.setCurrency(ply.getCurrency() - amount);
					ply.notifyCurrencyChange(amount, false);
				} else {
					ply.setCurrency((int) (ply.getCurrency() - (i.getItemCost() - (i
							.getItemCost() * discount))));
					ply.notifyCurrencyChange(
							(int) ((i.getItemCost() - (i.getItemCost() * discount))),
							false);
				}
			}
			i.runExtraData(context, plugin);
			i.runPurchaseAnnoucement(context, plugin);
			if (!isFree) {
				plugin.getServer()
						.getLogger()
						.log(Level.INFO,
								"Silver Transaction: "
										+ ply.getPlayer().getName()
										+ " purchased "
										+ ((Item) context
												.getSessionData("item2"))
												.getItemType().name()
										+ " for "
										+ NumberFormat
												.getInstance()
												.format(((Item) context
														.getSessionData("item2"))
														.getItemCost()));
			}
			plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {

				@Override
				public void run() {
					ply.saveCurrency();
					if (i instanceof ChatTitleItem || i instanceof ChatColorItem || i.getItemType().isSavable()) {
						ply.getInventoryStore().addItem(i);
					}
					ply.saveAccount();
				}
				
			});
		}
		return Prompt.END_OF_CONVERSATION;
	}
}
