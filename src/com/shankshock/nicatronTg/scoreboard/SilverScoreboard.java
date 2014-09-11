package com.shankshock.nicatronTg.scoreboard;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.shankshock.nicatronTg.Registration.Registration;
import com.shankshock.nicatronTg.Registration.SPlayer;

public class SilverScoreboard implements Listener {

	private final Registration plugin;
	private final HashMap<String, Scoreboard> scoreboards = new HashMap<String, Scoreboard>();

	public SilverScoreboard(Registration instance) {
		this.plugin = instance;
	}

	private void storeBoard(final String player) {
        plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
                ScoreboardManager sb = plugin.getServer().getScoreboardManager();
                Scoreboard board = sb.getNewScoreboard();
                Objective obj = board.registerNewObjective("silver", "dummy");

                obj.setDisplaySlot(DisplaySlot.SIDEBAR);
                obj.setDisplayName(ChatColor.AQUA + "Shank" + ChatColor.DARK_GRAY + "Shock");

                scoreboards.put(player, board);
            }
        });
	}

	public void updateBoard(final String player) {
        plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
                Scoreboard b = scoreboards.get(player);

                if (b == null) {
                    ScoreboardManager sb = plugin.getServer().getScoreboardManager();
                    Scoreboard board = sb.getNewScoreboard();
                    Objective obj = board.registerNewObjective("silver", "dummy");

                    obj.setDisplaySlot(DisplaySlot.SIDEBAR);
                    obj.setDisplayName(ChatColor.AQUA + "Shank" + ChatColor.DARK_GRAY + "Shock");

                    scoreboards.put(player, board);
                }

                b = scoreboards.get(player);

                Objective obj = b.getObjective("silver");

                Score score = obj.getScore(Bukkit.getOfflinePlayer(ChatColor.GRAY + "$"));
                SPlayer sply = plugin.players.get(player);
                int currency = sply.getCurrency();

                score.setScore(currency);
                scoreboards.put(player, b);

                Player ply = plugin.getServer().getPlayer(player);

                ply.setScoreboard(b);
            }
        });
	}
	
	@EventHandler(priority = EventPriority.MONITOR)	
	public void onPlayerQuit(PlayerQuitEvent e) {
		scoreboards.remove(e.getPlayer().getName());
	}

}
