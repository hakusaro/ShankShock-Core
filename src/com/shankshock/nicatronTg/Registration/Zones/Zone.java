package com.shankshock.nicatronTg.Registration.Zones;

import org.bukkit.Location;

import com.shankshock.nicatronTg.Registration.Registration;

public class Zone {
	private double x1, y1, z1, x2, y2, z2;
	private String name;
	private String world;
	private boolean shadowZone = true;
	private boolean offersShopDiscounts = false;
	private double shopDiscountAmount = 0.00;
	private String[] enablesShopCategories;

	public Zone(Location loc1, Location loc2, String name, boolean shadowZone,
			boolean offersShopDiscounts, double shopDiscountAmount,
			String[] enablesShopCategories) {
		this.x1 = loc1.getX();
		this.y1 = loc1.getY();
		this.z1 = loc1.getZ();
		this.x2 = loc2.getX();
		this.y2 = loc2.getY();
		this.z2 = loc2.getZ();
		world = loc1.getWorld().getName();
		this.name = name;
		this.shadowZone = shadowZone;
		this.offersShopDiscounts = offersShopDiscounts;
		this.shopDiscountAmount = shopDiscountAmount;
		this.enablesShopCategories = enablesShopCategories;
	}

	public Zone(Location loc1, Location loc2, String name, boolean shadowZone) {
		this.x1 = loc1.getX();
		this.y1 = loc1.getY();
		this.z1 = loc1.getZ();
		this.x2 = loc2.getX();
		this.y2 = loc2.getY();
		this.z2 = loc2.getZ();
		world = loc1.getWorld().getName();
		this.name = name;
		this.shadowZone = shadowZone;
	}

	public Location getStartLocation(Registration plugin) {
		return new Location(plugin.getServer().getWorld(world), x1, y1, z1);
	}

	public Location getEndLocation(Registration plugin) {
		return new Location(plugin.getServer().getWorld(world), x2, y2, z2);
	}

	public String getWorldName() {
		return world;
	}

	public String getName() {
		return name;
	}

	public String setName(String name) {
		return this.name = name;
	}

	public boolean getShadowZone() {
		return shadowZone;
	}

	public void setShadowZone(boolean shadowZone) {
		this.shadowZone = shadowZone;
	}

	public double getShopDiscount() {
		return shopDiscountAmount;
	}

	public boolean hasShopDiscount() {
		return offersShopDiscounts;
	}

	public String[] getEnabledCategories() {
		return enablesShopCategories;
	}

}
