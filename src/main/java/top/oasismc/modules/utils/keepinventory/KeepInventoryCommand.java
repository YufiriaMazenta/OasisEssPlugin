package top.oasismc.modules.utils.keepinventory;

//待优化
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static top.oasismc.OasisEss.*;

public class KeepInventoryCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("oasis.perm.keep")) {
            List<String> commands = getTextConfig().getConfig().getStringList("keepInventory.close.commands");
            for (String cmd : commands) {
                cmd = color(cmd.replace("%player%", sender.getName()));
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
            }
            getActionBarSender().sendActionBar((Player) sender, color(getTextConfig().getConfig().getString("keepInventory.close.self")));
        }
        else {
            List<String> commands = getTextConfig().getConfig().getStringList("keepInventory.open.commands");
            for (String cmd : commands) {
                cmd = color(cmd.replace("%player%", sender.getName()));
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
            }
            getActionBarSender().sendActionBar((Player) sender, color(getTextConfig().getConfig().getString("keepInventory.open.self")));
        }
        return true;
    }
}
