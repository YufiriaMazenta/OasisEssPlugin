package top.oasismc.modules.auth;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

import static top.oasismc.OasisEss.*;
import static top.oasismc.modules.auth.VerifyCommand.getEmailsConfig;
import static top.oasismc.modules.auth.LoginCommand.getPlayerTryLoginNumMap;

public class LoginListener implements Listener {

    private static final Map<String, Boolean> playerIsLoginMap;//存储玩家是否登录事件
    private static final Map<String, String> playerTempIPMap;

    static {
        playerIsLoginMap = new HashMap<>();
        playerTempIPMap = new HashMap<>();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void playerJoin(AsyncPlayerPreLoginEvent event) {
        String ip = event.getAddress().getHostName();
        playerTempIPMap.put(event.getName(), ip);
        playerIsLoginMap.put(event.getName(), false);
        getEmailsConfig().reloadConfig();
        getPlayerTryLoginNumMap().put(event.getName(), 0);
        if (getEmailsConfig().getConfig().getString(event.getName() + ".email") == null) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    sendMsg(Bukkit.getPlayer(event.getName()), "auth.noReg");
                }
            }.runTaskLaterAsynchronously(getPlugin(), 60L);
            return;
        }
        if (!ip.equals(getEmailsConfig().getConfig().getString(event.getName() + ".ip"))) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    sendMsg(Bukkit.getPlayer(event.getName()), "auth.login.noLogin");
                }
            }.runTaskLaterAsynchronously(getPlugin(), 60L);
        }
        else {
            playerIsLoginMap.put(event.getName(), true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void playerQuit(PlayerQuitEvent event) {
        playerIsLoginMap.put(event.getPlayer().getName(), false);
        getPlayerTryLoginNumMap().remove(event.getPlayer().getName());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void noLoginMove(PlayerMoveEvent event) {
        if (!playerIsLoginMap.getOrDefault(event.getPlayer().getName(), false)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void noLoginInteract(PlayerInteractEvent event) {
        if (!playerIsLoginMap.getOrDefault(event.getPlayer().getName(), false)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void noLoginInteractEntity(PlayerInteractEntityEvent event) {
        if (!playerIsLoginMap.getOrDefault(event.getPlayer().getName(), false)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void noLoginDropItem(PlayerDropItemEvent event) {
        if (!playerIsLoginMap.getOrDefault(event.getPlayer().getName(), false)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void noLoginChat(AsyncPlayerChatEvent event) {
        if (!playerIsLoginMap.getOrDefault(event.getPlayer().getName(), false)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void noLoginHurt(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            if (!playerIsLoginMap.get(event.getEntity().getName())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void noLoginClickInventory(InventoryClickEvent event) {
        try {
            if (event.getWhoClicked() instanceof Player) {
                if (!playerIsLoginMap.get(event.getWhoClicked().getName())) {
                    event.setCancelled(true);
                }
            }
        } catch (NullPointerException ignored) {}
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void noLoginSendCommand(PlayerCommandPreprocessEvent event) {
        if (!playerIsLoginMap.getOrDefault(event.getPlayer().getName(), false)) {
            boolean inWhitelist = false;
            for (String whitelistCmd : getPlugin().getConfig().getStringList("modules.auth.whitelistCommands")) {
                inWhitelist = event.getMessage().startsWith(whitelistCmd) || inWhitelist;
            }
            if (!inWhitelist) {
                event.setCancelled(true);
            }
        }
    }

    public static Map<String, Boolean> getPlayerIsLoginMap() {
        return playerIsLoginMap;
    }

    public static Map<String, String> getPlayerTempIPMap() {
        return playerTempIPMap;
    }

}
