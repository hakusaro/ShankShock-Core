package com.shankshock.Redis;

import com.google.gson.Gson;
import com.lambdaworks.redis.pubsub.RedisPubSubListener;
import com.shankshock.Redis.Messages.KickMessage;
import com.shankshock.Redis.Messages.Message;
import com.shankshock.Redis.Messages.WhoRequest;
import com.shankshock.Redis.Messages.WhoResponse;
import com.shankshock.Redis.RedisDatabase.PlayerChatMessage;
import com.shankshock.Redis.RedisDatabase.PlayerDeafMessage;
import com.shankshock.Redis.RedisDatabase.PlayerStateChange;
import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.SPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

class RedisListener<K, V> implements RedisPubSubListener<String, String> {
	private final Registration plugin;
	private final RedisDatabase redis;

	public RedisListener(Registration instance, RedisDatabase redis) {
		this.plugin = instance;
		this.redis = redis;
	}

	@Override
	public void message(String channel, String message) {

		if (message.equals("[heartbeat]")) {
			return;
		}

		if (channel.equals("test")) {
			System.out.println("Redis: Test message: " + message);
		}

		if (channel.equals("activate")) {
			final String playerName = message;
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				@Override
				public void run() {
					SPlayer sply = plugin.players.get(playerName);
					if (sply != null) {
						sply.setActivated(true);
						sply.sendMessage(ChatColor.AQUA + "ShankShock:" + " Your account has been activated!");
					}
				}
			});
		}

		if (channel.equals("nickname")) {
			final String playerName = message;
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				@Override
				public void run() {
					SPlayer sply = plugin.players.get(playerName);

					if (sply != null) {
						sply.getPlayer().setDisplayName(plugin.conn.nickname(sply));
						plugin.conn.setPlayerListName(sply.getPlayer(), true);
						sply.sendMessage(ChatColor.AQUA + "ShankShock:" + " Your display name has been changed.");
					}
				}

			});
		}

		if (channel.equals("cscj")) {
			Gson gson = new Gson();
			final PlayerStateChange psc = gson.fromJson(message, PlayerStateChange.class);
			if (psc.getId().equals(plugin.getConfig().getString("serverid"))) {
				return;
			}
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				@Override
				public void run() {
					plugin.broadcastMessage(ChatColor.AQUA + "Player " + ChatColor.GREEN + psc.getName() + ChatColor.AQUA + " connected to " + ChatColor.GOLD + psc.getId() + ChatColor.AQUA + ".");
				}
			});
		}

		if (channel.equals("cscl")) {
			Gson gson = new Gson();
			final PlayerStateChange psc = gson.fromJson(message, PlayerStateChange.class);
			if (psc.getId().equals(plugin.getConfig().getString("serverid"))) {
				return;
			}
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				@Override
				public void run() {
					plugin.broadcastMessage(ChatColor.AQUA + "Player " + ChatColor.GRAY + psc.getName() + ChatColor.AQUA + " disconnected from " + ChatColor.GOLD + psc.getId() + ChatColor.AQUA + ".");
				}
			});
		}

		if (channel.equals("v2csc")) {
			Gson gson = new Gson();
			final PlayerChatMessage pcm = gson.fromJson(message, PlayerChatMessage.class);

			if (pcm.getOriginServerId().equals(plugin.getConfig().getString("serverid"))) {
				return;
			}

			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				@Override
				public void run() {
					for (Player p : plugin.getServer().getOnlinePlayers()) {
						if (p != null) {
							p.sendMessage("[" + ChatColor.AQUA + "CSC" + ChatColor.WHITE + "] " + pcm.getMessage());
						}
					}
				}

			});
		}

		if (channel.equals("cscc")) {
			Gson gson = new Gson();
			ByteArrayInputStream bos = gson.fromJson(message, ByteArrayInputStream.class);
			Message msg = null;

			try {
				ObjectInputStream in = new ObjectInputStream(bos);
				msg = (Message) in.readObject();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				System.err.println("Redis: Intercepted Redis packet with unsupported message type. Is the server out of date?");

				if (plugin.debug) {
					e.printStackTrace();
				}

			}

			if (msg instanceof WhoRequest) {
				StringBuilder players = new StringBuilder();
				for (Player ply : plugin.getServer().getOnlinePlayers()) {
					if (!players.toString().equals("")) {
						players.append(", ");
					}
					players.append(ply.getDisplayName());
				}

				Message resp = msg.handle(String.format("Online from %s (%d/%d): %s%s", plugin.getConfig().getString("serverid"), plugin.getServer().getOnlinePlayers().length, plugin.getServer().getMaxPlayers(), ChatColor.WHITE, players.toString()));
				if (resp != null) {
					redis.sendMessage("cscc", resp);
				}
			} else if (msg instanceof WhoResponse) {
				msg.handle(plugin);
			} else if (msg instanceof KickMessage) {
				msg.handle(plugin);
			} else {
				// TODO: Unsupported message
			}
		}

		/**
		 * The reload settings channel - allows a server to push an update to
		 * all servers that may be a setting changed in the core.
		 */
		if (channel.equals("rns")) {
			/**
			 * Force a reload of the news system.
			 */
			if (message.equals("news")) {
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						plugin.getServer().broadcastMessage(ChatColor.RED + "BREAKING NEWS!" + ChatColor.AQUA + " Type " + ChatColor.GOLD + "/news " + ChatColor.AQUA + "to view the latest changelog.");
					}
				});
			}
			/*
			 * Force a reload of the MOTD.
			 */
			if (message.equals("motd")) {
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						plugin.redisDatabase.refreshKey("mc:global:motd");
						plugin.getServer().broadcastMessage(ChatColor.GOLD + "> The server MOTD now reads: '" + plugin.globalConfig.getValue("motd") + "'.");
					}
				});
			}
		}

		if (channel.equals("dms")) {
			final PlayerDeafMessage pdm = new Gson().fromJson(message, PlayerDeafMessage.class);

			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				@Override
				public void run() {
					if (pdm.isCurrentlyDeaf()) {
						plugin.getServer().broadcastMessage(ChatColor.AQUA + "Player " + ChatColor.GREEN + pdm.getPlayerName() + ChatColor.GRAY + " left" + ChatColor.AQUA + " chat.");
					} else {
                        plugin.getServer().broadcastMessage(ChatColor.AQUA + "Player " + ChatColor.GREEN + pdm.getPlayerName() + ChatColor.GREEN + " joined" + ChatColor.AQUA + " chat.");
					}
				}
			});
		}

	}

	@Override
	public void message(String pattern, String channel, String message) {
	}

	@Override
	public void subscribed(String channel, long count) {
	}

	@Override
	public void psubscribed(String pattern, long count) {
	}

	@Override
	public void unsubscribed(String channel, long count) {
		if (redis.isShutdown()) {
			return;
		}
		redis.getRedisPubSubConnection().subscribe(channel);
		System.out.println("Redis: Re-subscribed to channel: " + channel);
	}

	@Override
	public void punsubscribed(String pattern, long count) {

	}

}