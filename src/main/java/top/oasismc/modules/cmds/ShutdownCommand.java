package top.oasismc.modules.cmds;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static top.oasismc.OasisEss.*;

public class ShutdownCommand implements TabExecutor {

    private int time;
    private static ShutdownCommand command;
    private ShutdownCommand() {}

    static {
        command = new ShutdownCommand();
    }

    public static ShutdownCommand getCommand() {return command;}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("oasis.admin"))
            return true;
        time = 5;
        if (args.length != 0)
            time = Integer.parseInt(args[0]);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (time > 0) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        String title = getTextConfig().getConfig().getString("commands.shutdown.title", "%time%");
                        title = title.replace("%time%", time + "");
                        String subTitle = getTextConfig().getConfig().getString("commands.shutdown.subtitle", "%time%");
                        subTitle = subTitle.replace("%time%", time + "");
                        player.sendTitle(color(title), color(subTitle), 20, 70, 10);
                    }
                    time --;
                } else {
                    Bukkit.shutdown();
                }
            }
        }.runTaskTimer(getPlugin(), 0, 20);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
