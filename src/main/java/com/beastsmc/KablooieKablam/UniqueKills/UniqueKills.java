package com.beastsmc.KablooieKablam.UniqueKills;

import java.sql.SQLException;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class UniqueKills extends JavaPlugin
  implements Listener
{
  public MySQLHandler mysql;

  public void onEnable()
  {
    saveDefaultConfig();
    try {
      this.mysql = new MySQLHandler(
        getConfig().getString("mysql.database"), 
        getConfig().getString("mysql.host"), 
        getConfig().getString("mysql.port"), 
        getConfig().getString("mysql.username"), 
        getConfig().getString("mysql.password"));
    }
    catch (SQLException e) {
      e.printStackTrace();
      Bukkit.getPluginManager().disablePlugin(this);
    }
    getCommand("ks").setExecutor(new UKillsCommandExecutor(this));
  }
}

/* Location:           /Users/zane/KillScore.jar
 * Qualified Name:     com.beastsmc.KablooieKablam.UniqueKills.UniqueKills
 * JD-Core Version:    0.6.2
 */