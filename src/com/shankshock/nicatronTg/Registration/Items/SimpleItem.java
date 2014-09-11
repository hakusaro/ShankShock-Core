package com.shankshock.nicatronTg.Registration.Items;


public class SimpleItem {

	private ItemType itemType;
	private float uses;
	public ItemType getItemType() {
		return itemType;
	}

	public void setItemType(ItemType itemType) {
		this.itemType = itemType;
	}

	public float getUses() {
		return uses;
	}

	public void setUses(float uses) {
		this.uses = uses;
	}

	public float getMeta1() {
		return meta1;
	}

	public void setMeta1(float meta1) {
		this.meta1 = meta1;
	}

	public float getMeta2() {
		return meta2;
	}

	public void setMeta2(float meta2) {
		this.meta2 = meta2;
	}

	private float meta1;
	private float meta2;
	
	public SimpleItem(ItemType itemType, float uses, float meta1, float meta2) {
		
	}

	public SimpleItem() { }
}
