package com.shankshock.nicatronTg.Registration.Items;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.SPlayer;
import com.shankshock.nicatronTg.Registration.Items.SilverManager.ShopType;

public class MinecraftItem extends Item {

	ItemStack item;

	public MinecraftItem(ItemType type, String name, ShopType category,
			String description, int cost, ItemStack item) {
		super(type, name, category, true, description, cost);

		this.item = item;
	}

	@Override
	public void runExtraData(ConversationContext context, Registration plugin) {
		Player ply = (Player) context.getForWhom();
		int amount = (Integer) context.getSessionData("count");
		item.setAmount(amount);
		HashMap<Integer, ItemStack> extraItems = ply.getInventory().addItem(item);

		int returnAmount = 0;
		for (ItemStack s : extraItems.values()) {
			returnAmount += s.getAmount();
		}

		if (returnAmount != 0) {
			int returnSilver = (int) (returnAmount * (this.getItemCost() - (this.getItemCost() * (Double) context.getSessionData("discount"))));
			SPlayer sply = plugin.players.get(ply.getName());
			sply.setCurrency(sply.getCurrency() + returnSilver);
			context.getForWhom().sendRawMessage(ChatColor.AQUA + "Shop: Some items you ordered wouldn't fit in your inventory. They have been refunded.");
		}
	}

	@Override
	public void runPurchaseAnnoucement(ConversationContext context,
			Registration plugin) {
		context.getForWhom().sendRawMessage(ChatColor.AQUA + "Purchase complete! Your items have been delivered.");
	}

	public ItemStack getItemStack() {
		return item;
	}

}
