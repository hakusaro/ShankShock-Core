package com.shankshock.Redis.Messages;

import java.io.Serializable;

public interface Message extends Serializable {
	public String origin = "";

	public Message handle(Object data);

	public String serialize();
}
