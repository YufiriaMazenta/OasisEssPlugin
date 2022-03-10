package top.oasismc.modules.mob;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;

public class ExplodeListener implements Listener {

    @EventHandler
    public void onCreeperExplosion(ExplosionPrimeEvent event) {
        if (event.getEntityType() != EntityType.CREEPER) {
            return;
        }
        Creeper creeper = (Creeper) event.getEntity();
        int power = 3;
        if (creeper.isPowered())
            power *= 2;
        double scale = creeper.getHealth() / 20;
        float finalRadius = (float) (power * scale);
        event.setRadius(finalRadius);
    }

}
