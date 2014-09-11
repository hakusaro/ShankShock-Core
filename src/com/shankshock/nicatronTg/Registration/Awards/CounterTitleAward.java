package com.shankshock.nicatronTg.Registration.Awards;

import org.bukkit.entity.Player;

import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.SPlayer;
import com.shankshock.nicatronTg.Registration.Awards.AwardManager.AwardType;
import com.shankshock.nicatronTg.Registration.Items.Item;
import com.shankshock.nicatronTg.Registration.Items.ItemType;
import com.shankshock.nicatronTg.Registration.Items.SilverManager;

public class CounterTitleAward extends CounterAward {

	private ItemType itemType;

	public CounterTitleAward(AwardType type, String description,
			boolean hidden, float maxMetaData, ItemType item) {
		super(type, description, hidden, maxMetaData);
		this.itemType = item;
	}

	@Override
	public void awardAchievement(Registration plugin, Player ply) {
		SPlayer sply = plugin.players.get(ply.getName());

		Item i = null;

		for (Item item : SilverManager.items) {
			if (item.getItemType() == itemType) {
				i = item;
			}
		}

		sply.getInventoryStore().addItem(i);
	}

}
