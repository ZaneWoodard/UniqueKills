package com.beastsmc.KablooieKablam.UniqueKills;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.avaje.ebean.ExpressionList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UKillsCommandExecutor
        implements CommandExecutor
{
    private final UniqueKills plugin;

    public UKillsCommandExecutor(UniqueKills plugin)
    {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            int kills = 0;
            if(sender instanceof Player) {
                ExpressionList<Kill> query = this.plugin.getDatabase()
                                                   .find(Kill.class)
                                                   .where().eq("killerUUID", ((Player) sender).getUniqueId());
                String generatedSql = query.query().getGeneratedSql();

                //TODO remove this debug code
                plugin.getLogger().info(generatedSql);

                Future killsFuture = query.findFutureRowCount();
                plugin.commandCallbackHandler.addPending(sender, killsFuture, this::executeSelfCallback);
            } else {
                sender.sendMessage(ChatColor.RED + "Cannot execute lookup on non-player!");
            }
            return true;
        }

        if (args.length == 1) {
            if (sender.hasPermission("uniquekills.lookup.other")) {
                String killer = args[0];
                OfflinePlayer target = Bukkit.getOfflinePlayer(killer);
                if(target.hasPlayedBefore()) {
                    ExpressionList<Kill> query = this.plugin.getDatabase()
                            .find(Kill.class)
                            .where().eq("killerUUID", target.getUniqueId());
                    String generatedSql = query.query().getGeneratedSql();

                    //TODO remove this debug code
                    plugin.getLogger().info(generatedSql);

                    Future killsFuture = query.findFutureRowCount();
                    plugin.commandCallbackHandler.addPending(sender, killsFuture, this::executeOtherCallback);
                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED + killer + " is not in our records!");
                    return false;
                }

            }
            sender.sendMessage(ChatColor.RED + "You do not have permission to lookup others!");
            return true;
        }

        sender.sendMessage(ChatColor.RED + "Use /ukills");
        return true;
    }

    public void executeOtherCallback(CommandSender sender, Future<Integer> value) {
        int kills = 0;
        try {
            kills = value.get();
        } catch (ExecutionException | InterruptedException e) {
            sender.sendMessage(ChatColor.RED + "Your Kill Score could not be loaded, please report this error.");
            e.printStackTrace();
            return;
        }

        sender.sendMessage(ChatColor.RED + "Player's Kill Score is " + ChatColor.DARK_RED + kills + ChatColor.RED + ".");

    }
    public void executeSelfCallback(CommandSender sender, Future<Integer> value) {
        int kills = 0;
        try {
            kills = value.get();
        } catch (ExecutionException | InterruptedException e) {
            sender.sendMessage(ChatColor.RED + "Your kill score could not be loaded! Please report this error.");
            e.printStackTrace();
            return;
        }

        sender.sendMessage(ChatColor.RED + "Your kill score is " + ChatColor.DARK_RED + kills + ChatColor.RED + ".");
        if ((kills >= 50) && (!sender.hasPermission("ranks.warrior"))) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + sender.getName() + " group add Warrior");
            sender.sendMessage(ChatColor.RED + "Congratulations! You were just promoted to " + ChatColor.DARK_RED + "Warrior" + ChatColor.RED + ".");
            if ((sender.hasPermission("ranks.leader")) && (sender.hasPermission("ranks.architect")) && (sender.hasPermission("ranks.merchant"))) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + sender.getName() + " group add Adventurer");
                sender.sendMessage(ChatColor.AQUA + "Congratulations! You were just promoted to " + ChatColor.GRAY + "Adventurer" + ChatColor.AQUA + ".");
            }
        }
        if ((kills >= 150) && (!sender.hasPermission("ranks.conqueror"))) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + sender.getName() + " group add Conqueror");
            sender.sendMessage(ChatColor.RED + "Congratulations! You were just promoted to " + ChatColor.DARK_RED + "Conqueror" + ChatColor.RED + ".");
            if ((sender.hasPermission("ranks.pioneer")) && (sender.hasPermission("ranks.artisan")) && (sender.hasPermission("ranks.tycoon"))) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + sender.getName() + " group add Legend");
                sender.sendMessage(ChatColor.DARK_PURPLE + "Congratulations! You were just promoted to " + ChatColor.WHITE + "Legend" + ChatColor.DARK_PURPLE + ".");
            }
        }
    }
}