package com.shankshock.nicatronTg.Registration.Misc;

import org.bukkit.ChatColor;

public class ChatColorPair {
	public String permission = "";
	public String color = "";

	public ChatColorPair(String permission, String color) {
		this.permission = permission;
		this.color = color.replace("&", ChatColor.COLOR_CHAR + "");
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color.replace("&", ChatColor.COLOR_CHAR + "");
	}
}
