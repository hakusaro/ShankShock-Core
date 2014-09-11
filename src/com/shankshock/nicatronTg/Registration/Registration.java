package com.shankshock.nicatronTg.Registration;

import com.shankshock.Redis.GlobalConfig;
import com.shankshock.Redis.RedisDatabase;
import com.shankshock.Redis.WorldConfig;
import com.shankshock.SuperPick.SuperPick;
import com.shankshock.nicatronTg.Registration.Awards.AwardManager;
import com.shankshock.nicatronTg.Registration.Engines.*;
import com.shankshock.nicatronTg.Registration.Engines.SQLDatabase.Runner;
import com.shankshock.nicatronTg.Registration.InventoryConversations.InventoryStart;
import com.shankshock.nicatronTg.Registration.Items.SilverManager;
import com.shankshock.nicatronTg.Registration.Listeners.BlockListener;
import com.shankshock.nicatronTg.Registration.Listeners.EntityListener;
import com.shankshock.nicatronTg.Registration.Listeners.PlayerListener;
import com.shankshock.nicatronTg.Registration.Listeners.ServerListener;
import com.shankshock.nicatronTg.Registration.Misc.ServerIncrementialSaver;
import com.shankshock.nicatronTg.Registration.ShopConversations.ShopCategoryChooser;
import com.shankshock.nicatronTg.Registration.Zones.ZoneManager;
import com.shankshock.nicatronTg.scoreboard.SilverScoreboard;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * The ShankShock Core.
 * 
 * @author nicatronTg
 */

public class Registration extends JavaPlugin implements
		ConversationAbandonedListener {
	/*
	 * Player event listener
	 */
	private final PlayerListener playerListener = new PlayerListener(this);
	/*
	 * Block event listener
	 */
	private final BlockListener blockListener = new BlockListener(this);
	/*
	 * Entity event listener
	 */
	private final EntityListener entityListener = new EntityListener(this);
	/*
	 * Server event listener
	 */
	private final ServerListener serverListener = new ServerListener(this);

	private CommandExecutor executor;
	public ZoneManager zoneManager;
	public DatabaseConnectionEngine conn;
	public World mainWorld;
	public NewsEngine newsEngine = new NewsEngine(this);
	public boolean debug;
	public HashMap<String, SPlayer> players = new HashMap<String, SPlayer>();
	public HashMap<String, WorldConfig> worlds = new HashMap<String, WorldConfig>();
	public AutorunEngine autorunEngine;
	public BlacklistEngine blacklistEngine;
	public PureEngine pureEngine;
	public boolean timeLockedOut;
	public boolean banLockedOut;
	public boolean isDeveloperServer;
	public ConversationFactory shopFactory;
	public ConversationFactory inventoryFactory;
	public SQLDatabase sqldb;
	public KeepAliveAgent keepAliveAgent;
	public WorldEditPlugin worldEdit;
	public SuperPick superPick;
	public AdEngine adEngine;
	public AwardManager awardManager;
	public SilverManager silverManager;
	public boolean kickAndLock;
	public RedisDatabase redisDatabase;
	public GlobalConfig globalConfig;
	public ChristmasEngine christmasEngine;
	private SilverScoreboard silverScoreboard;
	
	
	public SilverScoreboard getSilverScoreboard() {
		return silverScoreboard;
	}

	@Override
	public void onEnable() {
		registerCommands();
		autorunEngine = new AutorunEngine(this);
		blacklistEngine = new BlacklistEngine(this);
		shopFactory = new ConversationFactory(this).withModality(true).withPrefix(new ShopConversationPrefix()).withFirstPrompt(new ShopCategoryChooser(this)).thatExcludesNonPlayersWithMessage("Sorry, you must be a valid player to use the shop.").addConversationAbandonedListener(this).withEscapeSequence("LEAVENOW!");
		inventoryFactory = new ConversationFactory(this).withModality(true).withPrefix(new InventoryPrefix()).withFirstPrompt(new InventoryStart(this)).thatExcludesNonPlayersWithMessage("You must be a valid player to have an inventory!").addConversationAbandonedListener(this).withEscapeSequence("LEAVENOW!");
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(blockListener, this);
		pm.registerEvents(playerListener, this);
		pm.registerEvents(serverListener, this);
		pm.registerEvents(entityListener, this);

		conn = new DatabaseConnectionEngine(this);
		sqldb = new SQLDatabase(5, getConfig().getString("username"), getConfig().getString("password"), getConfig().getString("jdbcurl"));
		sqldb.setupRunners();
		debug = getConfig().getBoolean("debug");
		keepAliveAgent = new KeepAliveAgent(sqldb);
		getServer().getScheduler().runTaskTimerAsynchronously(this, keepAliveAgent, 5000, 5000);

		try {
			Class.forName("com.lambdaworks.redis.RedisClient");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		System.out.println("Loading ShankShock Minecraft Server Core.");
		redisDatabase = new RedisDatabase(this);
		globalConfig = new GlobalConfig(redisDatabase);
		conn.loadGroups();
		conn.loadPermissions();
		conn.setupChatColors();
		debug = getConfig().getBoolean("debug");
		existingPlayerReload();
		zoneManager = new ZoneManager(this);
		pureEngine = new PureEngine(this);
		adEngine = new AdEngine(this, 36000);
		awardManager = new AwardManager(this);
		silverManager = new SilverManager(this);
		worldEdit = (WorldEditPlugin) getServer().getPluginManager().getPlugin("WorldEdit");
		superPick = (SuperPick) getServer().getPluginManager().getPlugin("SuperPick");
		silverScoreboard = new SilverScoreboard(this);
		this.getServer().getScheduler().runTaskTimerAsynchronously(this, new ServerIncrementialSaver(this), 40, 18000);

		for (World w : getServer().getWorlds()) {
			if (!worlds.containsKey(w.getName())) {
				worlds.put(w.getName(), new WorldConfig(redisDatabase, w.getName(), getConfig().getString("uniqueid")));
			}
		}

		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				for (Player ply : getServer().getOnlinePlayers()) {
					SPlayer sply = players.get(ply.getName());

					if (sply.getChatPrefix().contains("500")) {
						ply.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 7000, 3), true);
					}
				}
			}
		}, 0, 2000);
		System.out.println("Identity: " + getConfig().getString("serverid") + " has UID: " + getConfig().getString("uniqueid"));
		christmasEngine = new ChristmasEngine(this);
	}

	public class KeepAliveAgent implements Runnable {

		public SQLDatabase DBX;

		public KeepAliveAgent(SQLDatabase DBX) {
			this.DBX = DBX;
		}

		@Override
		public void run() {
			for (Runner runner : DBX.getAllRunners()) {
				try {
					if (!runner.getConnection().isValid(5)) {
						runner.initialize();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			int realloc = DBX.amount - DBX.getAllRunners().size();

			if (realloc > 0) {
				DBX.addRunner(realloc);
			}
		}
	}

	private void registerCommands() {
		executor = new RegistrationCommandExecutor(this);
		getCommand("ban").setExecutor(executor);
		getCommand("ovban").setExecutor(executor);
		getCommand("reloadsettings").setExecutor(executor);
		getCommand("unban").setExecutor(executor);
		getCommand("reloadpermissions").setExecutor(executor);
		getCommand("news").setExecutor(executor);
		getCommand("ar").setExecutor(executor);
		getCommand("daytime").setExecutor(executor);
		getCommand("stability-check").setExecutor(executor);
		getCommand("sudo").setExecutor(executor);
		getCommand("name").setExecutor(executor);
		getCommand("bl").setExecutor(executor);
		getCommand("kick").setExecutor(executor);
		getCommand("zm").setExecutor(executor);
		getCommand("pure").setExecutor(executor);
		getCommand("store").setExecutor(executor);
		getCommand("shop").setExecutor(executor);
		getCommand("silverc").setExecutor(executor);
		getCommand("inventory").setExecutor(executor);
		getCommand("begin").setExecutor(executor);
		getCommand("gwho").setExecutor(executor);
		getCommand("gflag").setExecutor(executor);
		getCommand("wflag").setExecutor(executor);
		getCommand("whoami").setExecutor(executor);
		getCommand("changelog").setExecutor(executor);
		getCommand("reloadads").setExecutor(executor);
		getCommand("sva").setExecutor(executor);
		getCommand("antiangrywolf").setExecutor(executor);
		getCommand("changename").setExecutor(executor);
		getCommand("rules").setExecutor(executor);
		getCommand("silvertop25").setExecutor(executor);
		getCommand("creative").setExecutor(executor);
		getCommand("jail").setExecutor(executor);
		getCommand("release").setExecutor(executor);
		getCommand("afterdark").setExecutor(executor);
		getCommand("resetspleef").setExecutor(executor);
	}

	public class ShopConversationPrefix implements ConversationPrefix {
		@Override
		public String getPrefix(ConversationContext arg0) {
			return ChatColor.AQUA + "Shop: ";
		}
	}

	public class InventoryPrefix implements ConversationPrefix {

		@Override
		public String getPrefix(ConversationContext arg0) {
			return ChatColor.AQUA + "Inventory: ";
		}

	}

	@Override
	public void conversationAbandoned(final ConversationAbandonedEvent e) {
		getServer().getScheduler().runTaskAsynchronously(this, new Runnable() {

			@Override
			public void run() {
				redisDatabase.sendDeafMessage(((Player) e.getContext().getForWhom()).getDisplayName(), "", false);

			}

		});
	}

	private void existingPlayerReload() {
		for (Player p : getServer().getOnlinePlayers()) {
			String name = "";
			for (int i = 0; i < getServer().getOnlinePlayers().length; i++) {
				if (getServer().getOnlinePlayers()[i].getName().equals(p.getName())) {
					name = getServer().getOnlinePlayers()[i].getDisplayName();
				}
			}
			broadcastMessage(ChatColor.GREEN + name + " connected.");
		}
	}

	public void broadcastMessage(String msg) {
		for (Player p : getServer().getOnlinePlayers()) {
			p.sendMessage(ChatColor.GREEN + msg);
		}
	}

	@Override
	public void onDisable() {
		players.clear();
		redisDatabase.shutdown();
	}

	public String rainbow(String str2) {
		String temp = str2;
		String[] array = new String[temp.length()];

		for (int i = 0; i < array.length; i++) {
			array[i] = temp.substring(i, i + 1);
		}

		// ROYGBIV
		ChatColor[] cc = { ChatColor.RED, ChatColor.GOLD, ChatColor.GREEN,
				ChatColor.AQUA, ChatColor.LIGHT_PURPLE, };

		String str = "";
		for (int i = 0; i < array.length; i++) {
			str += cc[i % cc.length] + array[i];
		}
		return str;
	}
}
