package top.oasismc.modules.utils.respawn;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
import top.oasismc.OasisEss;

public class AutoRespawn implements Listener {

    private static final AutoRespawn listener;

    static {
        listener = new AutoRespawn();
    }

    private AutoRespawn() {}

    public static AutoRespawn getListener() {return listener;}

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
                event.getEntity().spigot().respawn();
            }
        }.runTaskLater(OasisEss.getPlugin(), 1L);
    }

}
