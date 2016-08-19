package com.simpleduino.hoodoo.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by Simple-Duino on 30/05/2016.
 * Copyrights Simple-Duino, all rights reserved
 */
public class HoodooCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(args.length >= 1)
        {
            switch(args[0])
            {
                case "start":
                    new StartCommand(sender, args);
                    break;

                case "timer":
                    new timerCommand(sender, args);
                    break;

                case "admin":
                    new AdminCommand(sender, args);
                    break;
            }
        }
        else
        {
            sender.sendMessage(ChatColor.RED + "Erreur: Pas assez d'arguments, syntaxe: /hd <admin | timer | start>");
        }
        return false;
    }
}
