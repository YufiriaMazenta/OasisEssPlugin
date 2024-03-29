package top.oasismc.modules.cmds;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import top.oasismc.api.config.ConfigFile;
import top.oasismc.modules.utils.message.AutoBroadCastRunnable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static top.oasismc.api.customrecipe.RecipeManager.getKeyList;
import static top.oasismc.api.customrecipe.RecipeManager.loadRecipesFromConfig;
import static top.oasismc.OasisEss.*;

public class ReloadCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendPluginInfo(commandSender);
            return true;
        }
        switch (args[0]) {
            case "info" -> {
                sendPluginInfo(commandSender);
                return true;
            }
            case "reload" -> {
                reloadPlugin(commandSender);
                return true;
            }
            default -> {
                commandSender.sendMessage(color("&4Wrong parameter"));
                return true;
            }
        }
    }

    private void sendPluginInfo(CommandSender sender) {
        sender.sendMessage(color("&7Plugin Name: &a" + getPlugin().getName()));
        sender.sendMessage(color("&7Plugin Version: &6" + getPlugin().getDescription().getVersion()));
        sender.sendMessage(color("&7Plugin Authors: &aStringStream&7, &dChiyodaXiaoYi"));
    }

    private void reloadPlugin(CommandSender sender) {
        getPlugin().reloadConfig();
        for (ConfigFile config : getConfigs()) {
            config.reloadConfig();
        }//重新加载配置文件

        for (String s : getKeyList()) {
            Bukkit.removeRecipe(Objects.requireNonNull(NamespacedKey.fromString(s, getPlugin())));
        }
        getKeyList().clear();
        loadRecipesFromConfig();
        //重新加载合成表

        getBroadCastRunnable().cancel();
        setBroadCastRunnable(new AutoBroadCastRunnable(getPlugin().getConfig().getInt("modules.broadcast.interval", 300)));
        //重新加载自动消息

        sendMsg(sender, "reload");
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("info", "reload");
        }
        return null;
    }

}
