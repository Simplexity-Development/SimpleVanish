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
    private static final HashSet<Player> playersToHideFrom = new HashSet<>();
    private static final HashSet<Player> vanishedPlayers = new HashSet<>();
    private static final HashSet<Player> viewingPlayers = new HashSet<>();
    private static final HashSet<Player> flyingPlayers = new HashSet<>();
    private static final HashSet<Player> InvulnerablePlayers = new HashSet<>();
    private static final HashSet<Player> nightVisionPlayers = new HashSet<>();

    public static HashSet<Player> getPlayersToHideFrom() {
        return playersToHideFrom;
    }

    public static HashSet<Player> getVanishedPlayers() {
        return vanishedPlayers;
    }

    public static HashSet<Player> getViewingPlayers() {
        return viewingPlayers;
    }

    public static HashSet<SubCommand> getSubCommands() {
        return subCommands;
    }

    public static HashSet<Player> getFlyingPlayers() {
        return flyingPlayers;
    }

    public static HashSet<Player> getInvulnerablePlayers() {
        return InvulnerablePlayers;
    }

    public static HashSet<Player> getNightVisionPlayers() {
        return nightVisionPlayers;
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
}
