package com.shankshock.nicatronTg.Registration.Awards;

import org.bukkit.entity.Player;

import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.SPlayer;
import com.shankshock.nicatronTg.Registration.Awards.AwardManager.AwardType;

public abstract class Award {
	String description;
	boolean pureDisabled;
	AwardType type;

	public Award(AwardType type, String description, boolean hidden) {
		this.type = type;
		this.description = description;
		this.pureDisabled = hidden;
	}

	public AwardType getType() {
		return type;
	}

	public String getName() {
		return type.getName();
	}

	public String getDescription() {
		return description;
	}

	protected abstract void awardAchievement(Registration plugin, Player ply);

	public void addAchievement(Registration plugin, Player ply) {
		SPlayer sply = plugin.players.get(ply.getName());
		sply.getAwardStore().addAward(type);
		sply.saveAccount();
		this.awardAchievement(plugin, ply);
	}
}
