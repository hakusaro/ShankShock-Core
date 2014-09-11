package com.shankshock.nicatronTg.Registration.Awards;

import org.bukkit.entity.Player;

import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.SPlayer;
import com.shankshock.nicatronTg.Registration.Awards.AwardManager.AwardType;
import com.shankshock.nicatronTg.Registration.Items.Item;
import com.shankshock.nicatronTg.Registration.Items.ItemType;
import com.shankshock.nicatronTg.Registration.Items.SilverManager;

public class DarkphantomSealOfAwesome extends Award {

	public DarkphantomSealOfAwesome(AwardType type, String description,
			boolean hidden) {
		super(type, description, hidden);
	}

	@Override
	public void awardAchievement(Registration plugin, Player ply) {
		SPlayer sply = plugin.players.get(ply.getName());

		for (Item i : SilverManager.items) {
			if (i.getItemType() == ItemType.TITLE_ARCHITECT) {
				sply.getInventoryStore().addItem(i);
			}
		}
	}

}
