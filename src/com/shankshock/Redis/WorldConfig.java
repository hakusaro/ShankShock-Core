package com.shankshock.Redis;

import java.util.HashMap;

public class WorldConfig implements Config{

	private final RedisDatabase redis;
	private final String worldName;
	private final String uniqueid;
	private final HashMap<String, String> defaultKeys = new HashMap<String, String>();
	private String keyPrefix = "mc:server:";
	
	public WorldConfig(RedisDatabase redis, String worldName, String uniqueid) {
		this.redis = redis;
		this.worldName = worldName;
		this.uniqueid = uniqueid;
		keyPrefix += uniqueid + ":world:" + worldName + ":";
		setupDefaults();
		initialize();
	}
	
	private void setupDefaults() {
		defaultKeys.put("nobuild", "false");
		defaultKeys.put("nosilver", "false");
		defaultKeys.put("silver.multiplier", "0.00");
	}
	
	private void initialize() {
		for (String key : defaultKeys.keySet()) {
			if (!exists(key)) {
				setValue(key, defaultKeys.get(key));
				System.out.println("Redis: Added default key " + key + " to Redis for world " + worldName + " on serverid " + uniqueid);
			}
		}
	}
	
	public boolean setSilverMultiplier(String s) {
		try {
			double amount = Double.parseDouble(s);
			
			if (amount >= 2) {
				return false;
			}
			
			setValue("silver.multiplier", s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public RedisDatabase getRedis() {
		return redis;
	}

	public String getWorldName() {
		return worldName;
	}

	public String getUniqueid() {
		return uniqueid;
	}

	public HashMap<String, String> getDefaultKeys() {
		return defaultKeys;
	}

	public String getKeyPrefix() {
		return keyPrefix;
	}

	public void setKeyPrefix(String keyPrefix) {
		this.keyPrefix = keyPrefix;
	}

	public boolean setNoSilver(String s) {
		try {
			Boolean.parseBoolean(s);
			
			setValue("nosilver", s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean setNoBuild(String s) {
		try {
			Boolean.parseBoolean(s);
			
			setValue("nobuild", s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public double getSilverMultiplier() {
		return Double.parseDouble(getValue("silver.multiplier"));
	}
	
	public boolean canEarnSilver() {
		return !Boolean.parseBoolean(getValue("nosilver"));
	}
	
	public boolean canBuild() {
		return !Boolean.parseBoolean(getValue("nobuild"));
	}
	
	@Override
	public void setValue(String key, String value) {
		redis.setValue(keyPrefix + key, value);
	}

	@Override
	public String getValue(String key) {
		return redis.getValue(keyPrefix + key);
	}

	@Override
	public boolean exists(String key) {
		return redis.getRedis().exists(keyPrefix + key);
	}

}
