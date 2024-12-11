package simplexity.simpleVanish.listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import simplexity.simpleVanish.config.LocaleHandler;
import simplexity.simpleVanish.handling.VanishHandler;
import simplexity.simpleVanish.objects.PlayerVanishSettings;
import simplexity.simpleVanish.objects.VanishPermission;
import simplexity.simpleVanish.saving.Cache;
import simplexity.simpleVanish.saving.SqlHandler;

import java.util.UUID;

public class PlayerLeaveListener implements Listener {
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (!Cache.getVanishedPlayers().contains(player)) {
            Cache.getPlayersToHideFrom().remove(player);
            return;
        }
        UUID uuid = player.getUniqueId();
        PlayerVanishSettings vanishSettings = Cache.getVanishSettings(uuid);
        if (player.hasPermission(VanishPermission.SILENT_LEAVE) && vanishSettings.shouldLeaveSilently()) {
            event.quitMessage(null);
            sendMessageToPlayersWithViewPerms(player);
        }
        if (player.hasPermission(VanishPermission.PERSIST) && vanishSettings.shouldVanishPersist()) {
            VanishHandler.getInstance().handlePlayerLeave(player);
            return;
        }
        vanishSettings.setVanished(false);
        SqlHandler.getInstance().savePlayerSettings(player.getUniqueId(), vanishSettings);
        VanishHandler.getInstance().handlePlayerLeave(player);
    }

    private void sendMessageToPlayersWithViewPerms(Player player) {
        Component message = VanishHandler.getInstance().parsePlayerMessage(player,
                LocaleHandler.Message.VIEW_USER_LEFT_SILENTLY.getMessage());
        if (message == null) return;
        for (Player viewPlayer : Cache.getViewingPlayers()) {
            viewPlayer.sendMessage(message);
        }
    }


}
