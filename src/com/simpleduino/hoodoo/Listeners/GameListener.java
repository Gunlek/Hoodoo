package com.simpleduino.hoodoo.Listeners;

import com.simpleduino.hoodoo.Cooldowns.SpawnDetonator;
import com.simpleduino.hoodoo.Cooldowns.StartingCooldown;
import com.simpleduino.hoodoo.Events.GameStartEvent;
import com.simpleduino.hoodoo.Hoodoo;
import com.simpleduino.hoodoo.Teams.HoodooTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;

/**
 * Created by Simple-Duino on 27/05/2016.
 * Copyrights Simple-Duino, all rights reserved
 */

public class GameListener implements Listener {

    public static HoodooTeam teamBlue, teamRed;
    public static int gameCountdownId;
    private int counter = 0;

    @EventHandler
    public void onGameStart(GameStartEvent e)
    {
        PlayerListener.gameStarted = true;
        teamBlue = new HoodooTeam("Def", ChatColor.BLUE);
        teamRed = new HoodooTeam("Att", ChatColor.RED);
        ArrayList onlinePlayers = new ArrayList();
        for(Player p : Bukkit.getOnlinePlayers())
        {
            p.getInventory().clear();
            p.getInventory().setHelmet(null);
            p.getInventory().setChestplate(null);
            p.getInventory().setLeggings(null);
            p.getInventory().setBoots(null);
        }
        for(Player p : PlayerListener.playerLink.keySet())
        {
            onlinePlayers.add(p);
        }
        new SpawnDetonator(Bukkit.getWorld("Hoodoo")).runTaskTimer(Hoodoo.getPlugin((Hoodoo.class)), 0, 20L*10);
        randomizeTeams(onlinePlayers);
        new StartingCooldown().runTaskTimer(Hoodoo.getPlugin(Hoodoo.class), 0, 20L);
    }

    private void randomizeTeams(ArrayList<Player> pList)
    {
        for(Player p : pList)
        {
            Integer i = Integer.valueOf(Long.toString(Math.round(Math.random())));
            if(i==0)
            {
                if(teamBlue.getTeamSize() >= pList.size()/2)
                {
                    teamRed.addMember(p);
                    PlayerListener.playerLink.get(p).setTeam(teamRed);
                }
                else {
                    teamBlue.addMember(p);
                    PlayerListener.playerLink.get(p).setTeam(teamBlue);
                }
            }
            else
            {
                if (teamRed.getTeamSize() >= pList.size() / 2) {
                    teamBlue.addMember(p);
                    PlayerListener.playerLink.get(p).setTeam(teamBlue);
                }
                else {
                    teamRed.addMember(p);
                    PlayerListener.playerLink.get(p).setTeam(teamRed);
                }
            }
        }
    }
}
