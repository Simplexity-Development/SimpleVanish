package simplexity.simpleVanish.listeners;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import simplexity.simpleVanish.config.ConfigHandler;
import simplexity.simpleVanish.saving.Cache;

import java.util.Iterator;
import java.util.List;

public class PingServerListener implements Listener {
    @EventHandler
    public void onPingServer(PaperServerListPingEvent pingEvent) {
        if (Cache.getVanishedPlayers().isEmpty()) return;
        if (!ConfigHandler.getInstance().shouldRemoveFromServerList()) return;
        List<PaperServerListPingEvent.ListedPlayerInfo> onlinePlayersToShow = pingEvent.getListedPlayers();
        Iterator<PaperServerListPingEvent.ListedPlayerInfo> iterator = onlinePlayersToShow.iterator();
        while (iterator.hasNext()) {
            PaperServerListPingEvent.ListedPlayerInfo listedPlayerInfo = iterator.next();
            for (Player player : Cache.getVanishedPlayers()) {
                if (player.getUniqueId().equals(listedPlayerInfo.id())) {
                    iterator.remove();
                    pingEvent.setNumPlayers(pingEvent.getNumPlayers() - 1);
                    break;
                }
            }
        }
    }
}
