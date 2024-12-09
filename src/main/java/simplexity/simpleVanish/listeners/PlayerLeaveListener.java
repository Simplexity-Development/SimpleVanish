package simplexity.simpleVanish.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import simplexity.simpleVanish.SimpleVanish;
import simplexity.simpleVanish.handling.VanishHandler;
import simplexity.simpleVanish.objects.PlayerVanishSettings;
import simplexity.simpleVanish.objects.VanishSettingsPermission;
import simplexity.simpleVanish.saving.SqlHandler;

import java.util.UUID;

public class PlayerLeaveListener implements Listener {
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (!SimpleVanish.getVanishedPlayers().contains(player)) {
            SimpleVanish.getPlayersToHideFrom().remove(player);
            return;
        }
        UUID uuid = player.getUniqueId();
        PlayerVanishSettings vanishSettings = SqlHandler.getInstance().getVanishSettings(uuid);
        if (player.hasPermission(VanishSettingsPermission.SILENT_LEAVE) && vanishSettings.shouldLeaveSilently()) {
            event.quitMessage(null);
        }
        if (player.hasPermission(VanishSettingsPermission.PERSIST) && vanishSettings.shouldVanishPersist()) {
            VanishHandler.handlePlayerLeave(player);
            return;
        }
        vanishSettings.setVanished(false);
        SqlHandler.getInstance().savePlayerSettings(player.getUniqueId(), vanishSettings);
        VanishHandler.handlePlayerLeave(player);
    }


}
