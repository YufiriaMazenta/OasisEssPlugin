package top.oasismc.modules.utils.drop;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class IronGolemDeathListener implements Listener {

    private static IronGolemDeathListener listener = new IronGolemDeathListener();

    private IronGolemDeathListener() {}

    public static IronGolemDeathListener getListener() {
        return listener;
    }

    @EventHandler
    public void onIronGolemDeath(EntityDeathEvent event) {
        if (event.getEntityType().equals(EntityType.IRON_GOLEM)) {
            for (ItemStack drop : event.getDrops()) {
                drop.setAmount(drop.getAmount() * 4);
            }
            event.setDroppedExp(event.getDroppedExp() * 4);
        }
    }

}
