package top.oasismc.modules.utils.message;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import top.oasismc.OasisEss;

import static top.oasismc.OasisEss.color;

public class DeathMsgListener implements Listener {

    private DeathMsgListener() {}

    private final static DeathMsgListener listener;

    static {
        listener = new DeathMsgListener();
    }

    public static DeathMsgListener getListener() {
        return listener;
    }

    @EventHandler
    public void playerDeath(PlayerDeathEvent p) {
        String deathMsg = p.getDeathMessage();
    }
}
