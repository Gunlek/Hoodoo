package com.simpleduino.hoodoo.Commands.Admin;

import com.simpleduino.hoodoo.Cooldowns.GameCountdown;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Simple-Duino on 01/06/2016.
 * Copyrights Simple-Duino, all rights reserved
 */
public class setTimerCommand {

    public setTimerCommand(CommandSender sender, String[] args) {
        if(args.length >= 3) {
            if (sender instanceof Player) {
                GameCountdown.countdown = Integer.parseInt(args[2]);
                ((Player)sender).sendMessage(ChatColor.GREEN + "Le timer de la partie a ete modifie");
                ((Player)sender).sendMessage(ChatColor.YELLOW + "Il reste " + Integer.toString((int)Math.floor(GameCountdown.countdown/60)) + " minutes et " + Integer.toString(GameCountdown.countdown%60) + " secondes");
            }
            else
            {
                sender.sendMessage(ChatColor.RED + "Erreur: Cette commande doit être executée par un joueur");
            }
        }
        else
        {
            sender.sendMessage(ChatColor.RED + "Erreur: Pas assez d'arguments, syntaxe: /hd admin settimer <time>");
        }
    }

}
