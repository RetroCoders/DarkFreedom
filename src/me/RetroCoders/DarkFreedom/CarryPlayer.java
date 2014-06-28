package me.RetroCoders.DarkFreedom;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

public class CarryPlayer
{
  Player targetPlayer;
  Player targetCarrier;
  boolean carrying = false;
  
  public CarryPlayer(Player player, Player carrier)
  {
    this.targetPlayer = player;
    this.targetCarrier = carrier;
  }
  
  public void onCarrierMove()
  {
    if (this.carrying)
    {
      Location teleportLocation = this.targetCarrier.getLocation();
      teleportLocation.setY(teleportLocation.getY() + 2.0D);
      this.targetPlayer.teleport(teleportLocation);
    }
  }
  
  public void onPlayerMove(PlayerMoveEvent event)
  {
    if (this.carrying) {
      event.setCancelled(true);
    }
  }
  
  public boolean isBeingCarried()
  {
    return this.carrying;
  }
  
  public void setCarrying(boolean flag)
  {
    this.carrying = flag;
  }
  
  public Player getPlayer()
  {
    return this.targetPlayer;
  }
  
  public Player getCarrier()
  {
    return this.targetCarrier;
  }
}
