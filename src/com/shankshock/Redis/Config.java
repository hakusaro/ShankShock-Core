package com.shankshock.Redis;

public interface Config {
	public abstract void setValue(String key, String value);
	
	public abstract String getValue(String key);
	
	public abstract boolean exists(String key);
}
