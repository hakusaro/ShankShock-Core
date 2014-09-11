/*
 * 
 */
package com.shankshock.nicatronTg.Registration.Engines;

import com.shankshock.nicatronTg.Registration.Awards.AwardManager.AwardType;
import com.shankshock.nicatronTg.Registration.Misc.ChatColorPair;
import com.shankshock.nicatronTg.Registration.Misc.Permission;
import com.shankshock.nicatronTg.Registration.Misc.PermissionSet;
import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.SPlayer;
import com.shankshock.nicatronTg.Registration.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class DatabaseConnectionEngine {
	private final Registration plugin;
	public String groups[] = {};
	public ArrayList<PermissionSet> permissionSets = new ArrayList<PermissionSet>();
	public ArrayList<ChatColorPair> chatColors = new ArrayList<ChatColorPair>();
	public HashMap<Player, PermissionAttachment> permissions = new HashMap<Player, PermissionAttachment>();
	public String settingsLoader[] = {};

	public DatabaseConnectionEngine(Registration instance) {
		plugin = instance;
	}

	public void loadGroups() {
		String unparsedGroups = plugin.globalConfig.getValue("user.groups");
		groups = unparsedGroups.split(",");

		for (int i = 0; i < groups.length; i++) {
			groups[i] = groups[i].trim();
		}

		System.out.println("Redis: Group load for " + groups.length + " groups completed.");
	}

	public void loadPermissions() {
		for (int i = 0; i < groups.length; i++) {
			setupPermissions(groups[i]);
		}
		System.out.println("MySQL: Permission load for " + groups.length + " groups completed.");
	}

	public void setupChatColors() {
		try {
			ResultSet r = plugin.sqldb.executeQuery("SELECT * FROM `chatcolors`");
			ArrayList<ChatColorPair> chatColors = new ArrayList<ChatColorPair>();
			while (r.next() && !r.isAfterLast()) {
				chatColors.add(new ChatColorPair(r.getString(1), r.getString(2)));
			}
			this.chatColors = chatColors;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setupPermissions(String group) {
		try {
			ResultSet r = plugin.sqldb.executeQuery("SELECT * FROM `permissions2` WHERE `Group`=?", group);
			ArrayList<Permission> permissions = new ArrayList<Permission>();
			while (r.next()) {
				permissions.add(new Permission(r.getString("Permission"), r.getBoolean("revoke")));
			}
			PermissionSet perm = new PermissionSet(group, permissions);
			permissionSets.add(perm);
			System.out.println("Loaded " + perm.permissions.size() + " permissions for group " + group + ".");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public String getRegDate(String ply) {
		ResultSet r = plugin.sqldb.executeQuery("SELECT `regdate` FROM `data` WHERE `Username`=?", ply);

		try {
			if (r.isBeforeFirst() && !r.isAfterLast()) {
				r.next();
				long epoch = r.getLong("regdate");

				String regdate = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date(epoch * 1000));
				return regdate;
			}
		} catch (SQLException e) {
			return "Never";
		}
		return "Never";
	}

	public boolean addPlayerToDatabase(Player ply, String group) {
		String username = ply.getName();
		boolean status;
		status = plugin.sqldb.executeSQL("INSERT INTO `data` VALUES(?, ?, ?, ?, ?, ?, ?)", username, "x", group, "", "null", Utils.getUnixEpoch(), 1);

		plugin.players.get(ply.getName()).setRegistered(true);
		plugin.players.get(ply.getName()).setActivated(true);
        plugin.redisDatabase.getRedis().set("mc:player:" + ply.getName() + ":asa", "true");
        plugin.redisDatabase.getRedis().expire("mc:player:" + ply.getName() + ":asa", 172800);
		return status;
	}

	/**
	 * Determines if the player exists in the database.
	 * 
	 * @param username
	 *            - The player's username
	 * @return true - If the player has a database entry
	 */
	public boolean playerExists(String username) {
		ResultSet r = plugin.sqldb.executeQuery("SELECT Username FROM `data` WHERE Username=?", username);
		try {
			r.next();
			if (r.getString("username").equals(username)) {
				return true;
			}
		} catch (SQLException e) {
			return false;
		}
		return false;
	}

	public String nickname(SPlayer ply) {
		try {
			ResultSet r = plugin.sqldb.executeQuery("SELECT * FROM `data` WHERE Username=?", ply.getPlayer().getName());
			r.next();
			String unparsed = r.getString("nickname");
			unparsed = unparsed.replace("§", "");
			unparsed = unparsed.replace(" ", "");
			if (unparsed.length() > 16) {
				unparsed = "";
				return ply.getPlayer().getName();
			} else if (unparsed.equals("")) {
				unparsed = ply.getPlayer().getName();
			}
			ply.setUsingNickname(true);
			return unparsed;
		} catch (SQLException e) {
			return ply.getPlayer().getName();
		}
	}

	public void setupInvisiblePlayers(Player ply) {
		SPlayer sply = plugin.players.get(ply.getName());

		if (sply.getChatPrefix().contains("500")) {
			ply.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 7000, 3), true);
		}
	}
	
	public void releaseFromJail(Player ply) {
		final String playerName = ply.getName();
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			public void run() {
				String key = "mc:player:" + playerName + ":jailendtime";
				if (!plugin.redisDatabase.getRedis().exists(key)) {
					return;
				}
				
				long jailEndTime = Long.parseLong(plugin.redisDatabase.getRedis().get(key));
				
				if (jailEndTime <= System.currentTimeMillis()/1000) {
					plugin.redisDatabase.getRedis().del("mc:player:" + playerName + ":jailendtime");
					
					plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
						public void run() {
							Player ply = plugin.getServer().getPlayer(playerName);
							ply.teleport(new Location(plugin.getServer().getWorld("panem"), 75450, 67, 55525));
							ply.sendMessage(ChatColor.GREEN + "You have been released from jail automatically.");
						}
					});
				}
			}
		});
	}

	public void initialJoinSetup(Player ply) {
		try {
			if (playerExists(ply.getName())) {
				if (ply.isOp()) {
					ply.setOp(false);
				}

				if (!isActivated(ply)) {
					plugin.players.put(ply.getName(), new SPlayer(plugin, ply, true, false));
					SPlayer sply = plugin.players.get(ply.getName());
				} else {
					plugin.players.put(ply.getName(), new SPlayer(plugin, ply, true, true));
				}
				SPlayer sply = plugin.players.get(ply.getName());
				assignPlayerPermissions(ply);
				String nick = nickname(plugin.players.get(ply.getName()));
				ply.setDisplayName(nick);
				setPlayerListName(ply, true);
				sply.initialize();
				sply.sendMessage(ChatColor.AQUA + "Welcome to " + ChatColor.GOLD + plugin.getConfig().getString("serverid") + ChatColor.AQUA + ", " + ChatColor.GREEN + ply.getDisplayName() + ChatColor.AQUA + ".");
				sendNewsNotification(ply);
				setupInvisiblePlayers(ply);

				if (ply.hasPermission("shankshock.autorun")) {
					plugin.autorunEngine.run(ply);
				}

				if (getGroup(ply).equals("HiddenAdmin")) {
					sply.setHiddenAdmin(true);
				} else {
					sply.setHiddenAdmin(false);
				}

				if (sply.isActivated()) {
					plugin.awardManager.addAward(AwardType.ACTIVATION, ply);
				}

				if (sply.getAwardStore().getAwards().contains(AwardType.THE_END)) {
					plugin.awardManager.addAward(AwardType.LIVING_DEAD, ply);
				}
				cacheChatFormat(sply);
				releaseFromJail(ply);

			} else {

                if (plugin.redisDatabase.getRedis().exists("mc:register:lock")) {

                    ply.sendMessage(ChatColor.RED + "The server is temporarily not accepting new players.".toUpperCase());
                    ply.sendMessage(ChatColor.RED + "You will be disconnected.");
                    final String playerName = ply.getName();
                    plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                        @Override
                        public void run() {
                            plugin.getServer().getPlayer(playerName).kickPlayer("Apply for whitelist at http://shankshock.com/");
                            return;
                        }
                    }, 1);
                    return;
                }

				assignPlayerPermissions(ply);
				plugin.players.put(ply.getName(), new SPlayer(plugin, ply, false, false));

				addPlayerToDatabase(ply, "Default");
				SPlayer sply = plugin.players.get(ply.getName());
				sendNewsNotification(ply);
				setPlayerListName(ply, false);
				sply.initialize();
				sply.sendMessage("Welcome to " + plugin.getConfig().getString("serverid") + ", a ShankShock production.");
				sply.sendMessage("You've been given a rule book with the rules & some command info.");
				sply.sendMessage("You're standing on the spawn island - to leave, search for a sign that will have a command to run.");
				sply.sendMessage("With that, welcome to ShankShock.");
				sply.sendMessage("Feedback? Questions? Email Shank, shank@shankshock.com");
				plugin.awardManager.addAward(AwardType.ACTIVATION, ply);
				RuleBookItem rbi = new RuleBookItem();
				rbi.giveItem(ply, plugin);
				cacheChatFormat(sply);
				setupInvisiblePlayers(ply);
			}
		} catch (Exception e) {
			e.printStackTrace();
			
			final String playerName = ply.getName();
			
			plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
				public void run() {
					plugin.getServer().getPlayer(playerName).kickPlayer("A temporary server error has occurred. Please rejoin.");
				}
			});
		}
	}

	public void cacheChatFormat(SPlayer sply) {
		for (ChatColorPair c : plugin.conn.chatColors) {
			if (sply.getPlayer().hasPermission(c.getPermission())) {
				if (sply.getChatPrefix().length() <= 0) {
					sply.setChatFormat(c.getColor() + plugin.globalConfig.getValue("chat.format"));
				} else {
					sply.setChatFormat("「" + sply.getChatPrefix() + ChatColor.WHITE + "」" + c.getColor() + plugin.globalConfig.getValue("chat.format"));
				}
				return;
			}
		}
	}

	public void sendNewsNotification(Player ply) {
		if (!plugin.newsEngine.newsCheck(ply)) {
			plugin.players.get(ply.getName()).sendMessage(ChatColor.RED + "BREAKING NEWS!" + ChatColor.AQUA + " Type " + ChatColor.GOLD + "/news " + ChatColor.AQUA + "to view the latest changelog.");
			plugin.players.get(ply.getName()).setNeedsToReadNews(true);
		}
	}

	public void setPlayerListName(Player ply, boolean nick) {

		String base = (nick ? ply.getDisplayName() : ply.getName());

		for (ChatColorPair c : plugin.conn.chatColors) {
			if (ply.hasPermission(c.getPermission())) {
				String name = c.getColor() + base;
				if (name.length() > 16) {
					ply.setPlayerListName((nick ? ply.getDisplayName()
							: ply.getName()));
				} else {
					ply.setPlayerListName(name);
				}
			}
		}
	}

	public void reloadPermissions() {
		permissionSets.clear();
		for (int i = 0; i < groups.length; i++) {
			setupPermissions(groups[i]);
		}

		for (int i = 0; i < plugin.getServer().getOnlinePlayers().length; i++) {
			Player ply = plugin.getServer().getOnlinePlayers()[i];
			ply.removeAttachment(permissions.get(ply));
			permissions.remove(ply);
			assignPlayerPermissions(ply);
		}
	}

	public String getGroup(Player ply) {
		String str = "";

		SPlayer sply = plugin.players.get(ply.getName());

		if (sply == null) {
			if (!playerExists(ply.getName())) {
				str = "Default";
				return str;
			} else {
				try {
					ResultSet r = plugin.sqldb.executeQuery("SELECT * FROM `data` WHERE `Username`=?", ply.getName());
					r.next();
					str = r.getString("Group");
					return str;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return str;
		}

		if (sply.getGroup() == null) {
			if (!playerExists(ply.getName())) {
				str = "Default";
				return str;
			} else {
				try {
					ResultSet r = plugin.sqldb.executeQuery("SELECT * FROM `data` WHERE `Username`=?", ply.getName());
					r.next();
					str = r.getString("Group");
					sply.setGroup(str);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else {
			return sply.getGroup();
		}

		return str;
	}

	public boolean isActivated(Player ply) {
		int activated = 0;
		if (!playerExists(ply.getName())) {
			return false;
		} else {
			try {
				ResultSet r = plugin.sqldb.executeQuery("SELECT * FROM `data` WHERE `Username`=?", ply.getName());
				r.next();
				activated = r.getInt("activated");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (activated == 1) {
			return true;
		}
		return false;
	}

	public PermissionSet assignPlayerPermissions(Player ply, String group) {
		PermissionSet assignedBukkitPermissions = new PermissionSet();
		for (PermissionSet permissionSet : permissionSets) {
			if (permissionSet.group.equalsIgnoreCase(group)) {
				for (Permission p : permissionSet.permissions) {
					if (p.permission.startsWith("shankshock.internal.inherit.")) {
						// Permissions starting with shankshock.internal.inherit
						// are treated as groups, and will be assigned
						// accordingly
						for (Permission perm : assignPlayerPermissions(ply, p.permission.replace("shankshock.internal.inherit.", "")).permissions) {
							if (perm.permission.startsWith("group.")) {
								continue; // Ignore chat color based groups
							}
							assignedBukkitPermissions.add(perm);
						}
						continue;
					} else {
						assignedBukkitPermissions.add(p);
					}
				}
			}
		}
		return assignedBukkitPermissions;
	}

	public void assignPlayerPermissions(Player ply) {
		String group = getGroup(ply);

		PermissionAttachment bukkitPermissionAttachment = ply.addAttachment(plugin);
		permissions.put(ply, bukkitPermissionAttachment);
		for (Permission p : assignPlayerPermissions(ply, group).permissions) {
			bukkitPermissionAttachment.setPermission(p.permission, !p.revoke); // Invert
																				// revocation
		}
	}
}
