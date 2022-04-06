package top.oasismc.modules.utils;

import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;

public class LiftMobListener implements Listener {

    private static final LiftMobListener listener = new LiftMobListener();

    public static LiftMobListener getListener() {
        return listener;
    }

    private LiftMobListener() {}

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getHand().equals(EquipmentSlot.OFF_HAND))
            return;
        Player player = event.getPlayer();
        if (!(event.getRightClicked() instanceof Mob))
            return;
        if (event.getRightClicked() instanceof Villager villager) {
            if (!villager.getProfession().equals(Villager.Profession.NONE))
                return;
        }
        if (player.isSneaking())
            player.addPassenger(event.getRightClicked());
    }

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        if (!event.isSneaking())
            return;
        if (event.getPlayer().getPassengers().size() < 1)
            return;
        event.getPlayer().removePassenger(event.getPlayer().getPassengers().get(0));
    }

}
