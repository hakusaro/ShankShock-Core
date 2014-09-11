package com.shankshock.nicatronTg.Registration.Items;

import org.bukkit.conversations.ConversationContext;

import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.Items.SilverManager.ShopType;

public abstract class Item {
	String itemName;
	String itemDescription;
	String itemShortName;
	ShopType category;
	boolean pureDisabled;
	int cost;
	ItemType id;

	public Item(ItemType id, String name, ShopType category,
			boolean pureDisabled, String description, int cost) {
		itemName = name;
		itemDescription = description;
		this.cost = cost;
		this.category = category;
		this.pureDisabled = pureDisabled;
		this.id = id;
	}

	public ItemType getItemType() {
		return id;
	}

	public String getItemName() {
		return itemName;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public int getItemCost() {
		return cost;
	}

	public ShopType getItemCategory() {
		return category;
	}

	public String getItemCategoryName() {
		return category.name();
	}

	public boolean getPureDisabled() {
		return pureDisabled;
	}

	public abstract void runExtraData(ConversationContext context,
			Registration plugin);

	public abstract void runPurchaseAnnoucement(ConversationContext context,
			Registration plugin);
}
