package com.shankshock.Redis;

import java.util.HashMap;

import com.google.gson.Gson;
import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisConnection;
import com.lambdaworks.redis.pubsub.RedisPubSubConnection;
import com.shankshock.Redis.Messages.KickMessage;
import com.shankshock.Redis.Messages.Message;
import com.shankshock.nicatronTg.Registration.Registration;

public class RedisDatabase {

	private final Registration plugin;
	private RedisClient redisClient;
	private RedisPubSubConnection<String, String> redisPubSubConnection;
	private RedisClient redisSendClient;
	private RedisConnection<String, String> redis;
	private RedisListener<String, String> redisListener;
	private boolean shutdown;
	private String[] channels = { "test", "activate", "nickname", "cscj",
			"cscl", "cscc", "v2csc", "rns", "dms" };
	private HashMap<String, String> keyValues = new HashMap<String, String>();

	public RedisDatabase(Registration instance) {
		this.plugin = instance;
		redisListener = new RedisListener<String, String>(plugin, this);
		initialize();
	}

	public RedisPubSubConnection<String, String> getRedisPubSubConnection() {
		return redisPubSubConnection;
	}

	public boolean isShutdown() {
		return shutdown;
	}

	private void initialize() {
		redisClient = new RedisClient(
				plugin.getConfig().getString("redishost"), plugin.getConfig()
						.getInt("redisport"));
		redisPubSubConnection = redisClient.connectPubSub();
		redisPubSubConnection.addListener(redisListener);
		redisPubSubConnection.auth(plugin.getConfig().getString("redispass"));

		redisPubSubConnection.subscribe(channels);

		redisSendClient = new RedisClient(plugin.getConfig().getString(
				"redishost"), plugin.getConfig().getInt("redisport"));
		redis = redisSendClient.connect();
		redis.auth(plugin.getConfig().getString("redispass"));

		plugin.getServer().getScheduler()
				.runTaskTimerAsynchronously(plugin, new Heartbeat(), 600, 1200);

		System.out.println("Redis: Connection completed.");

	}

	public void shutdown() {
		shutdown = true;
		redisPubSubConnection.unsubscribe(channels);
		redisPubSubConnection.close();

		redisClient.shutdown();
		System.out.println("Redis: Shutdown completed.");
	}
	
	public RedisConnection<String, String> getRedis() {
		return redis;
	}
	
	/**
	 * Sets a key & value in both the cache and the Redis database.
	 * @param key - The key to set.
	 * @param value - The value to set.
	 */
	public void setValue(String key, String value) {
		if (keyValues.containsKey(key)) {
			keyValues.put(key, value);
		}
		
		redis.set(key, value);
	}
	
	/**
	 * Gets a given value from Redis or cache based on a given key. This call is indiscriminate and will return a raw value from Redis or the cache, no matter where it is.
	 * @param key - The key to get.
	 * @return value - The stored value in that particular key.
	 */
	public String getValue(String key) {
		if (keyValues.containsKey(key)) {
			return keyValues.get(key);
		} else {
			keyValues.put(key, redis.get(key));
		}
		return keyValues.get(key);
	}
	
	/**
	 * Refreshes the a key in the cache.
	 * @param key - The raw key value to refresh.
	 */
	public void refreshKey(String key) {
		keyValues.put(key, redis.get(key));
	}

	/**
	 * Sends a message to all servers to update a given server setting.
	 * @param component - The component to update.
	 */
	public void sendSettingsUpdate(String component) {
		redis.publish("rns", component);
	}

	/**
	 * Sends a global kick message to all listening servers.
	 * @param name - The player to kick.
	 * @param reason - The reason.
	 */
	public void sendGlobalKick(String name, String reason) {
		sendMessage("cscc", new KickMessage(name, plugin
				.getConfig().getString("serverid"), reason));
	}

	/**
	 * Sends a server Message to all listening servers.
	 * @param channel - The channel to broadcast on.
	 * @param message - The Message object to send.
	 */
	public void sendMessage(String channel, Message message) {
		redis.publish(channel, message.serialize());
	}

	/**
	 * Sends a leave message to all listening servers.
	 * @param name - The player who left.
	 */
	public void sendServerLeave(String name) {
		Gson gson = new Gson();
		PlayerStateChange leavePacket = new PlayerStateChange(name, plugin
				.getConfig().getString("serverid"));
		redis.publish("cscl", gson.toJson(leavePacket));
	}

	/**
	 * Sends a cross server join message to all subscribed servers.
	 * @param name - The player who joined.
	 */
	public void sendServerJoin(String name) {
		Gson gson = new Gson();
		PlayerStateChange joinPacket = new PlayerStateChange(name, plugin
				.getConfig().getString("serverid"));
		redis.publish("cscj", gson.toJson(joinPacket));
	}

	/**
	 * Sends a cross server chat message to all subscribed servers.
	 * @param msg - The chat message.
	 */
	public void sendCSC(String msg) {
		Gson gson = new Gson();
		redis.publish(
				"v2csc",
				gson.toJson(new PlayerChatMessage(plugin.getConfig().getString(
						"serverid"), msg)));
	}

	/**
	 * Sends a message to all servers with the format 'playerName' can no longer see chat due to 'reason'.
	 * 
	 * If player is not currentlyDeaf, the message will read 'playerName' can see chat again.
	 * @param playerName
	 * @param reason
	 * @param currentlyDeaf
	 */
	public void sendDeafMessage(String playerName, String reason, boolean currentlyDeaf) {
		Gson gson = new Gson();
		redis.publish("dms", gson.toJson(new PlayerDeafMessage(plugin.getConfig().getString("serverid"), reason, playerName, currentlyDeaf)));
	}
	
	class PlayerDeafMessage {
		public String getOriginServerId() {
			return originServerId;
		}

		public void setOriginServerId(String originServerId) {
			this.originServerId = originServerId;
		}

		public String getReason() {
			return reason;
		}

		public void setReason(String reason) {
			this.reason = reason;
		}

		public String getPlayerName() {
			return playerName;
		}

		public void setPlayerName(String playerName) {
			this.playerName = playerName;
		}

		private String originServerId;
		private String reason;
		private String playerName;
		private boolean currentlyDeaf;
		
		public boolean isCurrentlyDeaf() {
			return currentlyDeaf;
		}

		public void setCurrentlyDeaf(boolean currentlyDeaf) {
			this.currentlyDeaf = currentlyDeaf;
		}

		public PlayerDeafMessage(String originServerId, String reason, String playerName, boolean currentlyDeaf) {
			this.originServerId = originServerId;
			this.reason = reason;
			this.playerName = playerName;
			this.currentlyDeaf = currentlyDeaf;
		}
		
		public PlayerDeafMessage() {
			
		}
	}
	
	class PlayerChatMessage {
		/**
		 * @return The server id of the server that sent the chat message.
		 */
		public String getOriginServerId() {
			return originServerId;
		}

		/**
		 * Sets the origin server id.
		 * @param originServerId
		 */
		public void setOriginServerId(String originServerId) {
			this.originServerId = originServerId;
		}
		
		/**
		 * Gets the chat message.
		 * @return The message.
		 */
		public String getMessage() {
			return message;
		}

		/**
		 * Sets the chat message.
		 * @param message
		 */
		public void setMessage(String message) {
			this.message = message;
		}

		private String originServerId;
		private String message;

		public PlayerChatMessage(String originServerId, String message) {
			this.originServerId = originServerId;
			this.message = message;
		}

		public PlayerChatMessage() {

		}
	}

	class PlayerStateChange {
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		private String name;
		private String id;

		public PlayerStateChange(String name, String id) {
			this.name = name;
			this.id = id;
		}

		public PlayerStateChange() {

		}
	}

	class Heartbeat implements Runnable {
		@Override
		public void run() {
			for (String s : channels) {
				redisPubSubConnection.publish(s, "[heartbeat]");
			}
		}
	}
}