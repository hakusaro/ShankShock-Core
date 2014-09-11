package com.shankshock.nicatronTg.Registration.InventoryConversations;

import java.text.NumberFormat;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.SPlayer;

public class SilverTransferItem extends NumericPrompt {

	private final Registration plugin;
	Player ply;
	SPlayer sply;

	public SilverTransferItem(Registration instance) {
		this.plugin = instance;
	}

	@Override
	public String getPromptText(ConversationContext context) {
		ply = (Player) context.getForWhom();
		sply = plugin.players.get(ply.getName());
		return "How much Silver would you like to send?";
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext arg0, Number arg1) {
		if (arg1.intValue() < 0) {
			return new SilverTransferItem(plugin);
		}

		if (arg1.intValue() > sply.getCurrency()) {
			return new SilverTransferItem(plugin);
		}

		Player dply = (Player) arg0.getSessionData("player");
		SPlayer dsply = plugin.players.get(dply.getName());
		dsply.sendMessage(sply.getPlayer().getDisplayName() + " (" + sply.getPlayer().getName() + ") just sent you " + arg1.intValue() + " silver.");
		dsply.addCurrency(arg1.intValue(), false);
		sply.delCurrency(arg1.intValue());
		arg0.getForWhom().sendRawMessage(
				ChatColor.AQUA + "Inventory: Transferred "
						+ NumberFormat.getInstance().format(arg1.intValue())
						+ " silver to " + dply.getName() + ".");
		return Prompt.END_OF_CONVERSATION;
	}

}
