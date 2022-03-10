package top.oasismc.modules.mob;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

import java.util.Locale;
import java.util.Random;

import static top.oasismc.OasisEss.*;

public class EliteMobListener implements Listener {

    private final static EliteMobListener listener;
    private Random random;

    static {listener = new EliteMobListener();}

    private EliteMobListener() { random = new Random(); }

    public static EliteMobListener getListener() {
        return listener;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void spawnEliteMob(EntitySpawnEvent event) {
        if (!(event.getEntity() instanceof Monster monster))
            return;
        if (Math.random() < getPlugin().getConfig().getDouble("modules.eliteMob.prop", 0.05)) {
            if (monster.getType() != EntityType.CREEPER) {
                monster.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 9999999, 3));
                monster.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 9999999, 0));
            }
            monster.addScoreboardTag("elite");
            monster.setGlowing(true);

            String name = getTextConfig().getConfig().getString("entity_name." + event.getEntity().getName(), event.getEntity().getName());;
            monster.setCustomName(color("&c&l精英" + name));
            monster.setCustomNameVisible(true);
            World w = monster.getWorld();
            w.spawnEntity(monster.getLocation(), EntityType.LIGHTNING);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEliteMobDamaged(EntityDamageByEntityEvent event) {
        if (!event.getEntity().getScoreboardTags().contains("elite"))
            return;
        switch (event.getEntityType()) {
            case ZOMBIE -> {
                if (Math.random() < 0.4) {
                    Projectile skull = ((ProjectileSource) event.getEntity()).launchProjectile(WitherSkull.class);
                    Vector vector = skull.getVelocity();
                    vector.multiply(5);
                }
            } case SKELETON -> {
                if (Math.random() < 0.25) {

                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEliteMobDamageEntity(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        if (damager instanceof Projectile) {
            ProjectileSource source = ((Projectile) event.getDamager()).getShooter();
            if (source instanceof Entity entity) {
                damager = entity;
            }
        } else {
            damager = event.getDamager();
        }
        if (damager.getScoreboardTags().contains("elite")) {
            event.setDamage(event.getDamage() * 2.5);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEliteMobDeath(EntityDeathEvent event) {
        if (event.getEntity().getScoreboardTags().contains("elite")) {
            if (Math.random() < 0.1) {
                String name = event.getEntityType().name().toUpperCase(Locale.ROOT);
                Material eggMaterial = Material.matchMaterial(name + "_SPAWN_EGG");
                if (eggMaterial == null)
                    return;
                ItemStack egg = new ItemStack(eggMaterial, 1);
                event.getDrops().add(egg);
            }
            if (event.getEntityType() != EntityType.CREEPER) {
                event.getDrops().add(new ItemStack(Material.TOTEM_OF_UNDYING, 1));
            }
            if (Math.random() < 0.5) {
                ItemStack luckPotion = new ItemStack(Material.POTION);
                PotionMeta meta = (PotionMeta) luckPotion.getItemMeta();
                meta.setBasePotionData(new PotionData(PotionType.LUCK));
                luckPotion.setItemMeta(meta);
                event.getDrops().add(luckPotion);
            }
            if (event.getEntity().getKiller() != null) {
                getPlugin().addAdvancement(event.getEntity().getKiller().getName(), "kill_elite_mob");
            }
        }
    }

}
