package top.oasismc.modules.combat;

import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import static top.oasismc.OasisEss.getPlugin;

public class ShieldListener implements Listener {

    Map<String, Integer> shieldBlockNumMap;

    private static final ShieldListener shieldListener;

    static {
        shieldListener = new ShieldListener();
    }

    private ShieldListener() {}

    public static ShieldListener getInstance() {
        return shieldListener;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void blocking(EntityDamageByEntityEvent event) {
        double blockingDamage = event.getDamage(EntityDamageEvent.DamageModifier.BLOCKING);
        if (blockingDamage == 0) {
            return;
        }
        if (!(event.getEntity() instanceof Player entity)) {
            return;
        }
        String sound = getPlugin().getConfig().getString("sounds.shield_block", "item.trident.hit_ground");
        int soundRadius = getPlugin().getConfig().getInt("sounds.soundRadius", 5);
        entity.getNearbyEntities(soundRadius, soundRadius, soundRadius).parallelStream().forEach(e->{
            if (e instanceof Player) {
                ((Player) e).playSound(entity.getLocation(), sound, 1, 0);
            }
        });
        entity.playSound(entity.getLocation(), sound, 1, 0);
        if (!entity.isSneaking() && !(event.getDamager() instanceof Projectile)) {
            double blockDamage = blockingDamage * getPlugin().getConfig().getDouble("modules.shield.prop", 0.75);
            event.setDamage(EntityDamageEvent.DamageModifier.BLOCKING, blockDamage);
            double resistanceDamage = - getResistanceDamage(event.getDamage() + blockDamage, entity);
            event.setDamage(EntityDamageEvent.DamageModifier.RESISTANCE, resistanceDamage);
            double armorDamage = - getArmorDamage(event.getDamage() + blockDamage + resistanceDamage, entity);
            event.setDamage(EntityDamageEvent.DamageModifier.ARMOR,  armorDamage);
            double protectEnchantDamage = - getEnchantDamage(event.getDamage() + blockDamage + resistanceDamage + armorDamage, entity);
            event.setDamage(EntityDamageEvent.DamageModifier.MAGIC, protectEnchantDamage);
            double absorptionDamage = - getAbsorptionDamage(event.getDamage() + blockDamage + resistanceDamage + armorDamage + protectEnchantDamage, entity);
            event.setDamage(EntityDamageEvent.DamageModifier.ABSORPTION, absorptionDamage);
        } else {
            if (event.getDamager() instanceof LivingEntity) {
                String effect = getPlugin().getConfig().getString("modules.shield.effect", "WEAKNESS");
                int effectTime = getPlugin().getConfig().getInt("modules.shield.effectTime", 2) * 20;
                int effectLevel = getPlugin().getConfig().getInt("modules.shield.effectLevel", 2) - 1;
                ((LivingEntity) event.getDamager()).addPotionEffect(new PotionEffect(PotionEffectType.getByName(effect), effectTime, effectLevel, false));
            }
        }
    }

//    @EventHandler(priority = EventPriority.HIGHEST)
//    public void test(EntityDamageByEntityEvent event) {
//        if (event.getEntity() instanceof Player player) {
//            sendMessage(player, "------------------");
//            sendMessage(player, "基础伤害:" + event.getDamage(EntityDamageEvent.DamageModifier.BASE));
//            sendMessage(player, "盾牌减免:" + event.getDamage(EntityDamageEvent.DamageModifier.BLOCKING));
//            sendMessage(player, "护甲减免:" + event.getDamage(EntityDamageEvent.DamageModifier.ARMOR));
//            sendMessage(player, "附魔减免:" + event.getDamage(EntityDamageEvent.DamageModifier.MAGIC));
//            sendMessage(player, "抗性减免:" + event.getDamage(EntityDamageEvent.DamageModifier.RESISTANCE));
//            sendMessage(player, "伤害吸收:" + event.getDamage(EntityDamageEvent.DamageModifier.ABSORPTION));
//            sendMessage(player, "最终伤害:" + event.getFinalDamage());
//        }
//    }

    private double getArmorDamage(double originalDamage, Player player) {
        double armor = Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_ARMOR)).getValue();
        double armor_touch = Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS)).getValue();
        return originalDamage * Math.min(20, Math.max(armor / 5, armor - (originalDamage / (2 + armor_touch / 4)))) / 25;
    }

    private double getEnchantDamage(double originalDamage, Player player) {
        int level = Arrays.stream(player.getInventory().getArmorContents()).toList().parallelStream().filter(Objects::nonNull).mapToInt(
                i -> i.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL)
        ).sum();
        return originalDamage * level * 0.04;
    }

    private double getResistanceDamage(double originalDamage, Player player) {
        int resistanceLevel = 0;
        if (player.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE) == null) {
            return 0;
        }
        resistanceLevel = player.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE).getAmplifier() + 1;
        return originalDamage * resistanceLevel * 0.2;
    }

    private double getAbsorptionDamage(double originalDamage, Player player) {
        double absorption = player.getAbsorptionAmount();
        return Math.min(originalDamage, absorption);
    }

}
