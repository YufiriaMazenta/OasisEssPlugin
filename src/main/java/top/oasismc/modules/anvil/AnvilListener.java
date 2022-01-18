package top.oasismc.modules.anvil;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static top.oasismc.core.Unit.color;
import static top.oasismc.core.Unit.info;

public class AnvilListener implements Listener {

    private static final AnvilListener LISTENER;

    static {
        LISTENER = new AnvilListener();
    }

    private AnvilListener() {}

    public static AnvilListener getAnvilListener() {
        return LISTENER;
    }

    @EventHandler
    public void onPlayerRenameOnAnvil(PrepareAnvilEvent event) {
        if (event.getResult() == null)
            return;
        ItemStack item = event.getResult().clone();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(color(meta.getDisplayName()));
        item.setItemMeta(meta);
        event.setResult(item);
    }

}
