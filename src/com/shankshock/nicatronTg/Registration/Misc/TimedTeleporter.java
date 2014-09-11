package com.shankshock.nicatronTg.Registration.Misc;

import org.bukkit.Location;

import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.SPlayer;

public class TimedTeleporter implements Runnable {
	private final Registration plugin;
	Location destination = null;
	String name;

	public TimedTeleporter(Registration instance, Location destination,
			String name) {
		plugin = instance;
		this.destination = destination;
		this.name = name;
	}

	@Override
	public void run() {
		try {
			SPlayer sply = plugin.players.get(name);
			if (sply == null) {
				return;
			}
			sply.getPlayer().teleport(destination);
			sply.sendMessage("Scheduled teleport completed.");
		} catch (NullPointerException e) {
			if (plugin.debug) {
				System.out
						.println("NullPointerException occurred during teleport process.");
			}
		}
	}
}
