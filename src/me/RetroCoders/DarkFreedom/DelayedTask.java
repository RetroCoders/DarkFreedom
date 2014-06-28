package me.RetroCoders.DarkFreedom;

import org.bukkit.Server;
import org.bukkit.scheduler.BukkitScheduler;

public class DelayedTask
{
  boolean complete = true;
  long delayTime;
  
  public DelayedTask(long time)
  {
    this.delayTime = time;
  }
  
  public void activate()
  {
    if (this.complete)
    {
      this.complete = false;
      AvalancheFreedom.plugin.getServer().getScheduler().scheduleSyncDelayedTask(AvalancheFreedom.plugin, new Runnable()
      {
        public void run()
        {
          DelayedTask.this.actions();
          DelayedTask.this.complete = true;
        }
      }, this.delayTime);
    }
  }
  
  public void actions() {}
}
