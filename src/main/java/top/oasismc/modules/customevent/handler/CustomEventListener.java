package top.oasismc.modules.customevent.handler;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import top.oasismc.modules.customevent.events.DateStartEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static top.oasismc.OasisEss.*;

public class CustomEventListener implements Listener {

    private final Random random;
    private static final CustomEventListener LISTENER;
    private final Map<UUID, BossBar> bossBarMap;
    private BukkitRunnable giantBossBarThread;

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
        bossBarMap = new HashMap<>();
    }

    @EventHandler
    public void onDateStart(DateStartEvent event) {
        eventSwitchMap.put(1, false);
        if (giantBossBarThread != null) {
            giantBossBarThread.cancel();
            giantBossBarThread = null;
        }
        eventSwitchMap.put(2, false);
        eventSwitchMap.put(3, false);
        eventSwitchMap.put(4, false);
        eventSwitchMap.put(5, false);
        int eventId = random.nextInt(21);
        switch (eventId) {
            case 0 -> {
                bcByKey("event.spawnGiantEvent");
                eventSwitchMap.put(1, true);
                startGiantBossBarThread();
                return;
            }
            case 5 -> event2();
            case 10 -> event3();
            case 15 -> event4();
            case 20 -> event5();
        }
        bcByKey("event.dateStart");
    }

    @EventHandler
    public void randomSpawnGiant(EntitySpawnEvent event) {
        if (!eventSwitchMap.getOrDefault(1, false)) {
            return;
        }
        if (event.getLocation().getBlock().getLightFromSky() == 15) {
            if (event.getEntityType() == EntityType.ZOMBIE) {
                if (random.nextInt(100) < getPlugin().getConfig().getInt("modules.dateStartEvent.spawnGiantEvent.probability", 1)) {
                    event.setCancelled(true);
                    Entity entity = event.getLocation().getWorld().spawnEntity(event.getLocation(), EntityType.GIANT);
                    bossBarMap.put(entity.getUniqueId(), Bukkit.createBossBar(color("&4&l原始巨人"), BarColor.RED, BarStyle.SOLID, BarFlag.PLAY_BOSS_MUSIC));
                    entity.getNearbyEntities(50, 50, 50).forEach(e -> {
                        if (e instanceof Player) {
                            bossBarMap.get(entity.getUniqueId()).addPlayer((Player) e);
                        }
                    });
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

    @EventHandler(priority = EventPriority.HIGH)
    public void onGiantDamaged(EntityDamageEvent event) {
        if (event.getEntityType() != EntityType.GIANT)
            return;
        if (random.nextInt(101) < 10) {
            event.setCancelled(true);
            return;
        }
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
            for (Entity entity : event.getEntity().getNearbyEntities(20, 20, 20)) {
                ((LivingEntity) event.getEntity()).attack(entity);
            }
        } else if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
            event.setDamage(event.getDamage() / 2);
        }
    }

    private void startGiantBossBarThread() {
        giantBossBarThread = new BukkitRunnable() {
            @Override
            public void run() {
                bossBarMap.keySet().forEach(uuid -> {
                    Giant giant = (Giant) Bukkit.getEntity(uuid);
                    if (giant == null || giant.isDead()) {
                        bossBarMap.get(uuid).removeAll();
                        bossBarMap.remove(uuid);
                    } else {
                        double process = giant.getHealth() / giant.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                        bossBarMap.get(uuid).setProgress(process);
                        bossBarMap.get(uuid).removeAll();
                        giant.getNearbyEntities(50, 50, 50).forEach(e -> {
                            if (e instanceof Player) {

                                bossBarMap.get(uuid).addPlayer((Player) e);
                            }
                        });
                    }
                });
            }
        };
        giantBossBarThread.runTaskTimer(getPlugin(), 0, 1);
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
