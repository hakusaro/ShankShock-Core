package com.shankshock.nicatronTg.Registration.Items;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.SPlayer;
import com.shankshock.nicatronTg.Registration.Items.SilverManager.ShopType;

public class MinecraftKitItem extends Item {

	ItemStack[] item;

	public MinecraftKitItem(ItemType type, String name, ShopType category,
			String description, int cost, ItemStack... item) {
		super(type, name, category, true, description, cost);

		this.item = item;
	}

	@Override
	public void runExtraData(ConversationContext context, Registration plugin) {
		Player ply = (Player) context.getForWhom();
		HashMap<Integer, ItemStack> extraItems = ply.getInventory().addItem(item);

		int returnAmount = 0;
		for (ItemStack s : extraItems.values()) {
			returnAmount++;
			ply.getWorld().dropItem(ply.getLocation(), s);
		}

		if (returnAmount != 0) {
			context.getForWhom().sendRawMessage(ChatColor.AQUA + "Shop: Some items you ordered wouldn't fit in your inventory. They have been dropped on the floor.");
		}
	}

	@Override
	public void runPurchaseAnnoucement(ConversationContext context,
			Registration plugin) {
		context.getForWhom().sendRawMessage(ChatColor.AQUA + "Purchase complete! Your items have been delivered.");
		SPlayer sply = plugin.players.get(((Player) context.getForWhom()).getName());
		sply.notifyCurrency();
	}

}
