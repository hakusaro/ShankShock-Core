package com.shankshock.nicatronTg.Registration.InventoryConversations;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.PlayerNamePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

import com.shankshock.nicatronTg.Registration.Registration;

public class SilverTransferPlayerPrompt extends PlayerNamePrompt {

	private final Registration plugin;

	public SilverTransferPlayerPrompt(Registration instance) {
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
		return "Who would you like to send Silver to?";
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context,
			Player input) {
		context.setSessionData("player", input);
		return new SilverTransferItem(plugin);
	}

}
