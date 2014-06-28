package me.RetroCoders.DarkFreedom.Listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

public class EntityListener
  implements Listener
{
  Material[] disallowedBlockPhysics = { Material.SAND, Material.GRAVEL, Material.ANVIL };
  
  @EventHandler(priority=EventPriority.NORMAL)
  public void onBlockPhysics(BlockPhysicsEvent event)
  {
    if (!isAllowedBlockPhysics(event.getBlock())) {
      event.setCancelled(true);
    }
  }
  
  public boolean isAllowedBlockPhysics(Block b)
  {
    Material material = b.getType();
    boolean isAllowed = true;
    for (int i = 0; i < this.disallowedBlockPhysics.length; i++) {
      if (this.disallowedBlockPhysics[i].equals(material)) {
        isAllowed = false;
      }
    }
    return isAllowed;
  }
}
