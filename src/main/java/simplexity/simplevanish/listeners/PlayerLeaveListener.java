package simplexity.simplevanish.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import simplexity.simplevanish.config.Message;
import simplexity.simplevanish.handling.MessageHandler;
import simplexity.simplevanish.handling.VanishHandler;
import simplexity.simplevanish.objects.PlayerVanishSettings;
import simplexity.simplevanish.objects.VanishPermission;
import simplexity.simplevanish.saving.Cache;

public class PlayerLeaveListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (Cache.getVanishedPlayers().contains(player) || leaveSilently(player)) {
            event.quitMessage(null);
            MessageHandler.getInstance().sendAdminNotification(player,
                    Message.VIEW_USER_LEFT_SILENTLY.getMessage());
        }
        VanishHandler.getInstance().handlePlayerLeave(player);
    }

    private boolean leaveSilently(Player player) {
        if (!player.hasPermission(VanishPermission.SILENT_LEAVE)) return false;
        PlayerVanishSettings vanishSettings = Cache.getVanishSettings(player.getUniqueId());
        return vanishSettings.shouldLeaveSilently();
    }


}
