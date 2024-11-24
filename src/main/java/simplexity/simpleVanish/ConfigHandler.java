package simplexity.simpleVanish;

public class ConfigHandler {
    private static ConfigHandler instance;

    private boolean chatFakeJoin, chatFakeLeave, customizeFormat, removeFromTablist, glowWhileVanished;

    private String customJoin, customLeave, vanishedTablistFormat;

    public static ConfigHandler getInstance() {
        if (instance == null) instance = new ConfigHandler();
        return instance;
    }

    public void loadConfigValues(){
        SimpleVanish.getInstance().reloadConfig();
        chatFakeJoin = SimpleVanish.getInstance().getConfig().getBoolean("chat.fake-join", true);
        chatFakeLeave = SimpleVanish.getInstance().getConfig().getBoolean("chat.fake-leave", true);
        customizeFormat = SimpleVanish.getInstance().getConfig().getBoolean("customize-format", false);
        removeFromTablist = SimpleVanish.getInstance().getConfig().getBoolean("remove-from-tablist", true);
        glowWhileVanished = SimpleVanish.getInstance().getConfig().getBoolean("glow-while-vanished", true);
        customJoin = SimpleVanish.getInstance().getConfig().getString("custom-join", "<yellow><displayname> has joined!");
        customLeave = SimpleVanish.getInstance().getConfig().getString("custom-leave", "<gray><displayname> has left");
        vanishedTablistFormat = SimpleVanish.getInstance().getConfig().getString("vanished-tablist-format", "<gray>[Hidden]</gray> <i><username</i>");
    }

    public boolean isChatFakeJoin() {
        return chatFakeJoin;
    }

    public boolean isChatFakeLeave() {
        return chatFakeLeave;
    }

    public boolean isCustomizeFormat() {
        return customizeFormat;
    }

    public boolean isRemoveFromTablist() {
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
}
