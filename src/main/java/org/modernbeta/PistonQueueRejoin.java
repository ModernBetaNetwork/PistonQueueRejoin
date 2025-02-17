package org.modernbeta;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.data.TemporaryNodeMergeStrategy;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

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
        Player player = event.getPlayer();
        UserManager userManager = luckPerms.getUserManager();
        CompletableFuture<User> userFuture = userManager.loadUser(event.getPlayer().getUniqueId());
        Node node = Node.builder("queue.priority")
                .expiry(2, TimeUnit.MINUTES)
                .build();

        getLogger().info("Giving " + player.getName()  + " temporary queue priority");
        userFuture.thenAcceptAsync(user -> {
            user.data().add(node,
                TemporaryNodeMergeStrategy.REPLACE_EXISTING_IF_DURATION_LONGER);
            userManager.saveUser(user);
        });
    }

}
