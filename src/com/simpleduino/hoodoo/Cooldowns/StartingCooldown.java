package com.simpleduino.hoodoo.Cooldowns;

import com.simpleduino.hoodoo.Hoodoo;
import com.simpleduino.hoodoo.Listeners.GameListener;
import com.simpleduino.hoodoo.Listeners.PlayerListener;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

/**
 * Created by Simple-Duino on 31/05/2016.
 * Copyrights Simple-Duino, all rights reserved
 */
public class StartingCooldown extends BukkitRunnable {

    private int counter = 10;

    @Override
    public void run() {
        if(counter != 0) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.setGameMode(GameMode.SURVIVAL);
                p.setHealthScale(20);
                p.setHealth(20);
                p.setFoodLevel(20);
                p.playSound(p.getLocation(), Sound.CLICK, 10, 1);
                p.setExp(0);
                p.setLevel(counter);
                for(PotionEffect pe : p.getActivePotionEffects())
                {
                    p.removePotionEffect(pe.getType());
                }

            }
            counter--;
        }
        else
        {
            Scoreboard right_sb = Bukkit.getScoreboardManager().getNewScoreboard();
            Objective players_list = right_sb.registerNewObjective("Joueurs", "dummy");
            players_list.setDisplaySlot(DisplaySlot.SIDEBAR);
            Team blueTeam = right_sb.registerNewTeam("Defenseurs");
            blueTeam.setAllowFriendlyFire(false);
            Team redTeam = right_sb.registerNewTeam("Attaquants");
            redTeam.setAllowFriendlyFire(false);
            int sb_counter=1;
            for(Player p : GameListener.teamBlue.getMembers())
            {
                Score score = players_list.getScore(ChatColor.BLUE + p.getName());
                score.setScore(sb_counter);
                p.setLevel(0);
                p.teleport(PlayerListener.playerLink.get(p).getTeam().getGameSpawn());
                p.playSound(p.getLocation(), Sound.LEVEL_UP, 10, 1);
                sb_counter++;
                blueTeam.addEntry(p.getName());
                p.sendMessage(ChatColor.YELLOW + "##--"+ChatColor.MAGIC+"||"+ChatColor.RESET.toString() + ChatColor.YELLOW+" La partie commence, tu es "+ChatColor.BLUE.toString() + ChatColor.BOLD+" DEFENSEUR "+ChatColor.RESET.toString()+ChatColor.YELLOW.toString()+ChatColor.MAGIC+"||"+ChatColor.RESET.toString() + ChatColor.YELLOW+"--##");
            }
            Score separator = players_list.getScore(ChatColor.GREEN + "-----------");
            separator.setScore(sb_counter);
            sb_counter++;
            for(Player p : GameListener.teamRed.getMembers())
            {
                Score score = players_list.getScore(ChatColor.RED + p.getName());
                score.setScore(sb_counter);
                p.setLevel(0);
                p.teleport(PlayerListener.playerLink.get(p).getTeam().getSpawnPoint());
                p.playSound(p.getLocation(), Sound.LEVEL_UP, 10, 1);
                sb_counter++;
                redTeam.addEntry(p.getName());
                PlayerListener.AttCanMove = false;
                p.sendMessage(ChatColor.YELLOW + "##--"+ChatColor.MAGIC+"||"+ChatColor.RESET.toString() + ChatColor.YELLOW+" La partie commence, tu es "+ChatColor.RED.toString() + ChatColor.BOLD+" ATTAQUANT "+ChatColor.RESET.toString()+ChatColor.YELLOW.toString()+ChatColor.MAGIC+"||"+ChatColor.RESET.toString() + ChatColor.YELLOW+"--##");
            }
            for(Player p : Bukkit.getOnlinePlayers())
            {
                p.setScoreboard(right_sb);
                p.setGameMode(GameMode.SURVIVAL);
                p.getInventory().clear();
                p.setHealth(20);
                p.setFoodLevel(20);
                p.sendMessage(ChatColor.BOLD.toString() + ChatColor.AQUA + "-- Bienvenue sur Hoodoo --");
                p.sendMessage(ChatColor.AQUA + "--> Il y a deux equipes: Attaquants (rouge) et Defenseurs (bleu)");
                p.sendMessage(ChatColor.AQUA + "--> L'objectif pour les attaquants est de détruire le beacon dans la zone ennemie");
                p.sendMessage(ChatColor.AQUA + "--> Le défenseur "+ChatColor.BOLD.toString()+ChatColor.UNDERLINE+"DOIT"+ChatColor.RESET.toString() + ChatColor.AQUA + " empecher cette destruction");
                p.sendMessage(ChatColor.AQUA + "--> La partie s'arrete lorsque le beacon est cassé ou qu'il est toujours là après 20 minutes de jeu");
                p.sendMessage(ChatColor.AQUA + "--> Une partie dure donc 20 minutes ;-)");
                p.sendMessage(ChatColor.AQUA + "--> Certains \"passages secrets\" ont été dissimulés dans la map, utilisez les !");
                p.sendMessage(ChatColor.AQUA + "-------------------------------");
                p.sendMessage(ChatColor.AQUA + "-------------------------------");
                p.sendMessage(ChatColor.RED + "La map est de last_username, le mode de jeu est adapté sous forme de plugin pour la NativGaming par Gunlek");
                p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + ChatColor.UNDERLINE + "---------- BON JEU !! ----------");
                p.sendMessage(ChatColor.AQUA + "-------------------------------");
            }
            GameCountdown.countdown = 1200;
            new GameCountdown().runTaskTimer(Hoodoo.getPlugin(Hoodoo.class), 0, 20L);
            this.cancel();
        }
    }
}
