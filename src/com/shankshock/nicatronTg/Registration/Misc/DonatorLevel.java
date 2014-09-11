package com.shankshock.nicatronTg.Registration.Misc;

public enum DonatorLevel {
	DonatorLevelOne("DonatorLevelOne", 1),
	DonatorLevelTwo("DonatorLevelTwo", 2),
	DonatorLevelThree("DonatorLevelThree", 3),
	DonatorLevelFour("DonatorLevelFour", 4),
	DonatorLevelFive("DonatorLevelFive", 5);
	
	private String name = "";
	private int level = 0;
	
	DonatorLevel(String name, int level) {
		this.setName(name);
		this.setLevel(level);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
