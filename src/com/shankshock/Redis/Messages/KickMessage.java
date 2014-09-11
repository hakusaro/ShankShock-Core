package com.shankshock.Redis.Messages;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import com.google.gson.Gson;
import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.SPlayer;

public class KickMessage implements Message {

	private static final long serialVersionUID = -8157398664071684814L;
	private String who;
	private String origin;
	private String reason;

	public KickMessage() {
	}

	public KickMessage(String who, String o, String r) {
		this.who = who;
		origin = o;
		reason = r;
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

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	public Message handle(Object data) {
		final Registration plugin = (Registration) data;

		if (!plugin.getConfig().getString("serverid").equals(origin)) {
			plugin.getServer().getScheduler()
					.scheduleSyncDelayedTask(plugin, new Runnable() {

						@Override
						public void run() {
							SPlayer ply = plugin.players.get(who);
							if (ply != null) {
								ply.kickPlayer(reason);
							}
						}

					});
		}
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
