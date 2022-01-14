package top.oasismc.modules.utils.keepinventory;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;

public class KeepInventoryListener implements Listener {

    private static final KeepInventoryListener instance;

    static {
        instance = new KeepInventoryListener();
    }

    private KeepInventoryListener() {}

    public static KeepInventoryListener getInstance() {
        return instance;
    }

    //使得玩家死亡不掉落
    @EventHandler
    public void playerDeath(PlayerDeathEvent p) {
        if (p.getEntity().hasPermission("oasis.perm.keep")) {
            p.setKeepInventory(true);
            p.setKeepLevel(true);
            p.setDroppedExp(0);
            p.getDrops().clear();
        }
    }

    //降低开启死亡不掉落玩家的经验获取为初始的一半
    @EventHandler
    public void expAdd(PlayerExpChangeEvent p) {
        if (p.getPlayer().hasPermission("oasis.perm.keep")) {
            if (p.getAmount() > 0) {
                p.setAmount(p.getAmount() / 2);
            }
        }
    }


}
