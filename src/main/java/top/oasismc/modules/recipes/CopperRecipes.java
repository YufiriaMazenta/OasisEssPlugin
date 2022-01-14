package top.oasismc.modules.recipes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import top.oasismc.api.customrecipe.RecipeManager;

import static top.oasismc.core.Unit.*;

//待弃用
public class CopperRecipes {

    public CopperRecipes() {
        addCopperToChainMailHelmet();
        addCopperToChainMailChest();
        addCopperToChainMailLeggings();
        addCopperToChainMailBoots();
        addCopperToRedStone();
        addCopperToIronPickaxe();
        addCopperToIronAxe();
        addCopperToIronSword();
    }

    private void addCopperToChainMailHelmet() {
        Material[] materials = new Material[9];
        materials[0] = (Material.COPPER_INGOT);
        materials[1] = (Material.COPPER_INGOT);
        materials[2] = (Material.COPPER_INGOT);
        materials[3] = (Material.COPPER_INGOT);
        materials[4] = (Material.AIR);
        materials[5] = (Material.COPPER_INGOT);
        materials[6] = (Material.AIR);
        materials[7] = (Material.AIR);
        materials[8] = (Material.AIR);
        ItemStack result = new ItemStack(Material.CHAINMAIL_HELMET);
        RecipeManager.addShapedRecipe("oasis.copper.helmet", result, materials);
    }

    private void addCopperToChainMailChest() {
        Material[] materials = new Material[9];
        materials[0] = (Material.COPPER_INGOT);
        materials[1] = (Material.AIR);
        materials[2] = (Material.COPPER_INGOT);
        materials[3] = (Material.COPPER_INGOT);
        materials[4] = (Material.COPPER_INGOT);
        materials[5] = (Material.COPPER_INGOT);
        materials[6] = (Material.COPPER_INGOT);
        materials[7] = (Material.COPPER_INGOT);
        materials[8] = (Material.COPPER_INGOT);
        ItemStack result = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
        RecipeManager.addShapedRecipe("oasis.copper.chestplate", result, materials);
    }

    private void addCopperToChainMailLeggings() {
        Material[] materials = new Material[9];
        materials[0] = (Material.COPPER_INGOT);
        materials[1] = (Material.COPPER_INGOT);
        materials[2] = (Material.COPPER_INGOT);
        materials[3] = (Material.COPPER_INGOT);
        materials[4] = (Material.AIR);
        materials[5] = (Material.COPPER_INGOT);
        materials[6] = (Material.COPPER_INGOT);
        materials[7] = (Material.AIR);
        materials[8] = (Material.COPPER_INGOT);
        ItemStack result = new ItemStack(Material.CHAINMAIL_LEGGINGS);
        RecipeManager.addShapedRecipe("oasis.copper.leggings", result, materials);
    }

    private void addCopperToChainMailBoots() {
        Material[] materials = new Material[9];
        materials[0] = (Material.AIR);
        materials[1] = (Material.AIR);
        materials[2] = (Material.AIR);
        materials[3] = (Material.COPPER_INGOT);
        materials[4] = (Material.AIR);
        materials[5] = (Material.COPPER_INGOT);
        materials[6] = (Material.COPPER_INGOT);
        materials[7] = (Material.AIR);
        materials[8] = (Material.COPPER_INGOT);
        ItemStack result = new ItemStack(Material.CHAINMAIL_BOOTS);
        RecipeManager.addShapedRecipe("oasis.copper.boots", result, materials);
    }

    private void addCopperToIronPickaxe() {
        Material[] materials = new Material[9];
        materials[0] = (Material.COPPER_INGOT);
        materials[1] = (Material.IRON_INGOT);
        materials[2] = (Material.COPPER_INGOT);
        materials[3] = (Material.AIR);
        materials[4] = (Material.STICK);
        materials[5] = (Material.AIR);
        materials[6] = (Material.AIR);
        materials[7] = (Material.STICK);
        materials[8] = (Material.AIR);
        ItemStack result = new ItemStack(Material.IRON_PICKAXE);
        ItemMeta meta = result.getItemMeta();
        meta.setDisplayName(color(getTextConfig().getConfig().getString("recipes.customName.copperPickaxe")));
        result.setItemMeta(meta);
        RecipeManager.addShapedRecipe("oasis.copper.pickaxe", result, materials);
    }

    private void addCopperToIronAxe() {
        Material[] materials = new Material[9];
        materials[0] = (Material.IRON_INGOT);
        materials[1] = (Material.COPPER_INGOT);
        materials[2] = (Material.AIR);
        materials[3] = (Material.COPPER_INGOT);
        materials[4] = (Material.STICK);
        materials[5] = (Material.AIR);
        materials[6] = (Material.AIR);
        materials[7] = (Material.STICK);
        materials[8] = (Material.AIR);
        ItemStack result = new ItemStack(Material.IRON_AXE);
        ItemMeta meta = result.getItemMeta();
        meta.setDisplayName(color(getTextConfig().getConfig().getString("recipes.customName.copperAxe")));
        result.setItemMeta(meta);
        RecipeManager.addShapedRecipe("oasis.copper.axe", result, materials);
    }

    private void addCopperToIronSword() {
        Material[] materials = new Material[9];
        materials[0] = (Material.AIR);
        materials[1] = (Material.IRON_INGOT);
        materials[2] = (Material.AIR);
        materials[3] = (Material.AIR);
        materials[4] = (Material.COPPER_INGOT);
        materials[5] = (Material.AIR);
        materials[6] = (Material.AIR);
        materials[7] = (Material.STICK);
        materials[8] = (Material.AIR);
        ItemStack result = new ItemStack(Material.IRON_SWORD);
        ItemMeta meta = result.getItemMeta();
        meta.setDisplayName(color(getTextConfig().getConfig().getString("recipes.customName.copperSword")));
        result.setItemMeta(meta);
        RecipeManager.addShapedRecipe("oasis.copper.sword", result, materials);
    }

    private void addCopperToRedStone() {
        Material[] materials = new Material[1];
        materials[0] = Material.COPPER_INGOT;
        ItemStack result = new ItemStack(Material.REDSTONE);
        result.setAmount(2);
        RecipeManager.addShapelessRecipe("oasis.copper.redstone", result, materials);
    }

}
