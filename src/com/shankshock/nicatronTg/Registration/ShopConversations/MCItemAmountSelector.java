package com.shankshock.nicatronTg.Registration.ShopConversations;

import java.text.NumberFormat;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.SPlayer;
import com.shankshock.nicatronTg.Registration.Items.Item;

public class MCItemAmountSelector extends NumericPrompt {

	Item item;
	SPlayer sply;
	private final Registration plugin;

	public MCItemAmountSelector(Registration instance) {
		plugin = instance;
	}

	@Override
	public String getPromptText(ConversationContext arg0) {
		sply = plugin.players.get(((Player) arg0.getForWhom()).getName());
		item = (Item) arg0.getSessionData("item2");
		double discount = (Double) arg0.getSessionData("discount");

		arg0.getForWhom().sendRawMessage(
				ChatColor.AQUA
						+ "Shop: You are purchasing at "
						+ ChatColor.GRAY
						+ NumberFormat.getInstance()
								.format((int) (item.getItemCost() - (item
										.getItemCost() * discount)))
						+ " silver " + ChatColor.AQUA + "per unit.");
		arg0.getForWhom().sendRawMessage(
				ChatColor.AQUA + "Shop: You currently have " + ChatColor.GRAY
						+ NumberFormat.getInstance().format(sply.getCurrency())
						+ " silver" + ChatColor.AQUA + ".");
		return "Enter an amount between 1 and 64. Your price will be calculated based on your choice.";
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext arg0, Number arg1) {
		double discount = (Double) arg0.getSessionData("discount");
		if (arg1.intValue() > 64 || arg1.intValue() < 1) {
			return new MCItemAmountSelector(plugin);
		}

		if (arg1.intValue()
				* (item.getItemCost() - (item.getItemCost() * discount)) > sply.getCurrency()) {
			return new MCItemAmountSelector(plugin);
		}
		arg0.setSessionData("count", arg1.intValue());
		return new FinalShopConfirmation(plugin);
	}

}
