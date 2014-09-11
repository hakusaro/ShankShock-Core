package com.shankshock.nicatronTg.Registration.ShopConversations;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;

import com.shankshock.nicatronTg.Registration.Registration;

public class WeatherChangePrompt extends ValidatingPrompt {

	private final Registration plugin;

	public WeatherChangePrompt(Registration instance) {
		plugin = instance;
	}

	@Override
	public String getPromptText(ConversationContext context) {
		context.getForWhom().sendRawMessage(
				ChatColor.AQUA + "Shop: Weather choices: stormy, sunny");
		return "What weather would you like it to be?";
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext arg0, String arg1) {
		if (arg1.toLowerCase().equals("sunny")) {
			arg0.setSessionData("storm", false);
		} else {
			arg0.setSessionData("storm", true);
		}
		return new FinalShopConfirmation(plugin);
	}

	@Override
	protected boolean isInputValid(ConversationContext arg0, String arg1) {
		if (arg1.toLowerCase().equals("stormy")
				|| arg1.toLowerCase().equals("sunny")) {
			return true;
		}
		return false;
	}
}
