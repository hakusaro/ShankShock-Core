package com.shankshock.nicatronTg.Registration.Misc;

import java.util.ArrayList;

public class ChatBundle {

	public ArrayList<ChatMessage> chats = new ArrayList<ChatMessage>();

	public ChatBundle() {
	}

	public ArrayList<ChatMessage> getChats() {
		return chats;
	}

	public void addChat(ChatMessage c) {
		if (chats.size() > 5) {
			chats.remove(0);
		}

		chats.add(c);
	}
}
