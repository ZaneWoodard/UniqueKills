package com.beastsmc.KablooieKablam.UniqueKills;

import java.util.List;
import java.util.UUID;

import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TopUKillsCommandExecutor
        implements CommandExecutor
{
    private final UniqueKills plugin;

    public TopUKillsCommandExecutor(UniqueKills plugin)
    {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0)
        {
            SqlQuery topQuery = plugin.getDatabase().createSqlQuery(
                    "SELECT killerUUID, count(killerUUID) as kills " +
                    "FROM playerkills "                              +
                    "GROUP BY killerUUID "                           +
                    "ORDER BY kills DESC "                           +
                    "LIMIT 10;"
            );

            List<SqlRow> results = topQuery.findList();
            for(SqlRow row : results) {
                UUID killerUUID = row.getUUID("killerUUID");
                Integer score = row.getInteger("kills");

                OfflinePlayer killer = Bukkit.getOfflinePlayer(killerUUID);
                sender.sendMessage(ChatColor.RED + killer.getName() + " has " + ChatColor.DARK_RED + score + ChatColor.RED + " kills.");
            }
        } else {
            sender.sendMessage("Use /topukills");
        }
        return true;
    }
}