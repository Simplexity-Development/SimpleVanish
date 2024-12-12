package simplexity.simpleVanish.saving;

import org.bukkit.entity.Player;
import simplexity.simpleVanish.commands.SubCommand;
import simplexity.simpleVanish.objects.PlayerVanishSettings;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class Cache {
    private static final HashMap<UUID, PlayerVanishSettings> cachedSettings = new HashMap<>();
    private static final HashSet<SubCommand> subCommands = new HashSet<>();
    private static final HashSet<Player> vanishedPlayers = new HashSet<>();

    public static HashSet<Player> getVanishedPlayers() {
        return vanishedPlayers;
    }

    public static HashSet<SubCommand> getSubCommands() {
        return subCommands;
    }

    public static HashMap<UUID, PlayerVanishSettings> getCachedSettings() {
        return cachedSettings;
    }

    public static PlayerVanishSettings getVanishSettings(UUID uuid) {
        if (!cachedSettings.containsKey(uuid)) {
            SqlHandler.getInstance().updateSettings(uuid);
        }
        return cachedSettings.get(uuid);
    }

    public static void updateSettingsCache(UUID uuid, PlayerVanishSettings settings) {
        cachedSettings.remove(uuid);
        cachedSettings.put(uuid, settings);
    }

    public static void removePlayerFromCache(UUID uuid) {
        cachedSettings.remove(uuid);
    }

    public static void saveSettings(UUID uuid, PlayerVanishSettings settings) {
        SqlHandler.getInstance().savePlayerSettings(uuid, settings);
        updateSettingsCache(uuid, settings);
    }
}
