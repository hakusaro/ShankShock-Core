package com.shankshock.nicatronTg.Registration;

import com.google.gson.Gson;
import com.shankshock.nicatronTg.Registration.Awards.Award;
import com.shankshock.nicatronTg.Registration.Awards.AwardManager.AwardType;
import com.shankshock.nicatronTg.Registration.Awards.CounterAward;
import com.shankshock.nicatronTg.Registration.Awards.RawAward;
import com.shankshock.nicatronTg.Registration.Items.Item;
import com.shankshock.nicatronTg.Registration.Items.ItemNotFoundException;
import com.shankshock.nicatronTg.Registration.Items.ItemType;
import com.shankshock.nicatronTg.Registration.Items.SimpleItem;
import com.shankshock.nicatronTg.Registration.Misc.ChatBundle;
import com.shankshock.nicatronTg.Registration.Misc.ChatMessage;
import com.shankshock.nicatronTg.scoreboard.SilverScoreboard;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.logging.Level;

public class SPlayer {
	/**
	 * The Registration plugin instance
	 */
	private final Registration plugin;

	/**
	 * The Minecraft player
	 */
	private final Player player;

	/**
	 * Denotes if the player is a part of the hidden admin group
	 */
	private boolean isHiddenAdmin = false;

	/**
	 * Connection time - the unix epoch when the player was last saved
	 */
	private long tempConnectionTime;
	
	public long getConnectionTime() {
		return tempConnectionTime;
	}

	public void setConnectionTime(long connectionTime) {
		this.tempConnectionTime = connectionTime;
	}
	
	/**
	 * The only use here is in generating awards for continuous server play over a single session.
	 * Connection time is fine for saving actual total time on server overall, because it saves
	 * at irregular intervals, and can be triggered several times. We favor high frequency saves
	 * due to the chance of data loss if the server goes down or the event fails to fire.
	 */
	private long originalConnectionTime;
	
	public boolean goingToPure;
	
	public long getOriginalConnectionTime() {
		return originalConnectionTime;
	}

	public void setOriginalConnectionTime(long originalConnectionTime) {
		this.originalConnectionTime = originalConnectionTime;
	}

	public Registration getPlugin() {
		return plugin;
	}

	public long getTempConnectionTime() {
		return tempConnectionTime;
	}

	public boolean isRegistered() {
		return isRegistered;
	}

	public boolean isActivated() {
		return isActivated;
	}

	public boolean isChatMuted() {
		return isChatMuted;
	}

	public long getChatMuteInitiationTime() {
		return chatMuteInitiationTime;
	}

	public int getChatWarnTimes() {
		return chatWarnTimes;
	}

	public ChatBundle getChats() {
		return chats;
	}

	public boolean isUsingNickname() {
		return isUsingNickname;
	}

	public int getCurrency() {
		return currency;
	}

	public boolean isHasFullyLoadedCurrency() {
		return playerInitialized;
	}

	public int getBlocksDestroyed() {
		return blocksDestroyed;
	}

	public int getMobsKilled() {
		return mobsKilled;
	}

	public boolean getNewsReadStatus() {
		return needsToReadNews;
	}

	public int getCurrencyToNotify() {
		return currencyToNotify;
	}

	public int getCurrencySkipNotifyTimes() {
		return currencySkipNotifyTimes;
	}

	public void setTempConnectionTime(long tempConnectionTime) {
		this.tempConnectionTime = tempConnectionTime;
	}

	public void setRegistered(boolean isRegistered) {
		this.isRegistered = isRegistered;
	}

	public void setActivated(boolean isActivated) {
		this.isActivated = isActivated;
	}

	public void setChatMuted(boolean isChatMuted) {
		this.isChatMuted = isChatMuted;
	}

	public void setChatMuteInitiationTime(long chatMuteInitiationTime) {
		this.chatMuteInitiationTime = chatMuteInitiationTime;
	}

	public void setChatWarnTimes(int chatWarnTimes) {
		this.chatWarnTimes = chatWarnTimes;
	}

	public void setChats(ChatBundle chats) {
		this.chats = chats;
	}

	public void setUsingNickname(boolean isUsingNickname) {
		this.isUsingNickname = isUsingNickname;
	}

	public void setCurrency(int currency) {
		this.currency = currency;
	}

	public void setHasFullyLoadedCurrency(boolean hasFullyLoadedCurrency) {
		this.playerInitialized = hasFullyLoadedCurrency;
	}

	public void setBlocksDestroyed(int blocksDestroyed) {
		this.blocksDestroyed = blocksDestroyed;
	}

	public void setMobsKilled(int mobsKilled) {
		this.mobsKilled = mobsKilled;
	}

	public void setInventoryStore(InventoryStore inventoryStore) {
		this.inventoryStore = inventoryStore;
	}

	public void setRawAwardStore(RawAwardStore rawAwardStore) {
		this.rawAwardStore = rawAwardStore;
	}

	public void setNeedsToReadNews(boolean needsToReadNews) {
		this.needsToReadNews = needsToReadNews;
	}

	public void setCurrencyToNotify(int currencyToNotify) {
		this.currencyToNotify = currencyToNotify;
	}

	public void setCurrencySkipNotifyTimes(int currencySkipNotifyTimes) {
		this.currencySkipNotifyTimes = currencySkipNotifyTimes;
	}

	public void setAwardStore(AwardStore awardStore) {
		this.awardStore = awardStore;
	}

	/**
	 * Denotes if the player is registered
	 */
	private boolean isRegistered = false;

	/**
	 * Denotes if the player is activated
	 */
	private boolean isActivated = false;

	/**
	 * Denotes if the player is currently chat muted
	 */
	private boolean isChatMuted = false;

	/**
	 * Denotes how long the chat mute lasts
	 */
	private long chatMuteInitiationTime = 0;

	/**
	 * Denotes how many times the player has been warned about spamming chat
	 */
	private int chatWarnTimes = 0;

	/**
	 * Recent chats
	 */
	private ChatBundle chats = new ChatBundle();

	/**
	 * Denotes if the player is or isn't using a nickname
	 */
	private boolean isUsingNickname = false;

	/**
	 * The player's current amount of currency
	 */
	private int currency = 0;

	/**
	 * A precaution to prevent the save loop from killing your silver amount if
	 * you join at the wrong time.
	 */
	private boolean playerInitialized = false;

	/**
	 * The player's group
	 */
	private String group;

	/**
	 * Number of blocks destroyed & logged by the currency plugin
	 */
	private int blocksDestroyed = 0;

	/**
	 * Number of mobs killed & logged by the currency plugin
	 */
	private int mobsKilled = 0;

	/**
	 * The player's title
	 */
	private String chatPrefix;

	/**
	 * The player's chat color
	 */
	private String chatColor;

	/**
	 * The player's inventory
	 */
	private InventoryStore inventoryStore;

	/**
	 * The player's raw award store
	 */
	private RawAwardStore rawAwardStore;
	
	/**
	 * The player's cached chat format
	 */
	private String chatFormat;

	/**
	 * Does the player need to read the news?
	 */
	private boolean needsToReadNews = false;
	
	/**
	 * For currency overflow
	 */
	
	private int currencyToNotify = 0;
	private int currencySkipNotifyTimes = 0;

	/**
	 * Gets the player's raw award store
	 */
	public RawAwardStore getRawAwardStore() {
		return rawAwardStore;
	}

	private AwardStore awardStore;

	public AwardStore getAwardStore() {
		return awardStore;
	}

	/**
	 * Stores awards that the player has already earned
	 */
	public class AwardStore {
		private ArrayList<AwardType> awards;

		public AwardStore() {
			awards = new ArrayList<AwardType>();
		}

		public AwardStore(ArrayList<AwardType> awards) {
			this.awards = awards;
		}

		public void addAward(AwardType type) {
			this.awards.add(type);
		}

		public boolean hasAward(AwardType type) {
			if (awards.contains(type)) {
				return true;
			}
			return false;
		}

		public ArrayList<AwardType> getAwards() {
			return awards;
		}
	}

	/**
	 * A raw award store, used for storing awards in progress. This is not
	 * intended to store awards that have been awarded already, but those who
	 * are still gaining progress
	 */
	public class RawAwardStore {
		private ArrayList<RawAward> awards;

		public RawAwardStore(ArrayList<RawAward> awards) {
			this.awards = awards;
		}

		public RawAwardStore() {
			awards = new ArrayList<RawAward>();
		}

		public ArrayList<RawAward> getRawAwards() {
			return awards;
		}

		public void addRawAward(RawAward award) {
			awards.add(award);
		}

		public void delRawAward(AwardType type) {
			for (RawAward a : awards) {
				if (a.getType() == type) {
					awards.remove(a);
				}
			}
		}

		public void setRawAwards(ArrayList<RawAward> awards) {
			this.awards = awards;
		}

		public boolean updateAward(ArrayList<Award> storedAwards,
				AwardType type, float amount) {
			CounterAward award = null;

			for (Award a : storedAwards) {
				if (a instanceof CounterAward && a.getType() == type) {
					award = (CounterAward) a;
				}
			}

			int awardCount = 0;

			for (RawAward r : awards) {
				if (r.getType() == type) {
					awardCount++;
				}
			}

			if (awardCount == 0) {
				awards.add(new RawAward(type, amount));
				return false;
			}

			for (RawAward r : awards) {
				if (r.getType() == award.getType()) {
					r.addMetaData(amount);
					if (r.getMetaData() >= award.getMaxMetaData()) {
						awards.remove(r);
						return true;
					} else {
						return false;
					}
				}
			}
			return false;
		}
	}

	public class InventoryStore {
		private ArrayList<ItemType> inventory;

		public InventoryStore(ArrayList<ItemType> inventory) {
			this.inventory = inventory;
		}

		public InventoryStore() {
			this.inventory = new ArrayList<ItemType>();
		}

		public ArrayList<ItemType> getInventory() {
			return inventory;
		}

		public void addItem(Item i) {
			if (!inventory.contains(i.getItemType())) {
				inventory.add(i.getItemType());
			}
		}

		public void delItem(Item i) {
			inventory.remove(i.getItemType());
		}

		public boolean hasItem(ItemType i) {
			for (ItemType f : inventory) {
				if (f == i) {
					return true;
				}
			}
			return false;
		}

	}
	
	public class SmartInventoryStore {
		private ArrayList<SimpleItem> inventory;
		
		public SmartInventoryStore(ArrayList<SimpleItem> inventory) {
			this.inventory = inventory;
		}
		
		public SmartInventoryStore() {
			this.inventory = new ArrayList<SimpleItem>();
		}
		
		public boolean addItem(SimpleItem i) {
			for (SimpleItem simpleItem : inventory) {
				if (i.getItemType() == simpleItem.getItemType()) {
					inventory.remove(simpleItem);
				}
			}
			
			return inventory.add(i);
		}
		
		public boolean removeItem(SimpleItem i) {
			return inventory.remove(i);
		}
		
		public SimpleItem getItem(ItemType itemType) throws ItemNotFoundException {
			for (SimpleItem i : inventory) {
				if (i.getItemType() == itemType) {
					return i;
				}
			}
			
			throw new ItemNotFoundException("The requested inventory item was not found in this SmartInventoryStore.");
		}
		
		public int useItem(ItemType itemType) throws ItemNotFoundException {
			for (SimpleItem si : inventory) {
				if (si.getItemType() == itemType) {
					if (si.getUses() == -1) {
						return -1;
					} else {
						si.setUses(si.getUses() - 1);
						
						if (si.getUses() == 0) {
							inventory.remove(si);
						}
						return 0;
					}
				}
			}
			throw new ItemNotFoundException("The requested item was not found in this SmartInventoryStore.");
		}
	}

	public SPlayer(Registration instance, Player ply, boolean registered,
			boolean activated) {
		isRegistered = registered;
		plugin = instance;
		player = ply;
		isActivated = activated;
		getChatPrefix();
		long connectionTime = System.currentTimeMillis()/1000;
		setConnectionTime(connectionTime);
		setOriginalConnectionTime(connectionTime);
	}

	public SPlayer(Registration instance, Player ply, boolean registered) {
		isRegistered = registered;
		plugin = instance;
		player = ply;
		getChatPrefix();
		long connectionTime = System.currentTimeMillis()/1000;
		setConnectionTime(connectionTime);
		setOriginalConnectionTime(connectionTime);
	}

	/**
	 * Gets the player's group
	 * 
	 * @return String - Player's group
	 */
	public String getGroup() {
		return this.group;
	}

	/**
	 * Sets the player's group
	 */
	public void setGroup(String group) {
		this.group = group;
	}

	/**
	 * Gets the Bukkit player object.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the server object.
	 */
	public Server getServer() {
		return plugin.getServer();
	}

	/**
	 * Sends a player a message in green.
	 */
	public void sendMessage(String msg) {
		player.sendMessage(ChatColor.GREEN + msg);
	}

	/**
	 * Sends a player a message in yellow.
	 */
	public void sendWarning(String msg) {
		player.sendMessage(ChatColor.YELLOW + msg);
	}

	/**
	 * Sends a player a message in red.
	 */
	public void sendError(String msg) {
		player.sendMessage(ChatColor.RED + msg);
	}

	/**
	 * Returns a ChatBundle object of the player's latest chat messages.
	 */
	public ChatBundle getChatBundle() {
		return chats;
	}

	/**
	 * Adds a chat message to the player's chat pool for spam checking.
	 */
	public void addChatMessage(ChatMessage c) {
		chats.addChat(c);
	}

	/**
	 * Runs a chat spam check, which will then take action if the player is
	 * repeating themselves.
	 */
	public void runSpamChecks() {
		int repeats = 0;
		ChatMessage lastIteration = null;
		for (ChatMessage c : chats.getChats()) {
			if (lastIteration == null) {
				lastIteration = c;
				continue;
			} else {
				if (lastIteration.time + 3 > c.time
						&& lastIteration.msg.equals(c.msg)) {
					repeats++;
					lastIteration = c;
				}
			}
		}

		if (repeats > 2) {
			sendWarning("You're only spamming yourself.");
			chatWarnTimes++;
			isChatMuted = true;
			chatMuteInitiationTime = System.currentTimeMillis() / 1000;
		}

		if (chatWarnTimes > 7) {
            //TODO: ADD SSS INTEGRATION HERE
		}
	}

	/**
	 * Saves the player's inventory & currency.
	 */
	public void saveAccount() {
		try {
			player.saveData();
			if (!playerInitialized) {
				return;
			}
			saveInventory();
			saveCurrency();
			saveAwards();
			saveRawAwards();
			savePlaytime();
		} catch (Exception e) {
			e.printStackTrace();
			sendError("ShankShock account save failed!");
		}
	}

	/**
	 * Returns the player's chat prefix, either from the database or ingame
	 * 
	 * @return String - The chat prefix (with color codes)
	 */
	public String getChatPrefix() {
		if (chatPrefix == null) {
			ResultSet r = plugin.sqldb.executeQuery(
					"SELECT `Prefix` from `prefix` WHERE `Username`=?",
					getPlayer().getName());
			try {
				r.next();
				chatPrefix = r.getString("prefix").trim();
			} catch (Exception e) {
				chatPrefix = "";
			}
		}
		return chatPrefix;
	}

	/**
	 * Sets a player's chat prefix.
	 * 
	 * @param prefix
	 *            - The prefix the player should have
	 */
	public boolean setChatPrefix(String prefix) {
		boolean success = plugin.sqldb
				.executeSQL(
						"INSERT INTO `prefix` (`Username`, `Prefix`) VALUES(?,?) ON DUPLICATE KEY UPDATE `Prefix`=?",
						getPlayer().getName(), prefix, prefix);
		chatPrefix = prefix;
		return success;
	}

	/**
	 * Gets the player's chat color to be added in prior to all chat messages
	 * from that player.
	 * 
	 * @return color - A string containing a chat color
	 */
	public String getChatColor() {
		if (chatColor == null) {
			ResultSet r = plugin.sqldb.executeQuery(
					"SELECT `color` from `chatcolor` WHERE `Username`=?",
					getPlayer().getName());
			try {
				r.next();
				chatColor = r.getString("color").trim();
			} catch (Exception e) {
				chatColor = ChatColor.WHITE + ""; //wat
			}
		}
		return chatColor;
	}

	/**
	 * Sets a player's chat color, inserting it if necessary
	 * 
	 * @param color
	 *            - the string of color that should be inserted
	 * @return true, if it was set properly
	 */
	public boolean setChatColor(String color) {
		chatColor = color;
		return plugin.sqldb
				.executeSQL(
						"INSERT INTO `chatcolor` (`Username`, `color`) VALUES(?,?) ON DUPLICATE KEY UPDATE `color`=?",
						getPlayer().getName(), color, color);
	}

	/**
	 * Notifies the player of a currency change
	 * 
	 * @param amount
	 *            - The amount of currency changing
	 * @param gain
	 *            - Is the currency change a gain?
	 */
	public void notifyCurrencyChange(int amount, boolean gain) {
		if (gain) {
			
			boolean smallAmount = false;
			if (amount < 100) {
				currencyToNotify += amount;
				currencySkipNotifyTimes++;
				smallAmount = true;
			}
			
			if (smallAmount && (currencySkipNotifyTimes < 10)) {
				return;
			}
			
			if (currencySkipNotifyTimes >= 10 && smallAmount) {
				amount = currencyToNotify;
				currencyToNotify = 0;
				currencySkipNotifyTimes = 0;
			}
			
			if (!smallAmount) {
				amount += currencyToNotify;
				currencyToNotify = 0;
				currencySkipNotifyTimes = 0;
			}
			
			sendMessage(ChatColor.GOLD + "You earned " + ChatColor.GRAY
					+ NumberFormat.getInstance().format(amount)
					+ ChatColor.GRAY + " silver" + ChatColor.GOLD + " pieces.");
		} else {
			sendMessage(ChatColor.GOLD + "You lost " + ChatColor.GRAY
					+ NumberFormat.getInstance().format(amount)
					+ ChatColor.GRAY + " silver" + ChatColor.GOLD + " pieces.");
		}
		
		SilverScoreboard ssb = plugin.getSilverScoreboard();
		
		ssb.updateBoard(player.getName());
		
	}

	/**
	 * Notifies the player of their current currency amount
	 */
	public void notifyCurrency() {
		sendMessage(ChatColor.GOLD + "You currently have " + ChatColor.GRAY
				+ NumberFormat.getInstance().format(currency) + ChatColor.GRAY
				+ " silver" + ChatColor.GOLD + " pieces.");
	}

	/**
	 * Saves the player's currency
	 */
	public boolean saveCurrency() {
		return plugin.sqldb.executeSQL(
				"UPDATE `funds` SET `Amount`=? WHERE `Username`=?", currency,
				player.getName());
	}

	/**
	 * Creates a new currency object in the database for the player
	 */
	public boolean createCurrency() {
		int currency = 0;
		boolean success;
		success = plugin.sqldb.executeSQL("INSERT INTO `funds` VALUES(?, ?)",
				player.getName(), currency);
		if (success) {
			this.currency = currency;
			notifyCurrencyChange(currency, true);
			plugin.getServer()
					.getLogger()
					.log(Level.INFO,
							"Silver Transaction: " + getPlayer().getName()
									+ " now has a silver account.");
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Loads a player's currency, sets up the currency store, and if needed,
	 * creates a new currency object
	 */
	public void initialize() {
		loadCurrency();
		loadInventory();
		loadAwards();
		loadRawAwards();

		if (currency > 1000000) {
			plugin.awardManager.addAward(AwardType.ROCKEFELLER, getPlayer());
		}
		
		SilverScoreboard ssb = plugin.getSilverScoreboard();
		
		ssb.updateBoard(player.getName());
		
		playerInitialized = true;
	}

	private void loadCurrency() {
		try {
			ResultSet r = plugin.sqldb.executeQuery(
					"SELECT `amount` FROM `funds` WHERE `Username`=?",
					player.getName());
			try {
				r.next();
				int currency = r.getInt("Amount");
				this.currency = currency;
			} catch (SQLException e) {
				createCurrency();
			}
		} catch (Exception e) {
			kickPlayer("Unexpected DB fail. Reconnect.");
		}
	}

	/**
	 * Creates a player's inventory if it doesn't exist
	 */
	public boolean createInventory() {
		inventoryStore = new InventoryStore();
		Gson gson = new Gson();
		String jsonInventory = gson.toJson(inventoryStore);
		return plugin.sqldb.executeSQL("INSERT INTO `inventories` VALUES(?,?)",
				getPlayer().getName(), jsonInventory);
	}

	/**
	 * Saves a player's inventory to the server (or creates it if necessary)
	 */
	public boolean saveInventory() {
		Gson gson = new Gson();
		String jsonInventory = gson.toJson(inventoryStore);
		return plugin.sqldb.executeSQL(
				"UPDATE `inventories` SET `Inventory`=? WHERE `Username`=?",
				jsonInventory, player.getName());
	}

	/**
	 * Saves a player's playtime and changes their connectionTime to reflect it.
	 * @return result - the player's new total connection minutes
	 */
	public long savePlaytime() {
		long currentTime = System.currentTimeMillis()/1000;
		long diffTime = currentTime - getConnectionTime();
		long minutesPlayed = diffTime/60;
		
		long result = plugin.redisDatabase.getRedis().incrby("mc:player:" + getPlayer().getName() + ":playmins", minutesPlayed);
		
		setConnectionTime(currentTime);
		
		long diffTotalTime = currentTime - getOriginalConnectionTime();
		if (diffTotalTime >= 28800) {
			plugin.awardManager.addAward(AwardType.MARATHON, getPlayer());
		}
		
		if (diffTotalTime >= 14400) {
			plugin.awardManager.addAward(AwardType.MINI_MARATHON, getPlayer());
		}
		
		return result;
	}
	
	/**
	 * Loads a player's inventory from the server
	 */
	public void loadInventory() {
		Gson gson = new Gson();
		try {
			String jsonInventory = "";
			ResultSet r = plugin.sqldb.executeQuery(
					"SELECT `Inventory` FROM `inventories` WHERE `Username`=?",
					player.getName());
			if (r.next()) {
				jsonInventory = r.getString("Inventory");
				inventoryStore = gson.fromJson(jsonInventory,
						InventoryStore.class);
			} else {
				createInventory();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a player's awards if it doesn't exist
	 */
	public void createAwards() {
		awardStore = new AwardStore();
		Gson gson = new Gson();
		String jsonAwards = gson.toJson(awardStore);
		plugin.sqldb.executeSQL("INSERT INTO `earnedawards` VALUES(?,?)",
				player.getName(), jsonAwards);
	}

	/**
	 * Saves a player's awards to the server (or creates it if necessary)
	 */
	public void saveAwards() {
		Gson gson = new Gson();
		String jsonAwards = gson.toJson(awardStore);
		plugin.sqldb.executeSQL(
				"UPDATE `earnedawards` SET `Awards`=? WHERE `Username`=?",
				jsonAwards, player.getName());
	}

	/**
	 * Loads a player's awards from the server
	 */
	public void loadAwards() {
		Gson gson = new Gson();
		try {
			String jsonAwards = "";
			ResultSet r = plugin.sqldb.executeQuery(
					"SELECT `Awards` FROM `earnedawards` WHERE `Username`=?",
					player.getName());

			if (r.next()) {
				jsonAwards = r.getString("Awards");
				awardStore = gson.fromJson(jsonAwards, AwardStore.class);

			} else {
				createAwards();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a player's awards if it doesn't exist
	 */
	public void createRawAwards() {
		rawAwardStore = new RawAwardStore();
		Gson gson = new Gson();
		String jsonAwards = gson.toJson(rawAwardStore);
		plugin.sqldb.executeSQL("INSERT INTO `inprogressawards` VALUES(?,?)",
				player.getName(), jsonAwards);
	}

	/**
	 * Saves a player's awards to the server (or creates it if necessary)
	 */
	public void saveRawAwards() {
		Gson gson = new Gson();
		String jsonAwards = gson.toJson(rawAwardStore);
		plugin.sqldb
				.executeSQL(
						"UPDATE `inprogressawards` SET `RawAwards`=? WHERE `Username`=?",
						jsonAwards, player.getName());
	}

	/**
	 * Loads a player's awards from the server
	 */
	public void loadRawAwards() {
		Gson gson = new Gson();
		try {
			String jsonAwards = "";
			ResultSet r = plugin.sqldb
					.executeQuery(
							"SELECT `RawAwards` FROM `inprogressawards` WHERE `Username`=?",
							player.getName());

			if (r.next()) {
				jsonAwards = r.getString("RawAwards");
				rawAwardStore = gson.fromJson(jsonAwards, RawAwardStore.class);
			} else {
				createRawAwards();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets a player's inventory store
	 */
	public InventoryStore getInventoryStore() {
		return inventoryStore;
	}

	/**
	 * Invites a player to pure using this player's invitations.
	 * 
	 * @param ply
	 *            - The player to be invited's name
	 * @param invites
	 *            - The number of invites to give to that player
	 * @return bool - If the invite was a success
	 */
	public boolean invitePlayerToPure(String ply, int invites) {
		return plugin.pureEngine.attemptPlayerInvite(this, ply, invites);
	}

	/**
	 * Determins if a player has access to pure.
	 * 
	 * @return bool - If the player has pure access
	 */
	public boolean hasPureAccess() {
		return plugin.pureEngine.isPlayerInvited(player.getName());
	}

	/**
	 * Determines if the player is a hidden admin
	 * 
	 * @return bool - If the player is a hidden admin or not
	 */
	public boolean isHiddenAdmin() {
		return isHiddenAdmin;
	}

	/**
	 * Sets the player's hidden admin status
	 * 
	 * @param isHiddenAdmin
	 *            - If the player should be a hidden admin
	 */
	public void setHiddenAdmin(boolean isHiddenAdmin) {
		this.isHiddenAdmin = isHiddenAdmin;
	}

	public void addCurrency(int currency, boolean pure) {
		if (pure) {
			currency = currency * 2;
		}

		this.currency = this.currency + currency;
		this.notifyCurrencyChange(currency, true);
	}

	/**
	 * Adds currency to the player, and notifies them of the change
	 * 
	 * @param currency
	 *            - The amount of currency to add to the player.
	 */
	public void addCurrency(int currency) {

		if (this.player.getWorld().getName().contains("stock")) {
			currency = currency * 2;
		}

		this.currency = this.currency + currency;
		this.notifyCurrencyChange(currency, true);
	}

	/**
	 * Removes currency from the player, and notifies them of the change.
	 * 
	 * @param currency
	 *            - The amount of currency to deduct.
	 */
	public void delCurrency(int currency) {
		this.currency = this.currency - currency;
		this.notifyCurrencyChange(currency, false);
	}

	/**
	 * Ticks the number of blocks that the player has destroyed for use in the
	 * currency system.
	 */
	public void tickBlocksDestroyed() {
		this.blocksDestroyed++;

		if (this.blocksDestroyed > 100) {
			addCurrency(10);
			this.blocksDestroyed = 0;
		}
	}

	/**
	 * Ticks the number of mobs destroyed by the player for use in the currency
	 * system.
	 */
	public void tickMobsDestroyed() {
		this.mobsKilled++;

		if (this.mobsKilled > 100) {
			addCurrency(69);
			this.mobsKilled = 0;
		}
	}

    public boolean getAdminSeeAll() {
        if (adminSeeAll == null) {
            if (plugin.redisDatabase.getRedis().exists("mc:player:" + this.getPlayer().getName() + ":asa")) {
                adminSeeAll = true;
                return true;
            } else {
                adminSeeAll = false;
                return false;
            }
        } else {
            return adminSeeAll;
        }
    }

    private Boolean adminSeeAll = null;

	public void kickPlayer(String reason) {
		getPlayer().kickPlayer(reason);
	}

	public String getChatFormat() {
		return chatFormat;
	}

	public void setChatFormat(String chatFormat) {
		this.chatFormat = chatFormat;
	}

}
