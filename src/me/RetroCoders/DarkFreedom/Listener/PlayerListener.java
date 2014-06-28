package me.RetroCoders.DarkFreedom;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
import me.RetroCoders.DarkFreedom.DF_Util;
import me.RetroCoders.DarkFreedom.DarkFreedom;
import me.RetroCoders.DarkFreedom.CarryPlayer;
import me.RetroCoders.DarkFreedom.Rollback.EnumUpdateType;
import me.RetroCoders.DarkFreedom.Rollback.RollbackManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.util.Vector;

public class AVM_PlayerListener
  implements Listener
{
  @EventHandler(priority=EventPriority.NORMAL)
  public void onPlayerMove(PlayerMoveEvent event)
  {
    Player player = event.getPlayer();
    if (AvalancheFreedom.jumpPadsEnabled) {
      AvalancheFreedom.updateJumppadsForPlayer(player);
    }
    for (CarryPlayer carryPlayer : AbilityManager.carryingPlayers)
    {
      if (carryPlayer.getPlayer().getName() == player.getName()) {
        carryPlayer.onPlayerMove(event);
      }
      if (carryPlayer.getCarrier().getName() == player.getName()) {
        carryPlayer.onCarrierMove();
      }
    }
    if (AbilityManager.isBurriedPlayer(player.getName()))
    {
      event.setCancelled(true);
      Location playerLocation = event.getTo().clone();
      playerLocation = event.getFrom();
      player.teleport(playerLocation);
    }
  }
  
  @EventHandler(priority=EventPriority.NORMAL)
  public void onPlayerTeleport(PlayerTeleportEvent event)
  {
    Player player = event.getPlayer();
    if (event.getTo().getWorld().getName().equalsIgnoreCase("adminworld")) {
      if (!AV_Util.isSuperAdmin(player))
      {
        event.setCancelled(true);
        player.sendMessage(ChatColor.RED + "You cannot teleport to that location because it is in a world you cannot enter.");
      }
      else if (!AvalancheFreedom.adminworldEnabled)
      {
        event.setCancelled(true);
        player.sendMessage(ChatColor.GRAY + "The adminworld is currently disabled.");
      }
    }
    if (AbilityManager.isBurriedPlayer(player.getName())) {
      event.setCancelled(true);
    }
  }
  
  @EventHandler(priority=EventPriority.NORMAL)
  public void onPlayerChat(AsyncPlayerChatEvent event)
  {
    Player player = event.getPlayer();
    if (!event.isCancelled()) {
      if (player.getWorld().getName().equalsIgnoreCase("adminworld"))
      {
        event.setCancelled(true);
        sendAdminworldChatMessage(player, event.getMessage());
      }
    }
    if ((!event.isCancelled()) && (AbilityManager.isBurriedPlayer(player.getName())))
    {
      event.setCancelled(true);
      player.sendMessage(ChatColor.RED + "You have been muted.");
    }
  }
  
  public void sendAdminworldChatMessage(Player player, String s)
  {
    for (Player p : DarkFreedom.server.getOnlinePlayers()) {
      if (p.getWorld().getName().equalsIgnoreCase("adminworld")) {
        p.sendMessage(ChatColor.BLUE + "[" + ChatColor.GRAY + "AdminWorld" + ChatColor.BLUE + "] " + ChatColor.AQUA + player.getName() + ChatColor.WHITE + ": " + s);
      }
    }
    DarkFreedom.logger.info("[AdminWorld] " + player.getName() + ": " + s);
  }
  
  @EventHandler(priority=EventPriority.NORMAL)
  public void onPlayerInteract(PlayerInteractEvent event)
  {
    Player player = event.getPlayer();
    Block block = event.getClickedBlock();
    if (AvalancheFreedom.areSuperpowersEnabledFor(player.getName())) {
      if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
        if ((block != null) && (!isPlacingBlock(player, event)))
        {
          block.getWorld().strikeLightning(block.getLocation());
          block.getWorld().createExplosion(block.getLocation(), 10.0F, false);
        }
      }
    }
  }
  
  public boolean isPlacingBlock(Player player, PlayerInteractEvent event)
  {
    if ((!event.isCancelled()) && (event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
      if (player.getItemInHand().getType() != Material.AIR) {
        return true;
      }
    }
    return false;
  }
  
  Random random = new Random();
  
  @EventHandler(priority=EventPriority.NORMAL)
  public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
  {
    Entity entity = event.getRightClicked();
    Player player = event.getPlayer();
    if (AvalancheFreedom.areSuperpowersEnabledFor(player.getName()))
    {
      if ((entity instanceof Player))
      {
        int rndInt = this.random.nextInt(6);
        if (rndInt < 5) {
          entity.setVelocity(new Vector(2, 2, 0));
        } else {
          entity.setVelocity(new Vector(0, 2, 2));
        }
      }
      else if ((entity instanceof Animals))
      {
        entity.setPassenger(player);
      }
    }
    else if (AbilityManager.isStrengthEnabledFor(player.getName()))
    {
      boolean alreadyCarrying = false;
      for (CarryPlayer carryplayer : AbilityManager.carryingPlayers) {
        if (carryplayer.getPlayer().getName() == ((Player)entity).getName())
        {
          carryplayer.setCarrying(!carryplayer.isBeingCarried());
          alreadyCarrying = true;
        }
      }
      if (!alreadyCarrying)
      {
        CarryPlayer carryPlayer = new CarryPlayer((Player)entity, player);
        carryPlayer.setCarrying(true);
        AbilityManager.carryingPlayers.add(carryPlayer);
      }
    }
  }
  
  @EventHandler(priority=EventPriority.NORMAL)
  public void onBreakBlock(BlockBreakEvent event)
  {
    Player player = event.getPlayer();
    if (DarkFreedom.rollbacksEnabled)
    {
      event.setCancelled(true);
      RollbackManager.addPlayerLogger(player);
      RollbackManager.updatePlayerLogger(player, event.getBlock(), EnumUpdateType.ADD_BROKEN);
      event.setCancelled(false);
    }
  }
  
  @EventHandler(priority=EventPriority.NORMAL)
  public void onDamageEntityByEntity(EntityDamageByEntityEvent event)
  {
    if ((event.getEntity() instanceof Player))
    {
      Player player = (Player)event.getEntity();
      if (AbilityManager.isStrengthEnabledFor(player.getName()))
      {
        event.setDamage(0);
        player.setHealth(20);
      }
    }
    if ((event.getDamager() instanceof Player))
    {
      Player player = (Player)event.getDamager();
      if (AbilityManager.isStrengthEnabledFor(player.getName()))
      {
        int rndInt = this.random.nextInt(1000);
        event.setDamage(event.getDamage() + rndInt);
        event.getEntity().setFireTicks(1000);
        
        AvalancheFreedom.spawnParticleSquare(event.getEntity(), Effect.MOBSPAWNER_FLAMES, 2);
        AvalancheFreedom.spawnParticleSquare(event.getEntity(), Effect.SMOKE, 2);
        event.getEntity().getWorld().createExplosion(event.getEntity().getLocation(), 2.0F, false);
        event.getEntity().setVelocity(event.getEntity().getLocation().subtract(player.getLocation()).toVector().multiply(6));
      }
    }
  }
  
  @EventHandler(priority=EventPriority.NORMAL)
  public void onDamageEntity(EntityDamageEvent event)
  {
    if ((event.getEntity() instanceof Player))
    {
      Player player = (Player)event.getEntity();
      if (AbilityManager.isStrengthEnabledFor(player.getName()))
      {
        event.setDamage(0);
        player.setHealth(20);
      }
    }
  }
  
  @EventHandler(priority=EventPriority.HIGHEST)
  public void onPlayerLogin(PlayerLoginEvent event)
  {
    Player player = event.getPlayer();
    if ((DarkFreedom.lockdownMode) && (!AV_Util.isSuperAdmin(player))) {
      if (AV_Util.isNewPlayer(player))
      {
        event.setResult(PlayerLoginEvent.Result.KICK_BANNED);
        event.setKickMessage(ChatColor.RED + "Server is currently in lockdown mode, please come back in a few minutes.");
      }
    }
  }
  
  @EventHandler(priority=EventPriority.MONITOR)
  public void onPlayerJoin(PlayerJoinEvent event)
  {
    Player player = event.getPlayer();
    if ((player.getWorld().getName().equalsIgnoreCase("adminworld")) && (!AV_Util.isSuperAdmin(player)))
    {
      String playerIP = player.getAddress().getHostName();
      try
      {
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("TotalFreedomMod")) {
          if (!TFM_SuperadminList.getSuperadminIPs().contains(playerIP)) {
            try
            {
              World firstAvailableWorld = (World)AV_Util.getAvailableWorlds().get(0);
              Location spawnLocation = firstAvailableWorld.getSpawnLocation();
              player.teleport(spawnLocation);
            }
            catch (Exception ex)
            {
              player.kickPlayer("You cannot enter this world.");
            }
          }
        }
      }
      catch (Exception ex) {}
    }
    if (!event.getPlayer().hasPlayedBefore())
    {
      player.setOp(true);
      player.sendMessage(ChatColor.YELLOW + "You are now op!");
    }
  }
}
