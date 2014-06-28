package me.RetroCoders.DarkFreedom;

import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NopeBanDelay
  extends DelayedTask
{
  Player targetPlayer;
  CommandSender sender;
  int playerNumb;
  
  public NopeBanDelay()
  {
    super(24L);
  }
  
  public void activateDelay(Player p, CommandSender newSender, int i)
  {
    this.targetPlayer = p;
    this.sender = newSender;
    this.playerNumb = i;
    activate();
  }
  
  public void actions()
  {
    this.targetPlayer.setHealth(0);
    this.targetPlayer.getWorld().createExplosion(this.targetPlayer.getLocation(), 4.0F, false);
    DarkFreedom.server.broadcastMessage(ChatColor.RED + this.targetPlayer.getName() + " was blown to pieces.");
    for (int i = 0; i < 3; i++) {
      this.targetPlayer.getWorld().strikeLightning(this.targetPlayer.getLocation());
    }
    DarkFreedom.spawnParticleSquare(this.targetPlayer, Effect.MOBSPAWNER_FLAMES, 4);
    this.targetPlayer.kickPlayer("NOPE");
    DarkFreedom.server.dispatchCommand(this.sender, "glist ban " + this.targetPlayer.getName());
    DarkFreedom.pendingPlayerExplosions.remove(this.playerNumb);
  }
}
