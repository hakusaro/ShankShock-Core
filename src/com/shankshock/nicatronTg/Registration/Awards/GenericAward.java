package com.shankshock.nicatronTg.Registration.Awards;

import org.bukkit.entity.Player;

import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.Awards.AwardManager.AwardType;

public class GenericAward extends Award {

	private int earnAmount = 0;
	
	public GenericAward(AwardType type, String description, boolean hidden) {
		super(type, description, hidden);
	}
	
	public GenericAward(AwardType type, String description, boolean hidden, int earnAmount) {
		super(type, description, hidden);
		this.earnAmount = earnAmount;
	}

	@Override
	protected void awardAchievement(Registration plugin, Player ply) {

	}
	
	public int getEarnAmount() {
		return earnAmount;
	}

}
