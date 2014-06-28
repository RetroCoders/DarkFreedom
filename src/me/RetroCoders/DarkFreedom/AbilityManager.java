package me.RetroCoders.DarkFreedom;

import java.util.ArrayList;

public class AbilityManager
{
  public static ArrayList<String> strengthEnabledFor = new ArrayList();
  public static ArrayList<CarryPlayer> carryingPlayers = new ArrayList();
  
  public static boolean isStrengthEnabledFor(String s)
  {
    if (strengthEnabledFor.contains(s)) {
      return true;
    }
    return false;
  }
  
  public static void addBurriedPlayer(String s)
  {
    if (!DarkFreedom.burriedPlayers.contains(s)) {
      DarkFreedom.burriedPlayers.add(s);
    }
  }
  
  public static void removeBurriedPlayer(String s)
  {
    for (int i = 0; i < DarkFreedom.burriedPlayers.size(); i++) {
      if (((String)DarkFreedom.burriedPlayers.get(i)).equals(s)) {
        DarkFreedom.burriedPlayers.remove(i);
      }
    }
  }
  
  public static boolean isBurriedPlayer(String s)
  {
    if (DarkFreedom.burriedPlayers.contains(s)) {
      return true;
    }
    return false;
  }
}
