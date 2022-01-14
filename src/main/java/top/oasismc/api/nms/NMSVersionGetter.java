package top.oasismc.api.nms;

import org.bukkit.Bukkit;

public class NMSVersionGetter {

    private static final String version;

    static {
        version = Bukkit.getServer().getClass().getPackage().getName().substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf('.') + 1);
    }

    public static String getVersion() {
        return version;
    }

}
