package top.oasismc.modules.utils.message;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
