package top.oasismc.modules.auth;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import top.oasismc.api.email.EmailSender;

import java.util.*;

import static top.oasismc.OasisEss.*;

public class RegCommand implements TabExecutor {

    private static Map<String, String> codes;//临时存储玩家验证码
    private static Map<String, String> tempEmails;//临时存储玩家邮箱
    private static Map<String, String> tempPwd;//临时存储玩家密码

    public RegCommand() {
        codes = new HashMap<>();
        tempEmails = new HashMap<>();
        tempPwd = new HashMap<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            info("This command can only used by player");
            return true;
        }
        if (VerifyCommand.getEmailsConfig().getConfig().contains(sender.getName())) {
            sendMsg(sender, "auth.reg.hasReg");
            return true;
        }
        if (args.length < 2)  {
            sendMsg(sender ,"auth.reg.lengthError");
            return true;
        }
        if (VerifyCommand.getEmailsConfig().getConfig().getStringList("hasRegEmails").contains(args[0])) {
            sendMsg(sender, "auth.reg.emailHasReg");
            return true;
        }
        if (!args[0].contains("@qq.com")) {
            sendMsg(sender, "auth.reg.notEmail");
            return true;
        }
        if (args[1].length() <= getPlugin().getConfig().getInt("modules.auth.minPwdLength", 6)) {
            sendMsg(sender, "auth.reg.pwdLengthError");
            return true;
        }
        if (codes.containsKey(sender.getName())) {
            sendMsg(sender, "auth.reg.hasSent");
        }
        args[0] = args[0].replace("@@", "@");
        try {//尝试发送邮件
            String code = EmailSender.send((Player) sender, args[0]);
            sendMsg(sender, "auth.reg.success");
            codes.put(sender.getName(), code);
            tempEmails.put(sender.getName(), args[0]);
            tempPwd.put(sender.getName(), args[1]);
            new BukkitRunnable() {
                @Override
                public void run() {
                    codes.remove(sender.getName());
                    tempEmails.remove(sender.getName());
                    tempPwd.remove(sender.getName());
                }
            }.runTaskLater(getPlugin(), 6000L);//线程在5分钟后清除掉哈希表的内容
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            String email = args[0];
            if (email.contains("@qq.com")) {
                return Collections.singletonList(email);
            }
            return Collections.singletonList(email + "@qq.com");
        }
        else if (args.length == 2) {
            return Collections.singletonList("<password>");
        }
        return null;
    }

    public static Map<String, String> getCodes() {
        return codes;
    }

    public static Map<String, String> getTempEmails() {
        return tempEmails;
    }

    public static Map<String, String> getTempPwd() {
        return tempPwd;
    }
}
