package top.oasismc.modules.utils;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.enchantments.Enchantment.DAMAGE_ARTHROPODS;

public class CreeperDamagedListener implements Listener {

    private static final CreeperDamagedListener LISTENER = new CreeperDamagedListener();

    private CreeperDamagedListener() {}

    public static CreeperDamagedListener getListener() {
        return LISTENER;
    }

    @EventHandler
    public void onCreeperDamaged(EntityDamageByEntityEvent event) {
        if (!event.getEntityType().equals(EntityType.CREEPER))
            return;
        if (!(event.getDamager() instanceof Player player))
            return;
        ItemStack item = player.getInventory().getItem(EquipmentSlot.HAND);
        if (!item.hasItemMeta())
            return;
        if (item.getEnchantmentLevel(DAMAGE_ARTHROPODS) != 0)
            event.setDamage(event.getDamage() + 2.5 * item.getEnchantmentLevel(DAMAGE_ARTHROPODS));
    }

}
