/* Zach Piispanen Registration zpg3 */
package com.shankshock.Redis.Messages;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import com.google.gson.Gson;

public class WhoRequest implements Message {

	private static final long serialVersionUID = -5336759244996758507L;
	private String command;
	private String who;
	private String origin;

	public WhoRequest() {
	}

	public WhoRequest(String cmd, String w, String o) {
		command = cmd;
		who = w;
		origin = o;
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

	@Override
	public Message handle(Object data) {
		String _data = (String) data;

		WhoResponse resp = new WhoResponse(command, who, origin, _data);
		return resp;
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
