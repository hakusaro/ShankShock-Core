package com.shankshock.nicatronTg.Registration.Misc;

public class ChatMessage {
	public long time;
	public String msg;

	public ChatMessage(long epoch, String message) {
		time = epoch;
		msg = message;
	}
}
