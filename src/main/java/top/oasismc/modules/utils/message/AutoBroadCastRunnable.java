package top.oasismc.modules.utils.message;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static top.oasismc.OasisEss.color;
import static top.oasismc.OasisEss.getPlugin;

public class AutoBroadCastRunnable extends BukkitRunnable {

    public AutoBroadCastRunnable(int interval) {
        startRunnable(interval);
    }

    @Override
    public void run() {
        List<String> messages = getPlugin().getConfig().getStringList("modules.broadcast.messages");
        if (messages.size() == 0) {
            return;
        }
        Bukkit.getOnlinePlayers().parallelStream().forEach(p -> {
            p.sendMessage(color(messages.get((int)(Math.random() * messages.size()))));
        });
        getPlugin().getLogger().info(color(messages.get((int)(Math.random() * messages.size()))));
    }

    private void startRunnable(int interval) {
        this.runTaskTimerAsynchronously(getPlugin(), 0, interval * 20L);
    }

}
