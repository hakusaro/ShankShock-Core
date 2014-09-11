package com.shankshock.nicatronTg.Registration.InventoryConversations;

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

public class ItemList extends ValidatingPrompt {

	private final Registration plugin;
	HashMap<Integer, Item> items;

	public ItemList(Registration instance) {
		this.plugin = instance;
	}

	@Override
	public String getPromptText(ConversationContext arg0) {
		items = new HashMap<Integer, Item>();
		Player ply = (Player) arg0.getForWhom();
		SPlayer sply = plugin.players.get(ply.getName());

		ArrayList<Item> alreadyAdded = new ArrayList<Item>();
		int temp = 1;

		arg0.getForWhom().sendRawMessage(
				ChatColor.AQUA + "Inventory: " + ChatColor.GOLD
						+ "Your Inventory:");

		for (Item i : SilverManager.items) {
			for (ItemType t : sply.getInventoryStore().getInventory()) {
				if (i.getItemType() == t && !alreadyAdded.contains(i)) {
					if (temp == 1) {
						items.put(temp, i);
						temp++;
					} else {
						items.put(temp, i);
						temp++;
					}
					alreadyAdded.add(i);
				}
			}
		}

		for (int k = 0; k < temp; k++) {
			Item i = items.get(k);

			if (i == null) {
				continue;
			}
			arg0.getForWhom().sendRawMessage(
					ChatColor.AQUA + "Inventory: " + ChatColor.GOLD + k
							+ ChatColor.AQUA + ". " + ChatColor.LIGHT_PURPLE
							+ i.getItemName());
		}
		return "Which item number would you like to see?";
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext arg0, String arg1) {

		if (arg1.toLowerCase().equals("back")) {
			return new InventoryStart(plugin);
		}

		int i = Integer.parseInt(arg1);
		Item choice = items.get(i);
		arg0.setSessionData("item", choice);
		return new ItemInfo(plugin);
	}

	@Override
	protected boolean isInputValid(ConversationContext arg0, String arg1) {
		if (arg1.toLowerCase().equals("back")) {
			return true;
		}
		try {
			int i = Integer.parseInt(arg1);
			if (items.get(i) != null) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

}
