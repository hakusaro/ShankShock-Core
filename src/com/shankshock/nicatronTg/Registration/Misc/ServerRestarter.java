package com.shankshock.nicatronTg.Registration.Misc;

import org.bukkit.Server;
import org.bukkit.entity.Player;

public class ServerRestarter implements Runnable {
	Server serv;
	Player ply;

	public ServerRestarter(Server server, Player player) {
		serv = server;
		ply = player;
	}

	@Override
	public void run() {

		serv.shutdown();
	}

}
