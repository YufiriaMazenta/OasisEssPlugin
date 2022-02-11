package top.oasismc;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import top.oasismc.api.config.ConfigFile;
import top.oasismc.api.nms.actionbar.ActionBarSender;
import top.oasismc.modules.anvil.AnvilListener;
import top.oasismc.modules.auth.VerifyCommand;
import top.oasismc.modules.auth.LoginCommand;
import top.oasismc.modules.auth.LoginListener;
import top.oasismc.modules.auth.RegCommand;
import top.oasismc.modules.cmds.HatCommand;
import top.oasismc.modules.cmds.ReloadCommand;
import top.oasismc.modules.cmds.ShutdownCommand;
import top.oasismc.modules.combat.AttackListener;
import top.oasismc.modules.combat.ShieldListener;
import top.oasismc.modules.customevent.handler.CustomEventListener;
import top.oasismc.modules.customevent.trigger.CustomEventTrigger;
import top.oasismc.modules.fish.FishListener;
import top.oasismc.modules.mob.MobSpawnListener;
import top.oasismc.modules.recipes.CopperRecipes;
import top.oasismc.modules.recipes.RecipeExpCheckListener;
import top.oasismc.modules.utils.ignite.IgniteListener;
import top.oasismc.modules.utils.keepinventory.KeepInventoryCommand;
import top.oasismc.modules.utils.keepinventory.KeepInventoryListener;
import top.oasismc.modules.utils.message.AutoBroadCastRunnable;
import top.oasismc.modules.utils.message.DeathMsgListener;
import top.oasismc.modules.utils.message.JoinQuitMsgListener;
import top.oasismc.modules.utils.nearbycreeperwarning.NearbyCreeperRunnable;
import top.oasismc.modules.utils.respawn.AutoRespawn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static org.bukkit.ChatColor.*;

public final class OasisEss extends JavaPlugin {

    private static OasisEss plugin;
    private static ConfigFile textConfig;
    private static ActionBarSender actionBarSender;
    private static List<ConfigFile> configs;
    private static AutoBroadCastRunnable broadCastRunnable;
    private static Set<NamespacedKey> advancementSet;

    public OasisEss() {
        setPlugin(this);
    }

    @Override
    public void onEnable() {
        configs = new ArrayList<>();

        saveDefaultConfig();
        textConfig = new ConfigFile("messages.yml");
        configs.add(textConfig);

        ReloadCommand reload = new ReloadCommand();
        Objects.requireNonNull(Bukkit.getPluginCommand("oasis")).setExecutor(reload);
        Objects.requireNonNull(Bukkit.getPluginCommand("oasis")).setTabCompleter(reload);

        actionBarSender = new ActionBarSender();
        advancementSet = new HashSet<>();

        regModules();

        info(GREEN + "Plugin Loaded");
    }

    @Override
    public void onDisable() {
        Bukkit.resetRecipes();
        Bukkit.getScheduler().cancelTasks(this);
        for (NamespacedKey key : advancementSet) {
            Bukkit.getUnsafe().removeAdvancement(key);
        }
        info(RED + "Plugin Disabled");
    }

    public static ActionBarSender getActionBarSender() {
        return actionBarSender;
    }

    public static ConfigFile getTextConfig() {
        return textConfig;
    }

    public static List<ConfigFile> getConfigs() {
        return configs;
    }

    public static void sendMsg(CommandSender sender, String key) {
        if (sender == null) {
            return;
        }
        String message = textConfig.getConfig().getString(key, key);
        message = message.replace("%player%", sender.getName());
        sender.sendMessage(color(message));
    }

    public static void bc(String message) {
        Bukkit.broadcastMessage(color(message));
    }

    public static void bcByKey(String key) {
        String msg = textConfig.getConfig().getString(key, key);
        bc(color(msg));
    }

    public static String color(String text) {
        return translateAlternateColorCodes('&', text);
    }

    public static void info(String text) {
        Bukkit.getLogger().info("[Oasis] INFO | " + text);
    }

    private void regModules() {
        regKeepInventory(getConfig().getBoolean("modules.keepInventory.enable", true));
        regCreeperWarning(getConfig().getBoolean("modules.creeperWarning.enable", true));
        regAuth(getConfig().getBoolean("modules.auth.enable", true));
        regRandomEvent(getConfig().getBoolean("modules.dateStartEvent.enable", true));
        regHatCmd(getConfig().getBoolean("modules.hat.enable", true));
        regAnvilListener(getConfig().getBoolean("modules.anvilColor.enable", true));
        regShutdownCmd();
        addRecipes();
        regFish();
        regAdvancements();
        broadCastRunnable = new AutoBroadCastRunnable(getConfig().getInt("modules.broadcast.interval", 300));

        Bukkit.getPluginManager().registerEvents(JoinQuitMsgListener.getListener(), this);
        Bukkit.getPluginManager().registerEvents(DeathMsgListener.getListener(), this);
        Bukkit.getPluginManager().registerEvents(AttackListener.getInstance(), this);
        Bukkit.getPluginManager().registerEvents(AutoRespawn.getListener(), this);
        Bukkit.getPluginManager().registerEvents(IgniteListener.getInstance(), this);
        Bukkit.getPluginManager().registerEvents(MobSpawnListener.getListener(), this);
        //Debug
        Bukkit.getPluginManager().registerEvents(ShieldListener.getInstance(), this);
    }

    private void regFish() {
        Bukkit.getPluginManager().registerEvents(FishListener.getListener(), this);
    }

    private void regAnvilListener(boolean load) {
        if (load)
            Bukkit.getPluginManager().registerEvents(AnvilListener.getAnvilListener(), this);
    }

    private void regHatCmd(boolean load) {
        if (load) {
            HatCommand cmd = new HatCommand();
            Bukkit.getPluginCommand("hat").setExecutor(cmd);
            Bukkit.getPluginCommand("hat").setTabCompleter(cmd);
        }
    }

    private void regRandomEvent(boolean load) {
        if (load) {
            Bukkit.getPluginManager().registerEvents(CustomEventTrigger.getTrigger(), this);
            Bukkit.getPluginManager().registerEvents(CustomEventListener.getListener(), this);
        }
    }

    private void regShutdownCmd() {
        Bukkit.getPluginCommand("shutdown").setExecutor(ShutdownCommand.getCommand());
        Bukkit.getPluginCommand("shutdown").setTabCompleter(ShutdownCommand.getCommand());
    }

    private void regKeepInventory(boolean load) {
        if (load) {
            Bukkit.getPluginManager().registerEvents(KeepInventoryListener.getInstance(), this);
            Objects.requireNonNull(Bukkit.getPluginCommand("keepInventory")).setExecutor(new KeepInventoryCommand());
            info("Module KeepInventory Loaded");
        }
    }


    private void regCreeperWarning(boolean load) {
        if (load) {
            new NearbyCreeperRunnable();
            info("Module Creeper Warning Loaded");
        }
    }

    private void regAuth(boolean load) {
        if (load) {
            RegCommand reg = new RegCommand();
            VerifyCommand check = new VerifyCommand();
            Bukkit.getPluginCommand("reg").setExecutor(reg);
            Bukkit.getPluginCommand("reg").setTabCompleter(reg);
            Bukkit.getPluginCommand("verify").setExecutor(check);
            Bukkit.getPluginCommand("verify").setTabCompleter(check);
            Bukkit.getPluginManager().registerEvents(new LoginListener(), this);
            Bukkit.getPluginCommand("login").setExecutor(new LoginCommand());
            info("Module Auth Loaded");
        }
    }


    public static OasisEss getPlugin() {
        return plugin;
    }

    private static void setPlugin(OasisEss plugin) {
        OasisEss.plugin = plugin;
    }

    public void addRecipes() {
        new CopperRecipes();
        try {
            Class.forName("top.oasismc.api.customrecipe.RecipeManager");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Bukkit.getPluginManager().registerEvents(RecipeExpCheckListener.getInstance(), this);
        info("Module CustomRecipes Loaded");
    }

    public static AutoBroadCastRunnable getBroadCastRunnable() {
        return broadCastRunnable;
    }


    public static void setBroadCastRunnable(AutoBroadCastRunnable broadCastRunnable) {
        OasisEss.broadCastRunnable = broadCastRunnable;
    }

    public void regAdvancements() {
        regAdvancement("oasis.advancement.elite1", "advancement/KillEliteMob.json");
    }

    private void regAdvancement(String key, String filePath) {
        File jsonFile = new File(getDataFolder(), filePath);
        if (!jsonFile.exists())
            saveResource(filePath, false);
        String advancementJSON;
        StringBuilder builder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(jsonFile));

            String temp;
            while ((temp = reader.readLine()) != null) {
                builder.append(temp);
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        advancementJSON = builder.toString();
        NamespacedKey namespacedKey = new NamespacedKey(this, key);
        try {
            Bukkit.getUnsafe().loadAdvancement(
                    namespacedKey,
                    advancementJSON
            );
            advancementSet.add(namespacedKey);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

}
