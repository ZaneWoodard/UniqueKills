package com.beastsmc.KablooieKablam.UniqueKills;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import javax.persistence.PersistenceException;

public class UniqueKills extends JavaPlugin implements Listener {

    public CommandCallbackHandler commandCallbackHandler;
    public MySQLHandler mysql;

    public void onEnable()
    {
        commandCallbackHandler = new CommandCallbackHandler();
        saveDefaultConfig();
        setupDatabase();
        getCommand("ks").setExecutor(new UKillsCommandExecutor(this));
    }

    private void setupDatabase() {
        try {
            getDatabase().find(Kill.class).findRowCount();
        } catch (PersistenceException ex) {
            getLogger().info("Installing database for " + getDescription().getName() + " due to first time usage");
            installDDL();
        }
    }
    @Override
    public List<Class<?>> getDatabaseClasses() {
        List<Class<?>> list = new ArrayList<>();
        list.add(Kill.class);
        return list;
    }
}