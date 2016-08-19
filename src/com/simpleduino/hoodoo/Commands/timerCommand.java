package com.simpleduino.hoodoo.Commands;

import com.simpleduino.hoodoo.Events.GameStartEvent;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Simple-Duino on 01/06/2016.
 * Copyrights Simple-Duino, all rights reserved
 */
public class timerCommand {

    public static int counter = 0;

    public timerCommand(CommandSender sender, String[] strings) {
        if(sender instanceof Player) {
            ((Player)sender).sendMessage(ChatColor.YELLOW + "Il reste " + Integer.toString((int)Math.floor(counter/60)) + " minutes et " + Integer.toString(counter%60) + " secondes");
        }
        else
        {
            sender.sendMessage(org.bukkit.ChatColor.RED + "Erreur: Cette commande doit être executée par un joueur");
        }
    }

}
