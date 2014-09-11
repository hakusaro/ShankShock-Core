package com.shankshock.nicatronTg.Registration.Zones;

import com.google.gson.Gson;
import com.shankshock.nicatronTg.Registration.Registration;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class ZoneManager {
	private ZoneStore zoneStore;
	private String path;
	private final Registration plugin;

	public ZoneManager(final Registration plugin) {
		this.plugin = plugin;
		this.path = "./shankshock/zones.json";
		System.out.println("Zone Manager: Initializing");
		loadZonesFromJson(path);
	}

	public ZoneManager(final Registration plugin, String path) {
		this.plugin = plugin;
		this.path = path;
		System.out.println("Zone Manager: Initializing");

		loadZonesFromJson(path);
	}

	public ZoneStore getZoneStore() {
		return zoneStore;
	}

	public void loadZonesFromJson() {
		System.out.println("Zone Manager: Loading zones.");
		loadZonesFromJson(path);
	}

	public void saveZonesToJson() {
		System.out.println("Zone Manager: Saving zones.");
		saveZonesToJson(path);
	}

	private void saveZonesToJson(String path) {
		try {
			FileWriter fw = new FileWriter(path);
			if (zoneStore == null) {
				zoneStore = new ZoneStore();
			}
			fw.write((new Gson().toJson(zoneStore, ZoneStore.class)));
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Zone Manager: Save completed.");
	}

	private void loadZonesFromJson(String path) {
		if (!(new File(path)).exists()) {
			saveZonesToJson(path);
		} else {
			try {
				Scanner s = new Scanner(new FileInputStream(path));
				String json = "";

				while (s.hasNextLine()) {
					json += s.nextLine();
				}
				s.close();
				zoneStore = new Gson().fromJson(json, ZoneStore.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("Zone Manager: Load completed.");
	}

	public boolean blockInZone(Location b, Zone zone) {
		Location l = b;

		if (!zone.getWorldName().equals(b.getWorld().getName())) {
			return false;
		}
		int x1 = zone.getStartLocation(plugin).getBlockX();
		int y1 = zone.getStartLocation(plugin).getBlockY();
		int z1 = zone.getStartLocation(plugin).getBlockZ();
		int x2 = zone.getEndLocation(plugin).getBlockX();
		int y2 = zone.getEndLocation(plugin).getBlockY();
		int z2 = zone.getEndLocation(plugin).getBlockZ();
		boolean x, y, z;
		x = (Math.min(x1, x2) <= l.getBlockX()) && (l.getBlockX() <= Math.max(x1, x2));
		y = (Math.min(y1, y2) <= l.getBlockY()) && (l.getBlockY() <= Math.max(y1, y2));
		z = (Math.min(z1, z2) <= l.getBlockZ()) && (l.getBlockZ() <= Math.max(z1, z2));

		return x & y & z;

	}

	public void setZone(Zone z, int id, byte datavalue) {
		Material m = Material.getMaterial(id);
		
		if (m == null) {
			return;
		} else {
			setZone(z, m, datavalue);
		}
	}
	
	public void setZone(Zone z, int id) {
		Material m = Material.getMaterial(id);
		
		if (m == null) {
			return;
		} else {
			setZone(z, m);
		}
	}
	
	public void setZone(Zone zone, Material m, byte datavalue) {
		
		Location loc1 = zone.getStartLocation(plugin);
		Location loc2 = zone.getEndLocation(plugin);
		
		int minx = Math.min(loc1.getBlockX(), loc2.getBlockX()), miny = Math.min(loc1.getBlockY(), loc2.getBlockY()), minz = Math.min(loc1.getBlockZ(), loc2.getBlockZ()), maxx = Math.max(loc1.getBlockX(), loc2.getBlockX()), maxy = Math.max(loc1.getBlockY(), loc2.getBlockY()), maxz = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
		for (int x = minx; x <= maxx; x++) {
			for (int y = miny; y <= maxy; y++) {
				for (int z = minz; z <= maxz; z++) {
					Block b = zone.getStartLocation(plugin).getWorld().getBlockAt(x, y, z);
					b.setType(m);
					b.setData(datavalue);
				}
			}
		}
	}
	
	public void setZone(Zone zone, Material m) {
		
		Location loc1 = zone.getStartLocation(plugin);
		Location loc2 = zone.getEndLocation(plugin);
		
		int minx = Math.min(loc1.getBlockX(), loc2.getBlockX()), miny = Math.min(loc1.getBlockY(), loc2.getBlockY()), minz = Math.min(loc1.getBlockZ(), loc2.getBlockZ()), maxx = Math.max(loc1.getBlockX(), loc2.getBlockX()), maxy = Math.max(loc1.getBlockY(), loc2.getBlockY()), maxz = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
		for (int x = minx; x <= maxx; x++) {
			for (int y = miny; y <= maxy; y++) {
				for (int z = minz; z <= maxz; z++) {
					Block b = zone.getStartLocation(plugin).getWorld().getBlockAt(x, y, z);
					b.setType(m);
				}
			}
		}
	}

	public String[] getRegionalCategories(Location loc) {
		ArrayList<String> enabledCategories = new ArrayList<String>();

		for (Zone z : zoneStore.getZones()) {
			String[] enabledCats = z.getEnabledCategories();
			if (blockInZone(loc, z) && enabledCats != null && enabledCats.length > 0) {
				for (String s : enabledCats) {
					enabledCategories.add(s);
				}
			}
		}

		if (enabledCategories.size() > 0) {
			return enabledCategories.toArray(new String[enabledCategories.size()]);
		}
		return null;
	}

	public double getShopDiscount(Location loc) {
		for (Zone z : zoneStore.getZones()) {
			if (blockInZone(loc, z) && z.hasShopDiscount()) {
				return z.getShopDiscount();
			}
		}
		return 0.00;
	}

	public Zone getZoneByName(String name) {
		for (Zone z : zoneStore.getZones()) {
			if (z.getName().equals(name)) {
				return z;
			}
		}
		return null;
	}

	public boolean checkBlockIsInAntiGriefZone(Location b) {
		for (Zone z : zoneStore.getZones()) {
			if (blockInZone(b, z) && z.getShadowZone()) {
				return true;
			} else {
				continue;
			}
		}
		return false;
	}

	public boolean checkBlockIsInZone(Location b) {
		for (Zone z : zoneStore.getZones()) {
			if (blockInZone(b, z)) {
				return true;
			} else {
				continue;
			}
		}
		return false;
	}

	public boolean checkForZoneGrief(Block b, Player ply, String eventType) {
		if (!ply.getWorld().getName().equals("panem")) {
			return false;
		}
		if (checkBlockIsInAntiGriefZone(b.getLocation())) {
			if (ply.hasPermission("shankshock.shadow")) {
				return false;
			}
			boolean ban = Boolean.parseBoolean(plugin.globalConfig.getValue("shadow.ban"));
			boolean kick = Boolean.parseBoolean(plugin.globalConfig.getValue("shadow.kick"));
			if (b.getType() == Material.SAND || b.getType() == Material.FENCE || b.getType() == Material.TORCH || b.getType() == Material.SAPLING || b.getType() == Material.LONG_GRASS || b.getType() == Material.LEAVES || b.getType() == Material.GRASS || b.getType() == Material.THIN_GLASS || b.getType() == Material.GLASS) {
				if (!ply.hasPermission("shankshock.shadow")) {
					ply.sendMessage(ChatColor.RED + "Please refrain from placing or breaking *ANY* blocks in spawn! You may be flagged as a griefer!");
					return true;
				}
			}

			if (kick) {
				ply.kickPlayer("Please refrain from breaking other people's creations.");
				return true;
			}

			return true;
		}
		return false;
	}
}
