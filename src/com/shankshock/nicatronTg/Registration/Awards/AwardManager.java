package com.shankshock.nicatronTg.Registration.Awards;

import com.shankshock.nicatronTg.Registration.Items.ItemType;
import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.SPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class AwardManager {
	private final Registration plugin;
	private ArrayList<Award> awards = new ArrayList<Award>();

	public AwardManager(Registration instance) {
		this.plugin = instance;
		setupAwards();
	}

	public void setupAwards() {
		awards.add(new DarkphantomSealOfAwesome(AwardType.SEAL_OF_AWESOME, "Dark's Seal of Awesome", true));
		awards.add(new ActivationAward(AwardType.ACTIVATION, "Did you live through the activation process?", false));
		awards.add(new CounterTitleAward(AwardType.CREEPER_MILESTONE_1, "Kill 250 creepers.", false, 250, ItemType.TITLE_BOMB_SQUAD_2));
		awards.add(new CounterTitleAward(AwardType.COW_MILESTONE_1, "Kill 250 cows.", false, 250, ItemType.TITLE_MOO));
		awards.add(new CounterTitleAward(AwardType.ZOMBIE_MILESTONE_1, "Kill 1000 skeletons & zombies.", false, 1000, ItemType.TITLE_PALADIN));
		awards.add(new CounterTitleAward(AwardType.SQUIRE, "Kill 500 skeletons & zombies.", false, 500, ItemType.TITLE_SQUIRE));
		awards.add(new CounterTitleAward(AwardType.FIREFIGHTER, "Kill 250 blazes.", false, 250, ItemType.TITLE_FIREFIGHTER));
		awards.add(new TitleAward(AwardType.ROCKEFELLER, "Over 1,000,000 Silver.", false, ItemType.TITLE_ROCKEFELLER));
		awards.add(new CounterTitleAward(AwardType.GHOSTBUSTER, "Kill 250 ghasts.", false, 250, ItemType.TITLE_GHOSTBUSTER));
		awards.add(new CounterTitleAward(AwardType.JELLO_LOVER, "Kill 250 slimes.", false, 250, ItemType.TITLE_JELLO_LOVER));
		awards.add(new NoGainAward(AwardType.THE_END, "Watch as life flashes before your eyes.", true));
		awards.add(new NoGainAward(AwardType.LIVING_DEAD, "Apparently the ban train *does* make return trips.", true));
		awards.add(new GenericCounterAward(AwardType.LAPIS_DIGGER, "Mine 1000 pieces of lapis lazuli.", false, 1000));
		awards.add(new GenericCounterAward(AwardType.DIAMONDS, "Mine 250 of the ore that everyone wants.", false, 250));
		awards.add(new GenericCounterAward(AwardType.REDSTONE, "Mine 2500 pieces of the ore that nobody wants.", false, 2500));
		awards.add(new CounterTitleAward(AwardType.ARACHNOPHOBIA, "Kill 250 spiders.", false, 250, ItemType.TITLE_SPIDER_SLAYER));
		awards.add(new CounterTitleAward(AwardType.SHADOW_CHILD, "Kill 250 endermen.", false, 250, ItemType.TITLE_SHADOW_CHILD));
		awards.add(new CounterTitleAward(AwardType.VOID_TOUCHED, "Kill 1000 endermen.", false, 1000, ItemType.TITLE_VOID_TOUCHED));
		awards.add(new CounterTitleAward(AwardType.COUNTER_TERRORIST, "Defuse 1000 creepers.", false, 1000, ItemType.TITLE_COUNTER_TERRORIST));
		awards.add(new TitleAward(AwardType.VOID_KING, "Take part in the slaying of an ender dragon.", false, ItemType.TITLE_VOID_KING));
		awards.add(new CounterTitleAward(AwardType.DARK_ONE, "Take part in the massacre of 10 ender dragons.", false, 10, ItemType.TITLE_DARK_ONE));
		awards.add(new GenericAward(AwardType.NEWS_WORTHY, "Stay informed with what's happening on the server.", false));
		awards.add(new GenericCounterAward(AwardType.STAY_INFORMED, "Read a new bit of news a fair few times.", false, 50));
		awards.add(new GenericAward(AwardType.DEBUGGER, "Join the server in debug mode.", false));
		awards.add(new GenericAward(AwardType.PURELY_AWESOME, "Welcome to Pure BlockShock.", false));
		awards.add(new CounterTitleAward(AwardType.CHRISTMAS_LV1, "Merry Christmas! 5 gifts found!", false, 5, ItemType.TITLE_SS_XMAS_2012));
		awards.add(new GenericCounterAward(AwardType.CHRISTMAS_LV2, "Merry Christmas! 25 gifts found!", false, 25));
		awards.add(new GenericCounterAward(AwardType.CHRISTMAS_LV3, "Merry Christmas! 50 gifts found!", false, 50));
		awards.add(new CounterTitleAward(AwardType.THE_DAMNED, "Holy shit. 250 Wither Skeletons killed.", false, 250, ItemType.TITLE_THE_DAMMNED));
		awards.add(new CounterTitleAward(AwardType.KATNISS_EVERDEEN, "Kill 2,500 hostile mobs with a bow.", false, 2500, ItemType.TITLE_THE_GIRL_WHO_WAS_ON_FIRE));
		awards.add(new CounterTitleAward(AwardType.NINJA, "Kill 500 hostile mobs with your bare hands.", false, 500, ItemType.TITLE_NINJITSU_MASTER));
		awards.add(new CounterTitleAward(AwardType.MARAUDER, "Kill 250 villagers.", false, 250, ItemType.TITLE_MARAUDER));
		awards.add(new CounterTitleAward(AwardType.EXECUTIONER, "Kill 500 hostile mobs with iron axes.", false, 500, ItemType.TITLE_EXECUTIONER));
		awards.add(new CounterTitleAward(AwardType.SWORDSMAN, "Kill 500 hostile mobs with a sword.", false, 500, ItemType.TITLE_SWORDSMAN));
		awards.add(new CounterTitleAward(AwardType.LYCAN_HUNTER, "Kill 500 hostile wolves.", false, 500, ItemType.TITLE_LYCAN_HUNTER));
		awards.add(new GenericAward(AwardType.MINI_MARATHON, "Stay connected to ShankShock for 4 hours.", false));
		awards.add(new GenericAward(AwardType.MARATHON, "Stay connected to ShankShock for 8 hours.", false, 8000));
		awards.add(new GenericAward(AwardType.CREATIVE, "Join the creative map for the first time.", false));
		awards.add(new GenericAward(AwardType.SHANKSHOCK_AFTER_DARK, "The seediest chat in the world.", false, 31337));
		awards.add(new CounterTitleAward(AwardType.ARTIST, "Add 20,000 blocks to creative.", false, 20000, ItemType.TITLE_ARTIST));
        awards.add(new TitleAward(AwardType.SHANKSHOCK_YEAR_FOUR, "Thanks for supporting ShankShock for the fourth year in a row!", false,ItemType.TITLE_SS4));
	}

	public void updateAward(AwardType type, float amount, Player ply) {
		SPlayer sply = plugin.players.get(ply.getName());

		if (sply.getAwardStore().hasAward(type)) {
			return;
		}

		if (sply.getRawAwardStore().updateAward(getAwards(), type, amount)) {
			addAward(type, ply);
		}
	}

	public void addAward(AwardType type, Player ply) {
		SPlayer sply = plugin.players.get(ply.getName());

		if (sply.getAwardStore().getAwards().contains(type)) {
			return;
		}

		for (Award a : awards) {
			if (a.getType() == type && !sply.getAwardStore().hasAward(type)) {
				a.addAchievement(plugin, ply);
				for (Player pl : plugin.getServer().getOnlinePlayers()) {
					pl.sendMessage(ChatColor.AQUA + ply.getDisplayName() + ChatColor.GOLD + " earned " + ChatColor.AQUA + type.getName() + ChatColor.GOLD + ".");
				}

				if (a instanceof GenericAward) {
					sply.addCurrency(((GenericAward) a).getEarnAmount(), false);
				}

				if (!(a instanceof NoGainAward)) {
					sply.addCurrency(5000, false);
				}
				sply.saveAccount();
			}
		}
	}

	public ArrayList<Award> getAwards() {
		return awards;
	}

	public enum AwardType {
		SEAL_OF_AWESOME("Seal of Awesome"),
		ACTIVATION("I'm New"),
		CREEPER_MILESTONE_1("Bomb Squad"),
		COW_MILESTONE_1("Cow Tipper"),
		ZOMBIE_MILESTONE_1("Paladin"),
		FIREFIGHTER("Fire Fighter"),
		ROCKEFELLER("John D. Rockefeller"),
		GHOSTBUSTER("Ghostbuster"),
		JELLO_LOVER("Jello Lover"),
		NECROMANCER("Necromancer"),
		THE_END("The End of the Line"),
		LIVING_DEAD("The Living Dead"),
		LAPIS_DIGGER("Lapis Digger"),
		DIAMONDS("Diamonds...Diamonds...Diamonds..."),
		REDSTONE("I'm so tired of this damn ore"),
		ARACHNOPHOBIA("Arachnophobia"),
		SHADOW_CHILD("Shadow Child"),
		VOID_TOUCHED("Void Touched"),
		VOID_KING("Void King"),
		DARK_ONE("Dark One"),
		COUNTER_TERRORIST("Counter Terrorist"),
		SQUIRE("Squire"),
		STAY_INFORMED("Stay Informed"),
		NEWS_WORTHY("News Worthy"),
		DEBUGGER("Debugger"),
		PURELY_AWESOME("Purely Awesome"),
		CHRISTMAS_LV1("Merry Christmas"),
		CHRISTMAS_LV2("Spoiled Child!"),
		CHRISTMAS_LV3("Gifted!"),
		THE_DAMNED("The Damned"),
		KATNISS_EVERDEEN("Katniss Everdeen"),
		NINJA("Ninjitsu Master"),
		LYCAN_HUNTER("Lycan Hunter"),
		MARAUDER("Marauder"),
		EXECUTIONER("Executioner"),
		SWORDSMAN("Swordsman"),
		MINI_MARATHON("Mini-Marathon"),
		MARATHON("Marathon"),
		CREATIVE("The Creative Type"),
		SHANKSHOCK_AFTER_DARK("ShankShock After Dark"),
		ARTIST("Artist"),
        SHANKSHOCK_YEAR_FOUR("ShankShock Anniversary: Year 4");

		private String commonName;

		AwardType(String commonName) {
			this.commonName = commonName;
		}

		public String getName() {
			return commonName;
		}

	}
}