package simplexity.simplevanish.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import simplexity.simplevanish.config.Message;
import simplexity.simplevanish.handling.MessageHandler;
import simplexity.simplevanish.handling.VanishHandler;
import simplexity.simplevanish.objects.PlayerVanishSettings;
import simplexity.simplevanish.objects.VanishPermission;
import simplexity.simplevanish.saving.Cache;

public class PlayerJoinListener implements Listener {


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        VanishHandler.getInstance().hideCurrentlyVanishedUsers(player);
        debug("Player: " + player.getName() +
              ", currentlyVanished: " + !ListenerUtils.userNotVanished(player));
        if (shouldVanish(player)) {
            debug("Current join message: " + event.joinMessage());
            event.joinMessage(null);
            debug("Set join message to null, current join message according to the event: '" + event.joinMessage() + "'");
            VanishHandler.getInstance().runVanishEvent(player, false,
                    Message.VIEW_USER_JOINED_SILENTLY.getMessage());
            debug("Ran vanish event on player %s, uuid: %s", player.getName(), player.getUniqueId());
            return;
        }
        if (joinSilently(player)) {
            debug("Current join message: " + event.joinMessage());
            event.joinMessage(null);
            debug("Set join message to null, current join message according to the event: '" + event.joinMessage() + "'");
            MessageHandler.sendAdminNotification(player,
                    Message.VIEW_USER_JOINED_SILENTLY.getMessage());
        }
    }

    private boolean shouldVanish(@NotNull Player player) {
        if (!player.hasPermission(VanishPermission.VANISH_COMMAND)) return false;
        if (!player.hasPermission(VanishPermission.PERSIST)) return false;
        PlayerVanishSettings vanishSettings = Cache.getVanishSettings(player.getUniqueId());
        if (!vanishSettings.shouldVanishPersist()) return false;
        return (vanishSettings.isVanished());
    }

    private boolean joinSilently(@NotNull Player player) {
        if (!player.hasPermission(VanishPermission.SILENT_JOIN)) return false;
        PlayerVanishSettings vanishSettings = Cache.getVanishSettings(player.getUniqueId());
        return vanishSettings.shouldJoinSilently();
    }

    private void debug(@NotNull String message, @Nullable Object... args) {
        MessageHandler.debug("[PlayerJoin] ", message, args);
    }


}
