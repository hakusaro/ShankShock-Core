package com.shankshock.nicatronTg.Registration.InventoryConversations;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;

import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.SPlayer;
import com.shankshock.nicatronTg.Registration.Awards.Award;
import com.shankshock.nicatronTg.Registration.Awards.AwardManager.AwardType;
import com.shankshock.nicatronTg.Registration.Awards.RawAward;

public class AwardList extends ValidatingPrompt {

	private final Registration plugin;
	HashMap<Integer, Award> awards;

	public AwardList(Registration instance) {
		this.plugin = instance;
	}

	@Override
	public String getPromptText(ConversationContext arg0) {
		awards = new HashMap<Integer, Award>();
		Player ply = (Player) arg0.getForWhom();
		SPlayer sply = plugin.players.get(ply.getName());

		ArrayList<Award> alreadyAdded = new ArrayList<Award>();
		int temp = 1;

		arg0.getForWhom().sendRawMessage(
				ChatColor.AQUA + "Inventory: " + ChatColor.GOLD
						+ "Your in progress or completed awards:");

		for (Award i : plugin.awardManager.getAwards()) {
			for (AwardType t : sply.getAwardStore().getAwards()) {
				if (i.getType() == t && !alreadyAdded.contains(i)) {
					if (temp == 1) {
						awards.put(temp, i);
						temp++;
					} else {
						awards.put(temp, i);
						temp++;
					}
					alreadyAdded.add(i);

				}
			}

			for (RawAward t : sply.getRawAwardStore().getRawAwards()) {
				if (i.getType() == t.getType() && !alreadyAdded.contains(i)) {
					if (temp == 1) {
						awards.put(temp, i);
						temp++;
					} else {
						awards.put(temp, i);
						temp++;
					}
					alreadyAdded.add(i);
				}
			}
		}

		for (int k = 0; k < temp; k++) {
			Award i = awards.get(k);

			if (i == null) {
				continue;
			}
			arg0.getForWhom().sendRawMessage(
					ChatColor.AQUA + "Inventory: " + ChatColor.GOLD + k
							+ ChatColor.AQUA + ". " + ChatColor.LIGHT_PURPLE
							+ i.getName());
		}
		return "Which award number would you like to see?";
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext arg0, String arg1) {
		int i = Integer.parseInt(arg1);
		Award choice = awards.get(i);
		arg0.setSessionData("award", choice);
		return new AwardInfo(plugin);
	}

	@Override
	protected boolean isInputValid(ConversationContext arg0, String arg1) {
		try {
			int i = Integer.parseInt(arg1);
			if (awards.get(i) != null) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

}
