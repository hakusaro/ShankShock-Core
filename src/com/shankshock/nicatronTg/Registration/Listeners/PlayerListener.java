package com.shankshock.nicatronTg.Registration.Listeners;

import com.shankshock.nicatronTg.Registration.Items.ItemType;
import com.shankshock.nicatronTg.Registration.Misc.ChatMessage;
import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.SPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import java.util.Random;

/**
 * Handle events for all Player related events
 * 
 * @author nicatronTg
 */
public class PlayerListener implements Listener {
	private final Registration plugin;

	public PlayerListener(Registration instance) {
		plugin = instance;
	}

	public long getCurrentEpoch() {
		return System.currentTimeMillis() / 1000;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerMove(PlayerMoveEvent event) {
		SPlayer sply = plugin.players.get(event.getPlayer().getName());

		if (sply == null) {
			return;
		}

		if (sply.getChatPrefix().contains("Spirit") || sply.getChatPrefix().contains("Ghost")) {
			int random = new Random().nextInt(100);
			if (random < 10) {
				event.getPlayer().getWorld().playEffect(event.getPlayer().getLocation(), Effect.ENDER_SIGNAL, 1);
				event.getPlayer().getWorld().playEffect(event.getPlayer().getLocation(), Effect.SMOKE, 1);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		TeleportCause cause = event.getCause();
		SPlayer sply = plugin.players.get(event.getPlayer().getName());
		if (sply == null) {
			return;
		}
		Player ply = sply.getPlayer();
		if (cause == TeleportCause.COMMAND || cause == TeleportCause.PLUGIN || cause == TeleportCause.UNKNOWN) {
			if (event.getTo().getWorld().getName().contains("stock") && !event.getFrom().getWorld().getName().contains("stock")) {

				if (sply.goingToPure) {
					event.setCancelled(false);
					sply.goingToPure = false;
					return;
				}

				if (event.getTo().getX() == 39 && event.getTo().getZ() == -55 && sply.hasPureAccess()) {
					event.setCancelled(false);
					return;
				}

				if (!event.getPlayer().hasPermission("shankshock.override")) {
					sply.sendError("Your destination resides in Pure, and therefore has been canceled.");
					event.setCancelled(true);
					return;
				}
				if (ply.hasPermission("commandbook.god")) {
					ply.performCommand("ungod");
				}

				if (ply.hasPermission("SuperPick.super")) {
					plugin.superPick.removeSuperPick(ply);
				}

				if (plugin.worldEdit.getSession(ply).hasSuperPickAxe()) {
					plugin.worldEdit.getSession(ply).disableSuperPickAxe();
				}
			}

		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerChat(final AsyncPlayerChatEvent event) {
		if (event.isCancelled()) {
			return;
		}

		SPlayer ply = plugin.players.get(event.getPlayer().getName());
		ply.addChatMessage(new ChatMessage(getCurrentEpoch(), event.getMessage()));
		if (ply.isChatMuted()) {
			if (getCurrentEpoch() > ply.getChatMuteInitiationTime() + 5) {
				ply.setChatMuted(false);
			} else {
				event.setCancelled(true);
				ply.runSpamChecks();
				return;
			}
		}

		ply.runSpamChecks();

		if (event.getMessage().contains("./")) {
			String msg = event.getMessage();
			String[] msgArray = msg.split(" ");
			for (int i = 0; i < msgArray.length; i++) {
				if (msgArray[i].startsWith("./")) {
					msgArray[i] = ChatColor.AQUA + msgArray[i].replace("./", "/") + ply.getChatColor();
				} else if (msgArray[i].startsWith("/")) {
					msgArray[i] = ChatColor.AQUA + msgArray[i] + ply.getChatColor();
				}
			}
			msg = "";
			for (String s : msgArray) {
				if (msg.equals("")) {
					msg = s;
				} else {
					msg += " " + s;
				}
			}

			event.setMessage(msg);
		}

		if (event.getMessage().contains("http://")) {
			String msg = event.getMessage();
			String[] msgArray = msg.split(" ");
			for (int i = 0; i < msgArray.length; i++) {
				if (msgArray[i].startsWith("http://")) {
					msgArray[i] = ChatColor.AQUA + msgArray[i] + ply.getChatColor();
				}
			}
			msg = "";
			for (String s : msgArray) {
				if (msg.equals("")) {
					msg = s;
				} else {
					msg += " " + s;
				}
			}

			event.setMessage(msg);
		}

		event.setMessage(plugin.players.get(event.getPlayer().getName()).getChatColor() + event.getMessage());

		event.setFormat(ply.getChatFormat());

		sendChatMessageWithFiltering(ply.getChatFormat(), event.getPlayer().getDisplayName(), event.getMessage());
		System.out.println("Chatlog: " + event.getPlayer().getName() + ": " + event.getMessage());
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(final PlayerJoinEvent event) {
		event.setJoinMessage("");

		final String currentPlayer = event.getPlayer().getName();
        final String inetAddress = event.getPlayer().getAddress().getAddress().toString();
		this.plugin.getServer().getScheduler().runTaskLaterAsynchronously(this.plugin, new Runnable() {

			@Override
			public void run() {
				plugin.conn.initialJoinSetup(event.getPlayer());
				plugin.redisDatabase.sendServerJoin(event.getPlayer().getName());
                final String regDate = plugin.conn.getRegDate(currentPlayer);
				plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
					public void run() {

						String nameSearch = "";
						for (Player p : plugin.getServer().getOnlinePlayers()) {
							if (p.getName().equals(currentPlayer)) {
								nameSearch = p.getDisplayName().length() == 0 ? currentPlayer
										: p.getDisplayName();
							}
						}

						final String displayName = nameSearch;

						for (Player p : plugin.getServer().getOnlinePlayers()) {
							if (p.getName().equals(currentPlayer)) {
								continue;
							}

                            if (p.hasPermission("shankshock.ban")) {
                                p.sendMessage(ChatColor.AQUA + "Join: " + ChatColor.WHITE + "[" + ChatColor.GREEN + displayName + ChatColor.WHITE + "] " + ChatColor.WHITE + "@ [" + ChatColor.GREEN + inetAddress + ChatColor.WHITE + "] (" + ChatColor.GREEN + regDate + ChatColor.WHITE + ")");
                            } else {
							    p.sendMessage(ChatColor.AQUA + "Player " + ChatColor.GREEN + displayName + ChatColor.AQUA + " connected.");
                            }
						}
					}
				});

			}
		}, 0);

	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerQuit(final PlayerQuitEvent event) {
		try {
			final SPlayer sply = plugin.players.get(event.getPlayer().getName());
			if (sply == null) {
				return;
			}
			plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
				@Override
				public void run() {
					sply.saveAccount();
					plugin.players.remove(event.getPlayer().getName());
				}
			});

		} catch (Exception e) {

		}
		event.getPlayer().removeAttachment(plugin.conn.permissions.get(event.getPlayer()));
		plugin.conn.permissions.remove(event.getPlayer());

		event.setQuitMessage(ChatColor.AQUA + "Player " + ChatColor.GRAY + event.getPlayer().getDisplayName() + ChatColor.AQUA + " disconnected.");
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			@Override
			public void run() {
				plugin.redisDatabase.sendServerLeave(event.getPlayer().getDisplayName());
			}
		});

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		Player ply = event.getPlayer();

        final String command = event.getMessage();
        final String playerName = event.getPlayer().getName();

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                for (Player p : plugin.getServer().getOnlinePlayers()) {
                    if (p.hasPermission("shankshock.adminseeall") && plugin.players.get(playerName).getAdminSeeAll()) {
                        p.sendMessage("[ASA]: " + ChatColor.GREEN + playerName + ": " + ChatColor.YELLOW + command);
                    }
                }
            }
        });

		if (event.getMessage().toLowerCase().startsWith("/spawnmob") && event.getMessage().toLowerCase().contains("enderdragon")) {
			event.setCancelled(true);
		}

		String startsWith = "";
		if (!event.getMessage().contains(" ")) {
			startsWith = event.getMessage();
		} else {
			startsWith = event.getMessage().split(" ")[0];
		}

        if (event.getPlayer().getWorld().getName().contains("creative") && (event.getMessage().contains("/ti") || event.getMessage().contains("/it"))) {
            event.setCancelled(true);
            return;
        }

		if (plugin.blacklistEngine.isCommandBlacklisted(startsWith)) {
			if (ply.getWorld().getName().contains("stock") || ply.getWorld().getName().contains("30daybuild") || ply.getWorld().getName().contains("xanadu")) {
				plugin.players.get(ply.getName()).sendError("You're in the stock map, and the command you attempted to execute was blacklisted.");
				event.setCancelled(true);
			}
		}
	}

	String[] badWords = { "porn", "sex", "fag", "gay", "fuck", "shit", "cunt",
			"whore", "twat", "asshole", "crap", "penis", "vagina", "nigger",
			"pussy", "cock", "douche", "dildo", "bitch", "vagoo",
			"lolicon", "shotacon", "shota", "lolis", "ecchi", "hentai", "porn",
			"rape", "incest", "faggot", "lucas" };

	public void sendChatMessageWithFiltering(String format, String displayName,
			String message) {
		message = Character.toUpperCase(message.charAt(0)) + message.substring(1);
		String censoredMessage = message;

		for (String s : badWords) {
			if (censoredMessage.contains(s)) {
				censoredMessage = censoredMessage.replace(s, "****");
			}

			s = s.toUpperCase();

			if (censoredMessage.contains(s)) {
				censoredMessage = censoredMessage.replace(s, "****");
			}
		}

		censoredMessage = Character.toUpperCase(censoredMessage.charAt(0)) + censoredMessage.substring(1);

		for (Player p : plugin.getServer().getOnlinePlayers()) {
			SPlayer sply = plugin.players.get(p.getName());

			if (sply == null) {
				p.sendMessage(String.format(format, displayName, censoredMessage));
				return;
			}

			if (sply.getInventoryStore().hasItem(ItemType.CHAT_NO_CENSOR)) {
				p.sendMessage(String.format(format, displayName, message));
				continue;
			}
			p.sendMessage(String.format(format, displayName, censoredMessage));
			continue;
		}

		final String format2 = format;
		final String displayName2 = displayName;
		final String message2 = censoredMessage;

		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			@Override
			public void run() {
				plugin.redisDatabase.sendCSC(String.format(format2, displayName2, message2));
			}
		});
	}
}
