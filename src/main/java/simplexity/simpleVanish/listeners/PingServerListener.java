package simplexity.simpleVanish.listeners;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.jetbrains.annotations.NotNull;
import simplexity.simpleVanish.config.ConfigHandler;
import simplexity.simpleVanish.saving.Cache;

import java.util.List;

public class PingServerListener implements Listener {
    @EventHandler
    public void onPingServer(PaperServerListPingEvent pingEvent) {
        if (Cache.getVanishedPlayers().isEmpty()) return;
        if (!ConfigHandler.getInstance().shouldRemoveFromServerList()) return;
        List<PaperServerListPingEvent.ListedPlayerInfo> onlinePlayersToShow = pingEvent.getListedPlayers();

        for (PaperServerListPingEvent.ListedPlayerInfo listedPlayerInfo : onlinePlayersToShow) {
            for (Player player : Cache.getVanishedPlayers()) {
                if (player.getUniqueId().equals(listedPlayerInfo.id())) {
                    onlinePlayersToShow.remove(listedPlayerInfo);
                    pingEvent.setNumPlayers(pingEvent.getNumPlayers() - 1);
                }
                System.out.println("Checking '" + player.getName() + "' [" + player.getUniqueId() + "] "
                        + " against '" + listedPlayerInfo.name() + "' [" + listedPlayerInfo.id() + "]");
            }
        }
    }
}
