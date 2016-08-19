package com.simpleduino.hoodoo.Commands;

import com.simpleduino.hoodoo.Commands.Admin.SetSpawnCommand;
import com.simpleduino.hoodoo.Commands.Admin.joinTeamCommand;
import com.simpleduino.hoodoo.Commands.Admin.resetMapCommand;
import com.simpleduino.hoodoo.Commands.Admin.setTimerCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Simple-Duino on 30/05/2016.
 * Copyrights Simple-Duino, all rights reserved
 */
public class AdminCommand {

    public AdminCommand(CommandSender sender, String[] args) {
        if(sender instanceof Player)
        {
            if(args.length >= 2)
            {
                if(args[0].equalsIgnoreCase("admin")) {
                    switch (args[1].toLowerCase()) {
                        case "setspawn":
                            new SetSpawnCommand(sender, args);
                            break;

                        case "settimer":
                            new setTimerCommand(sender, args);
                            break;

                        case "jointeam":
                            new joinTeamCommand(sender, args);
                            break;

                        case "reset":
                            new resetMapCommand();
                            break;
                    }
                }
            }
            else
            {
                sender.sendMessage(ChatColor.RED + "Erreur: Pas assez d'arguments, syntaxe: /hd admin <jointeam | setspawn | settimer>");
            }
        }
        else
        {
            sender.sendMessage(ChatColor.RED + "Erreur: Cette commande doit être executée par un joueur");
        }
    }
}
