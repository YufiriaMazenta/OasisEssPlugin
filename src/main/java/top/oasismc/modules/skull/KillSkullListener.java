package top.oasismc.modules.skull;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class KillSkullListener implements Listener {

    private static final KillSkullListener listener;

    static {
        listener = new KillSkullListener();
    }

    private KillSkullListener() {}

    public static KillSkullListener getListener() {
        return listener;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDead(PlayerDeathEvent event) {
        if (event.getEntity().getKiller() == null)
            return;
        Player player = event.getEntity().getKiller();
        ItemStack item = player.getInventory().getItem(EquipmentSlot.HAND);
        if (!item.hasItemMeta())
            return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null)
            return;
        if (meta.getEnchantLevel(Enchantment.DAMAGE_ALL) < 5)
            return;
        if (meta.getEnchantLevel(Enchantment.LOOT_BONUS_MOBS) < 3)
            return;
        if (meta.getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS) < 3)
            return;
        if (Math.random() < 0.15) {
            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            ItemMeta headMeta = head.getItemMeta();
            if (headMeta != null) {
                ((SkullMeta) headMeta).setOwningPlayer(event.getEntity());
            }
            head.setItemMeta(headMeta);
            event.getDrops().add(head);
        }
    }

}
