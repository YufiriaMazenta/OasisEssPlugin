package top.oasismc.modules.utils.ignite;

import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.type.TNT;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import static top.oasismc.OasisEss.getPlugin;

public class IgniteListener implements Listener {

    private static final IgniteListener instance;

    static {
        instance = new IgniteListener();
    }

    private IgniteListener() {}

    public static IgniteListener getInstance() {
        return instance;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void ignite(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!event.hasItem()) {
            return;
        }
        ItemStack item = event.getItem();
        try {
            if (item.getItemMeta().getEnchantLevel(Enchantment.FIRE_ASPECT) == 2) {
                switch (event.getClickedBlock().getType()) {
                    case CAMPFIRE:
                    case SOUL_CAMPFIRE:
                        Lightable blockData = (Lightable) (event.getClickedBlock().getBlockData());
                        if (!blockData.isLit()) {
                            blockData.setLit(true);
                            event.getClickedBlock().setBlockData(blockData);
                            player.playSound(event.getClickedBlock().getLocation(), getPlugin().getConfig().getString("sounds.ignite_campfire", "entity.generic.burn"), 1, 1);
                        }
                        break;
                    case TNT:
                        TNT tnt = (TNT) (event.getClickedBlock().getBlockData());
                        if (!tnt.isUnstable()) {
                            tnt.setUnstable(true);
                            event.getClickedBlock().setBlockData(tnt);
                        }
                        break;
                }
            }
        } catch (NullPointerException ignore) {

        }

    }

}
