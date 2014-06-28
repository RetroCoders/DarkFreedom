package me.RetroCoders.DarkFreedom;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class JumppadHandler
{
  Player targetPlayer;
  CombinedVelocity cv;
  
  public JumppadHandler(Player p)
  {
    this.targetPlayer = p;
    this.cv = new CombinedVelocity(this.targetPlayer);
  }
  
  public void handle()
  {
    if (DarkFreedom.jumpPadsEnabled)
    {
      Location targetPlayerLocation = this.targetPlayer.getLocation();
      targetPlayerLocation.setY(targetPlayerLocation.getY() - 1.0D);
      
      int blockID = targetPlayerLocation.getBlock().getTypeId();
      if (DarkFreedom.advancedJumpPads)
      {
        Location targetPlayerLocationLeftX = this.targetPlayer.getLocation();
        targetPlayerLocationLeftX.setX(targetPlayerLocationLeftX.getX() - 1.0D);
        
        Location targetPlayerLocationRightX = this.targetPlayer.getLocation();
        targetPlayerLocationRightX.setX(targetPlayerLocationRightX.getX() + 1.0D);
        
        Location targetPlayerLocationLeftZ = this.targetPlayer.getLocation();
        targetPlayerLocationLeftZ.setZ(targetPlayerLocationLeftZ.getZ() - 1.0D);
        
        Location targetPlayerLocationRightZ = this.targetPlayer.getLocation();
        targetPlayerLocationRightZ.setZ(targetPlayerLocationRightZ.getZ() + 1.0D);
        
        int blockIDLeftX = targetPlayerLocationLeftX.getBlock().getTypeId();
        int blockIDRightX = targetPlayerLocationRightX.getBlock().getTypeId();
        
        int blockIDLeftZ = targetPlayerLocationLeftZ.getBlock().getTypeId();
        int blockIDRightZ = targetPlayerLocationRightZ.getBlock().getTypeId();
        
        this.cv = new CombinedVelocity(this.targetPlayer);
        if (blockID == DarkFreedom.jumpPadBlockID)
        {
          this.cv.setVelocityY(DarkFreedom.jumpPadHeight);
          if (this.targetPlayer.getFallDistance() > 0.0F) {
            this.targetPlayer.setFallDistance(0.0F);
          }
        }
        if (blockIDLeftX == DarkFreedom.jumpPadBlockID)
        {
          this.cv.setVelocityX(DarkFreedom.jumpPadHeight);
          this.cv.setVelocityY(this.cv.getVelocityY() + 0.3F);
        }
        if (blockIDRightX == DarkFreedom.jumpPadBlockID)
        {
          this.cv.setVelocityX(-DarkFreedom.jumpPadHeight);
          this.cv.setVelocityY(this.cv.getVelocityY() + 0.3F);
        }
        if (blockIDLeftZ == DarkFreedom.jumpPadBlockID)
        {
          this.cv.setVelocityZ(DarkFreedom.jumpPadHeight);
          this.cv.setVelocityY(this.cv.getVelocityY() + 0.3F);
        }
        if (blockIDRightZ == DarkFreedom.jumpPadBlockID)
        {
          this.cv.setVelocityZ(-DarkFreedom.jumpPadHeight);
          this.cv.setVelocityY(this.cv.getVelocityY() + 0.3F);
        }
        if (this.cv.getHasBeenModified()) {
          this.cv.setPlayerVelocity();
        }
      }
      else if (blockID == DarkFreedom.jumpPadBlockID)
      {
        this.targetPlayer.setVelocity(new Vector(0.0F, DarkFreedom.jumpPadHeight, 0.0F));
        if (this.targetPlayer.getFallDistance() > 0.0F) {
          this.targetPlayer.setFallDistance(0.0F);
        }
      }
    }
  }
}
