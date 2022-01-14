package top.oasismc.modules.auth;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import top.oasismc.api.config.ConfigFile;

import java.util.Collections;
import java.util.List;

import static top.oasismc.core.Unit.getConfigs;
import static top.oasismc.core.Unit.sendMsg;
import static top.oasismc.modules.auth.RegCommand.*;

public class VerifyCommand implements TabExecutor {

    private static ConfigFile emailsConfig;

    public VerifyCommand() {
        emailsConfig = new ConfigFile("email.yml");
        getConfigs().add(emailsConfig);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!getCodes().containsKey(sender.getName())) {
            sendMsg(sender, "auth.check.noCode");
            return true;
        }
        if (args.length == 0) {
            sendMsg(sender, "auth.check.lengthError");
            return true;
        }
        if (!getCodes().get(sender.getName()).equals(args[0])) {
            sendMsg(sender, "auth.check.wrong");
            return true;
        } else {
            emailsConfig.getConfig().set(sender.getName() + ".email", getTempEmails().get(sender.getName()));
            emailsConfig.getConfig().set(sender.getName() + ".ip", ((Player) sender).getAddress().getHostName());
            emailsConfig.getConfig().set(sender.getName() + ".pwd", getTempPwd().get(sender.getName()));
            List<String> hasRegEmails = emailsConfig.getConfig().getStringList("hasRegEmails");
            hasRegEmails.add(getTempEmails().get(sender.getName()));
            emailsConfig.getConfig().set("hasRegEmails", hasRegEmails);
            LoginListener.getPlayerIsLoginMap().put(sender.getName(), true);
            emailsConfig.saveConfig();
            getTempEmails().remove(sender.getName());
            sendMsg(sender, "auth.check.success");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.singletonList("<code>");
    }


    public static ConfigFile getEmailsConfig() {
        return emailsConfig;
    }

}
