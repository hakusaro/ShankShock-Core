package com.shankshock.nicatronTg.Registration.Engines;

import com.shankshock.nicatronTg.Registration.Awards.AwardManager.AwardType;
import com.shankshock.nicatronTg.Registration.Items.Item;
import com.shankshock.nicatronTg.Registration.Items.ItemType;
import com.shankshock.nicatronTg.Registration.Items.MinecraftItem;
import com.shankshock.nicatronTg.Registration.Items.SilverManager;
import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.SPlayer;
import com.shankshock.nicatronTg.Registration.Zones.Zone;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.Random;

public class ChristmasEngine implements Listener{

	private final Registration plugin;
	private final ArrayList<ChristmasItem> lowChanceItems = new ArrayList<ChristmasItem>();
	private final ArrayList<ChristmasItem> medChanceItems = new ArrayList<ChristmasItem>();
	private final ArrayList<ChristmasItem> highChanceItems = new ArrayList<ChristmasItem>();
	private final ArrayList<Location> spawnedBlocks = new ArrayList<Location>();
	
	public ChristmasEngine(Registration instance) {
		plugin = instance;
		initialize();
	}
	
	public void initialize() {
		
		if (Boolean.parseBoolean(plugin.globalConfig.getValue("xmas")) == false) {
			System.out.println("Aborting initialize of Christmas Engine!");
			return;
		}
		
		System.out.println("Christmas Engine enabled. Loading.");
		PluginManager pm = plugin.getServer().getPluginManager();
		pm.registerEvents(this, plugin);
		System.out.println("Events registered.");
		setupGifts();
		System.out.println("Gifts setup.");
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				spawnGifts();
			}
		}, 6000, 20000);
		
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				plugin.getServer().broadcastMessage(ChatColor.GREEN + "You see a sleep deprived 17 year old student running to ShadowVale!");
			}
		}, 5800, 20000);
		System.out.println("Scheduler started!");
		plugin.getServer().broadcastMessage(ChatColor.GREEN + "A sleep deprived 17 year old student is getting ready to run to ShadowVale!");
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				spawnGifts();
			}
		}, 3600);
	}
	
	private void setupGifts() {
		lowChanceItems.add(new ChristmasItem(50000));
		lowChanceItems.add(new ChristmasItem(100000));
		lowChanceItems.add(new ChristmasItem(50000));
		lowChanceItems.add(new ChristmasItem(100000));
		lowChanceItems.add(new ChristmasItem(50000));
		lowChanceItems.add(new ChristmasItem(100000));
		lowChanceItems.add(new ChristmasItem(50000));
		lowChanceItems.add(new ChristmasItem(100000));
		medChanceItems.add(new ChristmasItem(50000));
		medChanceItems.add(new ChristmasItem(50000));
		lowChanceItems.add(new ChristmasItem(50000));
		lowChanceItems.add(new ChristmasItem(100000));
		lowChanceItems.add(new ChristmasItem(50000));
		lowChanceItems.add(new ChristmasItem(100000));
		lowChanceItems.add(new ChristmasItem(50000));
		lowChanceItems.add(new ChristmasItem(100000));
		lowChanceItems.add(new ChristmasItem(50000));
		lowChanceItems.add(new ChristmasItem(100000));
		medChanceItems.add(new ChristmasItem(50000));
		medChanceItems.add(new ChristmasItem(50000));
		medChanceItems.add(new ChristmasItem(50000));
		medChanceItems.add(new ChristmasItem(50000));
		medChanceItems.add(new ChristmasItem(50000));
		lowChanceItems.add(new ChristmasItem(ItemType.TITLE_MAGIC));
		lowChanceItems.add(new ChristmasItem(ItemType.TITLE_DOUCHEBAG_WIZARD));
		lowChanceItems.add(new ChristmasItem(ItemType.TITLE_DOUCHEBAG_WIZARD));
		lowChanceItems.add(new ChristmasItem(ItemType.TITLE_DOUCHEBAG_WIZARD));
		lowChanceItems.add(new ChristmasItem(ItemType.TITLE_DOUCHEBAG_WIZARD));
		lowChanceItems.add(new ChristmasItem(ItemType.TITLE_DOUCHEBAG_WIZARD));
		lowChanceItems.add(new ChristmasItem(ItemType.CHAT_COLOR_GOLD));
		lowChanceItems.add(new ChristmasItem(ItemType.CHAT_COLOR_BRIGHTGREEN));
		lowChanceItems.add(new ChristmasItem(ItemType.CHAT_COLOR_DARKGRAY));
		lowChanceItems.add(new ChristmasItem(ItemType.CHAT_COLOR_PINK));
		lowChanceItems.add(new ChristmasItem(ItemType.CHAT_COLOR_AQUA));
		;
		medChanceItems.add(new ChristmasItem(ItemType.MC_NETHER_STAR));
		medChanceItems.add(new ChristmasItem(ItemType.MC_EMERALD));
		medChanceItems.add(new ChristmasItem(ItemType.MC_DIAMOND));
		medChanceItems.add(new ChristmasItem(ItemType.TITLE_TIME_LORD));
		medChanceItems.add(new ChristmasItem(ItemType.TITLE_SV_BLACK_RED));
		medChanceItems.add(new ChristmasItem(ItemType.TITLE_PSYCHONAUT));
		medChanceItems.add(new ChristmasItem(ItemType.TITLE_BLACK_MESA_EAST));
		medChanceItems.add(new ChristmasItem(ItemType.TITLE_ANDREW_RYAN));
		medChanceItems.add(new ChristmasItem(ItemType.INSTANT_HEALTH_2));
		medChanceItems.add(new ChristmasItem(ItemType.TITLE_LYOKO_WARRIOR));
		medChanceItems.add(new ChristmasItem(ItemType.TITLE_ONE_FREE_MAN));
        medChanceItems.add(new ChristmasItem(10000));
        medChanceItems.add(new ChristmasItem(15000));
        medChanceItems.add(new ChristmasItem(20000));

		highChanceItems.add(new ChristmasItem(ItemType.XP));
		highChanceItems.add(new ChristmasItem(ItemType.XP));
		highChanceItems.add(new ChristmasItem(ItemType.XP));
		highChanceItems.add(new ChristmasItem(ItemType.STRENGTH_2));
		highChanceItems.add(new ChristmasItem(ItemType.SWIFTNESS_2));
		highChanceItems.add(new ChristmasItem(ItemType.GOLDEN_APPLE));
        highChanceItems.add(new ChristmasItem(ItemType.IRON_RAW));
        highChanceItems.add(new ChristmasItem(5000));
        highChanceItems.add(new ChristmasItem(5000));
        highChanceItems.add(new ChristmasItem(5000));
        highChanceItems.add(new ChristmasItem(5000));
	}
	
	public void spawnGifts() {
		Zone z = plugin.zoneManager.getZoneByName("anniversary");
		
		//int count = Math.round((float) Math.pow(1.5, plugin.getServer().getOnlinePlayers().length));

		int count = 50;
		
		//if (count > 25) {
		//	count = 30;
		//	
		//}
		
		int displayCount = count;
		
		if (z == null) {
			System.out.println("Sorry! No gift spawning this session, the zone doesn't exist.");
			return;
		}
		
		if (plugin.players.size() == 0) {
			System.out.println("No players! No gift spawning this session.");
			return;
		}
		
		for (Location l : spawnedBlocks) {
			Block b = l.getBlock();
			b.setType(Material.AIR);
		}
		
		while (count != 0) {
			Random r = new Random(System.nanoTime() + plugin.players.size() + plugin.hashCode());
			
			int ceil1, ceil2, y;
			
			ceil1 = Math.abs(z.getEndLocation(plugin).getBlockX()) - Math.abs(z.getStartLocation(plugin).getBlockX());
			ceil2 = Math.abs(z.getEndLocation(plugin).getBlockZ()) - Math.abs(z.getStartLocation(plugin).getBlockZ());
			y = z.getStartLocation(plugin).getBlockY();
			
			int xpos, zpos;
			
			xpos = z.getStartLocation(plugin).getBlockX() + r.nextInt(ceil1);
			zpos = z.getStartLocation(plugin).getBlockZ() + r.nextInt(ceil2);
			
			Location present = new Location(z.getStartLocation(plugin).getWorld(), xpos, y, zpos);
			
			if (plugin.zoneManager.blockInZone(present, z)) {
				Block b = present.getBlock();
				
				if (b.getType() != Material.AIR) {
					continue;
				} else {
					b.setType(Material.GLOWSTONE);
					spawnedBlocks.add(present);
					count--;
				}
			} else {
			}
		}
		
		plugin.getServer().broadcastMessage(ChatColor.GREEN + "Happy ShankShock Anniversary! Shank left " + ChatColor.GOLD + displayCount + ChatColor.GREEN + " gifts in ShadowVale plaza!");
		
	}
	
	private void giveRandomItem(Player ply) {
		SPlayer sply = plugin.players.get(ply.getName());
		if (sply == null) {
			return;
		}
		
//		plugin.awardManager.updateAward(AwardType.CHRISTMAS_LV1, 1, ply);
//		plugin.awardManager.updateAward(AwardType.CHRISTMAS_LV2, 1, ply);
//		plugin.awardManager.updateAward(AwardType.CHRISTMAS_LV3, 1, ply);
		
		Random r = new Random(System.nanoTime() + sply.getPlayer().getDisplayName().length() + System.nanoTime());
		float selector = r.nextFloat();
		
		if (selector < 0.001) {
			int item = r.nextInt(lowChanceItems.size());
			lowChanceItems.get(item).giveItem(sply);
		} else if (selector < 0.90 && selector > 0.001) {
			int item = r.nextInt(highChanceItems.size());
			highChanceItems.get(item).giveItem(sply);
		} else {
			int item = r.nextInt(medChanceItems.size());
			medChanceItems.get(item).giveItem(sply);
		}
	}
	
	// Events
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockBreak(BlockBreakEvent event) {
		Block b = event.getBlock();
		
		if (plugin.zoneManager.blockInZone(b.getLocation(), plugin.zoneManager.getZoneByName("anniversary"))) {
			if (b.getType() == Material.GLOWSTONE) {
				if (spawnedBlocks.contains(b.getLocation())) {
					giveRandomItem(event.getPlayer());
					event.setCancelled(true);
					event.getBlock().setType(Material.AIR);
					spawnedBlocks.remove(b.getLocation());
				} else {
					event.getPlayer().sendMessage(ChatColor.RED + "Nice try.");
					event.setCancelled(false);
				}
			} else {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockPlace(BlockPlaceEvent event) {
		Block b = event.getBlock();
		if (plugin.zoneManager.blockInZone(b.getLocation(), plugin.zoneManager.getZoneByName("anniversary"))) {
			event.setCancelled(true);
		}
	}
	
	// Items
	
	public enum ChristmasItemType {
		SILVER, MC_ITEM, ITEM 
	}
	
	class ChristmasItem {
		
		private ChristmasItemType type;
		private Item item;
		private int silver;
		public ChristmasItem(ItemType itemType) {
			
			for (Item i : SilverManager.items) {
				if (i.getItemType() == itemType) {
					item = i;
				}
			}
			
			if (item instanceof MinecraftItem) {
				type = ChristmasItemType.MC_ITEM;
			} else {
				type = ChristmasItemType.ITEM;
			}
		}
		
		public ChristmasItem(int silver) {
			this.silver = silver;
			type = ChristmasItemType.SILVER;
		}
		
		public void giveItem(SPlayer sply) {
            plugin.awardManager.addAward(AwardType.SHANKSHOCK_YEAR_FOUR, sply.getPlayer());
			if (type == ChristmasItemType.ITEM) {
				
				if (sply.getInventoryStore().hasItem(item.getItemType())) {
					giveRandomItem(sply.getPlayer());
					return;
				}
				
				sply.getInventoryStore().addItem(item);
				sply.sendMessage(ChatColor.GREEN + "Happy Anniversary! You unwrapped " + ChatColor.GOLD + item.getItemName());
				sply.sendMessage(ChatColor.GREEN + "The tag says: " + item.getItemDescription());
			} else if (type == ChristmasItemType.MC_ITEM) {
				sply.getPlayer().getInventory().addItem(((MinecraftItem) item).getItemStack());
				sply.sendMessage(ChatColor.GREEN + "Happy Anniversary! You unwrapped " + ChatColor.GOLD + item.getItemName());
				sply.sendMessage(ChatColor.GREEN + "The tag says: " + item.getItemDescription());
			} else if (type == ChristmasItemType.SILVER) {
				Random r = new Random(System.nanoTime() + sply.getChatColor().length() + sply.getPlayer().getName().length());
				int currency = r.nextInt(silver);
				sply.addCurrency(currency);
			}
		}
	}
}