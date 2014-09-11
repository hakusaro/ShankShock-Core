package com.shankshock.nicatronTg.Registration.Engines;

import java.sql.ResultSet;
import java.util.ArrayList;

import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.SPlayer;

public class PureEngine {
	private Registration plugin;
	private ArrayList<String> players = new ArrayList<String>();

	public PureEngine(Registration instance) {
		plugin = instance;
		cacheDatabase();
	}

	public boolean attemptPlayerInvite(SPlayer sply, String player,
			int invitesToAward) {
		try {
			ResultSet r = plugin.sqldb.executeQuery(
					"SELECT invites FROM `pure-invites` WHERE username=?", sply
							.getPlayer().getName());
			r.next();
			int invites = r.getInt("invites");
			if (invites > 0) {
				invites--;
				plugin.sqldb.executeSQL(
						"INSERT INTO `pure-invites` VALUES(?, ?)", player,
						invitesToAward);

				plugin.sqldb.executeSQL(
						"UPDATE `pure-invites` SET invites=? WHERE username=?",
						invites, sply.getPlayer().getName());
				cacheDatabase();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void cacheDatabase() {
		try {
			ResultSet results = plugin.sqldb
					.executeQuery("SELECT * FROM `pure-invites`");
			ArrayList<String> playerList = new ArrayList<String>();
			while (results.next()) {
				if (!results.isAfterLast()) {
					playerList.add(results.getString("username"));
				}
			}
			players = playerList;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isPlayerInvited(String s) {
		if (players.contains(s)) {
			return true;
		} else {
			return false;
		}
	}
}
