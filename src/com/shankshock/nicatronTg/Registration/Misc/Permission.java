package com.shankshock.nicatronTg.Registration.Misc;

public class Permission {
	public boolean revoke = false;
	public String permission = "";

	public Permission(String p, boolean Revoke) {
		this.revoke = Revoke;
		permission = p;
	}
}
