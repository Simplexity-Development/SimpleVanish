package simplexity.simpleVanish.handling;

import org.bukkit.entity.Player;
import simplexity.simpleVanish.SimpleVanish;
import simplexity.simpleVanish.events.PlayerUnvanishEvent;
import simplexity.simpleVanish.events.PlayerVanishEvent;
import simplexity.simpleVanish.objects.PlayerVanishSettings;
import simplexity.simpleVanish.saving.SqlHandler;

public class VanishHandler {
    public static void runVanishEvent(Player player) {
        PlayerVanishSettings settings = SqlHandler.getInstance().getVanishSettings(player.getUniqueId());
        PlayerVanishEvent vanishEvent = new PlayerVanishEvent(player, settings);
        SimpleVanish.getInstance().getServer().getPluginManager().callEvent(vanishEvent);
        if (vanishEvent.isCancelled()) return;
        for (Player hideFromPlayer : SimpleVanish.getPlayersToHideFrom()) {
            hideFromPlayer.hidePlayer(SimpleVanish.getInstance(), player);
            hideFromPlayer.unlistPlayer(player);
        }
        SimpleVanish.getVanishedPlayers().add(player);
    }

    public static void runUnvanishEvent(Player player) {
        PlayerUnvanishEvent unvanishEvent = new PlayerUnvanishEvent(player);
        SimpleVanish.getInstance().getServer().getPluginManager().callEvent(unvanishEvent);
        if (unvanishEvent.isCancelled()) return;
        for (Player hideFromPlayer : SimpleVanish.getPlayersToHideFrom()) {
            hideFromPlayer.showPlayer(SimpleVanish.getInstance(), player);
            hideFromPlayer.listPlayer(player);
        }
        SimpleVanish.getVanishedPlayers().remove(player);
    }
}
