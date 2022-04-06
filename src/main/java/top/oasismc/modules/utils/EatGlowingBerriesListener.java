package top.oasismc.modules.utils;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EatGlowingBerriesListener implements Listener {

    private static final EatGlowingBerriesListener LISTENER = new EatGlowingBerriesListener();

    private EatGlowingBerriesListener() {}

    public static EatGlowingBerriesListener getListener() {return LISTENER;}

    @EventHandler
    public void onEatGlowingBerries(PlayerItemConsumeEvent event) {
        if (!event.getItem().getType().equals(Material.GLOW_BERRIES))
            return;
        event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 100 * 20, 1));
    }

}
