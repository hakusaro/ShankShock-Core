package com.shankshock.nicatronTg.Registration.Awards;

import org.bukkit.entity.Player;

import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.Awards.AwardManager.AwardType;

public class GenericCounterAward extends CounterAward {

	public GenericCounterAward(AwardType type, String description,
			boolean hidden, float maxMetaData) {
		super(type, description, hidden, maxMetaData);
	}

	@Override
	public void awardAchievement(Registration plugin, Player ply) {
	}

}
