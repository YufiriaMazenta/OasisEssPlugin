package top.oasismc.modules.utils;

import net.minecraft.network.protocol.game.PacketPlayOutCamera;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.oasismc.OasisEss;

import java.util.List;

public class CameraCommand implements CommandExecutor, TabExecutor {

    private static final CameraCommand command = new CameraCommand();
    private CameraCommand() {}

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            OasisEss.info("Only player can use this command");
            return false;
        }
        if (!sender.hasPermission("oasis.camera"))
            return false;
        if (args.length < 1) {
            sendCamera((Player) sender, (Player) sender);
            OasisEss.sendMsg(sender, "camera.clear");
        } else {
            Player player = Bukkit.getPlayer(args[0]);
            if (player == null) {
                OasisEss.sendMsg(sender, "camera.null_player");
                return true;
            }
            sendCamera((Player) sender, player);
            String msg = OasisEss.getTextConfig().getConfig().getString("camera.success");
            msg = msg.replace("%player%", player.getName());
            sender.sendMessage(OasisEss.color(msg));
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }

    public static CameraCommand getCommand() {
        return command;
    }

    private void sendCamera(Player source, Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player.getPlayer();

        PacketPlayOutCamera packet = new PacketPlayOutCamera(craftPlayer.getHandle());
        CraftPlayer sourcePlayer = (CraftPlayer) source.getPlayer();
        sourcePlayer.getHandle().b.a(packet);
    }

}
