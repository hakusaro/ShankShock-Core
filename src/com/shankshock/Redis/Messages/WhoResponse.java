/* Zach Piispanen Registration zpg3 */
package com.shankshock.Redis.Messages;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.google.gson.Gson;
import com.shankshock.nicatronTg.Registration.Registration;

public class WhoResponse implements Message {

	private static final long serialVersionUID = -48218821471165365L;
	private String command;
	private String who;
	private String origin;
	private String message;

	public WhoResponse() {
	}

	public WhoResponse(String cmd, String w, String o, String msg) {
		command = cmd;
		who = w;
		origin = o;
		message = msg;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getWho() {
		return who;
	}

	public void setWho(String who) {
		this.who = who;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public Message handle(Object data) {
		final Registration plugin = (Registration) data;

		if (!plugin.getConfig().getString("serverid").equals(origin)) {
			return null;
		}

		plugin.getServer().getScheduler()
				.scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						Player ply = plugin.getServer().getPlayer(who);
						if (ply != null) {
							ply.sendMessage(ChatColor.GRAY + message);
						}
					}
				});

		return null;
	}

	@Override
	public String serialize() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(this);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Gson().toJson(bos);
	}
}
