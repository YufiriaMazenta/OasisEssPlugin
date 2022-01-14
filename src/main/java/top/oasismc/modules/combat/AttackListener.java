package top.oasismc.modules.combat;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static top.oasismc.core.Unit.getPlugin;


public class AttackListener implements Listener {

    private static final AttackListener attackListener;

    static {
        attackListener = new AttackListener();
    }

    private AttackListener() {}

    public static AttackListener getInstance() {
        return attackListener;
    }

    private static final Map<Material, Short> itemDurabilityMap;

    public static Map<Material, Short> getItemDurabilityMap() {
        return itemDurabilityMap;
    }

//    //实现吃东西被打打断
//    @EventHandler(priority = EventPriority.HIGHEST)
//    public void noEat(EntityDamageByEntityEvent event) {
//        if (event.getEntity() instanceof Player player) {
//            if (player.getItemInUse() != null) {
//                ItemStack main = player.getInventory().getItemInMainHand();
//                ItemStack off = player.getInventory().getItemInOffHand();
//                player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
//                player.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
//                new BukkitRunnable(){
//                    @Override
//                    public void run() {
//                        player.getInventory().setItemInMainHand(main);
//                        player.getInventory().setItemInOffHand(off);
//                    }
//                }.runTaskLaterAsynchronously(getPlugin(), 0);
//            }
//        }
//    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void arms(EntityDamageByEntityEvent event) {
        double attackRate = 1.0;
        Entity damager = event.getDamager();
        double damage = event.getDamage();
        if (damager instanceof Player) {
            try {
                ItemStack handItem = ((Player) damager).getInventory().getItem(EquipmentSlot.HAND);
                Damageable temp = (Damageable) (handItem.getItemMeta());
                float used;
                if (itemDurabilityMap.containsKey(handItem.getType())) {
                    used = (float) temp.getDamage() / itemDurabilityMap.get(handItem.getType());
                    attackRate -= used;
                    damage *= (Math.pow(1.4 ,attackRate) - 0.2);
                }
            } catch (Exception e) {
                damage = event.getDamage();
            }
        }//攻击者武器伤害修改,公式为:伤害 × (1.4 ^ 耐久剩余百分比 - 0.2)
        event.setDamage(damage);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void armors(EntityDamageByEntityEvent event) {
        double attackRate = 1;
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            try {
                ItemStack[] armor = ((Player) entity).getInventory().getArmorContents();
                float avgNowDur = 0;
                int armorNum = 0;
                for (ItemStack i : armor) {
                    if (i != null && itemDurabilityMap.containsKey(i.getType())) {
                        short usedDur = (short) ((Damageable) (i.getItemMeta())).getDamage();
                        short fullDur = (itemDurabilityMap.get(i.getType()));
                        avgNowDur += ((float) (fullDur - usedDur) / fullDur);
                        armorNum += 1;
                    }
                }
                if (armorNum != 0) {
                    avgNowDur /= armorNum;
                } else {
                    avgNowDur = 1;
                }
                attackRate = avgNowDur;
            } catch (Exception e) {
                e.printStackTrace();
            }
            event.setDamage(event.getDamage() / (Math.pow(1.35, attackRate) - 0.1));
            //计算公式:伤害 ÷ (1.35 ^ 耐久剩余百分比 - 0.1)
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void armorBreak(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        } else {
            Player entity = (Player) event.getEntity();
            ItemStack[] armor = entity.getInventory().getArmorContents();
            for (ItemStack i : armor) {
                if (i != null && itemDurabilityMap.containsKey(i.getType())) {
                    Damageable itemMeta = (Damageable) (i.getItemMeta());
                    short usedDur = (short) itemMeta.getDamage();
                    short fullDur = (itemDurabilityMap.get(i.getType()));
                    float dur = ((float) (fullDur - usedDur) / fullDur);
                    if (dur < 0.25) {
                        if (event.getDamage() >= 20) {
                            itemMeta.setDamage(fullDur);
                            i.setItemMeta((ItemMeta) itemMeta);
                        }
                    } else if (dur < 0.15) {
                        if (event.getDamage() >= 15) {
                            itemMeta.setDamage(fullDur);
                            i.setItemMeta((ItemMeta) itemMeta);
                        }
                    } else if (dur < 0.1) {
                        if (event.getDamage() >= 12) {
                            itemMeta.setDamage(fullDur);
                            i.setItemMeta((ItemMeta) itemMeta);
                        }
                    } else if (dur < 0.05) {
                        if (event.getDamage() >= 10) {
                            itemMeta.setDamage(fullDur);
                            i.setItemMeta((ItemMeta) itemMeta);
                        }
                    }// 当耐久低于数值,受到超过设定伤害会炸,对应数值:25%/20, 15%/15, 10%/12, 5%/10
                }
            }
            entity.getInventory().setArmorContents(armor);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void attackSound(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        Player damager = (Player) event.getDamager();
        Entity entity = event.getEntity();
        if (!(entity instanceof LivingEntity)) {
            return;
        }
        if (Objects.requireNonNull(((LivingEntity) entity).getAttribute(Attribute.GENERIC_ARMOR)).getValue() >= 3) {
            int radius = getPlugin().getConfig().getInt("sounds.soundRadius", 5);
            switch (damager.getInventory().getItem(EquipmentSlot.HAND).getType()) {
                case IRON_SWORD:
                case IRON_AXE:
                case IRON_HOE:
                case IRON_PICKAXE:
                case IRON_SHOVEL:
                case NETHERITE_SWORD:
                case NETHERITE_AXE:
                case NETHERITE_HOE:
                case NETHERITE_PICKAXE:
                case NETHERITE_SHOVEL:
                    entity.getNearbyEntities(radius, radius, radius).parallelStream().forEach(pl -> {
                        if (pl instanceof Player) {
                            ((Player) pl).playSound(entity.getLocation(), getPlugin().getConfig().getString("sounds.attack_ironSword", "entity.zombie.attack_iron_door"), 1, ((int) (Math.random() * 3)));
                        }
                    });
                    if (entity instanceof Player) {
                        ((Player) entity).playSound(entity.getLocation(), getPlugin().getConfig().getString("sounds.attack_ironSword", "entity.zombie.attack_iron_door"), 1, ((int) (Math.random() * 3)));
                    }
                    break;
                case DIAMOND_SWORD:
                case DIAMOND_AXE:
                case DIAMOND_HOE:
                case DIAMOND_PICKAXE:
                case DIAMOND_SHOVEL:
                    entity.getNearbyEntities(radius, radius, radius).parallelStream().forEach(pl -> {
                        if (pl instanceof Player) {
                            ((Player) pl).playSound(entity.getLocation(), getPlugin().getConfig().getString("sounds.attack_diamondSword", "entity.blaze.hurt"), 1, ((int) (Math.random() * 3)));
                        }
                    });
                    if (entity instanceof Player) {
                        ((Player) entity).playSound(entity.getLocation(), getPlugin().getConfig().getString("sounds.attack_diamondSword", "entity.blaze.hurt"), 1, ((int) (Math.random() * 3)));
                    }
                    break;

            }
        }
    }

    @EventHandler
    public void drawSword(PlayerItemHeldEvent event) {
        try {
            switch (event.getPlayer().getInventory().getItem(event.getNewSlot()).getType()) {
                case DIAMOND_SWORD:
                case IRON_SWORD:
                case NETHERITE_SWORD:
                    event.getPlayer().playSound(event.getPlayer().getLocation(), getPlugin().getConfig().getString("sounds.drawSword", "entity.drowned.shoot"), 1, 0);
                    break;
            }
        } catch (NullPointerException ignored) {

        }
    }

    static {
        itemDurabilityMap = new HashMap<>();
        //皮质盔甲
        itemDurabilityMap.put(Material.LEATHER_HELMET, (short) 55);
        itemDurabilityMap.put(Material.LEATHER_CHESTPLATE, (short) 80);
        itemDurabilityMap.put(Material.LEATHER_LEGGINGS, (short) 75);
        itemDurabilityMap.put(Material.LEATHER_BOOTS, (short) 65);
        //木制工具
        itemDurabilityMap.put(Material.WOODEN_SWORD, (short) 59);
        itemDurabilityMap.put(Material.WOODEN_PICKAXE, (short) 59);
        itemDurabilityMap.put(Material.WOODEN_AXE, (short) 59);
        itemDurabilityMap.put(Material.WOODEN_SHOVEL, (short) 59);
        itemDurabilityMap.put(Material.WOODEN_HOE, (short) 59);
        //石制工具
        itemDurabilityMap.put(Material.STONE_SWORD, (short) 131);
        itemDurabilityMap.put(Material.STONE_PICKAXE, (short) 131);
        itemDurabilityMap.put(Material.STONE_AXE, (short) 131);
        itemDurabilityMap.put(Material.STONE_SHOVEL, (short) 131);
        itemDurabilityMap.put(Material.STONE_HOE, (short) 131);
        //铁质工具
        itemDurabilityMap.put(Material.IRON_SWORD, (short) 250);
        itemDurabilityMap.put(Material.IRON_PICKAXE, (short) 250);
        itemDurabilityMap.put(Material.IRON_AXE, (short) 250);
        itemDurabilityMap.put(Material.IRON_SHOVEL, (short) 250);
        itemDurabilityMap.put(Material.IRON_HOE, (short) 250);
        //铁质盔甲
        itemDurabilityMap.put(Material.IRON_HELMET, (short) 165);
        itemDurabilityMap.put(Material.IRON_CHESTPLATE, (short) 240);
        itemDurabilityMap.put(Material.IRON_LEGGINGS, (short) 225);
        itemDurabilityMap.put(Material.IRON_BOOTS, (short) 195);
        //锁链盔甲
        itemDurabilityMap.put(Material.CHAINMAIL_HELMET, (short) 165);
        itemDurabilityMap.put(Material.CHAINMAIL_CHESTPLATE, (short) 240);
        itemDurabilityMap.put(Material.CHAINMAIL_LEGGINGS, (short) 225);
        itemDurabilityMap.put(Material.CHAINMAIL_BOOTS, (short) 195);
        //金制工具
        itemDurabilityMap.put(Material.GOLDEN_SWORD, (short) 32);
        itemDurabilityMap.put(Material.GOLDEN_PICKAXE, (short) 32);
        itemDurabilityMap.put(Material.GOLDEN_AXE, (short) 32);
        itemDurabilityMap.put(Material.GOLDEN_SHOVEL, (short) 32);
        itemDurabilityMap.put(Material.GOLDEN_HOE, (short) 32);
        //金制盔甲
        itemDurabilityMap.put(Material.GOLDEN_HELMET, (short) 77);
        itemDurabilityMap.put(Material.GOLDEN_CHESTPLATE, (short) 112);
        itemDurabilityMap.put(Material.GOLDEN_LEGGINGS, (short) 105);
        itemDurabilityMap.put(Material.GOLDEN_BOOTS, (short) 91);
        //钻石工具
        itemDurabilityMap.put(Material.DIAMOND_SWORD, (short) 1561);
        itemDurabilityMap.put(Material.DIAMOND_PICKAXE, (short) 1561);
        itemDurabilityMap.put(Material.DIAMOND_AXE, (short) 1561);
        itemDurabilityMap.put(Material.DIAMOND_SHOVEL, (short) 1561);
        itemDurabilityMap.put(Material.DIAMOND_HOE, (short) 1561);
        //钻石盔甲
        itemDurabilityMap.put(Material.DIAMOND_HELMET, (short) 363);
        itemDurabilityMap.put(Material.DIAMOND_CHESTPLATE, (short) 528);
        itemDurabilityMap.put(Material.DIAMOND_LEGGINGS, (short) 495);
        itemDurabilityMap.put(Material.DIAMOND_BOOTS, (short) 429);
        //下界合金工具
        itemDurabilityMap.put(Material.NETHERITE_SWORD, (short) 2031);
        itemDurabilityMap.put(Material.NETHERITE_PICKAXE, (short) 2031);
        itemDurabilityMap.put(Material.NETHERITE_AXE, (short) 2031);
        itemDurabilityMap.put(Material.NETHERITE_SHOVEL, (short) 2031);
        itemDurabilityMap.put(Material.NETHERITE_HOE, (short) 2031);
        //下界合金盔甲
        itemDurabilityMap.put(Material.NETHERITE_HELMET, (short) 407);
        itemDurabilityMap.put(Material.NETHERITE_CHESTPLATE, (short) 592);
        itemDurabilityMap.put(Material.NETHERITE_LEGGINGS, (short) 555);
        itemDurabilityMap.put(Material.NETHERITE_BOOTS, (short) 481);
        //海龟头
        itemDurabilityMap.put(Material.TURTLE_HELMET, (short) 275);
        //三叉戟
        itemDurabilityMap.put(Material.TRIDENT, (short) 250);
    }

}
