package top.oasismc.modules.fish;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import top.oasismc.modules.customevent.handler.CustomEventListener;

import java.util.*;

public class FishListener implements Listener {

    private static final FishListener listener = new FishListener();
    private final Map<Material, EntityType> fishMap;
    private Random random;

    private FishListener() {
        fishMap = new HashMap<>();
        fishMap.put(Material.COD, EntityType.COD);
        fishMap.put(Material.SALMON, EntityType.SALMON);
        fishMap.put(Material.TROPICAL_FISH, EntityType.TROPICAL_FISH);
        fishMap.put(Material.PUFFERFISH, EntityType.PUFFERFISH);
        random = new Random();
    }

    public static FishListener getListener() { return listener; }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            Entity caught = event.getCaught();
            if (CustomEventListener.getListener().getEventSwitchMap().getOrDefault(2, false)) {
                EntityType[] types = EntityType.values();
                List<EntityType> typeList = new ArrayList<>(List.of(types));
                typeList.remove(EntityType.PLAYER);
                typeList.remove(EntityType.PRIMED_TNT);
                typeList.remove(EntityType.MINECART_TNT);
                typeList.remove(EntityType.ENDER_DRAGON);
                typeList.remove(EntityType.WITHER);
                if (event.getCaught() == null)
                    return;
                event.getCaught().remove();
                Entity entity = event.getPlayer().getWorld().spawnEntity(event.getHook().getLocation(), typeList.get(random.nextInt(typeList.size())));
                event.getHook().setHookedEntity(entity);
                event.getHook().pullHookedEntity();
            } else {
                if (caught != null) {
                    if (caught.getType() != EntityType.DROPPED_ITEM) {
                        return;
                    }
                    Material material = ((Item) caught).getItemStack().getType();
                    if (fishMap.containsKey(material)) {
                        if (Math.random() < 0.5)
                            return;
                        caught.remove();
                        Entity entity = event.getPlayer().getWorld().spawnEntity(event.getHook().getLocation(), fishMap.get(material));
                        event.getHook().setHookedEntity(entity);
                        event.getHook().pullHookedEntity();
                    }
                }
            }
        }
    }

}
