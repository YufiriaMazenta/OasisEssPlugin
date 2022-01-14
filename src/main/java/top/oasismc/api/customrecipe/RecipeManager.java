package top.oasismc.api.customrecipe;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import top.oasismc.api.config.ConfigFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static top.oasismc.core.Unit.*;

public class RecipeManager {

    private static ConfigFile recipeConfig;
    private static List<String> keyList;

    static {
        recipeConfig = new ConfigFile("recipe.yml");
        keyList = new ArrayList<>();
        getConfigs().add(recipeConfig);
        loadRecipesFromConfig();
    }

    public static void loadRecipesFromConfig() {
        try {
            YamlConfiguration config = (YamlConfiguration) recipeConfig.getConfig();
            for (String recipeID : config.getKeys(false)) {
                List<String> materialList = config.getStringList(recipeID + ".items");
                Material[] materials = new Material[materialList.size()];
                if (config.getString(recipeID + ".type", "shaped").equals("shaped")) {
                    if (materialList.size() != 9) {
                        info("An error occurred while loading the recipe " + recipeID + ", please check the configuration");
                    }
                }
                for (int i = 0; i < materialList.size(); i++) {
                    materials[i] = Material.getMaterial(materialList.get(i));
                }//从配置文件里拿合成内容

                ItemStack result = new ItemStack(Objects.requireNonNull(Material.getMaterial(config.getString(recipeID + ".result", "AIR"))));
                result.setAmount(config.getInt(recipeID + ".resultAmount", 1));//设置合成的物品数量

                String customName = config.getString(recipeID + ".customName");
                if (customName != null && !customName.equals("none") && !customName.equals("")) {
                    ItemMeta meta = result.getItemMeta();
                    meta.setDisplayName(color(config.getString(recipeID + ".customName")));
                    result.setItemMeta(meta);
                }//设置合成物品的名字
                String key = Objects.requireNonNull(config.getString(recipeID + ".key")).toLowerCase();
                switch (config.getString(recipeID + ".type", "shaped")) {
                    case "shaped"://有序
                        addShapedRecipe(
                                key,
                                result,
                                materials
                        );
                        break;
                    case "shapeless"://无序
                        addShapelessRecipe(
                                key,
                                result,
                                materials
                        );
                        break;
                    case "furnace":
                        addFurnaceRecipe(
                                key,
                                result,
                                materials[0],
                                config.getInt(recipeID + ".exp", 0),
                                config.getInt(recipeID + ".time", 10) * 20
                        );
                        break;

                }
                keyList.add(config.getString(recipeID + ".key"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String> getKeyList() {
        return keyList;
    }

    public static void addShapedRecipe(String key, ItemStack result, Material[] itemList) {
        NamespacedKey recipeKey = new NamespacedKey(getPlugin(), key);
        ShapedRecipe recipe = new ShapedRecipe(recipeKey, result);
        recipe = recipe.shape("abc", "def", "ghi");
        int i = 0;
        String temp = "abcdefghi";
        for (Material material : itemList) {
            recipe.setIngredient(temp.charAt(i), material);
            i++;
        }
        Bukkit.getServer().addRecipe(recipe);
    }

    public static void addShapelessRecipe(String key, ItemStack result, Material[] itemList) {
        NamespacedKey recipeKey = new NamespacedKey(getPlugin(), key);
        ShapelessRecipe recipe = new ShapelessRecipe(recipeKey, result);
        for (Material material : itemList) {
            recipe.addIngredient(material);
        }
        Bukkit.getServer().addRecipe(recipe);
    }

    public static void addFurnaceRecipe(String key, ItemStack result, Material item, int exp, int cookingTime) {
        NamespacedKey recipeKey = new NamespacedKey(getPlugin(), key);
        FurnaceRecipe recipe = new FurnaceRecipe(recipeKey, result, item, exp, cookingTime);
        Bukkit.getServer().addRecipe(recipe);
    }

    public static ConfigFile getRecipeConfig() {
        return recipeConfig;
    }
}
