package com.shankshock.nicatronTg.Registration.Engines;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.shankshock.nicatronTg.Registration.Registration;

public class NewsEngine {
	private final Registration plugin;

	public NewsEngine(Registration instance) {
		plugin = instance;
	}

	public void displayNews(Player ply) {
		ply.sendMessage(ChatColor.AQUA + "Changelog #" + plugin.globalConfig.getValue("changelog.number") + ": " + plugin.globalConfig.getValue("changelog.link"));
	}

	public void resetReadNews() {
		plugin.sqldb.executeSQL("UPDATE `newsread` SET `Read` = ?", false);
	}

	public void readNews(Player ply) {
		plugin.sqldb.executeSQL(
				"UPDATE `newsread` SET `Read` = ? WHERE `Name` = ?", true,
				ply.getName());
	}

	public boolean newsCheck(Player ply) {
		try {
			ResultSet r = plugin.sqldb.executeQuery(
					"SELECT * FROM `newsread` WHERE `Name`=?", ply.getName());
			if (!r.first()) {
				plugin.sqldb.executeSQL("INSERT INTO `newsread` VALUES(?, ?)",
						ply.getName(), false);
				return false;
			} else {
				return r.getBoolean("Read");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
