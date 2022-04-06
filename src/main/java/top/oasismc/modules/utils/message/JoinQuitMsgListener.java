package top.oasismc.modules.utils.message;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static top.oasismc.OasisEss.*;

public class JoinQuitMsgListener implements Listener {

    private static JoinQuitMsgListener listener;

    static {
        listener = new JoinQuitMsgListener();
    }

    private JoinQuitMsgListener() {}

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String msg = getTextConfig().getConfig().getString("join", "")
                .replace("%player%", event.getPlayer().getName());
        if (msg.equals("")) {
            return;
        }
        event.setJoinMessage(color(msg));
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        Pattern pattern = Pattern.compile("^[A-Za-z0-9\\\\_]+$");
        Matcher matcher = pattern.matcher(player.getName());
        if (!matcher.find()) {
            String msg = getTextConfig().getConfig().getString("nameNotAllow", "&c您的游戏名不合法,请使用符合Minecraft正版命名规则的ID进入!");
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, color(msg));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        String msg = getTextConfig().getConfig().getString("quit", "")
                .replace("%player%", event.getPlayer().getName());
        if (msg.equals("")) {
            return;
        }
        event.setQuitMessage(color(msg));
    }

    public static JoinQuitMsgListener getListener() { return listener; }

}
