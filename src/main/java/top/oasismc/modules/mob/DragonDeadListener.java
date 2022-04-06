package top.oasismc.modules.mob;

import org.bukkit.Material;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class DragonDeadListener implements Listener {

    @EventHandler
    public void onDragonDead(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof EnderDragon dragon))
            return;
        if (dragon.getDragonBattle().hasBeenPreviouslyKilled()) {
            Player killer = dragon.getKiller();
            if (killer == null)
                return;
            killer.getInventory().addItem(new ItemStack(Material.DRAGON_EGG));
        }
    }

}
