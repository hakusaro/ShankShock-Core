package com.shankshock.nicatronTg.Registration.Items;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.entity.Player;

import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.Items.SilverManager.ShopType;

public class AssassinationItem extends Item {

	public AssassinationItem(ItemType itemType, String name, ShopType category,
			String description, int cost) {
		super(itemType, name, category, true, description, cost);
	}

	@Override
	public void runExtraData(ConversationContext context, Registration plugin) {
		Player player = (Player) context.getSessionData("player");
		player.damage(1000);
		return;
	}

	@Override
	public void runPurchaseAnnoucement(ConversationContext context,
			Registration plugin) {
		plugin.broadcastMessage(ChatColor.RED + "A player was assassinated.");
	}

}
