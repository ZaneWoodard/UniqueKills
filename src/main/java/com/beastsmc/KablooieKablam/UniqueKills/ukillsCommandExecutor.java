package com.beastsmc.KablooieKablam.UniqueKills;

import java.sql.SQLException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ukillsCommandExecutor
  implements CommandExecutor
{
  private final UniqueKills plugin;

  public ukillsCommandExecutor(UniqueKills plugin)
  {
    this.plugin = plugin;
  }

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (args.length == 0) {
      int kills = 0;
      try {
        kills = this.plugin.mysql.getPlayerKills(sender.getName());
      } catch (SQLException e) {
        e.printStackTrace();
      }
      sender.sendMessage(ChatColor.RED + "Your kill score is " + ChatColor.DARK_RED + kills + ChatColor.RED + ".");
      if ((!sender.hasPermission("ranks.warrior")) && (kills >= 50)) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + sender.getName() + " group add Warrior");
        sender.sendMessage(ChatColor.RED + "Congratulations! You were just promoted to " + ChatColor.DARK_RED + "Warrior" + ChatColor.RED + ".");
        if ((sender.hasPermission("ranks.leader")) && (sender.hasPermission("ranks.architect")) && (sender.hasPermission("ranks.merchant"))) {
          Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + sender.getName() + " group add Adventurer");
          sender.sendMessage(ChatColor.AQUA + "Congratulations! You were just promoted to " + ChatColor.GRAY + "Adventurer" + ChatColor.AQUA + ".");
        }
      }
      if ((!sender.hasPermission("ranks.conqueror")) && (kills >= 150)) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + sender.getName() + " group add Conqueror");
        sender.sendMessage(ChatColor.RED + "Congratulations! You were just promoted to " + ChatColor.DARK_RED + "Conqueror" + ChatColor.RED + ".");
        if ((sender.hasPermission("ranks.pioneer")) && (sender.hasPermission("ranks.artisan")) && (sender.hasPermission("ranks.tycoon"))) {
          Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + sender.getName() + " group add Legend");
          sender.sendMessage(ChatColor.DARK_PURPLE + "Congratulations! You were just promoted to " + ChatColor.WHITE + "Legend" + ChatColor.DARK_PURPLE + ".");
        }
      }
      return true;
    }

    if (args.length == 1) {
      if (sender.hasPermission("uniquekills.lookup.other")) {
        String killer = args[0];
        int kills = 0;
        try {
          kills = this.plugin.mysql.getPlayerKills(killer);
        } catch (SQLException e) {
          e.printStackTrace();
        }
        sender.sendMessage(ChatColor.RED + killer + "'s Kill Score is " + ChatColor.DARK_RED + kills + ChatColor.RED + ".");
        return true;
      }
      sender.sendMessage(ChatColor.RED + "You do not have permission to lookup others!");
      return true;
    }

    sender.sendMessage(ChatColor.RED + "Use /ukills");
    return true;
  }
}

/* Location:           /Users/zane/KillScore.jar
 * Qualified Name:     com.beastsmc.KablooieKablam.UniqueKills.ukillsCommandExecutor
 * JD-Core Version:    0.6.2
 */