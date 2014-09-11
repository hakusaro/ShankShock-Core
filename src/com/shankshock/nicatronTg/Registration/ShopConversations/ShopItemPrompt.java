package com.shankshock.nicatronTg.Registration.ShopConversations;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;

import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.SPlayer;
import com.shankshock.nicatronTg.Registration.Items.Item;
import com.shankshock.nicatronTg.Registration.Items.ItemType;
import com.shankshock.nicatronTg.Registration.Items.SilverManager;
import com.shankshock.nicatronTg.Registration.Items.SilverManager.ShopType;

public class ShopItemPrompt extends ValidatingPrompt {

	private final Registration plugin;
	private HashMap<Integer, Item> itemCache;
	private int temp;
	private ArrayList<ItemType> inventory;
	private SPlayer sply;
	private Player ply;

	public ShopItemPrompt(Registration instance) {

		plugin = instance;
	}

	@Override
	public String getPromptText(ConversationContext arg0) {
		arg0.getForWhom().sendRawMessage(
				ChatColor.AQUA + "Shop: " + ChatColor.YELLOW
						+ "Select an item number.");
		arg0.getForWhom().sendRawMessage(
				ChatColor.AQUA + "Shop: " + ChatColor.AQUA + "Format: "
						+ ChatColor.AQUA + ChatColor.LIGHT_PURPLE + "[NAME] "
						+ ChatColor.AQUA + "-" + ChatColor.GRAY + " [COST]");

		double discount;
		boolean usingDiscount = (Boolean) arg0.getSessionData("usingdiscount");

		ply = (Player) arg0.getForWhom();
		sply = plugin.players.get(ply.getName());
		inventory = sply.getInventoryStore().getInventory();
		if (usingDiscount) {
			discount = (Double) arg0.getSessionData("discount");
		} else {
			discount = 0.00;
		}
		itemCache = new HashMap<Integer, Item>();

		temp = 1; // Used for making the item numbers start at 1, rather than 0.

		for (Item i : SilverManager.items) {
			// Filter out all non-Pure items in the category if the player is in
			// Pure
			if (i.getPureDisabled() && ((Boolean) arg0.getSessionData("pure"))) {
				continue;
			}

			// Add all items that are still valid to a cache to use later
			if (i.getItemCategory() == (ShopType) arg0
					.getSessionData("category")) {
				if (temp == 1) {
					itemCache.put(temp, i);
					temp++;
				} else {
					itemCache.put(temp, i);
					temp++;
				}
			}
		}

		for (int k = 0; k < temp; k++) {
			Item i = itemCache.get(k);

			if (i == null) {
				continue;
			}

			if (inventory.contains(i.getItemType())) {
				arg0.getForWhom().sendRawMessage(
						ChatColor.AQUA + "Shop: " + ChatColor.GOLD + k
								+ ChatColor.AQUA + ". "
								+ ChatColor.LIGHT_PURPLE + i.getItemName()
								+ ChatColor.AQUA + " - " + ChatColor.GREEN
								+ "Owned.");
				continue;
			}

			if (usingDiscount) {
				arg0.getForWhom().sendRawMessage(
						ChatColor.AQUA
								+ "Shop: "
								+ ChatColor.GOLD
								+ k
								+ ChatColor.AQUA
								+ ". "
								+ ChatColor.LIGHT_PURPLE
								+ i.getItemName()
								+ ChatColor.AQUA
								+ " - "
								+ ChatColor.GRAY
								+ NumberFormat.getInstance()
										.format((i.getItemCost() - (i
												.getItemCost() * discount)))
								+ " ("
								+ NumberFormat.getInstance().format(
										i.getItemCost()) + ")");
			} else {
				arg0.getForWhom().sendRawMessage(
						ChatColor.AQUA
								+ "Shop: "
								+ ChatColor.GOLD
								+ k
								+ ChatColor.AQUA
								+ ". "
								+ ChatColor.AQUA
								+ " - "
								+ ChatColor.LIGHT_PURPLE
								+ i.getItemName()
								+ ChatColor.AQUA
								+ " - "
								+ ChatColor.GRAY
								+ NumberFormat.getInstance().format(
										(i.getItemCost())));
			}
		}
		return "Which item number would you like to see? Type back to go back to the shop picker.";
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext arg0, String arg1) {
		if (arg1.equals("back")) {
			return new ShopCategoryChooser(plugin);
		}
		Item i = itemCache.get(Integer.parseInt(arg1));
		arg0.setSessionData("item", itemCache.get(Integer.parseInt(arg1))
				.getItemType());

		if (inventory.contains(i.getItemType())) {
			arg0.setSessionData("free", true);
		} else {
			arg0.setSessionData("free", false);
		}

		return new ItemInfoPrompt(plugin);
	}

	@Override
	protected boolean isInputValid(ConversationContext arg0, String arg1) {
		if (arg1.equals("back")) {
			return true;
		}

		for (int k = 1; k < temp; k++) {
			Item i = itemCache.get(k);

			// Item exists?
			if (i == null) {
				continue;
			}

			// Player is attempting to buy an item that exists only outside of
			// Pure but is in Pure?
			if (i.getPureDisabled() && ((Boolean) arg0.getSessionData("pure"))) {
				continue;
			}

			try {
				// Is the item actually valid?
				if (k == Integer.parseInt(arg1)) {
					// Does the player already have a Pure invite and are they
					// trying to buy it again?
					if (i.getItemType() == ItemType.PURE_INVITATION
							&& sply.hasPureAccess()) {
						return false;
					}
					return true;
				}
			} catch (NumberFormatException e) {
				return false;
			}
		}
		return false;
	}

}
