package top.oasismc.modules.customevent.handler;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Giant;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;
import top.oasismc.modules.customevent.events.AsyncDateStartEvent;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import static top.oasismc.OasisEss.bcByKey;
import static top.oasismc.OasisEss.getPlugin;

public class CustomEventListener implements Listener {

    private final Random random;
    private static final CustomEventListener LISTENER;

    static {
        LISTENER = new CustomEventListener();
    }

    private final Map<Integer, Boolean> eventSwitchMap;

    public static CustomEventListener getListener() {
        return LISTENER;
    }
    private CustomEventListener() {
        random = new Random();
        eventSwitchMap = new ConcurrentHashMap<>();
    }

    @EventHandler
    public void onDateStart(AsyncDateStartEvent event) {
        eventSwitchMap.put(1, false);
        eventSwitchMap.put(2, false);
        eventSwitchMap.put(3, false);
        eventSwitchMap.put(4, false);
        eventSwitchMap.put(5, false);
        int eventId = random.nextInt(21);
        switch (eventId) {
            case 0 -> {
                bcByKey("event.spawnGiantEvent");
                eventSwitchMap.put(1, true);
            }
            case 5 -> event2();
            case 10 -> event3();
            case 15 -> event4();
            case 20 -> event5();
            default -> bcByKey("event.dateStart");
        }
    }

    @EventHandler
    public void randomSpawnGiant(EntitySpawnEvent event) {
        if (!eventSwitchMap.getOrDefault(1, false)) {
            return;
        }
        if (event.getLocation().getBlock().getLightFromSky() == 15) {
            if (event.getEntity().getType() == EntityType.ZOMBIE) {
                if (random.nextInt(100) < getPlugin().getConfig().getInt("modules.dateStartEvent.spawnGiantEvent.probability", 1)) {
                    event.setCancelled(true);
                    event.getLocation().getWorld().spawnEntity(event.getLocation(), EntityType.GIANT);
                }
            }
        }
    }

    @EventHandler
    public void onGiantDeath(EntityDeathEvent event) {
        if (event.getEntity().getType() != EntityType.GIANT)
            return;
        event.getDrops().add(new ItemStack(Material.ROTTEN_FLESH, random.nextInt(10, 21)));
        event.getDrops().add(new ItemStack(Material.ZOMBIE_HEAD, random.nextInt(1, 4)));
        if (random.nextInt(0, 100) < 5) {
            event.getDrops().add(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 1));
        }
    }

    private void event2() {
        //TODO
    }
    private void event3() {
        //TODO
    }
    private void event4() {
        //TODO
    }
    private void event5() {
        //TODO
    }

}
