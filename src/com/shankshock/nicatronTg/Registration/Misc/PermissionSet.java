package com.shankshock.nicatronTg.Registration.Misc;

import java.util.ArrayList;

public class PermissionSet {
	public String group = "";
	public ArrayList<Permission> permissions = new ArrayList<Permission>();

	public PermissionSet(String group, ArrayList<Permission> perms) {
		permissions = perms;
		this.group = group;
	}

	public PermissionSet() {
		group = "none";
	}

	public void add(Permission p) {
		permissions.add(p);
	}
}
