package com.shankshock.nicatronTg.Registration.ShopConversations;

import java.text.NumberFormat;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;

import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.SPlayer;
import com.shankshock.nicatronTg.Registration.Items.SilverManager.ShopType;

public class ShopCategoryChooser extends ValidatingPrompt {

	private final Registration plugin;
	private boolean enableBlackMarket = false;
	private boolean enableAdminShop = false;

	public ShopCategoryChooser(Registration instance) {
		plugin = instance;
	}

	@Override
	public String getPromptText(ConversationContext context) {
		context.getForWhom().sendRawMessage(ChatColor.RED + "Shop: To exit at any time, type: " + ChatColor.RED + "LEAVENOW!");
		context.getForWhom().sendRawMessage(ChatColor.AQUA + "Shop: Welcome to the official ShankShock silver shop.");
		setupDiscount(context);
		setupFinalDiscount(context);

		if ((Boolean) context.getSessionData("pure")) {
			context.getForWhom().sendRawMessage(ChatColor.AQUA + "Shop: " + ChatColor.YELLOW + "Shop is currently limited, as you're in Pure.");
		}

		context.getForWhom().sendRawMessage(ChatColor.AQUA + "Shop: " + ChatColor.GOLD + "Shops:");

		String[] categories = plugin.zoneManager.getRegionalCategories(((Player) context.getForWhom()).getLocation());

		if (categories != null) {
			for (String s : categories) {
				if (s.contains("blackmarket")) {
					enableBlackMarket = true;
				}
			}
		} else {
			enableBlackMarket = false;
		}

		if (categories != null) {
			for (String s : categories) {
				if (s.contains("adminshop")) {
					enableAdminShop = true;
				}
			}
		} else {
			enableAdminShop = false;
		}

		if (!((Boolean) context.getSessionData("pure"))) {
			context.getForWhom().sendRawMessage(ChatColor.AQUA + "Shop: " + ChatColor.GREEN + "1. Items");
			context.getForWhom().sendRawMessage(ChatColor.AQUA + "Shop: " + ChatColor.GREEN + "2. Blocks");
			context.getForWhom().sendRawMessage(ChatColor.AQUA + "Shop: " + ChatColor.GREEN + "3. Potions");
			context.getForWhom().sendRawMessage(ChatColor.AQUA + "Shop: " + ChatColor.RED + "4. Armor");
			context.getForWhom().sendRawMessage(ChatColor.AQUA + "Shop: " + ChatColor.RED + "5. Kits");
			context.getForWhom().sendRawMessage(ChatColor.AQUA + "Shop: " + ChatColor.GOLD + "6. Spawn Eggs");
		}
		context.getForWhom().sendRawMessage(ChatColor.AQUA + "Shop: " + ChatColor.LIGHT_PURPLE + "7. Misc");
		context.getForWhom().sendRawMessage(ChatColor.AQUA + "Shop: " + ChatColor.LIGHT_PURPLE + "8. Chat Titles");
		context.getForWhom().sendRawMessage(ChatColor.AQUA + "Shop: " + ChatColor.LIGHT_PURPLE + "9. Chat Colors");

		if (enableBlackMarket) {
			context.getForWhom().sendRawMessage(ChatColor.AQUA + "Shop: " + ChatColor.DARK_GRAY + "10. Black Market");
		}

		if (enableAdminShop) {
			context.getForWhom().sendRawMessage(ChatColor.AQUA + "Shop: " + ChatColor.GOLD + "11. Admin Shop");
		}

		return "Which shop would you like to see?";
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context,
			String input) {
		
		try {
			int choice = Integer.parseInt(input.toString());
			
			for (ShopType s : ShopType.values()) {
				if (s.getChoiceNumber() == choice) {
					context.setSessionData("category", s);
				}
			}
			
		} catch (NumberFormatException e) { }
		
		for (ShopType s : ShopType.values()) {
			if (s.getShopName().equalsIgnoreCase(input)) {
				context.setSessionData("category", s);
			}
		}
		return new ShopItemPrompt(plugin);
	}

	@Override
	protected boolean isInputValid(ConversationContext context, String input) {

		if (((Boolean) context.getSessionData("pure"))) {
			
			int[] allowedItems = {8, 7, 9};
			
			for (int i : allowedItems) {
				if (Integer.toString(i).equals(input.toString())) {
					return true;
				}
			}
			
			if (input.equalsIgnoreCase("chat titles") || input.equalsIgnoreCase("misc") || input.equalsIgnoreCase("chat colors")) {
				return true;
			}
			return false;
		}

		try {
			int choice = Integer.parseInt(input.toString());
			
			if (choice > 0 && choice <= 11) {
				if (choice < 10) {
					return true;
				}
				
				if (choice == 10 && enableBlackMarket) {
					return true;
				}
				
				if (choice == 11 && enableAdminShop) {
					return true;
				}
			}
		} catch (NumberFormatException e) { }
		
		if (input.equalsIgnoreCase("Chat Titles") || input.equalsIgnoreCase("kits") || input.equalsIgnoreCase("items") || input.equalsIgnoreCase("blocks") || input.equalsIgnoreCase("potions") || input.equalsIgnoreCase("misc") || input.equalsIgnoreCase("armor") || input.equalsIgnoreCase("spawn eggs") || (input.equalsIgnoreCase("black market") && enableBlackMarket) || input.equalsIgnoreCase("chat colors") || (input.equalsIgnoreCase("admin shop") && enableAdminShop)) {
			return true;
		}
		return false;
	}

	public void setupFinalDiscount(ConversationContext context) {
		if (plugin.zoneManager.getShopDiscount(((Player) context.getForWhom()).getLocation()) > 0.00) {
			double discount = plugin.zoneManager.getShopDiscount(((Player) context.getForWhom()).getLocation());
			context.getForWhom().sendRawMessage(ChatColor.AQUA + "Shop: " + ChatColor.GOLD + "Regional Discount: " + ChatColor.GRAY + discount * 100 + "%");

			double oldDiscount = (Double) context.getSessionData("discount");

			discount += oldDiscount;

			if (discount > .9) {
				discount = .9;
			}
			context.getForWhom().sendRawMessage(ChatColor.AQUA + "Shop: " + ChatColor.GOLD + "Final Discount: " + discount * 100 + "%");
			context.setSessionData("discount", discount);
			context.setSessionData("usingdiscount", true);

		}
	}

	public void setupDiscount(ConversationContext context) {
		SPlayer sply = plugin.players.get(((Player) context.getForWhom()).getName());
		context.setSessionData("discount", 0.00);

		if (sply.getPlayer().getWorld().getName().contains("stock")) {
			context.setSessionData("pure", true);
		} else {
			context.setSessionData("pure", false);
		}

		boolean usingDiscount = false;

		if (plugin.conn.getGroup(sply.getPlayer()).contains("Donator")) {
			context.setSessionData("discount", .25);
			usingDiscount = true;
		}

		if (plugin.conn.getGroup(sply.getPlayer()).contains("DonatorPlus")) {
			context.setSessionData("discount", .5);
			usingDiscount = true;
		}

		if (plugin.conn.getGroup(sply.getPlayer()).contains("DonatorLevelFive")) {
			context.setSessionData("discount", .5);
			usingDiscount = true;
		}

		if (plugin.conn.getGroup(sply.getPlayer()).contains("Admin") || plugin.conn.getGroup(sply.getPlayer()).contains("Shank") || plugin.conn.getGroup(sply.getPlayer()).contains("Leo") || plugin.conn.getGroup(sply.getPlayer()).contains("Developer")) {
			context.setSessionData("discount", .99);
			usingDiscount = true;
		}

		double discount;

		if (usingDiscount) {
			discount = (Double) context.getSessionData("discount");
		} else {
			discount = 0.00;
		}

		if (discount != 0.00) {
			context.getForWhom().sendRawMessage(ChatColor.AQUA + "Shop: " + ChatColor.GOLD + "DONATOR DISCOUNT: " + ChatColor.GRAY + (discount * 100) + "%" + ChatColor.GOLD + " off all items.");
		}

		context.getForWhom().sendRawMessage(ChatColor.AQUA + "Shop: " + ChatColor.GOLD + "Available Silver: " + ChatColor.GRAY + NumberFormat.getInstance().format(sply.getCurrency()));

		if (usingDiscount) {
			context.setSessionData("usingdiscount", true);
			return;
		}
		context.setSessionData("usingdiscount", false);
	}

}
