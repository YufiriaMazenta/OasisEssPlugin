package top.oasismc.modules.utils.message;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import top.oasismc.OasisEss;
import top.oasismc.api.message.BaseComponentGetter;

import static top.oasismc.OasisEss.color;

public class DeathMsgListener implements Listener {

    private DeathMsgListener() {}

    private final static DeathMsgListener listener;

    static {
        listener = new DeathMsgListener();
    }

    public static DeathMsgListener getListener() {
        return listener;
    }

    @EventHandler
    public void playerDeath(PlayerDeathEvent p) {
        p.getEntity().getLocation().getChunk().unload();
        String deathMsg = OasisEss.getTextConfig().getConfig().getString("playerDeath", "");
        EntityDamageEvent event = p.getEntity().getLastDamageCause();
        String killer;
        if (event == null) {
            killer = "Null";
        } else {
            killer = event.getCause().name();
            switch (event.getCause()) {
                case BLOCK_EXPLOSION -> killer = OasisEss.getTextConfig().getConfig().getString("killer.block_explosion", "Boom");
                case CONTACT -> {
                    String block = ((EntityDamageByBlockEvent) event).getDamager().getType().name();
                    killer = OasisEss.getTextConfig().getConfig().getString("killer.contact." + block, "Unknown");
                }
                case CRAMMING -> killer = OasisEss.getTextConfig().getConfig().getString("killer.cramming", "Cramming");
                case DRAGON_BREATH -> killer = OasisEss.getTextConfig().getConfig().getString("killer.dragon_breath", "Dragon Breath");
                case DROWNING -> killer = OasisEss.getTextConfig().getConfig().getString("killer.drowning", "Water Drowning");
                case DRYOUT -> killer = OasisEss.getTextConfig().getConfig().getString("killer.dryout", "Lack of Water");
                case ENTITY_ATTACK, THORNS -> {
                    String entity = ((EntityDamageByEntityEvent) event).getDamager().getName();
                    killer = OasisEss.getTextConfig().getConfig().getString("killer.entity_attack." + entity, entity);
                }
                case ENTITY_EXPLOSION -> {
                    String entity = ((EntityDamageByEntityEvent) event).getDamager().getName();
                    killer = OasisEss.getTextConfig().getConfig().getString("killer.entity_explosion." + entity, entity);
                }
                case FALL -> killer = OasisEss.getTextConfig().getConfig().getString("killer.fall", "Fall");
                case FALLING_BLOCK -> {
                    String block = ((EntityDamageByBlockEvent) event).getDamager().getType().name();
                    killer = OasisEss.getTextConfig().getConfig().getString("killer.falling_block." + block, "Unknown");
                }
                case FIRE, FIRE_TICK -> killer = OasisEss.getTextConfig().getConfig().getString("killer.fire", "Fire");
                case FLY_INTO_WALL -> killer = OasisEss.getTextConfig().getConfig().getString("killer.fly_into_wall", "Hit the Wall");
                case FREEZE -> killer = OasisEss.getTextConfig().getConfig().getString("killer.freeze", "Freeze");
                case HOT_FLOOR -> killer = OasisEss.getTextConfig().getConfig().getString("killer.hot_floor", "Hot Floor");
                case LAVA -> killer = OasisEss.getTextConfig().getConfig().getString("killer.lava", "Lava");
                case LIGHTNING -> killer = OasisEss.getTextConfig().getConfig().getString("killer.lighting", "Lighting");
                case MAGIC, PROJECTILE -> {
                    if (event instanceof EntityDamageByEntityEvent) {
                        Entity entity = ((EntityDamageByEntityEvent) event).getDamager();
                        if (entity instanceof Projectile) {
                            if (((Projectile) entity).getShooter() instanceof Entity)
                                if (!(((Projectile) entity).getShooter() instanceof Player)) {
                                    killer = OasisEss
                                            .getTextConfig()
                                            .getConfig()
                                            .getString("killer.shoot." + ((Entity) ((Projectile) entity).getShooter()).getName(), ((Projectile) entity).getShooter().toString());
                                } else {
                                    killer = ((Player) ((Projectile) entity).getShooter()).getName();
                                }
                        } else {
                            killer = OasisEss.getTextConfig().getConfig().getString("killer.magic", "Magic");
                        }
                    } else {
                        killer = OasisEss.getTextConfig().getConfig().getString("killer.magic", "Magic");
                    }
                }
                case POISON -> killer = OasisEss.getTextConfig().getConfig().getString("killer.poison", "Poison");
                case STARVATION -> killer = OasisEss.getTextConfig().getConfig().getString("killer.hunger", "Hunger");
                case SUFFOCATION -> killer = OasisEss.getTextConfig().getConfig().getString("killer.suffocation", "Suffocation");
                case VOID -> killer = OasisEss.getTextConfig().getConfig().getString("killer.void", "Void");
                case WITHER -> killer = OasisEss.getTextConfig().getConfig().getString("killer.wither", "Wither");
            }
        }
        deathMsg = deathMsg.replace("%player%", p.getEntity().getName()).replace("%killer%", killer);
        p.setDeathMessage(color(deathMsg));
    }
}
