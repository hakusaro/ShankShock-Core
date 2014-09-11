package com.shankshock.nicatronTg.Registration.Awards;

import com.shankshock.nicatronTg.Registration.Awards.AwardManager.AwardType;

public class RawAward {
	private float metadata = (float) 0.000;
	private AwardType award;

	public RawAward() {

	}

	public RawAward(AwardType type, float metadata) {
		this.award = type;
		this.metadata = metadata;
	}

	public AwardType getType() {
		return award;
	}

	public double getMetaData() {
		return metadata;
	}

	public void setMetaData(float amount) {
		metadata = amount;
	}

	public void addMetaData(float amount) {
		metadata = metadata + amount;
	}
}
