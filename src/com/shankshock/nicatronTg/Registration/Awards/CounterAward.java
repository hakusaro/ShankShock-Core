package com.shankshock.nicatronTg.Registration.Awards;

import org.bukkit.entity.Player;

import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.Awards.AwardManager.AwardType;

public abstract class CounterAward extends Award {

	private float maxMetaData;

	public CounterAward(AwardType type, String description, boolean hidden,
			float maxMetaData) {
		super(type, description, hidden);
		this.maxMetaData = maxMetaData;
	}

	@Override
	public abstract void awardAchievement(Registration plugin, Player ply);

	public float getMaxMetaData() {
		return maxMetaData;
	}

}
