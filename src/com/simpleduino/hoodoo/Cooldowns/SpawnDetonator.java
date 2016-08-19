package com.simpleduino.hoodoo.Cooldowns;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simple-Duino on 06/06/2016.
 * Copyrights Simple-Duino, all rights reserved
 */
public class SpawnDetonator extends BukkitRunnable {

    private World world;
    private int cycles = 0;

    public SpawnDetonator(World w)
    {
        this.world = w;
    }

    @Override
    public void run() {
        if(this.cycles >= 10)
        {
            for(Entity e : world.getEntities())
            {
                if(!(e.getType().equals(EntityType.DROPPED_ITEM)))
                    e.remove();
            }
        }
        ItemStack bow = new ItemStack(Material.BOW, 1);
        bow.addEnchantment(Enchantment.ARROW_FIRE, 1);
        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
        bow.setDurability((short)380);
        ItemStack arrow = new ItemStack(Material.ARROW);
        arrow.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        this.world.dropItem(new Location(this.world, 27, 36, 203), bow);
        this.world.dropItem(new Location(this.world, 27, 36, 203), arrow);
        this.world.dropItem(new Location(this.world, 25, 36, 171), bow);
        this.world.dropItem(new Location(this.world, 25, 36, 171), arrow);
        this.cycles++;
    }
}
