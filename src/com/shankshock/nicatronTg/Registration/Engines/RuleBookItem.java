package com.shankshock.nicatronTg.Registration.Engines;

import org.bukkit.Material;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.Items.Item;
import com.shankshock.nicatronTg.Registration.Items.ItemType;
import com.shankshock.nicatronTg.Registration.Items.SilverManager.ShopType;

public class RuleBookItem extends Item{

	public RuleBookItem(ItemType type, String name, ShopType category,
			String description, int cost) {
		super(type, name, category, false, description, cost);
	}

	public RuleBookItem() {
		super(null, "Rules", ShopType.NOT_FOR_SALE, false, "-", 0);
	}
	
	@Override
	public void runExtraData(ConversationContext context, Registration plugin) {
		Player ply = (Player) context.getForWhom();
		
		giveItem(ply, plugin);
	}
	
	public void giveItem(Player ply, Registration plugin) {
		ItemStack i = new ItemStack(Material.WRITTEN_BOOK);
		
		ItemFactory itemFactory = plugin.getServer().getItemFactory();
		
		BookMeta bookMeta = (BookMeta) itemFactory.getItemMeta(Material.WRITTEN_BOOK);
		
		bookMeta.setTitle("The Manual");
		bookMeta.setAuthor("Shank");
		bookMeta.setPages("Welcome to BlockShock, a ShankShock Production.\n\nThis manual contains the rules, commands, and basics for surviving on the server.\n\nMore info @ ShankShock.Com.",
				"## The Rules\n\n1. No chat spamming.\n2. No hacking mods.\n3. Don't steal, break, or cause grief to any player or their creations.\n4. Don't ask for admin.\n5. Don't advertise.\n6. Respect all players.",
				"## Commands\n\n1. The usual home/warp commands.\n2. /inventory\n3. /shop\n4. /name",
				"## Mods\n\nExplosions & TNT are disabled. Creepers can't break your stuff. You can't drown. Falling damage and fire spread are disabled too.",
				"## Conversations\n\nSome commands, such as the inventory and shop commands, will take over your chat box. Simply type a response to what they ask for to use them. You can always exit with /quit.",
				"## Exiting Spawn\n\nTo exit spawn, simply warp to another location.\n\n/warp ShadowVale",
				"## Help\n\nAdmins have light blue name colors in chat. The owners are dark red and cyan. You can ask them for any help you need, though they don't have to give it. Admins aren't your slaves.",
				"## More Info\n\nMore information can be found at:\n\nwiki.shankshock.com\nshankshock.com");
		
		i.setItemMeta(bookMeta);
		
		ply.getInventory().addItem(i);
	}

	@Override
	public void runPurchaseAnnoucement(ConversationContext context,
			Registration plugin) {
	}

}
