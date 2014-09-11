package com.shankshock.nicatronTg.Registration.ShopConversations;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;

import com.shankshock.nicatronTg.Registration.Registration;

public class NameChangePrompt extends ValidatingPrompt {

	private final Registration plugin;

	public NameChangePrompt(Registration instance) {
		plugin = instance;
	}

	@Override
	public String getPromptText(ConversationContext context) {
		context.getForWhom().sendRawMessage(
				ChatColor.AQUA
						+ "Shop: Your name must not exceed 14 characters.");
		return "What name would you like?";
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext arg0, String arg1) {
		arg0.setSessionData("name", arg1);
		return new FinalShopConfirmation(plugin);
	}

	@Override
	protected boolean isInputValid(ConversationContext arg0, String arg1) {
		if (arg1.length() > 14 || arg1.length() == 0) {
			return false;
		}
		return true;
	}

}
