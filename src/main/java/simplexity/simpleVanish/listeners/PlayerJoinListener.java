package simplexity.simpleVanish.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import simplexity.simpleVanish.SimpleVanish;
import simplexity.simpleVanish.commands.settings.SilentJoin;
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
        PlayerVanishSettings vanishSettings = SqlHandler.getInstance().getVanishSettings(player.getUniqueId());
        if (player.hasPermission(SILENT_JOIN) && vanishSettings.shouldJoinSilently()) {
            event.joinMessage(null);
        }
        if (!player.hasPermission(VANISH_VIEW)) {
            SimpleVanish.getPlayersToHideFrom().add(player);
        }
        if (!shouldBeVanished(player, vanishSettings)) return;
        VanishHandler.runVanishEvent(player);

    }

    private boolean shouldBeVanished(Player player, PlayerVanishSettings vanishSettings) {
        if (!player.hasPermission(VANISH_COMMAND)) return false;
        if (!vanishSettings.shouldVanishPersist()) return false;
        if (!vanishSettings.isVanished()) return false;
        return true;
    }
}
