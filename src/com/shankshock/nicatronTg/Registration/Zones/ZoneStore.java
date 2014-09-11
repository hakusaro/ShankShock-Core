package com.shankshock.nicatronTg.Registration.Zones;

import java.util.ArrayList;

public class ZoneStore {
	private ArrayList<Zone> zoneStore = new ArrayList<Zone>();

	public ZoneStore() {

	}

	public void addZone(Zone z) {
		zoneStore.add(z);
	}

	public void removeZone(Zone z) {
		zoneStore.remove(z);
	}

	public ArrayList<Zone> getZones() {
		return zoneStore;
	}

	public void removeZone(String name) {
		for (Zone z : zoneStore) {
			if (z.getName().toLowerCase().equals(name.toLowerCase())) {
				zoneStore.remove(z);
			}
		}
	}
}
