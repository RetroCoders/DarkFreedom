package me.RetroCoders.DarkFreedom;

import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;

public class SuperAdmins
{
  public static boolean isSeniorAdmin(CommandSender user)
  {
    try
    {
      if (Bukkit.getServer().getPluginManager().isPluginEnabled("TotalFreedomMod")) {
        return TFM_AdminList.isSeniorAdmin(user);
      }
    }
    catch (Exception ex) {}
    return false;
  }
  
  public static boolean isUserSuperadmin(CommandSender user)
  {
    try
    {
      if (Bukkit.getServer().getPluginManager().isPluginEnabled("TotalFreedomMod")) {
        return TFM_AdminList.isSuperAdmin(user);
      }
    }
    catch (Exception ex) {}
    return false;
  }
}
