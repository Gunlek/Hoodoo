package com.simpleduino.hoodoo.Listeners;

import com.simpleduino.guild.GuildAPI.GuildSQL;
import com.simpleduino.hoodoo.Cooldowns.GameCountdown;
import com.simpleduino.hoodoo.Cooldowns.LobbyReturn;
import com.simpleduino.hoodoo.Events.GameStartEvent;
import com.simpleduino.hoodoo.Hoodoo;
import com.simpleduino.hoodoo.Players.HoodooPlayer;
import org.bukkit.*;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

/**
 * Created by Simple-Duino on 27/05/2016.
 * Copyrights Simple-Duino, all rights reserved
 */
public class PlayerListener implements Listener {

    public static HashMap<Player, HoodooPlayer> playerLink = new HashMap<>();
    public static boolean AttCanMove = true;
    public static boolean gameStarted = false;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        final Player p = e.getPlayer();
        playerLink.put(e.getPlayer(), new HoodooPlayer(e.getPlayer()));

        if(PlayerListener.gameStarted)
        {
            for(Player p1 : Bukkit.getOnlinePlayers())
            {
                if(p1.getScoreboard() != null)
                {
                    p.setScoreboard(p1.getScoreboard());
                    break;
                }
            }
        }

        if(Bukkit.getServer().getMaxPlayers()==Bukkit.getServer().getOnlinePlayers().size())
        {
            Bukkit.getPluginManager().callEvent(new GameStartEvent(e.getPlayer()));
        }
        for(Player p1 : Bukkit.getOnlinePlayers())
        {
            p1.sendMessage(net.md_5.bungee.api.ChatColor.RED + e.getPlayer().getName()+ net.md_5.bungee.api.ChatColor.YELLOW+" a rejoint la partie");
            p1.sendMessage(net.md_5.bungee.api.ChatColor.DARK_AQUA+"("+Integer.toString(Bukkit.getOnlinePlayers().size())+"/"+Integer.toString(Bukkit.getServer().getMaxPlayers())+") joueur(s) avant le démarrage de la partie");
        }
    }

    @EventHandler
    public void onPlayerLeft(PlayerQuitEvent e)
    {
        playerLink.remove(e.getPlayer());
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent e)
    {
        if(e.getPlayer().isDead() || e.getPlayer().getHealth()==0)
        {
            e.setCancelled(true);
        }
        else if(PlayerListener.playerLink.get(e.getPlayer()).hasTeam() && PlayerListener.playerLink.get(e.getPlayer()).getTeam().getName().equalsIgnoreCase("att"))
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e)
    {
        Player p = e.getEntity();
        if(p.isDead())
        {
            Player killer = p.getKiller();
            if(PlayerListener.playerLink.get(killer).hasTeam()) {
                if(PlayerListener.playerLink.get(killer).getTeam().getName().equalsIgnoreCase("att")) {
                    killer.setLevel(killer.getLevel() + 1);
                }
            }

        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e)
    {
        final Player p = e.getPlayer();
        Hoodoo.getPlugin(Hoodoo.class).getServer().getScheduler().scheduleSyncDelayedTask(Hoodoo.getPlugin(Hoodoo.class), new Runnable(){

            @Override
            public void run() {
                if(PlayerListener.playerLink.get(p).hasTeam() && PlayerListener.playerLink.get(p).getTeam().getName().equalsIgnoreCase("def"))
                {
                    p.teleport(GameListener.teamBlue.getSpawnPoint());
                }
                else
                {
                    p.teleport(GameListener.teamRed.getSpawnPoint());
                }
            }
        }, 1L);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e)
    {
        if(e.getEntity() instanceof Player) {
            if (PlayerListener.playerLink.get(e.getEntity()).hasClass() && e.getCause() == EntityDamageEvent.DamageCause.FALL && PlayerListener.playerLink.get((Player)e.getEntity()).getClassName().equalsIgnoreCase("eagle"))
            {
                e.setCancelled(true);
            }
            if(PlayerListener.playerLink.get(e.getEntity()).hasTeam() && e.getCause().equals(EntityDamageEvent.DamageCause.FALL) && PlayerListener.playerLink.get((Player)e.getEntity()).getTeam().getName().equalsIgnoreCase("def"))
            {
                Player p = (Player)e.getEntity();
                Location l = p.getLocation();
                if(l.getWorld().getBlockAt(new Location(l.getWorld(), l.getX(), l.getY()-1, l.getZ())).getType().equals(Material.BEDROCK))
                {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJump(PlayerMoveEvent e)
    {
        if(e.getTo().getY()-e.getFrom().getY()>0 && PlayerListener.playerLink.get(e.getPlayer()).getTeam() != null)
        {
            if(PlayerListener.playerLink.get(e.getPlayer()).hasTeam() && PlayerListener.playerLink.get(e.getPlayer()).getName().equalsIgnoreCase("def")) {
                Player p = e.getPlayer();
                Location l = p.getLocation();
                if (l.getWorld().getBlockAt(new Location(l.getWorld(), l.getX(), l.getY() - 1, l.getZ())).getType().equals(Material.BEDROCK)) {
                    if (l.getWorld().getBlockAt(new Location(l.getWorld(), l.getX(), l.getY() + 3, l.getZ())).getType().equals(Material.BEDROCK)) {
                        p.teleport(new Location(l.getWorld(), l.getX(), l.getY() + 4, l.getZ(), l.getYaw(), l.getPitch()));
                    }
                    if (l.getWorld().getBlockAt(new Location(l.getWorld(), l.getX(), l.getY() + 4, l.getZ())).getType().equals(Material.BEDROCK)) {
                        p.teleport(new Location(l.getWorld(), l.getX(), l.getY() + 5, l.getZ(), l.getYaw(), l.getPitch()));
                    }
                    if (l.getWorld().getBlockAt(new Location(l.getWorld(), l.getX(), l.getY() + 5, l.getZ())).getType().equals(Material.BEDROCK)) {
                        p.teleport(new Location(l.getWorld(), l.getX(), l.getY() + 6, l.getZ(), l.getYaw(), l.getPitch()));
                    }
                    if (l.getWorld().getBlockAt(new Location(l.getWorld(), l.getX(), l.getY() + 6, l.getZ())).getType().equals(Material.BEDROCK)) {
                        p.teleport(new Location(l.getWorld(), l.getX(), l.getY() + 7, l.getZ(), l.getYaw(), l.getPitch()));
                    }
                    if (l.getWorld().getBlockAt(new Location(l.getWorld(), l.getX(), l.getY() + 7, l.getZ())).getType().equals(Material.BEDROCK)) {
                        p.teleport(new Location(l.getWorld(), l.getX(), l.getY() + 8, l.getZ(), l.getYaw(), l.getPitch()));
                    }
                    if (l.getWorld().getBlockAt(new Location(l.getWorld(), l.getX(), l.getY() + 8, l.getZ())).getType().equals(Material.BEDROCK)) {
                        p.teleport(new Location(l.getWorld(), l.getX(), l.getY() + 9, l.getZ(), l.getYaw(), l.getPitch()));
                    }
                    if (l.getWorld().getBlockAt(new Location(l.getWorld(), l.getX(), l.getY() + 9, l.getZ())).getType().equals(Material.BEDROCK)) {
                        p.teleport(new Location(l.getWorld(), l.getX(), l.getY() + 10, l.getZ(), l.getYaw(), l.getPitch()));
                    }
                    if (l.getWorld().getBlockAt(new Location(l.getWorld(), l.getX(), l.getY() + 10, l.getZ())).getType().equals(Material.BEDROCK)) {
                        p.teleport(new Location(l.getWorld(), l.getX(), l.getY() + 11, l.getZ(), l.getYaw(), l.getPitch()));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e)
    {
        if(e.getBlock().getType() == Material.BEACON)
        {
            if(PlayerListener.playerLink.get(e.getPlayer()).hasTeam() && PlayerListener.playerLink.get(e.getPlayer()).getTeam().getName().equalsIgnoreCase("att"))
            {
                Bukkit.getScheduler().cancelTask(GameListener.gameCountdownId);
                for(Player p : Bukkit.getOnlinePlayers())
                {
                    if(p.getScoreboard().getPlayerTeam(p).getName().equalsIgnoreCase("att"))
                    {
                        GuildSQL guildSQL = new GuildSQL();
                        if(guildSQL.hasGuild(p))
                        {
                            if(guildSQL.getDailyCoins(guildSQL.getGuild(p))<Integer.parseInt(guildSQL.getGuildCoinsPerDay(guildSQL.getGuild(p)))) {
                                guildSQL.setGuildCoins(guildSQL.getGuild(p), Integer.parseInt(guildSQL.getGuildCoins(guildSQL.getGuild(p))) + 100);
                                guildSQL.setDailyCoins(guildSQL.getGuild(p), guildSQL.getDailyCoins(guildSQL.getGuild(p)) + 100);
                                p.sendMessage(ChatColor.GREEN + "Vous rapportez 100 coins à votre guilde");
                            }
                            else
                            {
                                p.sendMessage(ChatColor.GREEN + "Vous avez déjà atteint le plafond de gains quotidiens pour votre guilde");
                            }
                        }
                    }
                    else if(p.getScoreboard().getPlayerTeam(p).getName().equalsIgnoreCase("def"))
                    {
                        GuildSQL guildSQL = new GuildSQL();
                        if(guildSQL.hasGuild(p))
                        {
                            if(guildSQL.getDailyCoins(guildSQL.getGuild(p))<Integer.parseInt(guildSQL.getGuildCoinsPerDay(guildSQL.getGuild(p)))) {
                                guildSQL.setGuildCoins(guildSQL.getGuild(p), Integer.parseInt(guildSQL.getGuildCoins(guildSQL.getGuild(p))) + 10);
                                guildSQL.setDailyCoins(guildSQL.getGuild(p), guildSQL.getDailyCoins(guildSQL.getGuild(p)) + 10);
                                p.sendMessage(ChatColor.GREEN + "Vous rapportez 10 coins à votre guilde");
                            }
                            else
                            {
                                p.sendMessage(ChatColor.GREEN + "Vous avez déjà atteint le plafond de gains quotidiens pour votre guilde");
                            }
                        }
                    }
                    p.sendMessage(ChatColor.RED.toString() + ChatColor.MAGIC + "|||||"+ChatColor.RESET.toString() + ChatColor.RED +"         LES ATTAQUANTS GAGNENT !          "+ChatColor.MAGIC+"|||||");
                    new LobbyReturn().runTaskLaterAsynchronously(Hoodoo.getPlugin(Hoodoo.class), 20L * 15);
                }
            }
            else
            {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e)
    {
        if(e.getPlayer().getLocation().getWorld().getBlockAt(new Location(e.getPlayer().getLocation().getWorld(), e.getPlayer().getLocation().getX(), e.getPlayer().getLocation().getY()-1, e.getPlayer().getLocation().getZ())).getType().equals(Material.BEDROCK)
                || e.getPlayer().getLocation().getWorld().getBlockAt(new Location(e.getPlayer().getLocation().getWorld(), e.getPlayer().getLocation().getX(), e.getPlayer().getLocation().getY()-1, e.getPlayer().getLocation().getZ())).getType().equals(Material.OBSIDIAN))
        {
            Player p = e.getPlayer();
            Location l = p.getLocation();
            if(l.getWorld().getBlockAt(new Location(l.getWorld(), l.getX()+1, l.getY(), l.getZ())).getType().equals(Material.IRON_DOOR_BLOCK))
            {
                p.teleport(new Location(l.getWorld(), l.getX()+5, l.getY(), l.getZ(), l.getYaw(), l.getPitch()));
            }
            if(l.getWorld().getBlockAt(new Location(l.getWorld(), l.getX()-1, l.getY(), l.getZ())).getType().equals(Material.IRON_DOOR_BLOCK))
            {
                p.teleport(new Location(l.getWorld(), l.getX()-5, l.getY(), l.getZ(), l.getYaw(), l.getPitch()));
            }
            if(l.getWorld().getBlockAt(new Location(l.getWorld(), l.getX(), l.getY(), l.getZ()+1)).getType().equals(Material.IRON_DOOR_BLOCK))
            {
                p.teleport(new Location(l.getWorld(), l.getX(), l.getY(), l.getZ()+5, l.getYaw(), l.getPitch()));
            }
            if(l.getWorld().getBlockAt(new Location(l.getWorld(), l.getX(), l.getY(), l.getZ()-1)).getType().equals(Material.IRON_DOOR_BLOCK))
            {
                p.teleport(new Location(l.getWorld(), l.getX(), l.getY(), l.getZ()-5, l.getYaw(), l.getPitch()));
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e)
    {
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getClickedBlock().getState() instanceof Sign && PlayerListener.playerLink.get(e.getPlayer()).hasTeam() && PlayerListener.playerLink.get(e.getPlayer()).getTeam().getName().equalsIgnoreCase("att") && PlayerListener.AttCanMove) {
                Sign s = (Sign) e.getClickedBlock().getState();
                String className = ChatColor.stripColor(s.getLine(1)).toLowerCase();
                String classPrice = ChatColor.stripColor(s.getLine(2).toLowerCase().replace(" xp", "").replace(" ", ""));
                if (e.getPlayer().getLevel() >= Integer.parseInt(classPrice)) {
                    Player p = e.getPlayer();
                    switch (className) {
                        case "elephant":
                            e.getPlayer().sendMessage(ChatColor.YELLOW + "Vous avez choisi le kit \"Elephant\"");
                            PlayerListener.playerLink.get(p).setClassName("elephant");
                            for(PotionEffect pe : p.getActivePotionEffects())
                            {
                                p.removePotionEffect(pe.getType());
                            }
                            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 99999, 3, false, false));
                            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 2, false, false));
                            p.getInventory().addItem(new ItemStack(Material.TNT, 128));
                            p.getInventory().addItem(new ItemStack(Material.FLINT_AND_STEEL, 1));
                            ItemStack gold_helmet = new ItemStack(Material.GOLD_HELMET, 1);
                            ItemStack gold_chestplate = new ItemStack(Material.GOLD_CHESTPLATE, 1);
                            ItemStack gold_leggings = new ItemStack(Material.GOLD_LEGGINGS, 1);
                            ItemStack gold_boots = new ItemStack(Material.GOLD_BOOTS, 1);
                            gold_helmet.addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 4);
                            gold_helmet.addEnchantment(Enchantment.DURABILITY, 3);
                            gold_chestplate.addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 4);
                            gold_chestplate.addEnchantment(Enchantment.DURABILITY, 3);
                            gold_leggings.addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 4);
                            gold_leggings.addEnchantment(Enchantment.DURABILITY, 3);
                            gold_boots.addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 4);
                            gold_boots.addEnchantment(Enchantment.DURABILITY, 3);
                            p.getInventory().setHelmet(gold_helmet);
                            p.getInventory().setChestplate(gold_chestplate);
                            p.getInventory().setLeggings(gold_leggings);
                            p.getInventory().setBoots(gold_boots);
                            ItemStack stick = new ItemStack(Material.STICK, 1);
                            stick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
                            p.getInventory().addItem(stick);
                            e.getPlayer().setLevel(e.getPlayer().getLevel()-Integer.parseInt(classPrice));
                            p.teleport(PlayerListener.playerLink.get(p).getTeam().getGameSpawn());
                            break;

                        case "eagle":
                            e.getPlayer().sendMessage(ChatColor.YELLOW + "Vous avez choisi le kit \"Aigle\"");
                            PlayerListener.playerLink.get(p).setClassName("eagle");
                            for(PotionEffect pe : p.getActivePotionEffects())
                            {
                                p.removePotionEffect(pe.getType());
                            }
                            p.setHealthScale(20);
                            p.setHealth(20);
                            p.getInventory().clear();
                            ItemStack bow = new ItemStack(Material.BOW, 1);
                            bow.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
                            bow.addEnchantment(Enchantment.ARROW_KNOCKBACK, 2);
                            p.getInventory().addItem(bow);
                            p.getInventory().addItem(new ItemStack(Material.ARROW, 32));
                            p.getInventory().addItem(new ItemStack(Material.LADDER, 32));
                            p.getInventory().addItem(new ItemStack(Material.ENDER_PEARL, 1));
                            ItemStack helmet = new ItemStack(Material.LEATHER_HELMET, 1);
                            ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
                            ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS, 1);
                            ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
                            LeatherArmorMeta lam = (LeatherArmorMeta) helmet.getItemMeta();
                            lam.setColor(Color.fromRGB(33, 33, 33));
                            helmet.setItemMeta(lam);
                            chestplate.setItemMeta(lam);
                            leggings.setItemMeta(lam);
                            boots.setItemMeta(lam);
                            helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                            chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                            leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                            boots.addEnchantment(Enchantment.PROTECTION_FALL, 2);
                            boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                            e.getPlayer().getInventory().setHelmet(helmet);
                            e.getPlayer().getInventory().setChestplate(chestplate);
                            e.getPlayer().getInventory().setLeggings(leggings);
                            e.getPlayer().getInventory().setBoots(boots);
                            e.getPlayer().setLevel(e.getPlayer().getLevel()-Integer.parseInt(classPrice));
                            p.teleport(PlayerListener.playerLink.get(p).getTeam().getGameSpawn());
                            e.getPlayer().setLevel(e.getPlayer().getLevel()-Integer.parseInt(classPrice));
                            break;

                        case "rabbit":
                            e.getPlayer().sendMessage(ChatColor.YELLOW + "Vous avez choisi le kit \"Lapin\"");
                            PlayerListener.playerLink.get(p).setClassName("rabbit");
                            for(PotionEffect pe : p.getActivePotionEffects())
                            {
                                p.removePotionEffect(pe.getType());
                            }
                            p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 99999, 4, false, false));
                            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999, 1, false, false));
                            p.setHealthScale(8);
                            p.setHealth(20);
                            p.getInventory().clear();
                            p.getInventory().addItem(new ItemStack(Material.STONE_SLAB2, 20));
                            p.getInventory().addItem(new ItemStack(Material.GOLD_PICKAXE, 1));
                            e.getPlayer().setLevel(e.getPlayer().getLevel()-Integer.parseInt(classPrice));
                            p.teleport(PlayerListener.playerLink.get(p).getTeam().getGameSpawn());
                            break;

                        case "wolf":
                            e.getPlayer().sendMessage(ChatColor.YELLOW + "Vous avez choisi le kit \"Loup\"");
                            PlayerListener.playerLink.get(p).setClassName("wolf");
                            for(PotionEffect pe : p.getActivePotionEffects())
                            {
                                p.removePotionEffect(pe.getType());
                            }
                            p.setHealthScale(20);
                            p.setHealth(20);
                            p.getInventory().clear();
                            ItemStack diamond_sword = new ItemStack(Material.DIAMOND_SWORD, 1);
                            diamond_sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
                            diamond_sword.addEnchantment(Enchantment.DURABILITY, 1);
                            p.getInventory().addItem(diamond_sword);
                            ItemStack chain_helmet = new ItemStack(Material.CHAINMAIL_HELMET, 1);
                            ItemStack chain_chestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1);
                            ItemStack chain_leggings = new ItemStack(Material.CHAINMAIL_LEGGINGS, 1);
                            ItemStack chain_boots = new ItemStack(Material.CHAINMAIL_BOOTS, 1);
                            chain_helmet.addEnchantment(Enchantment.PROTECTION_PROJECTILE, 2);
                            chain_chestplate.addEnchantment(Enchantment.PROTECTION_PROJECTILE, 2);
                            chain_leggings.addEnchantment(Enchantment.PROTECTION_PROJECTILE, 2);
                            chain_boots.addEnchantment(Enchantment.PROTECTION_PROJECTILE, 2);
                            chain_helmet.addEnchantment(Enchantment.DURABILITY, 2);
                            chain_chestplate.addEnchantment(Enchantment.DURABILITY, 2);
                            chain_leggings.addEnchantment(Enchantment.DURABILITY, 2);
                            chain_boots.addEnchantment(Enchantment.DURABILITY, 2);
                            p.getInventory().setHelmet(chain_helmet);
                            p.getInventory().setChestplate(chain_chestplate);
                            p.getInventory().setLeggings(chain_leggings);
                            p.getInventory().setBoots(chain_boots);
                            e.getPlayer().setLevel(e.getPlayer().getLevel()-Integer.parseInt(classPrice));
                            p.teleport(PlayerListener.playerLink.get(p).getTeam().getGameSpawn());
                            e.getPlayer().setLevel(e.getPlayer().getLevel()-Integer.parseInt(classPrice));
                            break;

                        case "beaver":
                            e.getPlayer().sendMessage(ChatColor.YELLOW + "Vous avez choisi le kit \"Castor\"");
                            PlayerListener.playerLink.get(p).setClassName("beaver");
                            for(PotionEffect pe : p.getActivePotionEffects())
                            {
                                p.removePotionEffect(pe.getType());
                            }
                            p.setHealthScale(20);
                            p.setHealth(20);

                            e.getPlayer().setLevel(e.getPlayer().getLevel()-Integer.parseInt(classPrice));
                            p.teleport(PlayerListener.playerLink.get(p).getTeam().getGameSpawn());
                            e.getPlayer().setLevel(e.getPlayer().getLevel()-Integer.parseInt(classPrice));
                            break;
                    }
                }
            }
            else if(e.getClickedBlock().getState() instanceof Sign && PlayerListener.playerLink.get(e.getPlayer()).hasTeam() && PlayerListener.playerLink.get(e.getPlayer()).getTeam().getName().equalsIgnoreCase("att") && !PlayerListener.AttCanMove)
            {
                e.getPlayer().sendMessage(ChatColor.YELLOW + "Encore "+ChatColor.RESET.toString()+ChatColor.RED+Integer.toString(GameCountdown.countdown-(60*19))+" secondes"+ChatColor.RESET.toString()+ChatColor.YELLOW+" avant que vous puissiez choisir un kit");
            }
        }
    }
}
