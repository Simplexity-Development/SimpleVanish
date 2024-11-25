package simplexity.simpleVanish.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import simplexity.simpleVanish.events.PlayerVanishEvent;

public class PlayerJoinListener implements Listener {
    private static final String SILENT_JOIN = "vanish.silent-join";
    private static final String VANISH_VIEW = "vanish.view";
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission(SILENT_JOIN)) {
            event.joinMessage(null);
        }
        Bukkit.getPluginManager().callEvent(new PlayerVanishEvent(player));
    }
}
