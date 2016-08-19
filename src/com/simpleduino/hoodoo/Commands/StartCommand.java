package com.simpleduino.hoodoo.Commands;

import com.simpleduino.hoodoo.Events.GameStartEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Simple-Duino on 27/05/2016.
 * Copyrights Simple-Duino, all rights reserved
 */
public class StartCommand {

    public StartCommand(CommandSender sender, String[] strings) {
        if(sender instanceof Player)
        {
            Bukkit.getPluginManager().callEvent(new GameStartEvent((Player)sender));
        }
        else
        {
            sender.sendMessage(ChatColor.RED + "Erreur: Cette commande doit être executée par un joueur");
        }
    }
}
