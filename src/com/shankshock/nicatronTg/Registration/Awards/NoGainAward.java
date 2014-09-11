package com.shankshock.nicatronTg.Registration.Awards;

import org.bukkit.entity.Player;

import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.Awards.AwardManager.AwardType;

public class NoGainAward extends Award {

	public NoGainAward(AwardType type, String description, boolean hidden) {
		super(type, description, hidden);
	}

	@Override
	protected void awardAchievement(Registration plugin, Player ply) {

	}

}
