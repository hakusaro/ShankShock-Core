package com.shankshock.nicatronTg.Registration.Misc;

import org.bukkit.entity.Player;

import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.SPlayer;

public class ServerIncrementialSaver implements Runnable {
	Registration plugin;

	public ServerIncrementialSaver(Registration instance) {
		this.plugin = instance;
	}

	@Override
	public void run() {
		try {
			for (Player ply : plugin.getServer().getOnlinePlayers()) {
				SPlayer sply = plugin.players.get(ply.getName());
				if (sply != null) {
					sply.saveAccount();
				}
				plugin.conn.sendNewsNotification(ply);
				plugin.conn.releaseFromJail(ply);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
