package com.shankshock.nicatronTg.Registration.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import com.shankshock.Redis.WorldConfig;
import com.shankshock.nicatronTg.Registration.Registration;

public class ServerListener implements Listener {
	private final Registration plugin;

	public ServerListener(Registration instance) {
		plugin = instance;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onServerListPing(ServerListPingEvent e) {

		if (plugin.debug) {
			e.setMotd("Debug mode!");
			return;
		}
		e.setMotd(plugin.globalConfig.getValue("motd"));
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onWorldLoad(WorldLoadEvent e) {
		if (plugin.worlds.containsKey(e.getWorld().getName())) {
			plugin.worlds.remove(e.getWorld().getName());
		}
		
		plugin.worlds.put(e.getWorld().getName(), new WorldConfig(plugin.redisDatabase, e.getWorld().getName(), plugin.getConfig().getString("uniqueid")));
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onWorldUnload(WorldUnloadEvent e) {
		plugin.worlds.remove(e.getWorld().getName());
	}

}
