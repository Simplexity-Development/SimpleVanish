package simplexity.simpleVanish;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.java.JavaPlugin;
import simplexity.simpleVanish.config.ConfigHandler;
import simplexity.simpleVanish.listeners.PlayerJoinListener;
import simplexity.simpleVanish.listeners.PlayerVanishListener;
import simplexity.simpleVanish.saving.SQLHandler;

public final class SimpleVanish extends JavaPlugin {

    private static SimpleVanish instance;
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        ConfigHandler.getInstance().loadConfigValues();
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerVanishListener(), this);
        SQLHandler.getInstance().init();
        // Plugin startup logic

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
