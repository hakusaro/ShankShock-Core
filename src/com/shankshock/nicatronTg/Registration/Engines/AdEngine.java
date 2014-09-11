package com.shankshock.nicatronTg.Registration.Engines;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.shankshock.nicatronTg.Registration.Registration;

public class AdEngine {

	private final Registration plugin;
	private ArrayList<String> ads = new ArrayList<String>();

	public AdEngine(Registration instance, int ticks) {
		plugin = instance;
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new TimedAdDeploy(), 0, ticks);
		setupAds();
	}

	public String getRandomAdvert() {
		Random r = new Random();
		int id = r.nextInt(ads.size());

		if (ads.size() == 1) {
			id = 0;
		}

		return ads.get(id);
	}

	public void setupAds() {
		try {
			ResultSet r = plugin.sqldb.executeQuery("SELECT * FROM `advertisements`");
			ads.clear();
			while (r.next()) {
				ads.add(r.getString("advertisement"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public class TimedAdDeploy implements Runnable {
		@Override
		public void run() {
			for (Player ply : plugin.getServer().getOnlinePlayers()) {
				ply.sendMessage(ChatColor.AQUA + getRandomAdvert());
			}
		}
	}
}
