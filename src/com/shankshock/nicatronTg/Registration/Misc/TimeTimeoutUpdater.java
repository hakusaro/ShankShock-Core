package com.shankshock.nicatronTg.Registration.Misc;

import com.shankshock.nicatronTg.Registration.Registration;

public class TimeTimeoutUpdater implements Runnable {
	Registration plugin;

	public TimeTimeoutUpdater(Registration instance) {
		this.plugin = instance;
	}

	@Override
	public void run() {

		plugin.timeLockedOut = false;
	}

}
