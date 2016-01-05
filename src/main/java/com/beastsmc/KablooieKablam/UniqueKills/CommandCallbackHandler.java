package com.beastsmc.KablooieKablam.UniqueKills;

import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;

public class CommandCallbackHandler extends BukkitRunnable {
    private final List<CommandInfo> pending;

    public CommandCallbackHandler() {

        pending = Collections.synchronizedList(new LinkedList<>());
    }
    public void addPending(CommandSender sender, Future value,  BiConsumer<CommandSender, Future> callback) {
        CommandInfo commandInfo = new CommandInfo(sender, value, callback);
        pending.add(commandInfo);
    }

    @Override
    public void run() {
        Iterator<CommandInfo> pendingIterator = pending.iterator();
        while(pendingIterator.hasNext()) {
            CommandInfo commandInfo = pendingIterator.next();
            if(commandInfo.value.isDone()) {
                pendingIterator.remove();
                commandInfo.callback.accept(commandInfo.sender, commandInfo.value);
            }
        }
    }

    protected class CommandInfo {
        CommandSender sender;
        Future value;
        BiConsumer<CommandSender, Future> callback;

        protected CommandInfo(CommandSender sender, Future value, BiConsumer<CommandSender, Future> callback) {
            this.sender = sender;
            this.value = value;
            this.callback = callback;
        }
    }
}
