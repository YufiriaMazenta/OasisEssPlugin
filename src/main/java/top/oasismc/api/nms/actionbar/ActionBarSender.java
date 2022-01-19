package top.oasismc.api.nms.actionbar;

import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

import static top.oasismc.api.nms.NMSVersionGetter.getVersion;
import static top.oasismc.OasisEss.*;

public class ActionBarSender {

    private Class<?> craftPlayerClass;
    private Method getHandle;

    public ActionBarSender() {
        try {
            craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + getVersion() + ".entity.CraftPlayer");
            getHandle = craftPlayerClass.getMethod("getHandle");
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void sendActionBar(Player player, String text) {
        if (player == null || !player.isOnline()) {
            info("Player is empty or not online");
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                AsyncActionBarSendEvent event = new AsyncActionBarSendEvent(player, text);
                Bukkit.getPluginManager().callEvent(event);

                //如果被取消,则返回
                if (event.isCancelled()) {
                    return;
                }

                //实例化要发送的数据包
                IChatBaseComponent message = new ChatComponentText(text);
                ClientboundSetActionBarTextPacket messagePacket = new ClientboundSetActionBarTextPacket(message);

                //获取craftPlayer对象并转化为entityPlayer
                Object craftPlayer = craftPlayerClass.cast(player);
                EntityPlayer entityPlayer = null;
                try {
                    entityPlayer = (EntityPlayer) getHandle.invoke(craftPlayer);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }

                //发包
                Objects.requireNonNull(entityPlayer).b.a(messagePacket);
            }
        }.runTaskAsynchronously(getPlugin());
    }

    public void sendActionBarToAll(String text) {
        Bukkit.getOnlinePlayers().parallelStream().forEach(
                player->{
                    sendActionBar(player, text);
                }
        );
    }

}
