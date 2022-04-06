package top.oasismc.modules.utils.respawn;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import top.oasismc.OasisEss;

import static top.oasismc.OasisEss.*;

public class AutoRespawn implements Listener, CommandExecutor {

    private static final AutoRespawn listener;

    static {
        listener = new AutoRespawn();
    }

    private AutoRespawn() {}

    public static AutoRespawn getListener() {return listener;}

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!event.getEntity().hasPermission("oasis.autorespawn"))
            return;
        new BukkitRunnable() {
            @Override
            public void run() {
                event.getEntity().spigot().respawn();
            }
        }.runTaskLater(OasisEss.getPlugin(), 1L);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (player.hasPermission("oasis.autorespawn")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + sender.getName() + " permission set oasis.autorespawn false");
                getActionBarSender().sendActionBar((Player) sender, color(getTextConfig().getConfig().getString("autorespawn.close")));
            } else {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + sender.getName() + " permission set oasis.autorespawn true");
                getActionBarSender().sendActionBar((Player) sender, color(getTextConfig().getConfig().getString("autorespawn.open")));
            }
        }
        return true;
    }
}
