package top.oasismc.api.config;

import com.google.common.base.Charsets;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static top.oasismc.OasisEss.*;

public class ConfigFile {

    private final File configFile;
    private FileConfiguration config;
    private final String path;

    //构造函数，将对应路径的配置文件读取或创建
    public ConfigFile(String path) {
        this.path = path;
        File dataFolder = getPlugin().getDataFolder();
        configFile = new File(dataFolder, path);
        createDefaultConfig();
    }

    //创建默认配置文件
    public void createDefaultConfig() {
        if (!configFile.exists()) {
            getPlugin().saveResource(path, false);
        }
        reloadConfig();
    }

    //获取配置文件实例
    public FileConfiguration getConfig() {
        if (configFile == null) {
            reloadConfig();
        }
        return config;
    }

    //重载配置文件
    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
        final InputStream defConfigStream = getPlugin().getResource(path);
        if (defConfigStream == null) {
            return;
        }
        config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
    }

    //保存配置文件
    public synchronized void saveConfig() {
        try {
            getConfig().save(configFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
