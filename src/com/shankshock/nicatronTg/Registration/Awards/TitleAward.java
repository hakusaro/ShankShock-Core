package com.shankshock.nicatronTg.Registration.Awards;

import org.bukkit.entity.Player;

import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.SPlayer;
import com.shankshock.nicatronTg.Registration.Awards.AwardManager.AwardType;
import com.shankshock.nicatronTg.Registration.Items.Item;
import com.shankshock.nicatronTg.Registration.Items.ItemType;
import com.shankshock.nicatronTg.Registration.Items.SilverManager;

public class TitleAward extends Award {

	private ItemType itemType;

	public TitleAward(AwardType type, String description, boolean hidden,
			ItemType itemType) {
		super(type, description, hidden);
		this.itemType = itemType;
	}

	@Override
	protected void awardAchievement(Registration plugin, Player ply) {
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
