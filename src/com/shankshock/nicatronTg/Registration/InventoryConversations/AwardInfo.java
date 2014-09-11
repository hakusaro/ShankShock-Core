package com.shankshock.nicatronTg.Registration.InventoryConversations;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;

import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.SPlayer;
import com.shankshock.nicatronTg.Registration.Awards.Award;
import com.shankshock.nicatronTg.Registration.Awards.CounterAward;
import com.shankshock.nicatronTg.Registration.Awards.RawAward;

public class AwardInfo extends ValidatingPrompt {

	private final Registration plugin;
	Award award;

	public AwardInfo(Registration instance) {
		this.plugin = instance;
	}

	@Override
	public String getPromptText(ConversationContext arg0) {
		award = (Award) arg0.getSessionData("award");
		arg0.getForWhom().sendRawMessage(
				ChatColor.AQUA + "Inventory: " + ChatColor.GOLD + "Award: "
						+ ChatColor.AQUA + award.getName());
		Player ply = (Player) arg0.getForWhom();
		SPlayer sply = plugin.players.get(ply.getName());
		if (award instanceof CounterAward) {
			CounterAward counterAward = (CounterAward) award;
			ArrayList<RawAward> rawAwards = sply.getRawAwardStore()
					.getRawAwards();
			RawAward temp = null;
			for (RawAward r : rawAwards) {
				if (r.getType() == award.getType()) {
					temp = r;
				}
			}

			if (temp == null) {
				arg0.getForWhom().sendRawMessage(
						ChatColor.AQUA + "Inventory: " + ChatColor.GOLD
								+ "Award Description: " + ChatColor.AQUA
								+ award.getDescription());
				arg0.getForWhom().sendRawMessage(
						ChatColor.AQUA + "Inventory: " + ChatColor.GOLD
								+ "Award Status: " + ChatColor.AQUA
								+ counterAward.getMaxMetaData() + "/"
								+ counterAward.getMaxMetaData());
			} else {
				arg0.getForWhom().sendRawMessage(
						ChatColor.AQUA + "Inventory: " + ChatColor.GOLD
								+ "Award Description: " + ChatColor.AQUA
								+ award.getDescription());
				arg0.getForWhom().sendRawMessage(
						ChatColor.AQUA + "Inventory: " + ChatColor.GOLD
								+ "Award Status: " + ChatColor.AQUA
								+ temp.getMetaData() + "/"
								+ counterAward.getMaxMetaData());
			}
		} else {
			arg0.getForWhom().sendRawMessage(
					ChatColor.AQUA + "Inventory: " + ChatColor.GOLD
							+ "Award Description: " + ChatColor.AQUA
							+ award.getDescription());
		}
		return "Return back to the award list?";
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext arg0, String arg1) {
		if (arg1.toLowerCase().equals("back")
				|| arg1.toLowerCase().equals("yes")) {
			return new AwardList(plugin);
		} else {
			return new InventoryStart(plugin);
		}
	}

	@Override
	protected boolean isInputValid(ConversationContext arg0, String arg1) {
		return true;
	}
}
