package com.shankshock.nicatronTg.Registration.Awards;

import com.shankshock.nicatronTg.Registration.Awards.AwardManager.AwardType;
import com.shankshock.nicatronTg.Registration.Items.Item;
import com.shankshock.nicatronTg.Registration.Items.ItemType;
import com.shankshock.nicatronTg.Registration.Items.SilverManager;
import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.SPlayer;
import org.bukkit.entity.Player;

public class ActivationAward extends NoGainAward {

	public ActivationAward(AwardType type, String description, boolean hidden) {
		super(type, description, hidden);
	}

	@Override
	protected void awardAchievement(Registration plugin, Player ply) {
		SPlayer sply = plugin.players.get(ply.getName());

		Item i = null;

		for (Item item : SilverManager.items) {
			if (item.getItemType() == ItemType.TITLE_NOOB) {
				i = item;
			}
		}

		sply.getInventoryStore().addItem(i);
	}

}
