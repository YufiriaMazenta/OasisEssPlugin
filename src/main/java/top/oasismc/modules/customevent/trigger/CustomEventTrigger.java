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

    private static final CustomEventTrigger trigger;
    private static long lastTrigTime;

    static {
        trigger = new CustomEventTrigger();
        lastTrigTime = System.currentTimeMillis();
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
            if (System.currentTimeMillis() - lastTrigTime < 10000) {
                return;
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    Bukkit.getPluginManager().callEvent(new AsyncDateStartEvent(event.getWorld()));
                    lastTrigTime = System.currentTimeMillis();
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
                    if (System.currentTimeMillis() - lastTrigTime < 10000) {
                        return;
                    }
                    Bukkit.getPluginManager().callEvent(new AsyncDateStartEvent(world));
                    lastTrigTime = System.currentTimeMillis();
                }
            }
        }.runTaskTimerAsynchronously(getPlugin(), 1, 0);
    }

}
