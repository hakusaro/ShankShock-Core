package com.shankshock.nicatronTg.Registration.Engines;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.shankshock.nicatronTg.Registration.Registration;

public class AutorunEngine {

	private final Registration plugin;

	public AutorunEngine(Registration instance) {
		plugin = instance;
	}

	public ArrayList<String> readAutoruns(String username) {
		ArrayList<String> ars = new ArrayList<String>();
		try {
			ResultSet r = plugin.sqldb.executeQuery(
					"SELECT * FROM `autoruns` WHERE `username`=?", username);
			while (r.next()) {
				if (!r.isAfterLast()) {
					ars.add(r.getString("command"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ars;
	}

	public boolean remove(String command, Player ply) {
		return plugin.sqldb.executeSQL(
				"DELETE FROM `autoruns` WHERE username=? AND command=?", ply
						.getPlayer().getName(), command);
	}

	public boolean deleteAll(Player ply) {
		return plugin.sqldb.executeSQL(
				"DELETE FROM `autoruns` WHERE username=?", ply.getPlayer()
						.getName());
	}

	public boolean add(String command, Player ply) {
		return plugin.sqldb.executeSQL("INSERT INTO `autoruns` VALUES(?, ?)",
				ply.getName(), command);
	}

	public void run(Player ply) {
		// ResultSet r =
		// plugin.sqldb.executeQuery("SELECT * FROM `autoruns` WHERE `username`=?",
		// ply.getName());
		// try {
		// while (r.next()) {
		// ply.performCommand(r.getString("command").replace("/", ""));
		// }
		// } catch (Exception e) {
		// }
	}
}
