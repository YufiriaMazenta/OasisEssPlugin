package top.oasismc.modules.utils.nearbycreeperwarning;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

import static top.oasismc.OasisEss.*;

public class NearbyCreeperRunnable {

    BukkitRunnable runnable;

    /*此处代码逻辑:
    异步遍历玩家列表,对每个玩家附近的实体进行遍历,如果有苦力怕则发送ActionBar
     */
    public NearbyCreeperRunnable() {
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach(
                        player -> {
                            player.getNearbyEntities(
                                    getPlugin().getConfig().getInt("modules.creeperWarning.x", 5),
                                    getPlugin().getConfig().getInt("modules.creeperWarning.y", 5),
                                    getPlugin().getConfig().getInt("modules.creeperWarning.z", 5)
                                    ).parallelStream().forEach(
                                            entity -> {
                                                if (entity.getType().equals(EntityType.CREEPER)) {
                                                    getActionBarSender().sendActionBar(player,
                                                            color(getTextConfig().getConfig().getString("creeperWarning",
                                                                    "&c&lThere is a Creeper near you"
                                                            ))
                                                    );
                                                }
                                            }
                            );
                        }
                );
            }
        };
        runnable.runTaskTimer(getPlugin(), 0, 20L);
    }

}
