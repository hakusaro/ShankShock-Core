package com.shankshock.Redis;

import org.bukkit.ChatColor;

import java.util.HashMap;

public class GlobalConfig implements Config{
	private final String redisPrefix = "mc:global:";
	private final RedisDatabase redis;
	private final HashMap<String, String> defaultRedisConfiguration = new HashMap<String, String>();
	
	public GlobalConfig(RedisDatabase redis) {
		this.redis = redis;
		setupDefaults();
		initialize();
	}

	private void setupDefaults() {
		defaultRedisConfiguration.put("motd", "ShankShock.");
		defaultRedisConfiguration.put("shadow.ban", "false");
		defaultRedisConfiguration.put("shadow.kick", "false");
		defaultRedisConfiguration.put("chat.format", "%s" + ChatColor.COLOR_CHAR + "f: %s");
		defaultRedisConfiguration.put("changelog.number", "0");
		defaultRedisConfiguration.put("changelog.link", "http://shankshock.com/");
		defaultRedisConfiguration.put("xmas", "false");
		defaultRedisConfiguration.put("shop.sellback.multiplier", ".50");
		defaultRedisConfiguration.put("pushover.key", "KcOqLpZG5ZwMWYkkdcTijzwDWDjnnt");
		defaultRedisConfiguration
				.put("user.groups",
						"Shank, LeoOwner, Developer, Donator, DonatorPlus, DonatorLevelOne, DonatorLevelTwo, DonatorLevelThree, DonatorLevelFour, DonatorLevelFive, Admin, HeadAdmin, HiddenAdmin, LuinkOwner, Default");
	}

	private void initialize() {
		for (String key : defaultRedisConfiguration.keySet()) {
			if (!exists(key)) {
				setValue(key, defaultRedisConfiguration.get(key));
				System.out.println("Redis: Inserted default value for key: " + key);
			}
		}
	}
	
	/**
	 * Gets a value from Redis.
	 * @param key - The key that the value is stored at.
	 * @return attached value
	 */
	@Override
	public String getValue(String key) {
		return redis.getValue(redisPrefix + key);
	}
	
	/**
	 * Sets a value in Redis.
	 * @param key - The key the value is stored at.
	 * @param value - The value the key should be.
	 */
	@Override
	public void setValue(String key, String value) {
		redis.setValue(redisPrefix + key, value);
	}
	
	@Override
	public boolean exists(String key) {
		return redis.getRedis().exists(redisPrefix + key);
	}
}
