package simplexity.simplevanish.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import simplexity.simplevanish.saving.Cache;

public class ListenerUtils {

    public static boolean userNotVanished(Entity entity) {
        if (!(entity instanceof Player player)) return true;
        return userNotVanished(player);
    }

    public static boolean userNotVanished(Player player) {
        return (!Cache.getVanishedPlayers().contains(player));
    }
}
