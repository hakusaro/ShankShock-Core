package com.shankshock.nicatronTg.Registration.ShopConversations;

import java.text.NumberFormat;

import org.bukkit.ChatColor;
import org.bukkit.conversations.BooleanPrompt;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.SPlayer;
import com.shankshock.nicatronTg.Registration.InventoryConversations.ItemList;
import com.shankshock.nicatronTg.Registration.Items.ChatColorItem;
import com.shankshock.nicatronTg.Registration.Items.ChatTitleItem;
import com.shankshock.nicatronTg.Registration.Items.Item;
import com.shankshock.nicatronTg.Registration.Items.SilverManager.ShopType;

public class ItemSellbackConfirmation extends BooleanPrompt {

	private final Registration plugin;
	private int refund;
	private boolean clearChatTitle = false;
	private boolean clearChatColor = false;
	public ItemSellbackConfirmation(Registration instance) {
		plugin = instance;
	}

	@Override
	public String getPromptText(ConversationContext context) {
		Item i = (Item) context.getSessionData("itemobj");
		int cost = i.getItemCost();
		refund = (int) Math.round(cost - (.5 * cost));
		
		context.getForWhom().sendRawMessage(ChatColor.GREEN + "Original Price: " + ChatColor.GRAY + NumberFormat.getInstance().format(i.getItemCost()) + ChatColor.GOLD + " silver.");
		context.getForWhom().sendRawMessage(ChatColor.GREEN + "Refund Amount: " + ChatColor.GRAY + NumberFormat.getInstance().format(refund) + ChatColor.GOLD + " silver.");
		
		SPlayer sply = plugin.players.get(((Player) context.getForWhom()).getName());
		
		if (i instanceof ChatTitleItem) {
			ChatTitleItem chatTitle = (ChatTitleItem) i;
			if (chatTitle.getPrefix().equals(sply.getChatPrefix())) {
				clearChatTitle = true;
			}
		}
		
		if (i instanceof ChatColorItem) {
			ChatColorItem chatColor = (ChatColorItem) i;
			if (chatColor.getChatColor().equals(sply.getChatColor())) {
				clearChatColor = true;
			}
		}
		
		if (i.getItemCategory() == ShopType.NOT_FOR_SALE) {
			context.getForWhom().sendRawMessage(ChatColor.RED + "WARNING: " + ChatColor.YELLOW + "This item is not for sale in any shop. Selling this item back will destroy the item. You may not be able to get another.");
		}
		
		return "Are you sure you want to sell the item back?";
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context,
			boolean input) {
		if (input) {
			final SPlayer ply = plugin.players.get(((Player) context.getForWhom()).getName());
			final Item i = (Item) context.getSessionData("itemobj");
			final boolean clearChatColorCopy = clearChatColor;
			final boolean clearChatTitleCopy = clearChatTitle;
			plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {

				@Override
				public void run() {
					ply.addCurrency(refund, false);
					ply.saveCurrency();
					ply.getInventoryStore().delItem(i);
					if (clearChatColorCopy) {
						ply.setChatColor("");
					}
					
					if (clearChatTitleCopy) {
						ply.setChatPrefix("");
					}
					
					plugin.conn.cacheChatFormat(ply);
					ply.saveAccount();
				}
				
			});
		}
		return new ItemList(plugin);
	}

}
