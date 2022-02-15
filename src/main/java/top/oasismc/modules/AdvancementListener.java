package top.oasismc.modules;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import static top.oasismc.OasisEss.*;

public class AdvancementListener implements Listener {

    private static final AdvancementListener listener;

    static {
        listener = new AdvancementListener();
    }

    private AdvancementListener() {}

    public static AdvancementListener getListener() {return listener;}

    @EventHandler
    public void onKillPlayer(PlayerStatisticIncrementEvent event) {
        if (!event.getStatistic().equals(Statistic.PLAYER_KILLS))
            return;
        int value = event.getNewValue();
        if (value >= 100 && value < 1000) {
            getPlugin().addAdvancement(event.getPlayer().getName(), "kill_100_player");
        } else if (value >= 1000) {
            getPlugin().addAdvancement(event.getPlayer().getName(), "kill_1000_player");
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        getPlugin().addAdvancement(event.getPlayer().getName(), "join_oasis");
        Player player = event.getPlayer();
        World world = player.getWorld();
        player.teleport(new Location(world, 100, 100, 100));
    }

    @EventHandler
    public void onUseTotem(PlayerStatisticIncrementEvent event) {
        if (!event.getStatistic().equals(Statistic.USE_ITEM))
            return;
        if (event.getMaterial() == null)
            return;
        if (!event.getMaterial().equals(Material.TOTEM_OF_UNDYING))
            return;
        int newValue = event.getNewValue();
        if (newValue >= 5) {
            getPlugin().addAdvancement(event.getPlayer().getName(), "use_totem_5");
            if (newValue >= 10) {
                getPlugin().addAdvancement(event.getPlayer().getName(), "use_totem_10");
            }
        }
    }

    @EventHandler
    public void onKillOp(PlayerDeathEvent event) {
        if (event.getEntity().isOp()) {
            if (event.getEntity().getKiller() == null)
                return;
            getPlugin().addAdvancement(event.getEntity().getKiller().getName(), "kill_op");
        }
    }

    @EventHandler
    public void onDrinkCrudePotion(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();
        switch (item.getType()) {
            case POTION -> {
                if (!item.hasItemMeta())
                    return;
                ItemMeta meta = item.getItemMeta();
                PotionMeta potionMeta = (PotionMeta) meta;
                if (potionMeta == null)
                    return;
                if (potionMeta.getBasePotionData().getType().equals(PotionType.AWKWARD)) {
                    getPlugin().addAdvancement(event.getPlayer().getName(), "drink_crude_potion");
                }
            }
            case ROTTEN_FLESH -> {
                getPlugin().addAdvancement(event.getPlayer().getName(), "eat_rotten_flesh");
            }
        }
    }

    @EventHandler
    public void onGetAdvancement(PlayerAdvancementDoneEvent event) {
        Player player = event.getPlayer();
        switch (event.getAdvancement().getKey().getKey()) {
            case "kill_1000_player" -> {
                ItemStack resistancePotion = new ItemStack(Material.POTION);
                PotionMeta resistanceMeta = (PotionMeta) Bukkit.getItemFactory().getItemMeta(Material.POTION);
                resistanceMeta.setDisplayName(color("&3&l不死药"));
                resistanceMeta.addCustomEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 600 * 20, 4), true);
                resistanceMeta.setColor(Color.fromRGB(175, 238, 238));
                resistancePotion.setItemMeta(resistanceMeta);
                player.getInventory().addItem(resistancePotion);
                sendMsg(player, "advancements.kill_1000_player");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "attribute " + player.getName() + " minecraft:generic.attack_damage base set 2");
                player.giveExp(32767);
            }
        }
    }

}
