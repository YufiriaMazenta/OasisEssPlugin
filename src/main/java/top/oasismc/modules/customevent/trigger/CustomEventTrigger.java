package top.oasismc.modules.customevent.trigger;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.TimeSkipEvent;
import org.bukkit.scheduler.BukkitRunnable;
import top.oasismc.modules.customevent.events.AsyncDateStartEvent;

import static top.oasismc.OasisEss.getPlugin;

public class CustomEventTrigger implements Listener {

    private static CustomEventTrigger trigger;

    static {
        trigger = new CustomEventTrigger();
    }

    public static CustomEventTrigger getTrigger() {
        return trigger;
    }

    private CustomEventTrigger() {
        startDateStartEventTrigger();
    }

    @EventHandler
    public void timeSkip(TimeSkipEvent event) {
        if (event.getSkipReason() == TimeSkipEvent.SkipReason.NIGHT_SKIP) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Bukkit.getPluginManager().callEvent(new AsyncDateStartEvent(event.getWorld()));
                }
            }.runTaskAsynchronously(getPlugin());
        }
    }

    private void startDateStartEventTrigger() {
        new BukkitRunnable() {
            @Override
            public void run() {
                World world = Bukkit.getServer().getWorld("world");
                if (world.getTime() == 0) {
                    Bukkit.getPluginManager().callEvent(new AsyncDateStartEvent(world));
                }
            }
        }.runTaskTimerAsynchronously(getPlugin(), 1, 0);
    }

}
