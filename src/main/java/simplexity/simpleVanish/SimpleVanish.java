package simplexity.simpleVanish;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import simplexity.simpleVanish.commands.Vanish;
import simplexity.simpleVanish.config.ConfigHandler;
import simplexity.simpleVanish.listeners.PlayerJoinListener;
import simplexity.simpleVanish.saving.SQLHandler;

import java.util.HashSet;

public final class SimpleVanish extends JavaPlugin {

    private static SimpleVanish instance;
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();
    private static final HashSet<Player> playersToHideFrom = new HashSet<>();
    private static final HashSet<Player> vanishedPlayers = new HashSet<>();


    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        ConfigHandler.getInstance().loadConfigValues();
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        this.getCommand("vanish").setExecutor(new Vanish());
        SQLHandler.getInstance().init();
        // Plugin startup logic

    }

    public static SimpleVanish getInstance() {
        return instance;
    }

    public static MiniMessage getMiniMessage() {
        return miniMessage;
    }

    public static HashSet<Player> getPlayersToHideFrom() {
        return playersToHideFrom;
    }

    public static HashSet<Player> getVanishedPlayers() {
        return vanishedPlayers;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
