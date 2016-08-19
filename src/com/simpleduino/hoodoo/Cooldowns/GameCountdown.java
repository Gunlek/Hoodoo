package com.simpleduino.hoodoo.Cooldowns;

import com.simpleduino.guild.GuildAPI.GuildSQL;
import com.simpleduino.hoodoo.Commands.timerCommand;
import com.simpleduino.hoodoo.Hoodoo;
import com.simpleduino.hoodoo.Listeners.GameListener;
import com.simpleduino.hoodoo.Listeners.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.io.File;

/**
 * Created by Simple-Duino on 01/06/2016.
 * Copyrights Simple-Duino, all rights reserved
 */
public class GameCountdown extends BukkitRunnable {

    public static int countdown = 1200;

    @Override
    public void run() {
        if(((Player)Bukkit.getOnlinePlayers().toArray()[0]).getScoreboard() != null) {
            Scoreboard sb = ((Player) Bukkit.getOnlinePlayers().toArray()[0]).getScoreboard();
            Objective obj = sb.getObjective(DisplaySlot.SIDEBAR);
            if (countdown > 60) {
                if (countdown % 60 == 0 && (countdown / 60) % 5 == 0 && countdown != 1200) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.sendMessage(ChatColor.YELLOW + "##-- Il reste " + countdown / 60 + " minutes --##");
                    }
                }
            } else {
                Score timer = obj.getScore(ChatColor.GREEN + "Temps (sec)");
                timer.setScore(countdown);
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.setScoreboard(sb);
                }
                if (countdown == 0) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.setGameMode(GameMode.CREATIVE);
                        p.getInventory().clear();
                        p.getInventory().addItem(new ItemStack(Material.TNT, 512));
                        p.getInventory().addItem(new ItemStack(Material.FLINT_AND_STEEL, 1));
                        if(p.getScoreboard().getPlayerTeam(p).getName().equalsIgnoreCase("def"))
                        {
                            GuildSQL guildSQL = new GuildSQL();
                            if(guildSQL.hasGuild(p))
                            {
                                if(guildSQL.getDailyCoins(guildSQL.getGuild(p))<Integer.parseInt(guildSQL.getGuildCoinsPerDay(guildSQL.getGuild(p)))) {
                                    guildSQL.setGuildCoins(guildSQL.getGuild(p), Integer.parseInt(guildSQL.getGuildCoins(guildSQL.getGuild(p))) + 100);
                                    guildSQL.setDailyCoins(guildSQL.getGuild(p), guildSQL.getDailyCoins(guildSQL.getGuild(p)) + 100);
                                    p.sendMessage(ChatColor.GREEN + "Vous rapportez 100 coins à votre guilde");
                                }
                                else
                                {
                                    p.sendMessage(ChatColor.GREEN + "Vous avez déjà atteint le plafond de gains quotidiens pour votre guilde");
                                }
                            }
                        }
                        else if(p.getScoreboard().getPlayerTeam(p).getName().equalsIgnoreCase("att"))
                        {
                            GuildSQL guildSQL = new GuildSQL();
                            if(guildSQL.hasGuild(p))
                            {
                                if(guildSQL.getDailyCoins(guildSQL.getGuild(p))<Integer.parseInt(guildSQL.getGuildCoinsPerDay(guildSQL.getGuild(p)))) {
                                    guildSQL.setGuildCoins(guildSQL.getGuild(p), Integer.parseInt(guildSQL.getGuildCoins(guildSQL.getGuild(p))) + 10);
                                    guildSQL.setDailyCoins(guildSQL.getGuild(p), guildSQL.getDailyCoins(guildSQL.getGuild(p)) + 10);
                                    p.sendMessage(ChatColor.GREEN + "Vous rapportez 10 coins à votre guilde");
                                }
                                else
                                {
                                    p.sendMessage(ChatColor.GREEN + "Vous avez déjà atteint le plafond de gains quotidiens pour votre guilde");
                                }
                            }
                        }
                        p.sendMessage(ChatColor.BLUE.toString() + ChatColor.MAGIC + "|||||" + ChatColor.RESET.toString() + ChatColor.BLUE + "         LES DEFENSEURS GAGNENT !          " + ChatColor.MAGIC + "|||||");
                    }
                    new LobbyReturn().runTaskLaterAsynchronously(Hoodoo.getPlugin(Hoodoo.class), 20L * 15);
                    this.cancel();
                }
            }
            if (countdown > 0) {
                countdown--;
            } else {
                this.cancel();
            }
            if (countdown <= 1200 - 60) {
                PlayerListener.AttCanMove = true;
            }
            timerCommand.counter = countdown;
            GameListener.gameCountdownId = this.getTaskId();
        }
        else
        {

        }
    }
}
