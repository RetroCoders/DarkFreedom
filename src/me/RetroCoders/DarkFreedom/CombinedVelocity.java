package net.Avalanche.AVFreedom;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class CombinedVelocity
{
  Player targetPlayer;
  float velocityX = 0.0F;
  float velocityY = 0.0F;
  float velocityZ = 0.0F;
  boolean hasBeenModified = false;
  
  public CombinedVelocity(Player p)
  {
    this.targetPlayer = p;
  }
  
  public void setVelocityX(float newX)
  {
    this.hasBeenModified = true;
    this.velocityX = newX;
  }
  
  public void setVelocityY(float newY)
  {
    this.hasBeenModified = true;
    this.velocityY = newY;
  }
  
  public void setVelocityZ(float newZ)
  {
    this.hasBeenModified = true;
    this.velocityZ = newZ;
  }
  
  public float getVelocityX()
  {
    return this.velocityX;
  }
  
  public float getVelocityY()
  {
    return this.velocityY;
  }
  
  public float getVelocityZ()
  {
    return this.velocityZ;
  }
  
  public boolean getHasBeenModified()
  {
    return this.hasBeenModified;
  }
  
  public void setPlayerVelocity()
  {
    if (this.hasBeenModified) {
      this.targetPlayer.setVelocity(new Vector(this.velocityX, this.velocityY, this.velocityZ));
    }
  }
}
