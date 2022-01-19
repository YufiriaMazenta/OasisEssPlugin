package top.oasismc.modules.recipes;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ShapedRecipe;

import java.util.Set;

import static top.oasismc.api.customrecipe.RecipeManager.getRecipeConfig;
import static top.oasismc.OasisEss.color;
import static top.oasismc.OasisEss.getTextConfig;

public class RecipeExpCheckListener implements Listener {


    private static final RecipeExpCheckListener instance;

    private RecipeExpCheckListener() {}

    static {
        instance = new RecipeExpCheckListener();
    }

    public static RecipeExpCheckListener getInstance() {
        return instance;
    }

    //待优化
    @EventHandler
    public void craft(CraftItemEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Set<String> recipes = getRecipeConfig().getConfig().getKeys(false);
        for (String key : recipes) {
            int level = getRecipeConfig().getConfig().getInt(key + ".takeLvl", 0);
            String keyName = getRecipeConfig().getConfig().getString(key + ".key", "");
            if (!(event.getRecipe() instanceof ShapedRecipe)) {
                continue;
            }
            if (((ShapedRecipe) event.getRecipe()).getKey().getKey().equals(keyName)) {
                if (((Player) event.getWhoClicked()).getLevel() < level) {
                    event.setCancelled(true);
                    event.getWhoClicked().closeInventory();
                    String warnTitle = getTextConfig().getConfig().getString("recipes.noEnoughExpTitle", "recipes.noEnoughExpTitle");
                    warnTitle = warnTitle.replace("%s%", String.valueOf(level));
                    String warnSubTitle = getTextConfig().getConfig().getString("recipes.noEnoughExpSubTitle", "recipes.noEnoughExpSubTitle");
                    warnSubTitle = warnSubTitle.replace("%s%", String.valueOf(level));
                    ((Player)event.getWhoClicked()).sendTitle(color(warnTitle), color(warnSubTitle), 10, 70, 20);
                    return;
                }
                ((Player) event.getWhoClicked()).setLevel(((Player) event.getWhoClicked()).getLevel() - level);
            }
        }
    }

}
