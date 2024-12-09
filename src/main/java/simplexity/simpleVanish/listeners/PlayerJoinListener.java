package simplexity.simpleVanish.listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import simplexity.simpleVanish.SimpleVanish;
import simplexity.simpleVanish.config.LocaleHandler;
import simplexity.simpleVanish.handling.VanishHandler;
import simplexity.simpleVanish.objects.PlayerVanishSettings;
import simplexity.simpleVanish.saving.SqlHandler;

public class PlayerJoinListener implements Listener {
    private static final String SILENT_JOIN = "vanish.silent-join";
    private static final String VANISH_VIEW = "vanish.view";
    private static final String VANISH_COMMAND = "vanish.command";

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission(VANISH_VIEW)) {
            hideVanishedPlayers(player);
        } else {
            SimpleVanish.getViewingPlayers().add(player);
        }
        PlayerVanishSettings vanishSettings = SqlHandler.getInstance().getVanishSettings(player.getUniqueId());
        if (player.hasPermission(SILENT_JOIN) && vanishSettings.shouldJoinSilently()) {
            event.joinMessage(null);
            sendMessageToPlayersWithViewPerms(player);
        }
        if (shouldNotBeVanished(player, vanishSettings)) return;
        VanishHandler.getInstance().runVanishEvent(player, false);
    }

    private boolean shouldNotBeVanished(Player player, PlayerVanishSettings vanishSettings) {
        if (!player.hasPermission(VANISH_COMMAND)) return true;
        if (!vanishSettings.shouldVanishPersist()) return true;
        return !vanishSettings.isVanished();
    }

    private void hideVanishedPlayers(Player player) {
        SimpleVanish.getPlayersToHideFrom().add(player);
        for (Player vanishedPlayer : SimpleVanish.getVanishedPlayers()) {
            player.hidePlayer(SimpleVanish.getInstance(), vanishedPlayer);
            player.unlistPlayer(vanishedPlayer);
        }
    }

    private void sendMessageToPlayersWithViewPerms(Player player) {
        Component message = VanishHandler.getInstance().parsePlayerMessage(player,
                LocaleHandler.Message.VIEW_USER_JOINED_SILENTLY.getMessage());

        if (message == null) return;
        for (Player viewPlayer : SimpleVanish.getViewingPlayers()) {
            viewPlayer.sendMessage(message);
        }
    }
}
