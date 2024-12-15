package simplexity.simpleVanish.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import simplexity.simpleVanish.config.Message;
import simplexity.simpleVanish.handling.MessageHandler;
import simplexity.simpleVanish.handling.VanishHandler;
import simplexity.simpleVanish.objects.PlayerVanishSettings;
import simplexity.simpleVanish.objects.VanishPermission;
import simplexity.simpleVanish.saving.Cache;

public class PlayerJoinListener implements Listener {


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        VanishHandler.getInstance().hideCurrentlyVanishedUsers(player);
        if (shouldVanish(player)) {
            event.joinMessage(null);
            VanishHandler.getInstance().runVanishEvent(player, false,
                    Message.VIEW_USER_JOINED_SILENTLY.getMessage());
            return;
        }
        if (joinSilently(player)) {
            event.joinMessage(null);
            MessageHandler.getInstance().sendAdminNotification(player,
                    Message.VIEW_USER_JOINED_SILENTLY.getMessage());
        }
    }

    private boolean shouldVanish(Player player) {
        if (!player.hasPermission(VanishPermission.VANISH_COMMAND)) return false;
        if (!player.hasPermission(VanishPermission.PERSIST)) return false;
        PlayerVanishSettings vanishSettings = Cache.getVanishSettings(player.getUniqueId());
        if (!vanishSettings.shouldVanishPersist()) return false;
        return (vanishSettings.isVanished());
    }

    private boolean joinSilently(Player player) {
        if (!player.hasPermission(VanishPermission.SILENT_JOIN)) return false;
        PlayerVanishSettings vanishSettings = Cache.getVanishSettings(player.getUniqueId());
        return vanishSettings.shouldJoinSilently();
    }


}
