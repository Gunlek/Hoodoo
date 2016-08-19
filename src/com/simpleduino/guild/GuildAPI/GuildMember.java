package com.simpleduino.guild.GuildAPI;

import org.bukkit.entity.Player;

/**
 * Created by Simple-Duino on 02/07/2016.
 * Copyrights Simple-Duino, all rights reserved
 */

public class GuildMember {

    private Player player;
    private GuildSQL guildSQL = new GuildSQL();

    public GuildMember(Player p)
    {
        this.player = p;
    }

    public int getRank()
    {
        return guildSQL.getRank(this.player);
    }

    public boolean hasGuild()
    {
        return guildSQL.hasGuild(this.player);
    }

    public String getGuild()
    {
        return guildSQL.getGuild(this.player);
    }

    public boolean hasNotifications()
    {
        return guildSQL.getNotif(this.player);
    }
}
