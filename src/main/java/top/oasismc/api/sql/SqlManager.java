package top.oasismc.api.sql;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class SqlManager {

    public SqlManager() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            Bukkit.getLogger().info(ChatColor.RED + "You");
        }
    }

}
