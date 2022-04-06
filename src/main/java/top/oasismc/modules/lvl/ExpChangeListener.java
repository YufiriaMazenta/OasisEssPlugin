package top.oasismc.modules.lvl;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import top.oasismc.OasisEss;

public class ExpChangeListener implements Listener {

    private static final ExpChangeListener listener = new ExpChangeListener();

    private ExpChangeListener() {}

    public static ExpChangeListener getListener() {return listener;}

    @EventHandler(priority = EventPriority.HIGH)
    public void onExpChange(PlayerExpChangeEvent event) {
        Player player = event.getPlayer();
        float old = player.getExp();
        int perLvl = OasisEss.getPlugin().getConfig().getInt("modules.customLvl.perLvl", 100);
        float add = (float) event.getAmount() / perLvl;
        player.setLevel(player.getLevel() + (int)(old + add));
        player.setExp((old+ add) % 1);
        event.setAmount(0);
    }

}
