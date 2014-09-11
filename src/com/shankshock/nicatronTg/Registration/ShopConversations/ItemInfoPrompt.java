package com.shankshock.nicatronTg.Registration.ShopConversations;

import com.shankshock.nicatronTg.Registration.Items.*;
import com.shankshock.nicatronTg.Registration.Items.SilverManager.ShopType;
import com.shankshock.nicatronTg.Registration.Misc.ChatColorPair;
import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.SPlayer;
import org.bukkit.ChatColor;
import org.bukkit.conversations.BooleanPrompt;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

import java.text.NumberFormat;

public class ItemInfoPrompt extends BooleanPrompt {

	boolean canPurchase;
	boolean isFree;
	Item item;
	private final Registration plugin;

	public ItemInfoPrompt(Registration instance) {
		plugin = instance;
	}

	@Override
	public String getPromptText(ConversationContext context) {

		ItemType itemChoice = (ItemType) context.getSessionData("item");
		item = null;
		for (Item i : SilverManager.items) {
			if (itemChoice == i.getItemType()
					&& i.getItemCategory() == ((ShopType) context
							.getSessionData("category"))) {
				item = i;
			}
		}

		isFree = (Boolean) context.getSessionData("free");

		double discount;

		if ((Boolean) context.getSessionData("usingdiscount")) {
			discount = (Double) context.getSessionData("discount");
		} else {
			discount = 0.00;
		}

		context.getForWhom().sendRawMessage(
				ChatColor.AQUA + "Shop: " + ChatColor.GOLD + "Item: "
						+ item.getItemName());
		context.getForWhom().sendRawMessage(
				ChatColor.AQUA + "Shop: " + ChatColor.GOLD + "Description: "
						+ item.getItemDescription());
		if (isFree) {
			context.getForWhom().sendRawMessage(
					ChatColor.AQUA + "Shop: " + ChatColor.GREEN
							+ "Previously owned.");
		} else {
			context.getForWhom().sendRawMessage(
					ChatColor.AQUA
							+ "Shop: "
							+ ChatColor.GOLD
							+ "Cost: "
							+ ChatColor.GRAY
							+ NumberFormat.getInstance()
									.format((item.getItemCost() - (item
											.getItemCost() * discount)))
							+ " silver.");
		}

		if (item instanceof ChatColorItem) {
			ChatColorItem item2 = (ChatColorItem) item;
			String chatColor = "";
			Player ply = (Player) context.getForWhom();
			for (ChatColorPair c : plugin.conn.chatColors) {
				if (ply.hasPermission(c.getPermission())) {
					chatColor = c.getColor();
				}
			}

			context.getForWhom().sendRawMessage(
					ChatColor.AQUA + "Shop: " + ChatColor.GOLD
							+ "A preview of your new chat color is below:");
			context.getForWhom().sendRawMessage(
					chatColor + ply.getDisplayName() + ChatColor.COLOR_CHAR + "f:"
							+ item2.getChatColor()
							+ " This is a test of my new chat color.");
		}

		if (item instanceof ChatTitleItem) {
			ChatTitleItem item2 = (ChatTitleItem) item;
			String chatColor = "";
			Player ply = (Player) context.getForWhom();
			for (ChatColorPair c : plugin.conn.chatColors) {
				if (ply.hasPermission(c.getPermission())) {
					chatColor = c.getColor();
				}
			}

			context.getForWhom().sendRawMessage(
					ChatColor.AQUA + "Shop: " + ChatColor.GOLD
							+ "A preview of your new title is below:");
			context.getForWhom().sendRawMessage(
					"[" + item2.getPrefix() + ChatColor.COLOR_CHAR + "f] " + chatColor
							+ ply.getDisplayName()
							+ ChatColor.COLOR_CHAR + "f: This is a test of my new title.");
		}

		SPlayer sply = plugin.players.get(((Player) context.getForWhom())
				.getName());

		if (isFree) {
			return "Use already purchased item?";
		}

		if (sply.getCurrency() >= (item.getItemCost() - (item.getItemCost() * discount))) {
			canPurchase = true;
			return "Buy it?";
		} else {
			canPurchase = false;
			return "You don't have enough" + ChatColor.GRAY + " silver "
					+ ChatColor.AQUA + "to buy this. Return to shop?";
		}
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext arg0, boolean arg1) {
		if ((canPurchase || isFree) && arg1) {
			arg0.setSessionData("item2", item);

			SPlayer sply = plugin.players.get(((Player) arg0.getForWhom())
					.getName());

			if (item instanceof PureInvitationItem && sply.hasPureAccess()) {
				return new ShopCategoryChooser(plugin);
			}

			if (item instanceof NameChangeItem) {
				return new NameChangePrompt(plugin);
			} else if (item instanceof MOTDChangeItem) {
				return new MOTDChangePrompt(plugin);
			} else if (item instanceof WeatherChangeItem) {
				return new WeatherChangePrompt(plugin);
			} else if (item instanceof AssassinationItem) {
				return new PlayerSelector(plugin);
			}
			if (item instanceof MinecraftItem) {
				return new MCItemAmountSelector(plugin);
			}
			return new FinalShopConfirmation(plugin);
		}

		if (canPurchase && !arg1) {
			return new ShopCategoryChooser(plugin);
		}

		if (!canPurchase && arg1) {
			return new ShopCategoryChooser(plugin);
		}

		if (!canPurchase && !arg1) {
			return Prompt.END_OF_CONVERSATION;
		}

		return null;
	}

}
