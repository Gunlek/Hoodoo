package com.simpleduino.hoodoo;

import com.simpleduino.guild.GuildAPI.GuildAPI;
import com.simpleduino.hoodoo.Commands.AdminCommand;
import com.simpleduino.hoodoo.Commands.HoodooCommands;
import com.simpleduino.hoodoo.Commands.StartCommand;
import com.simpleduino.hoodoo.Listeners.GameListener;
import com.simpleduino.hoodoo.Listeners.PlayerListener;
import com.simpleduino.hoodoo.Listeners.PluginListener;
import com.simpleduino.hoodoo.Players.HoodooPlayer;
import net.minecraft.server.v1_8_R3.PlayerList;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

/**
 * Created by Simple-Duino on 27/05/2016.
 * Copyrights Simple-Duino, all rights reserved
 */
public class Hoodoo extends JavaPlugin {

    File f = new File("plugins/Hoodoo/config.yml");

    public void onEnable()
    {
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        this.getServer().getPluginManager().registerEvents(new GameListener(), this);
        this.getServer().getPluginCommand("hd").setExecutor(new HoodooCommands());
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new PluginListener());
        this.getServer().createWorld(new WorldCreator("Hoodoo"));
        for(Player p : Bukkit.getOnlinePlayers())
        {
            PlayerListener.playerLink.put(p, new HoodooPlayer(p));
        }
        new GuildAPI();
        if(!f.exists())
        {
            f.getParentFile().mkdirs();
            try {
                f.createNewFile();
                YamlConfiguration cf = YamlConfiguration.loadConfiguration(f);
                cf.set("main.main-worldName", "world");
                cf.set("main.save-worldName", "worldSave");
                cf.set("returnLobby", "lb_1");
                cf.save(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onDisable()
    {

    }
}
