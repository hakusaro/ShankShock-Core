package com.shankshock.nicatronTg.Registration.InventoryConversations;

import java.text.NumberFormat;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;

import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.SPlayer;

public class InventoryStart extends ValidatingPrompt {

	private final Registration plugin;

	public InventoryStart(Registration instance) {
		plugin = instance;
	}

	@Override
	public String getPromptText(ConversationContext context) {
		Player ply = (Player) context.getForWhom();
		SPlayer sply = plugin.players.get(ply.getName());
		context.getForWhom().sendRawMessage(
				ChatColor.AQUA + "Inventory: "
						+ "Welcome to your ShankShock inventory!");
		context.getForWhom().sendRawMessage(
				ChatColor.AQUA + "Inventory: " + "To exit at any time, type: "
						+ ChatColor.RED + "LEAVENOW!");
		context.getForWhom().sendRawMessage(
				ChatColor.AQUA + "Inventory: " + ChatColor.GRAY + "Silver: "
						+ NumberFormat.getInstance().format(sply.getCurrency()));
		context.getForWhom().sendRawMessage(
				ChatColor.AQUA + "Inventory: " + "Options: ");
		context.getForWhom().sendRawMessage(
				ChatColor.AQUA + "Inventory: " + ChatColor.GOLD + "1. "
						+ ChatColor.AQUA + "View items");
		context.getForWhom().sendRawMessage(
				ChatColor.AQUA + "Inventory: " + ChatColor.GOLD + "2. "
						+ ChatColor.AQUA + "Transfer Silver");
		context.getForWhom().sendRawMessage(
				ChatColor.AQUA + "Inventory: " + ChatColor.GOLD + "3. "
						+ ChatColor.AQUA + "View awards");
		return "Which option number?";
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context,
			String input) {
		int option = Integer.parseInt(input);
		Player ply = (Player) context.getForWhom();
		SPlayer sply = plugin.players.get(ply.getName());

		if (option == 3) {
			return new AwardList(plugin);
		} else if (option == 2) {
			return new SilverTransferPlayerPrompt(plugin);
		} else if (option == 1) {
			if (sply.getInventoryStore().getInventory().size() == 0) {
				context.getForWhom().sendRawMessage(
						ChatColor.AQUA + "Inventory: " + ChatColor.RED
								+ "You have no items. Buy some to fix this!");
				return new InventoryStart(plugin);
			}
			return new ItemList(plugin);
		}
		return new InventoryStart(plugin);
	}

	@Override
	protected boolean isInputValid(ConversationContext context, String input) {
		int option;

		try {
			option = Integer.parseInt(input);
		} catch (NumberFormatException e) {
			return false;
		}

		if (option == 1) {
			return true;
		} else if (option == 2) {
			return true;
		} else if (option == 3) {
			return true;
		}
		return false;
	}

}
