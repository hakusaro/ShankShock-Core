package com.shankshock.nicatronTg.Registration.ShopConversations;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.PlayerNamePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

import com.shankshock.nicatronTg.Registration.Registration;

public class PlayerSelector extends PlayerNamePrompt {

	private final Registration plugin;

	public PlayerSelector(Registration instance) {
		super(instance);
		plugin = instance;
	}

	@Override
	public String getPromptText(ConversationContext arg0) {
		arg0.getForWhom().sendRawMessage(
				ChatColor.AQUA + "Shop: " + ChatColor.RED + "Online Players:");
		for (Player ply : plugin.getServer().getOnlinePlayers()) {
			arg0.getForWhom().sendRawMessage(
					ChatColor.AQUA + "Shop: " + ChatColor.RED + ply.getName()
							+ " (" + ply.getDisplayName() + ")");
		}
		return "Who would you like to assassinate?";
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context,
			Player input) {
		if (input.getWorld().getName().contains("stock")) {
			context.getForWhom()
					.sendRawMessage(
							ChatColor.RED
									+ "Player is in Pure, and can't be selected at this time.");
			return new PlayerSelector(plugin);
		}
		context.setSessionData("player", input);
		return new FinalShopConfirmation(plugin);
	}

}
