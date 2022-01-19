package top.oasismc.modules.auth;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static top.oasismc.OasisEss.*;
import static top.oasismc.modules.auth.VerifyCommand.getEmailsConfig;
import static top.oasismc.modules.auth.LoginListener.getPlayerIsLoginMap;

public class LoginCommand implements TabExecutor {

    private static final Map<String, Integer> playerTryLoginNumMap;

    static {
        playerTryLoginNumMap = new HashMap<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        if (getPlayerIsLoginMap().get(sender.getName())) {
            sendMsg(sender, "auth.login.hasLogin");
            return true;
        }
        if (args.length == 0) {
            sendMsg(sender, "auth.login.lengthError");
            return true;
        }
        if (!args[0].equals(getEmailsConfig().getConfig().getString(sender.getName() + ".pwd"))) {
            sendMsg(sender, "auth.login.wrong");
            int tryNum = playerTryLoginNumMap.getOrDefault(sender.getName(), 0);
            playerTryLoginNumMap.put(sender.getName(), tryNum + 1);
            if (tryNum >= getPlugin().getConfig().getInt("module.auth.maxTryPwdNum", 5)) {
                ((Player) sender).kickPlayer(color(getTextConfig().getConfig().getString("auth.login.tryNumMax")));
            }
            return true;
        } else {
            getEmailsConfig().getConfig().set(sender.getName() + ".ip", LoginListener.getPlayerTempIPMap().get(sender.getName()));
            getPlayerIsLoginMap().put(sender.getName(), true);
            getEmailsConfig().saveConfig();
            sendMsg(sender, "auth.login.success");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }


    public static Map<String, Integer> getPlayerTryLoginNumMap() {
        return playerTryLoginNumMap;
    }

}
