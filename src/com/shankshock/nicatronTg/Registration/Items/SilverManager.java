package com.shankshock.nicatronTg.Registration.Items;

import com.shankshock.nicatronTg.Registration.Awards.AwardManager.AwardType;
import com.shankshock.nicatronTg.Registration.Registration;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.SpawnEgg;

import java.util.ArrayList;
import java.util.HashMap;

public class SilverManager {
	private final Registration plugin;
	public HashMap<Material, Integer> blockPayouts = new HashMap<Material, Integer>();
	public HashMap<EntityType, Integer> mobPayouts = new HashMap<EntityType, Integer>();
	public HashMap<EntityType, AwardType> mobAwardsPrimary = new HashMap<EntityType, AwardType>();
	public HashMap<EntityType, AwardType> mobAwardsSecondary = new HashMap<EntityType, AwardType>();
	public HashMap<Material, AwardType> materialAwardsPrimary = new HashMap<Material, AwardType>();
	public HashMap<Material, AwardType> materialAwardsSecondary = new HashMap<Material, AwardType>();
	public final static ArrayList<Item> items = new ArrayList<Item>();

	public SilverManager(Registration instance) {
		plugin = instance;
		setupPayouts();
		setupItems();
	}

	private void setupPayouts() {
		blockPayouts.put(Material.DIAMOND_ORE, 350);
		blockPayouts.put(Material.IRON_ORE, 30);
		blockPayouts.put(Material.GOLD_ORE, 45);
		blockPayouts.put(Material.REDSTONE_ORE, 30);
		blockPayouts.put(Material.GLOWING_REDSTONE_ORE, 30);
		blockPayouts.put(Material.COAL_ORE, 5);
		blockPayouts.put(Material.LAPIS_ORE, 75);
		blockPayouts.put(Material.EMERALD_ORE, 750);

		mobPayouts.put(EntityType.ZOMBIE, 10);
		mobPayouts.put(EntityType.SPIDER, 10);
		mobPayouts.put(EntityType.SLIME, 10);
		mobPayouts.put(EntityType.SKELETON, 15);
		mobPayouts.put(EntityType.ENDERMAN, 25);
		mobPayouts.put(EntityType.BLAZE, 35);
		mobPayouts.put(EntityType.CREEPER, 25);
		mobPayouts.put(EntityType.PIG_ZOMBIE, 20);
		mobPayouts.put(EntityType.GHAST, 25);
		mobPayouts.put(EntityType.MAGMA_CUBE, 5);
		mobPayouts.put(EntityType.ENDER_DRAGON, 20000);
        mobPayouts.put(EntityType.GIANT, 10000);

		materialAwardsPrimary.put(Material.DIAMOND_ORE, AwardType.DIAMONDS);
		materialAwardsPrimary.put(Material.LAPIS_ORE, AwardType.LAPIS_DIGGER);
		materialAwardsPrimary.put(Material.REDSTONE_ORE, AwardType.REDSTONE);
		materialAwardsSecondary.put(Material.GLOWING_REDSTONE_ORE,
				AwardType.REDSTONE);

		mobAwardsPrimary.put(EntityType.CREEPER, AwardType.CREEPER_MILESTONE_1);
		mobAwardsPrimary.put(EntityType.SKELETON, AwardType.ZOMBIE_MILESTONE_1);
		mobAwardsPrimary.put(EntityType.ZOMBIE, AwardType.ZOMBIE_MILESTONE_1);
		mobAwardsPrimary.put(EntityType.BLAZE, AwardType.FIREFIGHTER);
		mobAwardsPrimary.put(EntityType.GHAST, AwardType.GHOSTBUSTER);
		mobAwardsPrimary.put(EntityType.SLIME, AwardType.JELLO_LOVER);
		mobAwardsPrimary.put(EntityType.SPIDER, AwardType.ARACHNOPHOBIA);
		mobAwardsPrimary.put(EntityType.COW, AwardType.COW_MILESTONE_1);
		mobAwardsPrimary.put(EntityType.ENDERMAN, AwardType.VOID_TOUCHED);

		mobAwardsSecondary.put(EntityType.CREEPER, AwardType.COUNTER_TERRORIST);
		mobAwardsSecondary.put(EntityType.SKELETON, AwardType.SQUIRE);
		mobAwardsSecondary.put(EntityType.ZOMBIE, AwardType.SQUIRE);
		mobAwardsSecondary.put(EntityType.ENDERMAN, AwardType.SHADOW_CHILD);
	}

	private void setupItems() {
		items.add(new NameChangeItem(ItemType.NAME_CHANGE, "Name Change", ShopType.MISC, false, "Changes your name to a new one.", 200000));
		items.add(new WeatherChangeItem(ItemType.WEATHER_CHANGE, "Weather Change", ShopType.MISC, "Changes the current world's weather.", 1000));
		items.add(new MOTDChangeItem(ItemType.MOTD_CHANGE, "MOTD Change", ShopType.MISC, false, "Changes the text displayed on the server list for the server.", 10000));
		items.add(new PureMultiInvitationItem(ItemType.PURE_FOUR_PACK, "Pure Invitation (4 Pack)", ShopType.NOT_FOR_SALE, "Contains 4 invitations to BlockShock Pure.", 20000));
		items.add(new PureInvitationItem(ItemType.PURE_INVITATION, "Pure Invitation", ShopType.NOT_FOR_SALE, "Contains an invitation to BlockShock Pure", 5000));
		items.add(new MinecraftItem(ItemType.DIAMOND_RAW, "Diamond", ShopType.ITEMS, "A diamond, in ore form.", 750, new ItemStack(Material.DIAMOND)));
		items.add(new MinecraftItem(ItemType.IRON_RAW, "Iron Ingot", ShopType.ITEMS, "An iron ingot.", 100, new ItemStack(Material.IRON_INGOT)));
		items.add(new MinecraftItem(ItemType.ENDER_PEARL, "An Ender Pearl", ShopType.ITEMS, "An odd looking thing that does odd things.", 250, new ItemStack(Material.ENDER_PEARL)));
		items.add(new MinecraftItem(ItemType.ENDER_EYE, "An Eye of Ender", ShopType.ITEMS, "An eye of ender.", 250, new ItemStack(Material.EYE_OF_ENDER)));
		items.add(new MinecraftItem(ItemType.RECORD11, "Record #11", ShopType.ITEMS, "This record can't be grabbed except in creative.", 1000, new ItemStack(Material.RECORD_11)));
		items.add(new MinecraftItem(ItemType.CHAIN_HELM, "Chainmail Helmet", ShopType.ITEMS, "A chainmail helmet.", 500, new ItemStack(Material.CHAINMAIL_HELMET)));
		items.add(new MinecraftItem(ItemType.CHAIN_SHIRT, "Chainmail Shirt", ShopType.ITEMS, "A chainmail shirt.", 500, new ItemStack(Material.CHAINMAIL_CHESTPLATE)));
		items.add(new MinecraftItem(ItemType.CHAIN_PANTS, "Chanmail Pants", ShopType.ITEMS, "Some pants made of chains.", 500, new ItemStack(Material.CHAINMAIL_LEGGINGS)));
		items.add(new MinecraftItem(ItemType.CHAIN_BOOTS, "Chainmail Boots", ShopType.ITEMS, "Some chain boots.", 500, new ItemStack(Material.CHAINMAIL_BOOTS)));
		items.add(new MinecraftItem(ItemType.SPONGE, "Sponge", ShopType.ADMIN_SHOP, "A sponge.", 1000, new ItemStack(Material.SPONGE)));
		items.add(new MinecraftItem(ItemType.OBSIDIAN, "Obsidian", ShopType.BLOCKS, "Some obsidian.", 50, new ItemStack(Material.OBSIDIAN)));
		items.add(new MinecraftItem(ItemType.XP, "XP Potion", ShopType.POTIONS, "A potion that grants experience.", 50, new ItemStack(Material.EXP_BOTTLE)));
		items.add(new MinecraftItem(ItemType.INSTANT_HEALTH_2, "Potion of Healing 2", ShopType.POTIONS, "A splash potion of instant healing 2.", 50, new ItemStack(Material.POTION, 1, (short) 16421))); // 16421
		items.add(new MinecraftItem(ItemType.REGENERATION_2, "Potion of Regeneration 2", ShopType.POTIONS, "A potion of regeneration 2.", 150, new ItemStack(Material.POTION, 1, (short) 8289)));
		items.add(new MinecraftItem(ItemType.SWIFTNESS_2, "Potion of Swiftness 2", ShopType.POTIONS, "A potion of swiftness 2.", 150, new ItemStack(Material.POTION, 1, (short) 8290)));
		items.add(new MinecraftItem(ItemType.STRENGTH_2, "Potion of Strength 2", ShopType.POTIONS, "A potion of strength 2.", 150, new ItemStack(Material.POTION, 1, (short) 8297)));
		items.add(new MinecraftItem(ItemType.POISON_2, "Potion of Poison 2", ShopType.POTIONS, "A potion of poison 2.", 150, new ItemStack(Material.POTION, 1, (short) 8292)));
		items.add(new MinecraftItem(ItemType.HARMING_2, "Potion of Harming 2", ShopType.POTIONS, "A potion of harming 2.", 350, new ItemStack(Material.POTION, 1, (short) 16428)));
		items.add(new MinecraftKitItem(ItemType.CHAINMAIL_ARMOR, "Chainmail Armor Set", ShopType.ARMOR, "A suit of chainmail armor.", 1750, new ItemStack(Material.CHAINMAIL_HELMET), new ItemStack(Material.CHAINMAIL_CHESTPLATE), new ItemStack(Material.CHAINMAIL_LEGGINGS), new ItemStack(Material.CHAINMAIL_BOOTS)));
		items.add(new MinecraftKitItem(ItemType.GOLD_ARMOR, "Gold Armor Set", ShopType.ARMOR, "Looks like butter.", 1600, new ItemStack(Material.GOLD_HELMET), new ItemStack(Material.GOLD_CHESTPLATE), new ItemStack(Material.GOLD_LEGGINGS), new ItemStack(Material.GOLD_BOOTS)));
		items.add(new MinecraftKitItem(ItemType.DIAMOND_ARMOR, "Diamond Armor Set", ShopType.ARMOR, "A suit of diamond armor.", 15000, new ItemStack(Material.DIAMOND_HELMET), new ItemStack(Material.DIAMOND_CHESTPLATE), new ItemStack(Material.DIAMOND_LEGGINGS), new ItemStack(Material.DIAMOND_BOOTS)));
		items.add(new MinecraftKitItem(ItemType.IRON_ARMOR, "Iron Armor Set", ShopType.ARMOR, "A suit of iron armor.", 2400, new ItemStack(Material.IRON_HELMET), new ItemStack(Material.IRON_CHESTPLATE), new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.IRON_BOOTS)));
		items.add(new MinecraftKitItem(ItemType.LEATHER_ARMOR, "Leather Armor Set", ShopType.ARMOR, "A bit of leather armor. Probably won't do much for protection.", 1000, new ItemStack(Material.LEATHER_HELMET), new ItemStack(Material.LEATHER_CHESTPLATE), new ItemStack(Material.LEATHER_LEGGINGS), new ItemStack(Material.LEATHER_BOOTS)));
		items.add(new ChatTitleItem(ItemType.TITLE_NOOB, "Title: Noob", ShopType.NOT_FOR_SALE, "Describes what you are for buying this!", 0, ChatColor.LIGHT_PURPLE + "NOOB"));
		items.add(new ChatTitleItem(ItemType.TITLE_KOTB, "Title: KoTB", ShopType.NOT_FOR_SALE, "Are you a Keeper of The Bucket?", 1600000, ChatColor.DARK_RED + "KoTB"));
		items.add(new ChatTitleItem(ItemType.TITLE_RACISTGHOST, "Title: Racist Ghost", ShopType.NOT_FOR_SALE, "Do you have a Racist Ghost behind you?", 2000000, ChatColor.DARK_GRAY + "RACIST" + ChatColor.WHITE + " GHOST"));
		items.add(new ChatTitleItem(ItemType.TITLE_MACINTOSH, "Title: iMac", ShopType.NOT_FOR_SALE, "I'm a Mac.", 25000, ChatColor.GRAY + "iMac"));
		items.add(new ChatTitleItem(ItemType.TITLE_PC, "Title: PC", ShopType.NOT_FOR_SALE, "...and I'm a PC", 25000, ChatColor.DARK_GRAY + "PC"));
		items.add(new ChatTitleItem(ItemType.TITLE_PC_SUPREME, "Title: PC Master Race", ShopType.NOT_FOR_SALE, "...and I'm a PC", 3500000, ChatColor.DARK_GRAY + "PC" + ChatColor.DARK_PURPLE + " MASTER " + ChatColor.DARK_GRAY + "RACE"));
		items.add(new ChatTitleItem(ItemType.TITLE_B7, "Title: B7", ShopType.NOT_FOR_SALE, "From another era.", 3500000, ChatColor.RED + "B7"));
		items.add(new ChatTitleItem(ItemType.TITLE_APERTURE, "Title: Aperture Staff", ShopType.NOT_FOR_SALE, "We do what we must, because, we can.", 25000, ChatColor.DARK_AQUA + "Aperture" + ChatColor.AQUA + " Staff"));
		items.add(new ChatTitleItem(ItemType.TITLE_BLACKMESA, "Title: Black Mesa", ShopType.NOT_FOR_SALE, "Working to make a better tomorrow for all mankind.", 25000, ChatColor.DARK_GRAY + "Black" + ChatColor.GRAY + " Mesa"));
		items.add(new ChatTitleItem(ItemType.TITLE_BRONY, "Title: BRONY", ShopType.NOT_FOR_SALE, "I'm a Brony. What are you going to do about it?", 5, plugin.rainbow("BRONY")));
		items.add(new ChatTitleItem(ItemType.TITLE_MINER, "Title: Miner", ShopType.NOT_FOR_SALE, "I mine!", 25000, ChatColor.GRAY + "Miner"));
		items.add(new ChatTitleItem(ItemType.TITLE_EXPLORER, "Title: Explorer", ShopType.NOT_FOR_SALE, "I adventure!", 25000, ChatColor.DARK_AQUA + "Explorer"));
		items.add(new ChatTitleItem(ItemType.TITLE_FARMER, "Title: Farmer", ShopType.NOT_FOR_SALE, "I farm!", 25000, ChatColor.DARK_GREEN + "Farmer"));
		items.add(new ChatTitleItem(ItemType.TITLE_PEETA, "Title: Peeta", ShopType.BLACKMARKET, "Express your love for the world's most famous lover boy.", 2100000, ChatColor.GOLD + "Peeta"));
		items.add(new ChatTitleItem(ItemType.TITLE_DISTRICT13, "Title: District 13", ShopType.NOT_FOR_SALE, "We have no fun here.", 25000, ChatColor.DARK_GRAY + "District 13"));
		items.add(new ChatTitleItem(ItemType.TITLE_SLAYER, "Title: Slayer", ShopType.TITLES, "I slay everything!", 100000, ChatColor.RED + "Slayer"));
		items.add(new ChatTitleItem(ItemType.TITLE_BUILDER, "Title: Builder", ShopType.NOT_FOR_SALE, "I build!", 25000, ChatColor.AQUA + "Builder"));
		items.add(new ChatTitleItem(ItemType.TITLE_WIZARD, "Title: Wizard", ShopType.NOT_FOR_SALE, "You're a wizard, Harry.", 25000, ChatColor.GRAY + "Wizard"));
		items.add(new ChatTitleItem(ItemType.TITLE_NINJA, "Title: Ninja", ShopType.NOT_FOR_SALE, "I'm a Ninja, I run the system.", 25000, ChatColor.DARK_GRAY + "Ninja"));
		items.add(new ChatTitleItem(ItemType.TITLE_PIRATE, "Title: Pirate", ShopType.NOT_FOR_SALE, "Call me Captain Jack Sparrow!", 25000, ChatColor.RED + "Pirate"));
		items.add(new ChatTitleItem(ItemType.TITLE_JEDI, "Title: Jedi", ShopType.NOT_FOR_SALE, "Luke I just DNA tested, and I'm totally your father.", 25000, ChatColor.BLUE + "Jedi"));
		items.add(new ChatTitleItem(ItemType.TITLE_NAVI, "Title: Navi", ShopType.BLACKMARKET, "Like Navi, from The Legend of Zelda.", 5100000, ChatColor.AQUA + "Navi"));
		items.add(new ChatTitleItem(ItemType.TITLE_WARLOCK, "Title: Warlock", ShopType.TITLES, "Shut up about Gandalf already!", 250000, ChatColor.AQUA + "War" + ChatColor.BLUE + "lock"));
		items.add(new ChatTitleItem(ItemType.TITLE_ALCHEMIST, "Title: Alchemist", ShopType.NOT_FOR_SALE, "I'm just a badass.", 25000, ChatColor.GOLD + "Alchemist"));
		items.add(new ChatTitleItem(ItemType.TITLE_PSYCHONAUT, "Title: Psychonaut", ShopType.TITLES, "I'm Raz, and apparently everyone except Lili decided to go literally go brainless.", 250000, ChatColor.DARK_PURPLE + "Psychonaut"));
		items.add(new ChatColorItem(ItemType.CHAT_COLOR_AQUA, "Chat Color: Aqua", ShopType.COLORS, "An aqua chat color.", 3000000, ChatColor.COLOR_CHAR + "b"));
		items.add(new ChatColorItem(ItemType.CHAT_COLOR_GOLD, "Chat Color: Gold", ShopType.COLORS, "A gold chat color.", 3000000, ChatColor.COLOR_CHAR + "6"));
		items.add(new ChatColorItem(ItemType.CHAT_COLOR_GRAY, "Chat Color: Gray", ShopType.COLORS, "A gray chat color.", 3000000, ChatColor.COLOR_CHAR + "7"));
		items.add(new ChatColorItem(ItemType.CHAT_COLOR_DARKGRAY, "Chat Color: Dark Gray", ShopType.COLORS, "A dark gray chat color.", 3000000, ChatColor.COLOR_CHAR + "8"));
		items.add(new ChatColorItem(ItemType.CHAT_COLOR_BLUE, "Chat Color: Blue", ShopType.COLORS, "A blue chat color.", 3000000, ChatColor.COLOR_CHAR + "9"));
		items.add(new ChatColorItem(ItemType.CHAT_COLOR_BRIGHTGREEN, "Chat Color: Bright Green", ShopType.COLORS, "A bright green chat color.", 3000000, ChatColor.COLOR_CHAR + "a"));
		items.add(new ChatColorItem(ItemType.CHAT_COLOR_PINK, "Chat Color: Pink", ShopType.COLORS, "A pink chat color.", 3000000, ChatColor.COLOR_CHAR + "d"));
		items.add(new ChatColorItem(ItemType.CHAT_COLOR_PURPLE, "Chat Color: Purple", ShopType.COLORS, "A purple chat color.", 3000000, ChatColor.COLOR_CHAR + "5"));
		items.add(new ChatTitleItem(ItemType.TITLE_SPACE_POPTART, "Title: SpacePoptart", ShopType.NOT_FOR_SALE, "Are you a space poptart?", 1333337, ChatColor.AQUA + "Space " + ChatColor.BLUE + "Poptart"));
		items.add(new ChatColorItem(ItemType.COLOR_RESET, "Chat Color Reset", ShopType.MISC, "An item that reverts your chat color to white.", 100, ""));
		items.add(new ChatTitleItem(ItemType.TITLE_RESET, "Chat Title Reset", ShopType.MISC, "An item that removes your title.", 100, ""));
		items.add(new AssassinationItem(ItemType.ASSASSINATION, "Hired kill", ShopType.BLACKMARKET, "Instantly kills a player.", 200000));
		items.add(new MinecraftItem(ItemType.ICE, "Ice Block", ShopType.BLOCKS, "A block of frozen water!", 250, new ItemStack(Material.ICE)));
		items.add(new MinecraftItem(ItemType.COBWEB, "Cobweb", ShopType.BLOCKS, "A block of cobwebs!", 250, new ItemStack(Material.WEB)));
		items.add(new MinecraftItem(ItemType.GOLDEN_APPLE, "Golden Apple", ShopType.ITEMS, "An apple covered in gold!", 100, new ItemStack(Material.GOLDEN_APPLE)));
		items.add(new ChatTitleItem(ItemType.TITLE_MM5, "Title: MM5", ShopType.NOT_FOR_SALE, "A Mario Marathon 5 themed chat title!", 30000, ChatColor.DARK_AQUA + "MM" + ChatColor.AQUA + "5"));
		items.add(new ChatTitleItem(ItemType.TITLE_DOUCHEBAG_WIZARD, "Title: Douchebag Wizard", ShopType.TITLES, "I'm a douchebag Wizard!", 15000, ChatColor.RED + "DOUCHEBAG " + ChatColor.AQUA + "WIZARD"));
		items.add(new ChatTitleItem(ItemType.TITLE_ARCHITECT, "Title: Architect", ShopType.NOT_FOR_SALE, "Leo's seal of awesome.", 10000000, ChatColor.GOLD + "Architect"));
		items.add(new MinecraftItem(ItemType.CHISELED_STONE_BRICK, "Chiseled Stone Brick", ShopType.BLOCKS, "It's been requested for far too long.", 50, new ItemStack(Material.SMOOTH_BRICK, 1, (short) 3)));
		items.add(new ChatTitleItem(ItemType.TITLE_BOMB_SQUAD_2, "Title: Bomb Squad", ShopType.NOT_FOR_SALE, "Awarded to the killers of many creepers.", 0, ChatColor.DARK_GRAY + "BOMB " + ChatColor.RED + "SQUAD"));
		items.add(new ChatTitleItem(ItemType.TITLE_MOO, "Title: Moo!", ShopType.NOT_FOR_SALE, "I like cows.", 0, ChatColor.DARK_GRAY + "M" + ChatColor.WHITE + "OO"));
		items.add(new ChatTitleItem(ItemType.TITLE_PALADIN, "Title: Paladin", ShopType.NOT_FOR_SALE, "I free the world of evil.", 0, ChatColor.GOLD + "P" + ChatColor.WHITE + "aladin"));
		items.add(new ChatTitleItem(ItemType.TITLE_FIREFIGHTER, "Title: Firefighter", ShopType.NOT_FOR_SALE, "I fight fires!", 0, ChatColor.GOLD + "Fire" + ChatColor.RED + "Fighter"));
		items.add(new ChatTitleItem(ItemType.TITLE_ADDICT, "Title: Addict", ShopType.NOT_FOR_SALE, "I'm addicted.", 0, ChatColor.GREEN + "Addict"));
		items.add(new ChatTitleItem(ItemType.TITLE_SUPER_ADDICT, "Title: Super Addict", ShopType.NOT_FOR_SALE, "I'm addicted.", 0, ChatColor.GREEN + "Super" + ChatColor.GOLD + " Addict"));
		items.add(new ChatTitleItem(ItemType.TITLE_NECROMANCER, "Title: Necromancer", ShopType.NOT_FOR_SALE, "I'm a Necromancer.", 0, ChatColor.DARK_GRAY + "Necromancer"));
		items.add(new ChatTitleItem(ItemType.TITLE_GHOSTBUSTER, "Title: Ghostbuster", ShopType.NOT_FOR_SALE, "I bust the Ghasts...er Ghosts...", 0, ChatColor.DARK_GRAY + "Ghost" + ChatColor.WHITE + "buster"));
		items.add(new ChatTitleItem(ItemType.TITLE_JELLO_LOVER, "Title: Jello Lover", ShopType.NOT_FOR_SALE, "I love killing Slimes!", 0, ChatColor.GREEN + "Jello" + ChatColor.RED + " Lover"));
		items.add(new ChatTitleItem(ItemType.TITLE_ROCKEFELLER, "Title: Rockefeller", ShopType.NOT_FOR_SALE, "John D. Rockefeller: I don't even need a beat. I could kick it a cappella. Doo-wop, shoo-wop. I'm John D. Rockefeller.", 0, ChatColor.GREEN + "$$$$$"));
		items.add(new ChatTitleItem(ItemType.TITLE_SPIDER_SLAYER, "Title: Spider Slayer", ShopType.NOT_FOR_SALE, "I hate those things.", 0, ChatColor.AQUA + "Spider " + ChatColor.DARK_GRAY + "Slayer"));
		items.add(new ChatTitleItem(ItemType.TITLE_SHADOW_CHILD, "Title: Shadow Child", ShopType.NOT_FOR_SALE, "I live in the shadows.", 0, ChatColor.DARK_GRAY + "Shadow Child"));
		items.add(new ChatTitleItem(ItemType.TITLE_VOID_KING, "Title: Void King", ShopType.NOT_FOR_SALE, "You have little that compares to my power.", 0, ChatColor.DARK_PURPLE + "V" + ChatColor.DARK_GRAY + "oid " + ChatColor.DARK_PURPLE + "K" + ChatColor.DARK_GRAY + "ing"));
		items.add(new ChatTitleItem(ItemType.TITLE_VOID_TOUCHED, "Title: Void Touched", ShopType.NOT_FOR_SALE, "The end is no match for me.", 0, ChatColor.DARK_PURPLE + "Void Touched"));
		items.add(new ChatTitleItem(ItemType.TITLE_COUNTER_TERRORIST, "Title: Counter Terrorist", ShopType.NOT_FOR_SALE, "Counter-Terrorists win.", 0, ChatColor.DARK_AQUA + "Counter " + ChatColor.DARK_GRAY + "Terrorist"));
		items.add(new ChatTitleItem(ItemType.TITLE_SQUIRE, "Title: Squire", ShopType.NOT_FOR_SALE, "Awarded to the slayer of the most evils.", 0, ChatColor.DARK_AQUA + "Squire"));
		items.add(new ChatTitleItem(ItemType.TITLE_DARK_ONE, "Title: Dark One", ShopType.NOT_FOR_SALE, "Awarded to those who kill the finest of beasts.", 0, ChatColor.DARK_PURPLE + "D" + ChatColor.DARK_GRAY + "ark " + ChatColor.DARK_PURPLE + "O" + ChatColor.DARK_GRAY + "ne"));
		items.add(new ChatTitleItem(ItemType.TITLE_MAGIC, "Title: Magic", ShopType.ADMIN_SHOP, "It's magic!", 2000000, ChatColor.AQUA + " " + ChatColor.MAGIC + "Magical" + " "));
		items.add(new MinecraftItem(ItemType.HALF_DOOR, "Half door", ShopType.ADMIN_SHOP, "It's a half door!", 150, new ItemStack(Material.WOODEN_DOOR)));
		items.add(new MinecraftItem(ItemType.BURNING_FURNACE, "Burning furnace", ShopType.ADMIN_SHOP, "It's a burning furnace.", 250, new ItemStack(Material.BURNING_FURNACE)));
		items.add(new MinecraftItem(ItemType.SNOW_COVER, "Snow cover", ShopType.ADMIN_SHOP, "Snow cover!", 50, new ItemStack(Material.SNOW)));
		items.add(new MinecraftItem(ItemType.MELON_SEEDS, "Melon seeds", ShopType.ADMIN_SHOP, "Melon seeds!", 75, new ItemStack(Material.MELON_SEEDS)));
		items.add(new MinecraftItem(ItemType.PUMPKIN_SEEDS, "Pumpkin seeds", ShopType.ADMIN_SHOP, "Pumpkin seeds!", 75, new ItemStack(Material.PUMPKIN_SEEDS)));
		items.add(new MinecraftItem(ItemType.SILVER_FISH, "Silver fish blocks", ShopType.ADMIN_SHOP, "Silver fish block.", 750, new ItemStack(Material.MONSTER_EGG)));
		items.add(new ChatTitleItem(ItemType.TITLE_LYOKO_WARRIOR, "Title: Lyoko Warrior", ShopType.NOT_FOR_SALE, "Transfer, scanner, virtualization.", 25000, ChatColor.AQUA + "Lyoko " + ChatColor.DARK_AQUA + "Warrior"));
		items.add(new ChatTitleItem(ItemType.TITLE_HACKER, "Title: Hacker", ShopType.NOT_FOR_SALE, "(Not the 12 year old type.)", 0, ChatColor.GOLD + "Ha" + ChatColor.RED + "ck" + ChatColor.DARK_GRAY + "er"));
		items.add(new ChatTitleItem(ItemType.TITLE_STAFF, "(Admin Title)", ShopType.NOT_FOR_SALE, "Given to administrators.", 0, ChatColor.AQUA + "SS" + ChatColor.AQUA + "A"));
		items.add(new ChatTitleItem(ItemType.TITLE_LOVER_BOY, "Title: Lover Boy", ShopType.ADMIN_SHOP, "You're like Peeta (just not insane).", 500000, ChatColor.LIGHT_PURPLE + "Lover" + ChatColor.RED + " Boy"));
		items.add(new ChatTitleItem(ItemType.TITLE_LEAH, "Title: RIP Leah", ShopType.NOT_FOR_SALE, "Blizzard has DLC planned for her, don't worry.", 25000, ChatColor.DARK_GRAY + "RIP " + ChatColor.GOLD + "Leah"));
		items.add(new ChatTitleItem(ItemType.TITLE_MASTER_OF_HELL, "Title: Master of Hell", ShopType.ADMIN_SHOP, "You've beaten Diablo III on Hell difficulty?", 250000, ChatColor.GOLD + "Master " + ChatColor.DARK_GRAY + "of" + ChatColor.RED + " Hell"));
		items.add(new ChatTitleItem(ItemType.TITLE_ONE_FREE_MAN, "Title: One Free Man", ShopType.ADMIN_SHOP, "Thanks for creating the 7 hour war and leaving right before it even happened.", 250000, ChatColor.GOLD + "One Free Man"));
		items.add(new ChatTitleItem(ItemType.TITLE_THE_GIRL_WHO_WAS_ON_FIRE, "Title: The Girl On Fire", ShopType.ADMIN_SHOP, "You never forget the face of the person who was your last hope.", 5000000, ChatColor.GOLD + "The " + ChatColor.RED + "Girl" + ChatColor.GOLD + " On " + ChatColor.RED + "Fire"));
		items.add(new ChatTitleItem(ItemType.TITLE_THE_GIVER, "Title: The Giver", ShopType.TITLES, "The Reciever of Memory", 1250000, ChatColor.DARK_GRAY + "The " + ChatColor.GOLD + "Giver"));
		items.add(new ChatTitleItem(ItemType.TITLE_ANDREW_RYAN, "Title: Andrew Ryan", ShopType.NOT_FOR_SALE, "Does a man not deserve the sweat off his brow?", 25000, ChatColor.AQUA + "Andrew " + ChatColor.RED + "Ryan"));
		items.add(new ChatTitleItem(ItemType.TITLE_ALTAIR, "Title: Altair", ShopType.ADMIN_SHOP, "#1 Assassain", 1250000, ChatColor.DARK_RED + "Altair"));
		items.add(new ChatTitleItem(ItemType.TITLE_THE_CHOSEN_ONE, "Title: The Chosen One", ShopType.TITLES, "All you have to do is kill Lord Voldemort before he really screws up everything.", 250000, ChatColor.GOLD + "The Chosen One"));
		items.add(new ChatTitleItem(ItemType.TITLE_BLACK_MESA_EAST, "Title: Black Mesa East", ShopType.TITLES, "At least it doesn't explode.", 250000, ChatColor.DARK_GRAY + "Black " + ChatColor.BLACK + "Mesa" + ChatColor.DARK_GRAY + " East"));
		items.add(new ChatTitleItem(ItemType.TITLE_SPECTRE, "Title: Spectre", ShopType.TITLES, "Your Spectre status has been approved.", 125000, ChatColor.GOLD + "Spectre"));
		items.add(new ChatTitleItem(ItemType.TITLE_DEADMAN, "Title: Deadman", ShopType.NOT_FOR_SALE, "The main attraction of this twisted master plan.", 25000, ChatColor.RED + "Deadman"));
		items.add(new ChatTitleItem(ItemType.TITLE_WRETCHED_EGG, "Title: The Wretched Egg", ShopType.BLACKMARKET, "She isn't always as nice as you think.", 100000, ChatColor.GOLD + "The " + ChatColor.RED + "Wretched " + ChatColor.DARK_GRAY + "Egg"));
		items.add(new ChatTitleItem(ItemType.TITLE_WARD_G, "Title: Ward G", ShopType.BLACKMARKET, "DOES NOT EXIST.", 100000, ChatColor.GREEN + "Ward G"));
		items.add(new ChatTitleItem(ItemType.TITLE_SHINY_SHINY, "Title: SHINY SHINY", ShopType.NOT_FOR_SALE, "Ibitsu na machi no kono shikakui sora kara", 0, ChatColor.AQUA + "SHINY" + ChatColor.RED + "SHINY"));
		items.add(new ChatTitleItem(ItemType.TITLE_MF_ALPHA, "Title: MineForts Alpha", ShopType.NOT_FOR_SALE, "Awarded to testers of MineForts Alpha.", 0, ChatColor.GREEN + "Mine" + ChatColor.GRAY + "Forts" + ChatColor.AQUA + " Alpha"));
		items.add(new ChatTitleItem(ItemType.TITLE_LUNA, "Title: Luna", ShopType.BLACKMARKET, "Awarded to people who nag the most for new titles.", 750000, ChatColor.BLACK + "L" + ChatColor.DARK_BLUE + "una"));
		items.add(new ChatTitleItem(ItemType.TITLE_SV_FOUNDING_MEMBER, "Title: SV Trailblazer", ShopType.NOT_FOR_SALE, "Awarded to the pioneers of Shadowvale.", 0, ChatColor.GOLD + "S" + ChatColor.GRAY + "V" + ChatColor.GOLD + " Trailblazer"));
		items.add(new ChatTitleItem(ItemType.TITLE_SV_LEGIONARY, "Title: SV Legionary", ShopType.NOT_FOR_SALE, "Awarded to the pioneers of Shadowvale.", 0, ChatColor.GOLD + "S" + ChatColor.GRAY + "V" + ChatColor.GOLD + " Legionary"));
		items.add(new ChatTitleItem(ItemType.TITLE_SV_MAINTAINER, "Title: SV Maintainer", ShopType.NOT_FOR_SALE, "Awarded to those who help Shadowvale stay in shape.", 0, ChatColor.GOLD + "S" + ChatColor.GRAY + "V" + ChatColor.GOLD + " Maintainer"));
		items.add(new ChatTitleItem(ItemType.TITLE_SV_BLACK_RED, "Title: SV", ShopType.NOT_FOR_SALE, "Awarded to players who love ShadowVale.", 0, ChatColor.GOLD + "S" + ChatColor.GRAY + "V"));
		items.add(new ChatTitleItem(ItemType.TITLE_SV_MINING_CORP, "Title: SV Mining Corp", ShopType.NOT_FOR_SALE, "Awarded to players who love ShadowVale.", 0, ChatColor.GOLD + "S" + ChatColor.GRAY + "V " + ChatColor.AQUA + "Mining Corp"));
		items.add(new ChatTitleItem(ItemType.TITLE_TIME_LORD, "Title: Time Lord", ShopType.TITLES, "Are you who?", 50000, ChatColor.BLUE + "Time Lord"));
		items.add(new ChatTitleItem(ItemType.TITLE_HALLOWEEN_2012, "Title: Haunted Spirit", ShopType.NOT_FOR_SALE, "Part of the Halloween 2012 mini-event.", 150000, ChatColor.RED + "Haunted" + ChatColor.GRAY + " Spirit"));
		items.add(new ChatTitleItem(ItemType.TITLE_500_MILES, "Title: 500 Miles", ShopType.NOT_FOR_SALE, "Awarded to those who swam across oceans to get to ShadowVale.", 0, ChatColor.WHITE + "500 Miles"));
		items.add(new GenericItem(ItemType.CHAT_NO_CENSOR, "Censor be gone", ShopType.MISC, false, "Disables the chat filter & censor.", 120));
		items.add(new NameResetItem(ItemType.NAME_RESET, "Name Reset", ShopType.MISC, "Resets your nickname to your original username.", 1000));
		items.add(new ChatTitleItem(ItemType.TITLE_COSMIC_OWL, "Title: Cosmic Owl", ShopType.TITLES, "Requested by Pentatonic.", 50000, ChatColor.GRAY + "Cosmic " + ChatColor.GOLD + "Owl"));
		items.add(new ChatTitleItem(ItemType.TITLE_SS_XMAS_2012, "Title: SS X-Mas", ShopType.NOT_FOR_SALE, "Merry Christmas, from ShankShock!", 0, ChatColor.GOLD + "SS " + ChatColor.RED + "X" + ChatColor.GOLD + "-" + ChatColor.GREEN + "Mas"));
		items.add(new ChatTitleItem(ItemType.TITLE_BAN_MASTER_GENERAL, "Title: Ban Master General", ShopType.NOT_FOR_SALE, "Awarded to the king of bans.", 0, ChatColor.DARK_RED + "Ban Master" + " " + ChatColor.GOLD + "General"));
		items.add(new MinecraftItem(ItemType.MC_NETHER_STAR, "Nether Star", ShopType.NOT_FOR_SALE, "A nether star.", 0, new ItemStack(Material.NETHER_STAR)));
		items.add(new MinecraftItem(ItemType.MC_EMERALD, "Emerald", ShopType.NOT_FOR_SALE, "An emerald.", 0, new ItemStack(Material.EMERALD)));
		items.add(new MinecraftItem(ItemType.MC_COAL, "Dirty Coal", ShopType.NOT_FOR_SALE, "Some dirty coal. Likely stolen from someone.", 0, new ItemStack(Material.COAL)));
		items.add(new MinecraftItem(ItemType.MC_CAKE, "Delicious Cake", ShopType.NOT_FOR_SALE, "Some delicious cake! Yumm!", 0, new ItemStack(Material.CAKE)));
		items.add(new MinecraftItem(ItemType.MC_MILK, "Decadent Milk", ShopType.NOT_FOR_SALE, "Some freshly spiked milk.", 0, new ItemStack(Material.MILK_BUCKET)));
		items.add(new MinecraftItem(ItemType.MC_COOKIE, "Warm Cookie", ShopType.NOT_FOR_SALE, "Chocolate chip, my favorite!", 0, new ItemStack(Material.COOKIE)));
		items.add(new MinecraftItem(ItemType.MC_DIAMOND, "Pretty Diamond", ShopType.NOT_FOR_SALE, "A pretty diamond!", 0, new ItemStack(Material.DIAMOND)));
		items.add(new ChatTitleItem(ItemType.TITLE_SUO, "Title: Suo Pavlichenko", ShopType.NOT_FOR_SALE, "For those of us who love Suo <3", 0, ChatColor.DARK_RED + "Suo" + ChatColor.RED + " Pavlichenko"));
		items.add(new ChatTitleItem(ItemType.TITLE_REAPER, "Title: Black Reaper", ShopType.NOT_FOR_SALE, "For those of us who love Hei <3", 0, ChatColor.BLACK + "Black Reaper"));
		items.add(new ChatTitleItem(ItemType.TITLE_HEI_SUO, "Title: Hei & Suo", ShopType.NOT_FOR_SALE, "Suo & Hei <3", 0, ChatColor.GRAY + "Hei " + ChatColor.BLACK + "& " + ChatColor.DARK_RED + "Suo"));
		items.add(new ChatTitleItem(ItemType.TITLE_KNIGHTS_OF_THE_BLOOD, "Title: Knights of the Blood", ShopType.TITLES, "A title.", 100000, ChatColor.RED + "K" + ChatColor.WHITE + "night" + ChatColor.RED + "s of t" + ChatColor.WHITE + "h" + ChatColor.RED + "e B" + ChatColor.WHITE + "loo" + ChatColor.RED + "d"));
		items.add(new ChatTitleItem(ItemType.TITLE_THE_DAMMNED, "Title: The Damned", ShopType.NOT_FOR_SALE, "Given to those who kill lots of wither skeletons.", 0, ChatColor.GRAY + "The " + ChatColor.RED + "Damned"));
		items.add(new ChatTitleItem(ItemType.TITLE_NINJITSU_MASTER, "Title: Ninjitsu Master", ShopType.NOT_FOR_SALE, "Given to those who kill with only their hands.", 0, ChatColor.DARK_GRAY + "Ninjitsu Master"));
		items.add(new ChatTitleItem(ItemType.TITLE_LYCAN_HUNTER, "Title: Lycan Hunter", ShopType.NOT_FOR_SALE, "Given for killing 250 hostile wolves.", 0, ChatColor.GRAY + "Lycan " + ChatColor.WHITE + "Hunter"));
		items.add(new ChatTitleItem(ItemType.TITLE_MARAUDER, "Title: Marauder", ShopType.NOT_FOR_SALE, "Given to those who kill 250 people.", 0, ChatColor.DARK_GRAY + "Marauder"));
		items.add(new ChatTitleItem(ItemType.TITLE_EXECUTIONER, "Title: Executioner", ShopType.NOT_FOR_SALE, "Given to those who kill 500 hostile mobs with iron axes.", 0, ChatColor.RED + "Executioner"));
		items.add(new ChatTitleItem(ItemType.TITLE_SWORDSMAN, "Title: Swordsman", ShopType.NOT_FOR_SALE, "Given to those who kill 500 hostile mobs with a sword.", 0, ChatColor.DARK_GRAY + "Swordsman"));
		items.add(new MinecraftItem(ItemType.CREEPER, "Spawn Egg: Creeper", ShopType.SPAWN_SHOP, "Allows you to spawn a creeper through a spawn egg.", 5000, new SpawnEgg(EntityType.CREEPER).toItemStack()));
		items.add(new MinecraftItem(ItemType.SKELETON, "Spawn Egg: Skeleton", ShopType.SPAWN_SHOP, "Allows you to spawn a skeleton through a spawn egg.", 10000, new SpawnEgg(EntityType.SKELETON).toItemStack()));
		items.add(new MinecraftItem(ItemType.BLAZE, "Spawn Egg: Blaze", ShopType.SPAWN_SHOP, "Allows you to spawn a blaze through a spawn egg.", 15000, new SpawnEgg(EntityType.BLAZE).toItemStack()));
		items.add(new MinecraftItem(ItemType.SPIDER, "Spawn Egg: Spider", ShopType.SPAWN_SHOP, "Allows you to spawn a spider through a spawn egg.", 10000, new SpawnEgg(EntityType.SPIDER).toItemStack()));
		items.add(new MinecraftItem(ItemType.CAVESPIDER, "Spawn Egg: Cave Spider", ShopType.SPAWN_SHOP, "Allows you to spawn a cave spider through a spawn egg.", 25000, new SpawnEgg(EntityType.CAVE_SPIDER).toItemStack()));
		items.add(new MinecraftItem(ItemType.WITCH, "Spawn Egg: Witch", ShopType.SPAWN_SHOP, "Allows you to spawn a witch through a spawn egg.", 15000, new SpawnEgg(EntityType.WITCH).toItemStack()));
		items.add(new MinecraftItem(ItemType.VILLAGER, "Spawn Egg: Villager", ShopType.SPAWN_SHOP, "Allows you to spawn a villager through a spawn egg.", 5000, new SpawnEgg(EntityType.VILLAGER).toItemStack()));
		items.add(new MinecraftItem(ItemType.BAT, "Spawn Egg: Bat", ShopType.SPAWN_SHOP, "Allows you to spawn a bat through a spawn egg.", 1000, new SpawnEgg(EntityType.BAT).toItemStack()));
		items.add(new MinecraftItem(ItemType.CHICKEN, "Spawn Egg: Chicken", ShopType.SPAWN_SHOP, "Which one came first?", 5000, new SpawnEgg(EntityType.CHICKEN).toItemStack()));
		items.add(new MinecraftItem(ItemType.COW, "Spawn Egg: Cow", ShopType.SPAWN_SHOP, "Allows you to spawn a cow through a spawn egg.", 5000, new SpawnEgg(EntityType.COW).toItemStack()));
		items.add(new MinecraftItem(ItemType.WOLF, "Spawn Egg: Wolf", ShopType.SPAWN_SHOP, "Allows you to spawn a wolf through a spawn egg.", 50000, new SpawnEgg(EntityType.WOLF).toItemStack()));
		items.add(new ChatTitleItem(ItemType.TITLE_SHISNO, "Title: Shisno", ShopType.TITLES, "A foul creature's excrement's excretement.", 100000, ChatColor.AQUA + "Shisno"));
		items.add(new ChatTitleItem(ItemType.TITLE_PAUL, "Title: Paul", ShopType.BLACKMARKET, "Paul.", 500000, ChatColor.GREEN + "Paul"));
		items.add(new ChatTitleItem(ItemType.TITLE_LEMON_PLEDGE, "Title: Lemon Pledge", ShopType.ADMIN_SHOP, "No...", 750000, ChatColor.YELLOW + "Lemon Pledge"));
		items.add(new ChatTitleItem(ItemType.TITLE_RETIRED, "Title: Retired Admin", ShopType.NOT_FOR_SALE, "Given to retired admins.", 0, ChatColor.DARK_PURPLE + "Retired" + ChatColor.LIGHT_PURPLE + " Admin"));
		items.add(new ChatTitleItem(ItemType.TITLE_STATE_ALCHEMIST, "Title: State Alchemist", ShopType.BLACKMARKET, "A dog of the military.", 1000000, ChatColor.AQUA + "State " + ChatColor.WHITE + "Alchemist"));
		items.add(new ChatTitleItem(ItemType.TITLE_FULLMETAL, "Title: Fullmetal Alchemist", ShopType.BLACKMARKET, "Human transmutation is strictly forbidden - for what could equal the value of a human soul.", 1000000, ChatColor.GRAY + "Fullmetal " + ChatColor.AQUA + "Alche" + ChatColor.WHITE + "mist"));
		items.add(new GenericItem(ItemType.CREATIVE_ACCESS, "Creative Admissions Ticket", ShopType.MISC, false, "A ticket to play on the creative map (using the /creative command).", 100000));
		items.add(new ChatTitleItem(ItemType.TITLE_ARTIST, "Title: Artist", ShopType.NOT_FOR_SALE, "Awarded to those who create the most.", 0, ChatColor.GOLD + "Artist"));
		items.add(new MinecraftItem(ItemType.NETHER_STAR, "Nether Star", ShopType.ITEMS, "A star from the nether.", 750000, new ItemStack(Material.NETHER_STAR)));
		items.add(new ChatTitleItem(ItemType.TITLE_GIRROCKSS, "Title: Mage of Doom", ShopType.NOT_FOR_SALE, "Personal title.", 0, ChatColor.GREEN + "Mage " + ChatColor.WHITE + "of " + ChatColor.DARK_GRAY + "Doom"));
		items.add(new ChatTitleItem(ItemType.TITLE_PURIST, "Title: Purity", ShopType.TITLES, "For people who play Pure.", 50000, ChatColor.WHITE + "Purity"));
		items.add(new ChatTitleItem(ItemType.TITLE_40_CAKES, "Title: 40 Cakes", ShopType.TITLES, "WHAT? 40 Cakes?!", 75000, ChatColor.GREEN + "40 " + ChatColor.LIGHT_PURPLE + "Cakes"));
		items.add(new ChatTitleItem(ItemType.TITLE_IRON_MAN, "Title: Iron Man", ShopType.TITLES, "A dude made of iron.", 100000, ChatColor.RED + "Iron " + ChatColor.DARK_GRAY + "Man"));
	    items.add(new ChatTitleItem(ItemType.TITLE_MARIO_MARATHON_6, "Title: Mario Marathon 6", ShopType.NOT_FOR_SALE, "Thanks for supporting Mario Marathon 6!", 0, ChatColor.AQUA + "MM" + ChatColor.DARK_RED + "6"));
        items.add(new ChatTitleItem(ItemType.TITLE_FMV, "Title: FMV", ShopType.BLACKMARKET, "You should know what this means.", 150000, ChatColor.RED + "FMV"));
        items.add(new ChatTitleItem(ItemType.TITLE_DELTRESE, "Title: Deltrese", ShopType.BLACKMARKET, "Hah!", 159595, ChatColor.LIGHT_PURPLE + "D" + ChatColor.BLACK + "eltrese"));
        items.add(new ChatTitleItem(ItemType.TITLE_SS4, "Title: ShankShock Anniversary Title (Year 4)", ShopType.NOT_FOR_SALE, "Thanks for being with us on year four!", 0, ChatColor.AQUA + "SS" + ChatColor.GOLD + "4"));
        items.add(new MinecraftItem(ItemType.PIG, "Spawn Egg: Pig", ShopType.SPAWN_SHOP, "Spawns a pig.", 5000, new SpawnEgg(EntityType.PIG).toItemStack()));
        items.add(new MinecraftItem(ItemType.SHEEP, "Spawn Egg: Sheep", ShopType.SPAWN_SHOP, "Spawns a sheep.", 5000, new SpawnEgg(EntityType.SHEEP).toItemStack()));
        items.add(new ChatTitleItem(ItemType.TITLE_KING_OF_SVUH, "Title: King of ShadowVale", ShopType.NOT_FOR_SALE, "King of ShadowVale.", 0, ChatColor.GOLD + "King " + ChatColor.WHITE
         + "of " + ChatColor.LIGHT_PURPLE + "Shadowvale"));
    }

	public enum ShopType {
		KITS("kits", 5, false),
		TITLES("chat titles", 8, true),
		COLORS("chat colors", 9, true),
		BLACKMARKET("black market", 10, false),
		ARMOR("armor", 4, false),
		ITEMS("items", 1, false),
		BLOCKS("blocks", 2, false),
		POTIONS("potions", 3, false),
		MISC("misc", 7, true),
		ADMIN_SHOP("admin shop", 11, false),
		NOT_FOR_SALE("nfs", -1, true),
		SPAWN_SHOP("spawn eggs", 6, false),
		HEAD_SHOP("heads"),
		ENCHANTMENTS("enchantments"),
		SELLOUT("sellout");

		private String commonName;
		private int choiceNumber;
		private boolean enabledInPure;
		
		ShopType(String commonName, int choiceNumber, boolean enabledInPure) {
			this.commonName = commonName;
			this.choiceNumber = choiceNumber;
			this.enabledInPure = enabledInPure;
		}
		
		public boolean isEnabledInPure() {
			return enabledInPure;
		}

		ShopType(String commonName) {
			this.commonName = commonName;
		}

		public String getCommonName() {
			return commonName;
		}

		public int getChoiceNumber() {
			return choiceNumber;
		}

		public String getShopName() {
			return commonName;
		}

	}
}
