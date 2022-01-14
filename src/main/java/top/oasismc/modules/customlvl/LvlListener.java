package top.oasismc.modules.customlvl;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

import static top.oasismc.core.Unit.getPlugin;

public class LvlListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerExpChange(PlayerExpChangeEvent event) {
        int perLvlExp = getPlugin().getConfig().getInt("modules.customExpLvl.perLvlExp");
        event.getPlayer().getExp();
    }
}
