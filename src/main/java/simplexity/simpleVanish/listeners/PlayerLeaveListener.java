package simplexity.simpleVanish.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import simplexity.simpleVanish.config.LocaleHandler;
import simplexity.simpleVanish.handling.MessageHandler;
import simplexity.simpleVanish.handling.VanishHandler;
import simplexity.simpleVanish.objects.PlayerVanishSettings;
import simplexity.simpleVanish.objects.VanishPermission;
import simplexity.simpleVanish.saving.Cache;

public class PlayerLeaveListener implements Listener {
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (Cache.getVanishedPlayers().contains(player) || leaveSilently(player)) {
            event.quitMessage(null);
            MessageHandler.getInstance().sendAdminNotification(player,
                    LocaleHandler.Message.VIEW_USER_LEFT_SILENTLY.getMessage());
        }
        VanishHandler.getInstance().handlePlayerLeave(player);
    }

    private boolean leaveSilently(Player player) {
        if (!player.hasPermission(VanishPermission.SILENT_LEAVE)) return false;
        PlayerVanishSettings vanishSettings = Cache.getVanishSettings(player.getUniqueId());
        return vanishSettings.shouldLeaveSilently();
    }


}
