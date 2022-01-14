package top.oasismc.modules.cmds;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

import static top.oasismc.core.Unit.sendMsg;

public class HatCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sendMsg(sender, "notPlayer");
            return true;
        }
        if (
                ((Player) sender).getInventory().getItemInMainHand() != null
            && !((Player) sender).getInventory().getItemInMainHand().getType().equals(Material.AIR)
        ) {
            ItemStack tmpItem = ((Player) sender).getInventory().getItem(EquipmentSlot.HEAD);
            if (tmpItem != null) {
                tmpItem = tmpItem.clone();
            } else {
                tmpItem = new ItemStack(Material.AIR);
            }
            ((Player) sender).getInventory().setItem(EquipmentSlot.HEAD, ((Player) sender).getInventory().getItemInMainHand());
            ((Player) sender).getInventory().setItem(EquipmentSlot.HAND, tmpItem);
        }
        sendMsg(sender, "commands.hat");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.singletonList("");
    }

}
