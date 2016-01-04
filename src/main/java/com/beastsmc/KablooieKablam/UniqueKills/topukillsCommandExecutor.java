package com.beastsmc.KablooieKablam.UniqueKills;

import java.sql.SQLException;
import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class topukillsCommandExecutor
  implements CommandExecutor
{
  private final UniqueKills plugin;

  public topukillsCommandExecutor(UniqueKills plugin)
  {
    this.plugin = plugin;
  }

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (args.length == 0)
    {
      ArrayList scores = new ArrayList();
      try {
        scores = this.plugin.mysql.getPlayerKills();
      } catch (SQLException e) {
        e.printStackTrace();
      }

      for (int x = 0; x < 10; x++) {
        PlayerScoreNode record = (PlayerScoreNode)scores.get(x);
        sender.sendMessage(ChatColor.RED + record.getName() + " has " + ChatColor.DARK_RED + record.getKills() + ChatColor.RED + " kills.");
      }

      return true;
    }

    sender.sendMessage("Use /topukills");
    return true;
  }
}

/* Location:           /Users/zane/KillScore.jar
 * Qualified Name:     com.beastsmc.KablooieKablam.UniqueKills.topukillsCommandExecutor
 * JD-Core Version:    0.6.2
 */