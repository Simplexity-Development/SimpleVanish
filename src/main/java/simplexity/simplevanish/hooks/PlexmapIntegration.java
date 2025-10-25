package simplexity.simplevanish.hooks;

import net.pl3x.map.core.Pl3xMap;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlexmapIntegration {

    private static final Set<UUID> flagAlreadyExisted = new HashSet<>();

    public static void hideVanishedPlayer(@NotNull Player player){
        net.pl3x.map.core.player.Player plexPlayer = Pl3xMap.api().getPlayerRegistry().get(player.getUniqueId());
        if (plexPlayer == null) return;
        if (plexPlayer.isHidden()) {
            flagAlreadyExisted.add(player.getUniqueId());
            return;
        }
        plexPlayer.setHidden(true, false);
    }

    public static void unHideVanishedPlayer(@NotNull Player player){
        net.pl3x.map.core.player.Player plexPlayer = Pl3xMap.api().getPlayerRegistry().get(player.getUniqueId());
        UUID playerUuid = player.getUniqueId();
        if (plexPlayer == null) {
            flagAlreadyExisted.remove(playerUuid);
            return;
        }
        if (flagAlreadyExisted.contains(playerUuid)) {
            flagAlreadyExisted.remove(playerUuid);
            return;
        }
        if (!plexPlayer.isHidden() || plexPlayer.isPersistentlyHidden()) return;
        plexPlayer.setHidden(false, false);
    }
}
