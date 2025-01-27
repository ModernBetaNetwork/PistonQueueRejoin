package org.modernbeta;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class PistonQueueRejoin extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        String player = event.getPlayer().getName();
        String commandRemove = "lp user " + player + " permission unsettemp queue.priority";
        String command = "lp user " + player + " permission settemp queue.priority true 2min";
        getServer().dispatchCommand(getServer().getConsoleSender(), commandRemove);
        getServer().dispatchCommand(getServer().getConsoleSender(), command);
    }

}
