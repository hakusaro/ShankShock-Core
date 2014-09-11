package com.shankshock.nicatronTg.Registration;

import com.shankshock.Redis.Messages.WhoRequest;
import com.shankshock.Redis.WorldConfig;
import com.shankshock.nicatronTg.Registration.Awards.AwardManager.AwardType;
import com.shankshock.nicatronTg.Registration.Engines.RuleBookItem;
import com.shankshock.nicatronTg.Registration.Items.Item;
import com.shankshock.nicatronTg.Registration.Items.ItemType;
import com.shankshock.nicatronTg.Registration.Items.SilverManager;
import com.shankshock.nicatronTg.Registration.Misc.DonatorLevel;
import com.shankshock.nicatronTg.Registration.Misc.ServerIncrementialSaver;
import com.shankshock.nicatronTg.Registration.Misc.TimeTimeoutUpdater;
import com.shankshock.nicatronTg.Registration.Misc.TimedTeleporter;
import com.shankshock.nicatronTg.Registration.Zones.Zone;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class RegistrationCommandExecutor implements CommandExecutor {
	private final Registration plugin;

	public RegistrationCommandExecutor(Registration instance) {
		plugin = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String commandLabel, String[] args) {
		String commandName = command.getName().toLowerCase();
		Player ply = null;
		SPlayer sply = null;
		if (sender instanceof Player) {
			ply = (Player) sender;
			sply = plugin.players.get(ply.getName());
		}

		if (commandName.equals("resetspleef")) {
			if (!ply.hasPermission("shankshock.ban")) {
				return true;
			}

			if (args.length < 2) {
				sply.sendError("Sorry! Wrong # args. Syntax: /resetspleef [String=zoneName] [Int=blocktype] [optional Byte=data]");
				return true;
			}

			try {

				LocalSession session = plugin.worldEdit.getSession(ply);
				BukkitWorld world = new BukkitWorld(ply.getWorld());

				com.sk89q.worldedit.Vector vec1 = session.getSelection(world).getMinimumPoint();
				com.sk89q.worldedit.Vector vec2 = session.getSelection(world).getMaximumPoint();

				if (!vec1.equals(vec2)) {
					sply.sendError("Please select the same block with both points of the WorldEdit wand.");
					return true;
				}

				final String loc = vec1.getX() + "," + vec1.getY() + "," + vec1.getZ();

				final String[] _args = args;
				final String worldName = world.getName();
				boolean saveBlockData = false;
				if (args.length == 3) {
					try {
						Byte.parseByte(args[2]);
						saveBlockData = true;
					} catch (Exception e) {
						saveBlockData = false;
					}
				}
				
				final boolean _saveBlockData = saveBlockData;

				plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
					public void run() {
						plugin.redisDatabase.getRedis().set("mc:server:" + plugin.worlds.get(worldName).getUniqueid() + ":" + worldName + ":block:" + loc + ":onuse", "clearspleef");
						plugin.redisDatabase.getRedis().set("mc:server:" + plugin.worlds.get(worldName).getUniqueid() + ":" + worldName + ":block:" + loc + ":zone", _args[0]);
						plugin.redisDatabase.getRedis().set("mc:server:" + plugin.worlds.get(worldName).getUniqueid() + ":" + worldName + ":block:" + loc + ":typeid", _args[1]);
						if (_saveBlockData) {
							plugin.redisDatabase.getRedis().set("mc:server:" + plugin.worlds.get(worldName).getUniqueid() + ":" + worldName + ":block:" + loc + ":data", _args[2]);
						}
					}
				});

			} catch (IncompleteRegionException e) {
				sply.sendError("Please select the same block with both points of the WorldEdit wand.");
			}
			return true;
		}

		if (commandName.equals("afterdark")) {
			if (!ply.hasPermission("shankshock.ban")) {
				return true;
			}

			if (args.length == 0) {
				sply.sendError("Failed! Syntax: /afterdark [player]");
				return true;
			}

			Player targetPly = plugin.getServer().getPlayer(args[0]);

			if (targetPly == null) {
				sply.sendError("Sorry! Player not found.");
				return true;
			}

			plugin.awardManager.addAward(AwardType.SHANKSHOCK_AFTER_DARK, targetPly);

			return true;
		}

		if (commandName.equals("release")) {
			if (!ply.hasPermission("shankshock.ban")) {
				return true;
			}

			if (args.length < 1) {
				sply.sendError("You must specify a player to release from jail!");
				sply.sendError("Syntax: /release playerName");
				return true;
			}

			Player targetPly = plugin.getServer().getPlayer(args[0]);

			if (targetPly == null) {
				sply.sendError("Sorry, that player isn't online. You can't release them.");
				return true;
			}

			targetPly.teleport(new Location(plugin.getServer().getWorld("panem"), 75450, 72, 55525));
			final String targetName = targetPly.getName();
			targetPly.sendMessage(ChatColor.GREEN + "You have been released from jail.");
			plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
				public void run() {
					plugin.redisDatabase.getRedis().del("mc:player:" + targetName + ":jailendtime");
				}
			}, 0);
			sply.sendMessage("Released!");
			return true;
		}

		if (commandName.equals("jail")) {
			if (!ply.hasPermission("shankshock.ban")) {
				return true;
			}

			if (args.length < 2) {
				sply.sendError("Syntax error! Invalid argument count.");
				sply.sendError("Syntax: /jail [silent=true/false] playername");
				sply.sendError("To remove players from the prison, use /release [player]");
				return true;
			}

			boolean silent = Boolean.parseBoolean(args[0]);

			Player targetPly = plugin.getServer().getPlayer(args[1]);

			if (targetPly == null) {
				sply.sendError("Player not found!");
				return true;
			}

			World w = plugin.getServer().getWorld("panem");
			ArrayList<Location> prisonLocations = new ArrayList<Location>();
			prisonLocations.add(new Location(w, 75436, 60, 55483));
			prisonLocations.add(new Location(w, 75436, 60, 55479));
			prisonLocations.add(new Location(w, 75436, 60, 55474));
			prisonLocations.add(new Location(w, 75436, 60, 55467));
			prisonLocations.add(new Location(w, 75436, 60, 55483));
			Random r = new Random(ply.getName().length() + System.nanoTime());

			Location prisonLocation = prisonLocations.get(r.nextInt(prisonLocations.size() - 1));

			if (silent) {
				prisonLocation = new Location(w, 75436, 60, 55470);
			}

			targetPly.sendMessage(ChatColor.RED + "You have been jailed. Depending on the reason behind this, your account's access may be limited.");
			targetPly.sendMessage(ChatColor.RED + "You can be released by an admin, or automatically after 3.14 hours.");

			final long endTime = System.currentTimeMillis() / 1000 + 11304;
			final String targetName = targetPly.getName();
			targetPly.teleport(prisonLocation);

			plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
				public void run() {
					plugin.redisDatabase.getRedis().set("mc:player:" + targetName + ":jailendtime", Long.toString(endTime));
				}
			}, 0);
			sply.sendMessage("Jailed!");
		}

		if (commandName.equals("creative")) {
			if (sply.getInventoryStore().hasItem(ItemType.CREATIVE_ACCESS)) {
				if (ply.getWorld().getName().contains("stock")) {
					sply.sendError("You're in Pure and can't teleport out this way.");
				}
				final String targetName = ply.getName();
				final String worldName = ply.getWorld().getName();
				final String location = ply.getLocation().getX() + "," + ply.getLocation().getY() + "," + ply.getLocation().getZ();
				final String key = "mc:player:" + targetName + ":creativeloc";
				
				boolean resetPosition = false;
				
				if (args.length == 1) {
					if (args[0].equals("r")) {
						resetPosition = true;
					}
				}
				
				final boolean resetTeleportPosition = resetPosition;
				
				plugin.awardManager.addAward(AwardType.CREATIVE, ply);

				plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
					public void run() {
						Location destination = new Location(plugin.getServer().getWorld("svcreative"), 5, 15, 1);
						if (worldName.equals("svcreative")) {
							plugin.redisDatabase.getRedis().set(key, location);
							plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
								public void run() {
									Player ply = plugin.getServer().getPlayer(targetName);
									ply.performCommand("begin");
								}
							});
						} else {
							boolean hasSavedLocation = plugin.redisDatabase.getRedis().exists(key);

							if (hasSavedLocation && !resetTeleportPosition) {
								String commaLocation = plugin.redisDatabase.getRedis().get(key);
								String[] location = commaLocation.split(",");
								double x = Double.parseDouble(location[0]);
								double y = Double.parseDouble(location[1]);
								double z = Double.parseDouble(location[2]);
								destination.setX(x);
								destination.setY(y);
								destination.setZ(z);
							}

							final Location _destination = destination;

							plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
								public void run() {
									Player ply = plugin.getServer().getPlayer(targetName);
									ply.teleport(_destination);
									ply.sendMessage(ChatColor.GREEN + "Welcome to Creative.");
								}
							});
						}
					}
				});
			} else {
				sply.sendError("Sorry, you don't have access to creative. You can buy access in the " + ChatColor.AQUA + "/shop");
			}
		}

		if (commandName.equals("donatorup") && ply.hasPermission("shankshock.owner")) {
			if (!(args.length < 2)) {
				sply.sendError("DonatorUp: Send a name followed by a level. We'll take it from there.");
				return true;
			}

			final String playerName = args[0];
			final String donatorLevel = args[1];
			DonatorLevel donatorLevelEnum = null;

			for (DonatorLevel m_donatorLevel : DonatorLevel.values()) {
				if (donatorLevel.equalsIgnoreCase(m_donatorLevel.getName())) {
					donatorLevelEnum = m_donatorLevel;
				}
			}

			if (donatorLevelEnum == null) {
				sply.sendError("Invalid level specified.");
				return true;
			}

			sply.sendMessage("Attempting to find player.");
			SPlayer donatedPlayer = null;
			if (plugin.players.containsKey(playerName)) {
				sply.sendMessage("Player on the server. Processing transaction on live player!");
				donatedPlayer = plugin.players.get(playerName);
				String group = donatedPlayer.getGroup();
				if (group.equalsIgnoreCase("Default")) {
					if (donatorLevelEnum.getLevel() >= 1) {
						donatedPlayer.addCurrency(50000, false);
					}

					if (donatorLevelEnum.getLevel() >= 2) {
						donatedPlayer.addCurrency(250000, false);
					}

					Item chatColor = null;

					for (Item i : SilverManager.items) {
						if (i.getItemType() == ItemType.CHAT_COLOR_GOLD) {
							chatColor = i;
						}
					}

					donatedPlayer.getInventoryStore().addItem(chatColor);

					sply.sendMessage("Player was just given silver.");
					sply.sendMessage("Player was just given a chat title.");
				}

				if (group.contains("Donator")) {
					DonatorLevel oldLevel = DonatorLevel.DonatorLevelOne;

					for (DonatorLevel m_donatorLevel : DonatorLevel.values()) {
						if (m_donatorLevel.getName().equalsIgnoreCase(group)) {
							oldLevel = m_donatorLevel;
						}
					}

					if (oldLevel.getLevel() == 1 && donatorLevelEnum.getLevel() >= 2) {
						donatedPlayer.addCurrency(250000, false);
						sply.sendMessage("Player was just given silver!");
					}
				}
			} else {
				sply.sendMessage("Player offline. Attempting an offline edit.");
				sply.sendMessage("Thread redirection! The execution handle is now being placed under a new runnable.");
			}
		}

		if (commandName.equals("rules")) {
			RuleBookItem i = new RuleBookItem();
			i.giveItem(ply, plugin);
			sply.sendMessage("You've been given a copy of the rules. Failing to read them will result in a ban.");
			return true;
		}

		if (commandName.equals("changename")) {
			if (sply != null) {
				if (ply.hasPermission("shankshock.changename") || ply.hasPermission("shankshock.ban")) {
					if (args.length < 0) {
						sply.sendError("Invalid new nickname specified.");
						return true;
					}
					final SPlayer sply2 = sply;
					final String name = mergedString(args, 0);

					if (name.length() > 16 || name.length() < 3) {
						sply.sendError("Name is too long/short, try again.");
						return true;
					}

					plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
						@Override
						public void run() {
							try {
								ResultSet r = plugin.sqldb.executeQuery("SELECT `Username` FROM `data` WHERE `Username`=? OR `nickname`=?", name, name);

								if (!r.next()) {
									if (plugin.redisDatabase.getRedis().exists("mc:player:" + sply2.getPlayer().getName() + ":namechanged")) {
										final Long seconds = plugin.redisDatabase.getRedis().ttl("mc:player:" + sply2.getPlayer().getName() + ":namechanged") / 60 / 60 / 24;
										plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
											@Override
											public void run() {
												if (sply2.getPlayer() != null) {
													sply2.sendError("Sorry, you can't change your nickname right now. You can change it in " + seconds + " days.");
												}
											}
										});
										return;
									}

									plugin.sqldb.executeSQL("UPDATE `data` SET `nickname`=? WHERE Username=?", name, sply2.getPlayer().getName());
									plugin.redisDatabase.getRedis().set("mc:player:" + sply2.getPlayer().getName() + ":namechanged", "true");
									plugin.redisDatabase.getRedis().expire("mc:player:" + sply2.getPlayer().getName() + ":namechanged", 2592000);
									plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
										@Override
										public void run() {
											sply2.getPlayer().setDisplayName(name);
											plugin.conn.setPlayerListName(sply2.getPlayer(), true);
											plugin.getServer().broadcast(ChatColor.GOLD + sply2.getPlayer().getName() + " changed their nickname.", Server.BROADCAST_CHANNEL_USERS);
										}
									});

								} else {
									plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
										@Override
										public void run() {
											if (sply2.getPlayer() != null) {
												sply2.sendError("That nickname was already taken. Sorry.");
											}
										}
									});
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}
			}
		}

		if (commandName.equals("antiangrywolf")) {
			if (!ply.hasPermission("shankshock.angrywolf")) {
				return true;
			}
			List<LivingEntity> livingStuff = ply.getWorld().getLivingEntities();
			int total = 0;
			for (LivingEntity ent : livingStuff) {
				if (ent.getLocation().distance(ply.getLocation()) < 10) {
					EntityType type = ent.getType();
					if (type == EntityType.WOLF) {
						total++;
						Wolf w = (Wolf) ent;
						w.setAngry(true);
						w.setAngry(false);
					}
				}
			}
			ply.sendMessage("Did magic on " + total + " wolves.");
		}

		if (commandName.equals("sva")) {
			if (!ply.hasPermission("shankshock.sva")) {
				return true;
			}

			HashMap<Integer, ItemType> tempItems = new HashMap<Integer, ItemType>();
			tempItems.put(1, ItemType.TITLE_SV_FOUNDING_MEMBER);
			tempItems.put(2, ItemType.TITLE_SV_MAINTAINER);
			tempItems.put(3, ItemType.TITLE_SV_LEGIONARY);
			tempItems.put(4, ItemType.TITLE_SV_MINING_CORP);
			tempItems.put(5, ItemType.TITLE_SV_BLACK_RED);
			tempItems.put(6, ItemType.TITLE_HALLOWEEN_2012);
			tempItems.put(7, ItemType.TITLE_500_MILES);
            tempItems.put(8, ItemType.TITLE_MARIO_MARATHON_6);
            tempItems.put(9, ItemType.TITLE_KING_OF_SVUH);
			if (args.length == 0) {
				sply.sendMessage("Usage: /sva [player] [title]");
				sply.sendMessage("Titles: 1 - Trailblazer");
				sply.sendMessage("Titles: 2 - Maintainer");
				sply.sendMessage("Titles: 3 - Legionary");
				sply.sendMessage("Titles: 4 - Mining Corp");
				sply.sendMessage("Titles: 5 - SV blue/red");
				sply.sendMessage("Titles: 6 - Halloween 2012");
				sply.sendMessage("Titles: 7 - 500 Miles");
                sply.sendMessage("Titles: 8 - MM6");
                sply.sendMessage("Titles: 9 - King of SVUH");
				return true;
			}

			// Takes 2 args: Player & Title

			Player dply = plugin.getServer().getPlayer(args[0]);

			if (dply == null) {
				sply.sendError("Invalid player (/sva player title)");
				return true;
			}
			int choice;

			try {
				choice = Integer.parseInt(args[1]);
			} catch (Exception e) {
				sply.sendError("Invalid number specified.");
				return true;
			}

			SPlayer sdply = plugin.players.get(dply.getName());

			ItemType itemType = tempItems.get(Math.min(choice, tempItems.size()));
			Item item = null;
			for (Item i : SilverManager.items) {
				if (i.getItemType() == itemType) {
					item = i;
					break;
				}
			}

			sdply.getInventoryStore().addItem(item);
			sdply.saveAccount();
			sdply.sendMessage("An item has been added to your inventory.");
			sply.sendMessage("Item added.");
			return true;
		}

		if (commandName.equals("reloadads")) {
			if (!ply.hasPermission("shankshock.reloadads")) {
				return true;
			}

			plugin.adEngine.setupAds();
			sply.sendMessage("Advertisements reset successfully.");
			return true;
		}

		if (commandName.equals("changelog")) {
			if (!ply.hasPermission("shankshock.ban")) {
				return true;
			}

			if (args.length == 2) {
				// This command is really for Shank's use only. Just makes
				// ticking it easier.
				plugin.globalConfig.setValue("changelog.number", args[0]);
				plugin.globalConfig.setValue("changelog.link", args[1]);
				plugin.newsEngine.resetReadNews();
				plugin.redisDatabase.sendSettingsUpdate("news");
				sply.sendMessage("Success.");
			}
		}

		if (commandName.equals("whoami")) {
			if (sply == null) {
				return false;
			}

			sply.sendMessage("You are connected from " + ChatColor.BLUE + ply.getAddress().getHostName());
			sply.sendMessage("Mojang Account: " + ChatColor.BLUE + ply.getName());
			sply.sendMessage("Registered: " + ChatColor.BLUE + plugin.conn.getRegDate(ply.getName()));
			sply.sendMessage("Activated? " + ChatColor.BLUE + sply.isActivated());
			sply.sendMessage("Display Name: " + ChatColor.BLUE + ply.getDisplayName());
			sply.sendMessage("Read the news? " + ChatColor.BLUE + !sply.getNewsReadStatus());
			sply.sendMessage("Silver: " + ChatColor.BLUE + NumberFormat.getInstance().format(sply.getCurrency()));
			sply.sendMessage("Group: " + sply.getGroup());
			return true;
		}

		if (commandName.equals("silvertop10") || commandName.equals("silvertop25")) {
			ResultSet r = plugin.sqldb.executeQuery("SELECT * FROM `funds` ORDER BY `funds`.`Amount` DESC LIMIT 0, 25");
			int currentPlace = 0;
			try {
				while (r.next()) {
					currentPlace++;
					sply.sendMessage(currentPlace + ". " + r.getString("Username") + ": " + ChatColor.GRAY + NumberFormat.getInstance().format(r.getInt("Amount")) + "sp.");
				}
			} catch (SQLException e) {
			}
		}

		if (commandName.equals("gflag")) {
			if (!ply.hasPermission("shankshock.ban")) {
				return true;
			}

			if (!(args.length == 2)) {
				sply.sendError("Invalid syntax for global flags.");
				sply.sendWarning("Syntax: /gflag [flag] [value]");
				sply.sendWarning("Flags: motd (String), shadow.ban (true/false), shadow.kick (true/false), chat.format (StringFormat), user.groups (String groups (csv))");
				sply.sendWarning("Strings should be wrapped in quotations to prevent accidental truncation.");
				sply.sendWarning("Replication warning: Only MOTD is replicated.");
				return true;
			}

			String flag = args[0];
			String value = args[1];
			boolean success = false;

			if (flag.equals("motd")) {
				plugin.globalConfig.setValue(flag, value);
				plugin.redisDatabase.sendSettingsUpdate("motd");
				success = true;
			}

			if (flag.equals("shadow.ban")) {
				try {
					Boolean.parseBoolean(value);

					plugin.globalConfig.setValue(flag, value);
				} catch (Exception e) {
					success = false;
				}
			}

			if (flag.equals("shadow.kick")) {
				try {
					Boolean.parseBoolean(value);

					plugin.globalConfig.setValue(flag, value);
				} catch (Exception e) {
					success = false;
				}
			}

			if (flag.equals("chat.format") || flag.equals("user.groups")) {
				sply.sendError("Flag setting not yet implemented or dangerous from game. Run in a Redis console instead.");
				success = false;
			}

			if (success) {
				sply.sendMessage("Flag set succeeded!");
			} else {
				sply.sendError("Flag set failed!");
			}
		}

		if (commandName.equals("wflag")) {

			if (!ply.hasPermission("shankshock.ban")) {
				return true;
			}

			if (args[0].equalsIgnoreCase("spew")) {
				for (String key : plugin.worlds.keySet()) {
					WorldConfig wc = plugin.worlds.get(key);

					sply.sendMessage("World Info: ");
					sply.sendMessage("World Name: " + key);

					sply.sendMessage("NoBuild: " + wc.getValue("nobuild"));
					sply.sendMessage("NoSilver: " + wc.getValue("nosilver"));
					sply.sendMessage("Silver.Multiplier: " + wc.getValue("silver.multiplier"));
				}
				return true;
			}

			if (args.length != 3) {
				sply.sendError("Invalid syntax for world flags.");
				sply.sendWarning("Syntax: /wflag [world-name OR spew] [flag] [value]");
				sply.sendWarning("Flags: nobuild (true/false), nosilver (true/false), and silver.multiplier (double 0-2)");
				sply.sendWarning("Note: World must be loaded in order to be edited.");
				return true;
			}

			World w = plugin.getServer().getWorld(args[0]);

			if (w == null) {
				sply.sendError("World isn't loaded and can't be flagged.");
				return true;
			}

			WorldConfig wc = plugin.worlds.get(w.getName());

			String option = args[1].toLowerCase();
			String value = args[2].toLowerCase();
			Boolean success = false;
			if (option.equals("nobuild")) {
				success = wc.setNoBuild(value);
			}

			if (option.equals("nosilver")) {
				success = wc.setNoSilver(value);
			}

			if (option.equals("silver.multiplier")) {
				success = wc.setSilverMultiplier(value);
			}

			if (success) {
				sply.sendMessage("Flag changed successfully.");
			} else {
				sply.sendError("Invalid world flag!");
			}
		}

		if (commandName.equals("gwho")) {
			plugin.redisDatabase.sendMessage("cscc", new WhoRequest("who", ply.getName(), plugin.getConfig().getString("serverid")));
		}

		if (commandName.equals("begin")) {
			ply.teleport(new Location(plugin.getServer().getWorld("panem"), 75450, 72, 55525));
			sply.sendMessage("Welcome to ShadowVale, a city designed and crafted for a new start.");
		}

		if (commandName.equals("inventory")) {
			if (sender instanceof Conversable) {
				plugin.inventoryFactory.buildConversation((Conversable) sender).begin();
				plugin.redisDatabase.sendDeafMessage(ply.getDisplayName(), "entering their inventory", true);
				return true;
			}
			return true;
		}

		if (commandName.equals("silverc")) {
			if (!ply.hasPermission("shankshock.silver")) {
				return true;
			}

			sply.setCurrency(sply.getCurrency() + Integer.parseInt(args[0]));
			sply.sendMessage("You've been given " + NumberFormat.getInstance().format(Integer.parseInt(args[0])) + "sp.");
			sply.saveAccount();
			return true;
		}

		if (commandName.equals("shop") || commandName.equals("store")) {
			if (sender instanceof Conversable) {
				plugin.shopFactory.buildConversation((Conversable) sender).begin();
				plugin.redisDatabase.sendDeafMessage(ply.getDisplayName(), "entering the shop", true);
				return true;
			}
			return true;
		}

		if (commandName.equals("pure")) {
			if (!sply.hasPureAccess()) {
				sply.sendError("You do not have access to Pure.");
				return true;
			}

			plugin.awardManager.addAward(AwardType.PURELY_AWESOME, ply);

			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("e")) {
					if (sply.getPlayer().getWorld().getName().contains("stock")) {
						sply.sendError("You're already in Pure.");
						return true;
					}

					sply.goingToPure = true;
					
					final String targetName = ply.getName();
					final String key = "mc:player:" + targetName + ":pureloc";
					plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
						public void run() {
							Location destination = new Location(plugin.getServer().getWorld("stock"), 39, 68, -55);
							boolean hasSavedLocation = plugin.redisDatabase.getRedis().exists(key);

							if (hasSavedLocation) {
								String commaLocation = plugin.redisDatabase.getRedis().get(key);
								String[] location = commaLocation.split(",");
								double x = Double.parseDouble(location[0]);
								double y = Double.parseDouble(location[1]);
								double z = Double.parseDouble(location[2]);
								destination.setX(x);
								destination.setY(y);
								destination.setZ(z);
							}

							final Location _destination = destination;

							plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
								public void run() {
									Player ply = plugin.getServer().getPlayer(targetName);
									ply.teleport(_destination);
									if (ply.hasPermission("commandbook.god")) {
										ply.performCommand("ungod");
									}

									if (ply.hasPermission("SuperPick.super")) {
										plugin.superPick.removeSuperPick(ply);
									}

									if (plugin.worldEdit.getSession(ply).hasSuperPickAxe()) {
										plugin.worldEdit.getSession(ply).disableSuperPickAxe();
									}

									ply.sendMessage(ChatColor.GREEN + "Welcome to Pure.");
								}
							});

						}
					});

					return true;
				} else if (args[0].equalsIgnoreCase("l")) {
					if (ply.getWorld().getName().contains("stock")) {
						if (plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new TimedTeleporter(plugin, new Location(plugin.getServer().getWorld("panem"), 75450, 72, 55525), sply.getPlayer().getName()), 20 * 30) != -1) {
							sply.sendMessage("A teleport has been scheduled. You will exit Pure in 30 seconds.");
							final String targetName = ply.getName();
							final String location = ply.getLocation().getX() + "," + ply.getLocation().getY() + "," + ply.getLocation().getZ();
							final String key = "mc:player:" + targetName + ":pureloc";
							plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
								public void run() {
									plugin.redisDatabase.getRedis().set(key, location);
								}
							});
						} else {
							sply.sendError("Unable to schedule a teleport. Sorry about that!");
						}
					} else {
						sply.sendError("You are not in Pure, and thus cannot leave.");
					}
					return true;
				} else if (args[0].equalsIgnoreCase("i")) {
					try {
						ResultSet r = plugin.sqldb.executeQuery("SELECT invites FROM `pure-invites` WHERE username=?", sply.getPlayer().getName());
						r.next();
						sply.sendMessage("You have " + ChatColor.AQUA + r.getInt("invites") + ChatColor.GREEN + " invites remaining.");
						return true;
					} catch (SQLException e) {
						e.printStackTrace();
						return true;
					}
				}
			} else if (args.length == 2) {
				if (args[0].equalsIgnoreCase("i")) {
					if (sply.invitePlayerToPure(args[1], 0)) {
						sply.sendMessage("Player " + args[1] + " has been invited to Pure.");
						for (Player p : plugin.getServer().getOnlinePlayers()) {
							if (p.getName().equalsIgnoreCase(args[1])) {
								SPlayer invitee = plugin.players.get(p.getName());
								invitee.sendMessage("You have been invited to Pure BlockShock!");
								invitee.sendMessage("You may now use /pure.");
								invitee.sendMessage("You have been given 1 invitation to Pure.");
								invitee.sendMessage("This is in beta. Expect odd behavior and random system failures.");
								return true;
							}
						}
					} else {
						sply.sendError("You have no more remaining invitations, and thus cannot invite people to Pure.");
						return true;
					}
				}
			}

			sply.sendError("Invalid syntax!");
			sply.sendError("/pure i - Tells you the number of invites you have left.");
			sply.sendError("/pure i [name] - Invites [name] to Pure using an invitation.");
			sply.sendError("/pure e - Teleports you to Pure.");
			sply.sendError("/pure l - Teleports you to Panem's spawn.");
		}

		if (commandName.equals("zm")) {
			if (ply.hasPermission("shankshock.shadow-manage")) {
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("list")) {
						sply.sendMessage("Zones loaded:");
						for (Zone z : plugin.zoneManager.getZoneStore().getZones()) {
							sply.sendMessage(z.getName() + ": " + z.getStartLocation(plugin).getX() + ", " + z.getStartLocation(plugin).getY() + ", " + z.getStartLocation(plugin).getZ() + " & " + z.getEndLocation(plugin).getX() + ", " + z.getEndLocation(plugin).getY() + ", " + z.getEndLocation(plugin).getZ() + ".");
						}
						return true;
					} else if (args[0].equalsIgnoreCase("reload")) {
						sply.sendMessage("Reloaded JSON database.");
						plugin.zoneManager.loadZonesFromJson();
						return true;
					} else if (args[0].equalsIgnoreCase("test")) {
						if (plugin.zoneManager.checkBlockIsInZone(ply.getLocation())) {
							sply.sendMessage("You are in a zone.");
						} else {
							sply.sendMessage("You are not in a zone.");
						}
						return true;
					} else if (args[0].equalsIgnoreCase("save")) {
						plugin.zoneManager.saveZonesToJson();
						sply.sendMessage("Saved.");
						return true;
					}
				} else if (args.length > 2) {
					if (args[0].equalsIgnoreCase("add")) {
						if (args.length >= 3) {
							// Make sure the name isn't already in use
							for (Zone z : plugin.zoneManager.getZoneStore().getZones()) {
								if (z.getName().equalsIgnoreCase(args[1])) {
									sply.sendError("Zone name already exists.");
									return true;
								}
							}

							// Make sure the world exists
							if (plugin.getServer().getWorld(args[2]) == null) {
								sply.sendError("World doesn't exist.");
								return true;
							}
							LocalSession session = plugin.worldEdit.getSession(ply);

							try {
								BukkitWorld world = new BukkitWorld(ply.getWorld());
								com.sk89q.worldedit.Vector vec1 = session.getSelection(world).getMinimumPoint();
								com.sk89q.worldedit.Vector vec2 = session.getSelection(world).getMaximumPoint();

								Location loc1 = new Location(plugin.getServer().getWorld(args[2]), vec1.getX(), vec1.getY(), vec1.getZ());
								Location loc2 = new Location(plugin.getServer().getWorld(args[2]), vec2.getX(), vec2.getY(), vec2.getZ());

								boolean isShadowZone = true;
								double shopDiscount = 0.00;
								String[] shopCategories = {};

								if (args.length >= 4) {
									isShadowZone = Boolean.parseBoolean(args[3]);
								}

								if (args.length >= 5) {
									shopDiscount = Double.parseDouble(args[4]);
									if (shopDiscount > 1) {
										shopDiscount = shopDiscount / 100;
									}
								}

								if (args.length == 6) {
									String tempString = args[5];
									shopCategories = tempString.split(",");
								}

								plugin.zoneManager.getZoneStore().addZone(new Zone(loc1, loc2, args[1], isShadowZone, (shopDiscount > 0.00 ? true
										: false), shopDiscount, shopCategories));
								plugin.zoneManager.saveZonesToJson();
								sply.sendMessage("Added new zone and saved.");
							} catch (IncompleteRegionException e) {
								sply.sendError("Incomplete WorldEdit wand region!");
							}
							return true;
						}
					}
				} else {
					sply.sendError("Invalid syntax.");
					sply.sendError("/zm add [name] [world=worldname] [shadowenable=true/false] [shopdiscount=0.00-1.00] [stores=name1,name2,name3]");
					sply.sendError("E.g.: /zm add shadowvale panem false 0.00 adminshop");
					sply.sendError("Valid shops: adminshop, blackmarket");
					sply.sendError("/zm reload");
					sply.sendError("/zm list");
					sply.sendError("/zm test");
					return true;
				}
			}

		}

		if (commandName.equals("kick") && ply.hasPermission("shankshock.ban")) {
			if (args.length > 0) {
				Player target = plugin.getServer().getPlayer(args[0]);
				if (target != null) {
					plugin.players.get(target.getName()).kickPlayer(args.length > 1 ? mergedString(args, 1)
							: "Kicked by administrator.");
					plugin.broadcastMessage(target.getDisplayName() + " was kicked.");
					return true;
				} else {
					plugin.redisDatabase.sendGlobalKick(args[0], (args.length > 1 ? mergedString(args, 1)
							: "Kicked by administrator."));
					plugin.broadcastMessage(args[0] + " was global kicked.");
					return true;
				}
			}
		}

		if (commandName.equals("savedb")) {
			if (ply.hasPermission("shankshock.ban")) {
				plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new ServerIncrementialSaver(plugin));
				return true;
			}
		}

		if (commandName.equals("bl")) {
			if (ply.hasPermission("shankshock.blacklist")) {
				if (args.length == 2) {
					if (args[0].equals("add")) {
						plugin.blacklistEngine.addCommandToBlacklist(args[1]);
						sply.sendMessage("Added command " + args[1] + " to blacklist.");
						return true;
					}
					if (args[0].equals("del")) {
						plugin.blacklistEngine.removeCommandFromBlacklist(args[1]);
						sply.sendMessage("Removed command " + args[1] + " from blacklist.");
						return true;
					}
				} else if (args.length == 1) {
					if (args[0].equals("list")) {
						sply.sendMessage("List of blacklisted commands: ");
						for (String s : plugin.blacklistEngine.getBlacklist()) {
							sply.sendMessage(s);
						}
						sply.sendMessage("End of blacklist.");
						return true;
					}
				} else {
					sply.sendError("Invalid blacklist syntax.");
					sply.sendError("All commands must have the preceeding slash.");
					sply.sendError("/bl add [cmd] - Add a command to the blacklist.");
					sply.sendError("/bl del [cmd] - Remove a command from the blacklist.");
					sply.sendError("/bl list - List all commands blacklisted.");
					return true;
				}
			}
		}

		if (commandName.equals("name")) {
			if (args.length < 1) {
				sply.sendError("Unfortunately, you didn't specify a name.");
				return true;
			}

			String name = args[0];

			for (Player p : plugin.getServer().getOnlinePlayers()) {
				if (p.getDisplayName().equalsIgnoreCase(name) && !p.getName().equalsIgnoreCase(name)) {
					sply.sendMessage(name + " is the nickname for " + p.getName() + ".");
					return true;
				} else if (p.getName().equalsIgnoreCase(name) && !p.getDisplayName().equalsIgnoreCase(name)) {
					sply.sendMessage(name + " has the nickname " + p.getDisplayName() + ".");
					return true;
				} else if (p.getName().equalsIgnoreCase(name) && p.getDisplayName().equalsIgnoreCase(name)) {
					sply.sendMessage(name + " is their real name, with no nickname.");
					return true;
				}
			}

			sply.sendError("Player isn't online or invalid name specified.");
			return true;
		}

		if (commandName.equals("sudo")) {
			if (ply.hasPermission("shankshock.sudo")) {
				ply.setOp(true);
				String sudo = "";
				if (args.length <= 0) {
					sply.sendError("Please specify a command to run.");
					return true;
				} else {
					for (int i = 0; i < args.length; i++) {
						sudo += args[i] + " ";
					}
					sudo = sudo.trim();
				}
				ply.performCommand(sudo);
				ply.setOp(false);
			}
		}

		if (commandName.equals("daytime") && ply.hasPermission("shankshock.daytime")) {
			if (plugin.timeLockedOut) {
				sply.sendError("A player has already made it daytime recently. Try again later.");
				return true;
			}
			ply.getWorld().setTime(8000);
			plugin.timeLockedOut = true;
			plugin.broadcastMessage(ply.getDisplayName() + " made it day time.");
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new TimeTimeoutUpdater(plugin), 6000);
			return true;
		}

		if (commandName.startsWith("ar") && ply.hasPermission("shankshock.autorun")) {
			if (args.length == 1) {
				if (args[0].equals("list")) {
					ArrayList<String> autoruns = plugin.autorunEngine.readAutoruns(ply.getName());
					sply.sendMessage("Autorun commands:");
					for (String s : autoruns) {
						sply.sendMessage(s);
					}
					return true;
				}

				if (args[0].equals("clear")) {
					plugin.autorunEngine.deleteAll(ply);
					sply.sendMessage("Autoruns cleared successfully.");
					return true;
				}

				if (args[0].equals("exec")) {
					plugin.autorunEngine.run(ply);
					sply.sendMessage("Your autoruns were executed successfully.");
					return true;
				}
			}
			if (args.length >= 2) {
				String cmd = "";
				for (int i = 1; i < args.length; i++) {
					cmd += args[i] + " ";
				}
				cmd = cmd.trim();
				if (args[0].equals("add")) {
					plugin.autorunEngine.add(cmd, ply);
					sply.sendMessage("Autorun command added.");
					return true;
				}

				if (args[0].equals("del")) {
					plugin.autorunEngine.remove(cmd, ply);
					sply.sendMessage("Autorun command deleted.");
					return true;
				}
			}

			sply.sendMessage("It appears you didn't specify a command.");
			sply.sendMessage("Valid options:");
			sply.sendMessage("/ar list, /ar clear, /ar del, /ar exec, /ar add");
			sply.sendMessage("Commands do not require slashes.");
			return true;
		}

		if (commandName.equals("restart")) {
			if ((ply != null) && !ply.hasPermission("shankshock.ban")) {
				return true;
			}
			plugin.broadcastMessage("The server is shutting down NOW!");
			for (Player p : plugin.getServer().getOnlinePlayers()) {
				p.kickPlayer("The server is restarting.");
			}
			if (ply == null) {
				plugin.getServer().getConsoleSender().sendMessage("Shutting down NOW!");
			}
			plugin.getServer().shutdown();
			return true;
		}

		if (commandName.equals("reloadpermissions")) {
			if ((ply != null) && !ply.hasPermission("shankshock.reloadpermissions")) {
				return true;
			}
			plugin.conn.reloadPermissions();
			if (ply == null) {
				plugin.getServer().getConsoleSender().sendMessage("Relaoded permissions.");
				return true;
			}
			sply.sendMessage(ChatColor.AQUA + "Reloaded permissions.");
		}

		if (commandName.equals("news")) {
			plugin.newsEngine.displayNews(ply);
			plugin.newsEngine.readNews(ply);

			if (sply.getNewsReadStatus()) {
				plugin.awardManager.updateAward(AwardType.STAY_INFORMED, 1, ply);
				plugin.awardManager.addAward(AwardType.NEWS_WORTHY, ply);
                plugin.players.get(ply.getName()).setNeedsToReadNews(false);
			}
		}

		if (commandName.equals("donate")) {
			sply.sendMessage("Donation gives you access to extra abilities in return for helping support the server.");
			sply.sendMessage(ChatColor.AQUA + "http://shankshock.com/donate.php");
			return true;
		}
		return true;
	}

	public String mergedString(String[] str, int index) {
		String result = "";
		for (int i = index; i < str.length; i++) {
			result += " " + str[i];
		}
		return result.trim();
	}

	public void debug(String str) {
		if (plugin.debug) {
			System.out.println(str);
		}
	}

}
