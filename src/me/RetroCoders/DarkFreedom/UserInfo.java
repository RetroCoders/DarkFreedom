package me.RetroCoders.DarkFreedom;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class UserInfo
{
  public static final Map<Player, UserInfo> userinfo = new HashMap();
  private final Player player;
  private final String ip_address;
  private final String player_name;
  private boolean user_frozen = false;
  private boolean is_muted = false;
  private boolean is_halted = false;
  private int msg_count = 0;
  private int block_destroy_total = 0;
  private int block_place_total = 0;
  private int freecam_destroy_count = 0;
  private int freecam_place_count = 0;
  private boolean user_caged = false;
  private Location user_cage_pos;
  private List<TFM_BlockData> user_cage_history = new ArrayList();
  private Material cage_material_outer = Material.GLASS;
  private Material cage_material_inner = Material.AIR;
  private boolean is_orbiting = false;
  private double orbit_strength = 10.0D;
  private boolean mob_thrower_enabled = false;
  private EntityType mob_thrower_creature = EntityType.PIG;
  private double mob_thrower_speed = 4.0D;
  private List<LivingEntity> mob_thrower_queue = new ArrayList();
  private BukkitTask mp44_schedule_id = null;
  private boolean mp44_armed = false;
  private boolean mp44_firing = false;
  private BukkitTask lockup_schedule_id = null;
  private String last_message = "";
  private boolean in_adminchat = false;
  private boolean all_commands_blocked = false;
  private Boolean superadmin_id_verified = null;
  private String last_command = "";
  
  public UserInfo(Player player)
  {
    this.player = player;
    this.ip_address = player.getAddress().getAddress().getHostAddress();
    this.player_name = player.getName();
  }
  
  public static UserInfo getPlayerData(Player p)
  {
    UserInfo playerdata = (UserInfo)userinfo.get(p);
    if (playerdata == null)
    {
      Iterator<Map.Entry<Player, UserInfo>> it = userinfo.entrySet().iterator();
      while (it.hasNext())
      {
        Map.Entry<Player, UserInfo> pair = (Map.Entry)it.next();
        UserInfo playerdata_test = (UserInfo)pair.getValue();
        if (playerdata_test.player_name.equalsIgnoreCase(p.getName()))
        {
          if (Bukkit.getOnlineMode())
          {
            playerdata = playerdata_test;
            break;
          }
          if (playerdata_test.ip_address.equalsIgnoreCase(p.getAddress().getAddress().getHostAddress()))
          {
            playerdata = playerdata_test;
            break;
          }
        }
      }
    }
    if (playerdata == null)
    {
      playerdata = new UserInfo(p);
      userinfo.put(p, playerdata);
    }
    return playerdata;
  }
  
  public String getIpAddress()
  {
    return this.ip_address;
  }
  
  public String getPlayerName()
  {
    return this.player_name;
  }
  
  public boolean isOrbiting()
  {
    return this.is_orbiting;
  }
  
  public void startOrbiting(double orbit_strength)
  {
    this.is_orbiting = true;
    this.orbit_strength = orbit_strength;
  }
  
  public void stopOrbiting()
  {
    this.is_orbiting = false;
  }
  
  public double orbitStrength()
  {
    return this.orbit_strength;
  }
  
  public void setCaged(boolean state)
  {
    this.user_caged = state;
  }
  
  public void setCaged(boolean state, Location location, Material material_outer, Material material_inner)
  {
    this.user_caged = state;
    this.user_cage_pos = location;
    this.cage_material_outer = material_outer;
    this.cage_material_inner = material_inner;
  }
  
  public boolean isCaged()
  {
    return this.user_caged;
  }
  
  public static enum CageLayer
  {
    INNER,  OUTER;
    
    private CageLayer() {}
  }
  
  public Material getCageMaterial(CageLayer layer)
  {
    switch (1.$SwitchMap$me$RetroCoders$DarkFreedom$UserInfo$CageLayer[layer.ordinal()])
    {
    case 1: 
      return this.cage_material_outer;
    case 2: 
      return this.cage_material_inner;
    }
    return this.cage_material_outer;
  }
  
  public Location getCagePos()
  {
    return this.user_cage_pos;
  }
  
  public void clearHistory()
  {
    this.user_cage_history.clear();
  }
  
  public void insertHistoryBlock(Location location, Material material)
  {
    this.user_cage_history.add(new TFM_BlockData(location, material));
  }
  
  public void regenerateHistory()
  {
    for (TFM_BlockData blockdata : this.user_cage_history) {
      blockdata.location.getBlock().setType(blockdata.material);
    }
  }
  
  class TFM_BlockData
  {
    public Material material;
    public Location location;
    
    public TFM_BlockData(Location location, Material material)
    {
      this.location = location;
      this.material = material;
    }
  }
  
  public boolean isFrozen()
  {
    return this.user_frozen;
  }
  
  public void setFrozen(boolean fr)
  {
    this.user_frozen = fr;
  }
  
  public void resetMsgCount()
  {
    this.msg_count = 0;
  }
  
  public void incrementMsgCount()
  {
    this.msg_count += 1;
  }
  
  public int getMsgCount()
  {
    return this.msg_count;
  }
  
  public void incrementBlockDestroyCount()
  {
    this.block_destroy_total += 1;
  }
  
  public int getBlockDestroyCount()
  {
    return this.block_destroy_total;
  }
  
  public void resetBlockDestroyCount()
  {
    this.block_destroy_total = 0;
  }
  
  public void incrementBlockPlaceCount()
  {
    this.block_place_total += 1;
  }
  
  public int getBlockPlaceCount()
  {
    return this.block_place_total;
  }
  
  public void resetBlockPlaceCount()
  {
    this.block_place_total = 0;
  }
  
  public void incrementFreecamDestroyCount()
  {
    this.freecam_destroy_count += 1;
  }
  
  public int getFreecamDestroyCount()
  {
    return this.freecam_destroy_count;
  }
  
  public void resetFreecamDestroyCount()
  {
    this.freecam_destroy_count = 0;
  }
  
  public void incrementFreecamPlaceCount()
  {
    this.freecam_place_count += 1;
  }
  
  public int getFreecamPlaceCount()
  {
    return this.freecam_place_count;
  }
  
  public void resetFreecamPlaceCount()
  {
    this.freecam_place_count = 0;
  }
  
  public void enableMobThrower(EntityType mob_thrower_creature, double mob_thrower_speed)
  {
    this.mob_thrower_enabled = true;
    this.mob_thrower_creature = mob_thrower_creature;
    this.mob_thrower_speed = mob_thrower_speed;
  }
  
  public void disableMobThrower()
  {
    this.mob_thrower_enabled = false;
  }
  
  public EntityType mobThrowerCreature()
  {
    return this.mob_thrower_creature;
  }
  
  public double mobThrowerSpeed()
  {
    return this.mob_thrower_speed;
  }
  
  public boolean mobThrowerEnabled()
  {
    return this.mob_thrower_enabled;
  }
  
  public void enqueueMob(LivingEntity mob)
  {
    this.mob_thrower_queue.add(mob);
    if (this.mob_thrower_queue.size() > 4)
    {
      LivingEntity oldmob = (LivingEntity)this.mob_thrower_queue.remove(0);
      if (oldmob != null) {
        oldmob.damage(500);
      }
    }
  }
  
  public void startArrowShooter(AvalancheFreedom plugin)
  {
    stopArrowShooter();
    this.mp44_schedule_id = plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new ArrowShooter(this.player), 1L, 1L);
    this.mp44_firing = true;
  }
  
  public void stopArrowShooter()
  {
    if (this.mp44_schedule_id != null)
    {
      this.mp44_schedule_id.cancel();
      this.mp44_schedule_id = null;
    }
    this.mp44_firing = false;
  }
  
  class ArrowShooter
    implements Runnable
  {
    private Player _player;
    
    public ArrowShooter(Player player)
    {
      this._player = player;
    }
    
    public void run()
    {
      Arrow shot_arrow = (Arrow)this._player.launchProjectile(Arrow.class);
      shot_arrow.setVelocity(shot_arrow.getVelocity().multiply(2.0D));
    }
  }
  
  public void armMP44()
  {
    this.mp44_armed = true;
    stopArrowShooter();
  }
  
  public void disarmMP44()
  {
    this.mp44_armed = false;
    stopArrowShooter();
  }
  
  public boolean isMP44Armed()
  {
    return this.mp44_armed;
  }
  
  public boolean toggleMP44Firing()
  {
    this.mp44_firing = (!this.mp44_firing);
    return this.mp44_firing;
  }
  
  public boolean isMuted()
  {
    return this.is_muted;
  }
  
  public void setMuted(boolean is_muted)
  {
    this.is_muted = is_muted;
  }
  
  public boolean isHalted()
  {
    return this.is_halted;
  }
  
  public void setHalted(boolean is_halted)
  {
    this.is_halted = is_halted;
  }
  
  public BukkitTask getLockupScheduleID()
  {
    return this.lockup_schedule_id;
  }
  
  public void setLockupScheduleID(BukkitTask lockup_schedule_id)
  {
    this.lockup_schedule_id = lockup_schedule_id;
  }
  
  public void setLastMessage(String last_message)
  {
    this.last_message = last_message;
  }
  
  public String getLastMessage()
  {
    return this.last_message;
  }
  
  public void setAdminChat(boolean in_adminchat)
  {
    this.in_adminchat = in_adminchat;
  }
  
  public boolean inAdminChat()
  {
    return this.in_adminchat;
  }
  
  public boolean allCommandsBlocked()
  {
    return this.all_commands_blocked;
  }
  
  public void setCommandsBlocked(boolean commands_blocked)
  {
    this.all_commands_blocked = commands_blocked;
  }
  
  public Boolean isSuperadminIdVerified()
  {
    return this.superadmin_id_verified;
  }
  
  public void setSuperadminIdVerified(Boolean superadmin_id_verified)
  {
    this.superadmin_id_verified = superadmin_id_verified;
  }
  
  public String getLastCommand()
  {
    return this.last_command;
  }
  
  public void setLastCommand(String last_command)
  {
    this.last_command = last_command;
  }
}
