package simplexity.simpleVanish.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import simplexity.simpleVanish.saving.Cache;

public class ListenerUtils {

    public static boolean shouldEarlyReturn(Entity entity) {
        if (!(entity instanceof Player player)) return true;
        return shouldEarlyReturn(player);
    }

    public static boolean shouldEarlyReturn(Player player) {
        return (!Cache.getVanishedPlayers().contains(player));
    }
}
