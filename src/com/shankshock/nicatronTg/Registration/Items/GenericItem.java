package com.shankshock.nicatronTg.Registration.Items;

import org.bukkit.conversations.ConversationContext;

import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.Items.SilverManager.ShopType;

public class GenericItem extends Item {

	public GenericItem(ItemType id, String name, ShopType category,
			boolean pureDisabled, String description, int cost) {
		super(id, name, category, pureDisabled, description, cost);
	}

	@Override
	public void runExtraData(ConversationContext context, Registration plugin) {

	}

	@Override
	public void runPurchaseAnnoucement(ConversationContext context,
			Registration plugin) {

	}

}
