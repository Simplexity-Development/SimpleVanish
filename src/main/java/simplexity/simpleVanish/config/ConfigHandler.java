package simplexity.simpleVanish.config;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import simplexity.simpleVanish.SimpleVanish;

import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

public class ConfigHandler {
    private final Logger logger = SimpleVanish.getInstance().getLogger();
    private static ConfigHandler instance;

    private boolean chatFakeJoin, chatFakeLeave, customizeFormat, removeFromTablist, glowWhileVanished, mysqlEnabled,
            removeFromServerList;
    private String customJoin, customLeave, vanishedTablistFormat, mysqlIP, databaseName, databaseUsername, databasePassword;

    private final HashSet<Material> containersToBlock = new HashSet<>();

    public static ConfigHandler getInstance() {
        if (instance == null) instance = new ConfigHandler();
        return instance;
    }

    public void loadConfigValues() {
        SimpleVanish.getInstance().reloadConfig();
        FileConfiguration config = SimpleVanish.getInstance().getConfig();
        containersToBlock.clear();
        chatFakeJoin = config.getBoolean("chat.fake-join", true);
        chatFakeLeave = config.getBoolean("chat.fake-leave", true);
        customizeFormat = config.getBoolean("customize-format", false);
        removeFromTablist = config.getBoolean("remove-from-tablist", true);
        glowWhileVanished = config.getBoolean("view.glow-while-vanished", true);
        mysqlEnabled = config.getBoolean("mysql.enabled", false);
        removeFromServerList = config.getBoolean("remove-from-server-list", false);
        customJoin = config.getString("custom-join", "<yellow><displayname> has joined!");
        customLeave = config.getString("custom-leave", "<gray><displayname> has left");
        vanishedTablistFormat = config.getString("view.tablist-format", "<gray>[Hidden]</gray> <i><username</i>");
        mysqlIP = config.getString("mysql.ip", "localhost:3306");
        databaseName = config.getString("mysql.database-name", "vanish");
        databaseUsername = config.getString("mysql.username", "username1");
        databasePassword = config.getString("mysql.password", "badpassword!");
    }

    private void reloadContainersToBlock(FileConfiguration config) {
        List<String> containerList = config.getStringList("prevent-container-animations-for");
        if (containerList.isEmpty()) {
            containersToBlock.clear();
            return;
        }
        for (String container : containerList) {
            Material material = Material.getMaterial(container);
            if (material == null) {
                logger.info(container + " is not a valid material, please check your config");
                continue;
            }
            containersToBlock.add(material);
        }
    }

    public boolean shouldChatFakeJoin() {
        return chatFakeJoin;
    }

    public boolean shouldChatFakeLeave() {
        return chatFakeLeave;
    }

    public boolean shouldCustomizeFormat() {
        return customizeFormat;
    }

    public boolean shouldRemoveFromTablist() {
        return removeFromTablist;
    }

    public boolean isGlowWhileVanished() {
        return glowWhileVanished;
    }

    public String getCustomJoin() {
        return customJoin;
    }

    public String getCustomLeave() {
        return customLeave;
    }

    public String getVanishedTablistFormat() {
        return vanishedTablistFormat;
    }

    public boolean isMysqlEnabled() {
        return mysqlEnabled;
    }

    public String getMysqlIP() {
        return mysqlIP;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getDatabaseUsername() {
        return databaseUsername;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public HashSet<Material> getContainersToBlock() {
        return containersToBlock;
    }

    public boolean shouldRemoveFromServerList() {
        return removeFromServerList;
    }
}
