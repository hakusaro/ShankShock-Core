package com.shankshock.nicatronTg.Registration.ShopConversations;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;

import com.shankshock.nicatronTg.Registration.Registration;

public class MOTDChangePrompt extends ValidatingPrompt {

	private final Registration plugin;

	public MOTDChangePrompt(Registration instance) {
		plugin = instance;
	}

	@Override
	public String getPromptText(ConversationContext arg0) {
		arg0.getForWhom()
				.sendRawMessage(
						ChatColor.AQUA
								+ "Shop: Requirements: Given MOTD be between 1 and 30 characters.");
		return "What would you like the new status screen MOTD to be?";
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext arg0, String arg1) {
		arg0.setSessionData("motd", arg1);
		return new FinalShopConfirmation(plugin);
	}

	@Override
	protected boolean isInputValid(ConversationContext arg0, String arg1) {
		if (arg1.trim().length() == 0 || arg1.trim().length() > 30) {
			return false;
		}
		return true;
	}

}
