package simplexity.simplevanish.config;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import simplexity.simplevanish.SimpleVanish;
import simplexity.simplevanish.handling.ScheduleHandler;

import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public class ConfigHandler {
    private final Logger logger = SimpleVanish.getInstance().getLogger();
    private static ConfigHandler instance;

    private boolean chatFakeJoin;
    private boolean chatFakeLeave;
    private boolean removeFromTablist;
    private boolean changeTablistFormat;
    private boolean glowWhileVanished;
    private boolean mysqlEnabled;
    private boolean removeFromServerList;
    private boolean removeFromSleepingPlayers;
    private boolean preventMessagingVanished;
    private boolean customJoinLeave;
    private boolean debug;
    private Long remindInterval;
    private String mysqlIP, databaseName, databaseUsername, databasePassword, customJoinMessage, customLeaveMessage;

    private final HashSet<Material> containersToBlock = new HashSet<>();

    public static ConfigHandler getInstance() {
        if (instance == null) instance = new ConfigHandler();
        return instance;
    }

    public void loadConfigValues() {
        SimpleVanish.getInstance().reloadConfig();
        LocaleHandler.getInstance().reloadLocale();
        ScheduleHandler.getInstance().stopScheduler();
        FileConfiguration config = SimpleVanish.getInstance().getConfig();
        containersToBlock.clear();
        reloadContainersToBlock(config);
        debug = config.getBoolean("debug", false);
        mysqlEnabled = config.getBoolean("mysql.enabled", false);
        mysqlIP = config.getString("mysql.ip", "localhost:3306");
        databaseName = config.getString("mysql.database-name", "vanish");
        databaseUsername = config.getString("mysql.username", "username1");
        databasePassword = config.getString("mysql.password", "badpassword!");
        chatFakeJoin = config.getBoolean("chat.fake-join-on-un-vanish", false);
        chatFakeLeave = config.getBoolean("chat.fake-leave-on-vanish", false);
        preventMessagingVanished = config.getBoolean("chat.prevent-direct-messages", true);
        customJoinLeave = config.getBoolean("chat.custom-message.enabled", false);
        customJoinMessage = config.getString("chat.custom-message.join", "<gray>[<green>+</green>]</gray> <displayname> <green>joined the game");
        customLeaveMessage = config.getString("chat.custom-message.leave", "<gray>[<dark_red>-</dark_red>]</gray> <displayname> <gray>left the game");
        removeFromTablist = config.getBoolean("remove-from.tablist", true);
        removeFromServerList = config.getBoolean("remove-from.server-list", true);
        removeFromSleepingPlayers = config.getBoolean("remove-from.required-sleeping-players", true);
        changeTablistFormat = config.getBoolean("view.change-tablist", false);
        glowWhileVanished = config.getBoolean("view.glow-while-vanished", true);
        boolean remindWhileVanished = config.getBoolean("remind-while-vanished", true);
        remindInterval = config.getLong("remind-interval-in-seconds", 10);
        if (remindWhileVanished) ScheduleHandler.getInstance().startScheduler();
    }

    private void reloadContainersToBlock(FileConfiguration config) {
        List<String> containerList = config.getStringList("prevent-opening");
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

    public boolean shouldRemoveFromTablist() {
        return removeFromTablist;
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

    public boolean shouldPreventMessagingVanished() {
        return preventMessagingVanished;
    }

    public boolean shouldRemoveFromSleepingPlayers() {
        return removeFromSleepingPlayers;
    }

    public Long getRemindInterval() {
        return remindInterval;
    }

    public boolean shouldChangeTablistFormat() {
        return changeTablistFormat;
    }

    public boolean shouldGlowWhileVanished() {
        return glowWhileVanished;
    }

    public String getCustomJoinMessage() {
        return customJoinMessage;
    }

    public String getCustomLeaveMessage() {
        return customLeaveMessage;
    }

    public boolean isCustomJoinLeave() {
        return customJoinLeave;
    }

    public boolean isDebug() {
        return debug;
    }
}
