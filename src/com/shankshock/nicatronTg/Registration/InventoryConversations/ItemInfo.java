package com.shankshock.nicatronTg.Registration.InventoryConversations;

import com.shankshock.nicatronTg.Registration.Items.ChatColorItem;
import com.shankshock.nicatronTg.Registration.Items.ChatTitleItem;
import com.shankshock.nicatronTg.Registration.Items.Item;
import com.shankshock.nicatronTg.Registration.Misc.ChatColorPair;
import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.ShopConversations.ItemSellbackConfirmation;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;

public class ItemInfo extends ValidatingPrompt {

	private final Registration plugin;
	Item item;

	public ItemInfo(Registration instance) {
		this.plugin = instance;
	}

	@Override
	public String getPromptText(ConversationContext arg0) {
		item = (Item) arg0.getSessionData("item");
		arg0.getForWhom().sendRawMessage(ChatColor.AQUA + "Inventory: " + ChatColor.GOLD + "Item: " + ChatColor.AQUA + item.getItemName());
		arg0.getForWhom().sendRawMessage(ChatColor.AQUA + "Inventory: " + ChatColor.GOLD + "Description: " + ChatColor.AQUA + item.getItemDescription());

		if (item instanceof ChatColorItem) {
			ChatColorItem item2 = (ChatColorItem) item;
			String chatColor = "";
			Player ply = (Player) arg0.getForWhom();
			for (ChatColorPair c : plugin.conn.chatColors) {
				if (ply.hasPermission(c.getPermission())) {
					chatColor = c.getColor();
				}
			}

			arg0.getForWhom().sendRawMessage(ChatColor.AQUA + "Inventory: " + ChatColor.GOLD + "A preview of your new chat color is below:");
			arg0.getForWhom().sendRawMessage(chatColor + ply.getDisplayName() + ChatColor.COLOR_CHAR + "f:" + item2.getChatColor() + " This is a test of my new chat color.");
		}

		if (item instanceof ChatTitleItem) {
			ChatTitleItem item2 = (ChatTitleItem) item;
			String chatColor = "";
			Player ply = (Player) arg0.getForWhom();
			for (ChatColorPair c : plugin.conn.chatColors) {
				if (ply.hasPermission(c.getPermission())) {
					chatColor = c.getColor();
				}
			}

			arg0.getForWhom().sendRawMessage(ChatColor.AQUA + "Shop: " + ChatColor.GOLD + "A preview of your new title is below:");
			arg0.getForWhom().sendRawMessage("[" + item2.getPrefix() + ChatColor.COLOR_CHAR + "f] " + chatColor + ply.getDisplayName() + ChatColor.COLOR_CHAR + "f: This is a test of my new title.");
		}

		return "Use or sell the item? Reply with your action ('use' or 'sell') or 'back' to go back.";
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context,
			String choice) {
		if (choice.equalsIgnoreCase("use")) {
			context.setSessionData("free", true);
			item.runExtraData(context, plugin);
			item.runPurchaseAnnoucement(context, plugin);
		} else if (choice.equalsIgnoreCase("sell")) {
			context.setSessionData("itemobj", item);
			return new ItemSellbackConfirmation(plugin);
		} else if (choice.equalsIgnoreCase("back")) {
			return new ItemList(plugin);
		}
		return Prompt.END_OF_CONVERSATION;
	}

	@Override
	protected boolean isInputValid(ConversationContext context, String choice) {

		if (choice.equalsIgnoreCase("back") || choice.equalsIgnoreCase("sell") || choice.equalsIgnoreCase("use")) {
			return true;
		}

		return false;
	}
}
