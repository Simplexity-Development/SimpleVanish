package simplexity.simplevanish.hooks;

import net.pl3x.map.core.Pl3xMap;
import org.bukkit.entity.Player;

public class Pl3xmapIntegration {

    public static void hideVanishedPlayer(Player player){
        net.pl3x.map.core.player.Player pl3xPlayer = Pl3xMap.api().getPlayerRegistry().get(player.getUniqueId());
        if (pl3xPlayer == null) return;
        if (pl3xPlayer.isHidden()) return;
        pl3xPlayer.setHidden(true, false);
    }

    public static void unHideVanishedPlayer(Player player){
        net.pl3x.map.core.player.Player pl3xPlayer = Pl3xMap.api().getPlayerRegistry().get(player.getUniqueId());
        if (pl3xPlayer == null) return;
        if (!pl3xPlayer.isHidden() || pl3xPlayer.isPersistentlyHidden()) return;
        pl3xPlayer.setHidden(false, false);
    }
}
