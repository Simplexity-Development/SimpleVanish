package simplexity.simpleVanish;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.java.JavaPlugin;
import simplexity.simpleVanish.commands.Vanish;
import simplexity.simpleVanish.commands.VanishSettings;
import simplexity.simpleVanish.config.ConfigHandler;
import simplexity.simpleVanish.listeners.AttackListener;
import simplexity.simpleVanish.listeners.ItemPickupListener;
import simplexity.simpleVanish.listeners.PlayerJoinListener;
import simplexity.simpleVanish.listeners.PlayerLeaveListener;
import simplexity.simpleVanish.listeners.TargetListener;
import simplexity.simpleVanish.saving.Cache;
import simplexity.simpleVanish.saving.SqlHandler;

public final class SimpleVanish extends JavaPlugin {

    private static SimpleVanish instance;
    private static boolean papiEnabled = false;
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    public static boolean isPapiEnabled() {
        return papiEnabled;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        ConfigHandler.getInstance().loadConfigValues();
        SqlHandler.getInstance().init();
        registerCommands();
        registerSubCommands();
        registerListeners();
        checkForPapi();
        // Plugin startup logic

    }

    private void registerCommands() {
        this.getCommand("vanish").setExecutor(new Vanish());
        this.getCommand("vanish-settings").setExecutor(new VanishSettings());
    }

    private void registerSubCommands() {
        Cache.getSubCommands().clear();
    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new AttackListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerLeaveListener(), this);
        this.getServer().getPluginManager().registerEvents(new TargetListener(), this);
        this.getServer().getPluginManager().registerEvents(new ItemPickupListener(), this);
    }

    private void checkForPapi() {
        if (this.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            papiEnabled = true;
        } else {
            this.getLogger().info("You do not have PlaceholderAPI loaded on your server. Any PlaceholderAPI placeholders used in this plugin's messages, will not work.");
        }
    }

    public static SimpleVanish getInstance() {
        return instance;
    }

    public static MiniMessage getMiniMessage() {
        return miniMessage;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
