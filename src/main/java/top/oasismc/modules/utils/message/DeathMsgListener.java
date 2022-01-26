package top.oasismc.modules.utils.message;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
        String deathMsg = OasisEss.getTextConfig().getConfig().getString("playerDeath", "");
        deathMsg = deathMsg.replace("%killer%", p.getEntity().getName()).replace("%player%", p.getEntity().getName());
        p.setDeathMessage(color(deathMsg));
    }
}
