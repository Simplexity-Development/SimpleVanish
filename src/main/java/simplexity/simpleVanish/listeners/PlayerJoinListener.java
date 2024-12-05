package simplexity.simpleVanish.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import simplexity.simpleVanish.SimpleVanish;

public class PlayerJoinListener implements Listener {
    private static final String SILENT_JOIN = "vanish.silent-join";
    private static final String VANISH_VIEW = "vanish.view";
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission(SILENT_JOIN)) {
            event.joinMessage(null);
        }
        if (player.hasPermission(VANISH_VIEW)) {
            SimpleVanish.getVanishedPlayers().add(player);
        } else {
            SimpleVanish.getPlayersToHideFrom().add(player);
        }
    }
}
