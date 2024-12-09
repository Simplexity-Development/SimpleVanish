package simplexity.simpleVanish.config;

import simplexity.simpleVanish.SimpleVanish;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ConfigHandler {
    private static ConfigHandler instance;

    private boolean chatFakeJoin, chatFakeLeave, customizeFormat, removeFromTablist, glowWhileVanished, mysqlEnabled;
    private String customJoin, customLeave, vanishedTablistFormat, mysqlIP, databaseName, databaseUsername, databasePassword;

    private final ArrayList<UUID> playersToHideFrom = new ArrayList<>();
    public static ConfigHandler getInstance() {
        if (instance == null) instance = new ConfigHandler();
        return instance;
    }

    public void loadConfigValues(){
        SimpleVanish.getInstance().reloadConfig();
        reloadPlayersToHideFrom();

        chatFakeJoin = SimpleVanish.getInstance().getConfig().getBoolean("chat.fake-join", true);
        chatFakeLeave = SimpleVanish.getInstance().getConfig().getBoolean("chat.fake-leave", true);
        customizeFormat = SimpleVanish.getInstance().getConfig().getBoolean("customize-format", false);
        removeFromTablist = SimpleVanish.getInstance().getConfig().getBoolean("remove-from-tablist", true);
        glowWhileVanished = SimpleVanish.getInstance().getConfig().getBoolean("view.glow-while-vanished", true);
        mysqlEnabled = SimpleVanish.getInstance().getConfig().getBoolean("mysql.enabled", false);
        customJoin = SimpleVanish.getInstance().getConfig().getString("custom-join", "<yellow><displayname> has joined!");
        customLeave = SimpleVanish.getInstance().getConfig().getString("custom-leave", "<gray><displayname> has left");
        vanishedTablistFormat = SimpleVanish.getInstance().getConfig().getString("view.tablist-format", "<gray>[Hidden]</gray> <i><username</i>");
        mysqlIP = SimpleVanish.getInstance().getConfig().getString("mysql.ip", "localhost:3306");
        databaseName = SimpleVanish.getInstance().getConfig().getString("mysql.database-name", "vanish");
        databaseUsername = SimpleVanish.getInstance().getConfig().getString("mysql.username", "username1");
        databasePassword = SimpleVanish.getInstance().getConfig().getString("mysql.password", "badpassword!");
    }

    private void reloadPlayersToHideFrom(){
        playersToHideFrom.clear();
        SimpleVanish.getInstance().getServer().getOnlinePlayers().forEach(player
                -> playersToHideFrom.add(player.getUniqueId()));
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

    public List<UUID> getPlayersToHideFrom() {
        return playersToHideFrom;
    }
}
