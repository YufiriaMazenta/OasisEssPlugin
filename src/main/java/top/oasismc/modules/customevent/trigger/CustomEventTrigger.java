package top.oasismc.modules.customevent.trigger;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import top.oasismc.modules.customevent.events.AsyncDateStartEvent;

import static top.oasismc.OasisEss.getPlugin;

public class CustomEventTrigger implements Listener {

    private static final CustomEventTrigger trigger;

    static {
        trigger = new CustomEventTrigger();
    }

    public static CustomEventTrigger getTrigger() {
        return trigger;
    }

    private CustomEventTrigger() {
        startDateStartEventTrigger();
    }

    private void startDateStartEventTrigger() {
        new BukkitRunnable() {
            @Override
            public void run() {
                World world = Bukkit.getServer().getWorld("world");
                if (world.getTime() == 5) {
                    Bukkit.getPluginManager().callEvent(new AsyncDateStartEvent(world));
                }
            }
        }.runTaskTimerAsynchronously(getPlugin(), 1, 1);
    }

}
