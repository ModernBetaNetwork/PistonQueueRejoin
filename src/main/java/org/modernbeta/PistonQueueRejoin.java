package org.modernbeta;

import net.luckperms.api.LuckPerms;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class PistonQueueRejoin extends JavaPlugin implements Listener {
    private LuckPerms luckPerms;

    @Override
    public void onEnable() {
        RegisteredServiceProvider<LuckPerms> provider =
            getServer().getServicesManager().getRegistration(LuckPerms.class);
        if (provider == null) {
            getLogger().severe("Couldn't get LuckPerms API provider!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        } else
            luckPerms = provider.getProvider();

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
