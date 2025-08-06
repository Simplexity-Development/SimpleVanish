package simplexity.simplevanish.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import simplexity.simplevanish.objects.PlayerVanishSettings;
import simplexity.simplevanish.objects.VanishPermission;
import simplexity.simplevanish.saving.Cache;

public class TargetListener implements Listener {
    @EventHandler
    public void onTarget(EntityTargetEvent targetEvent) {
        if (ListenerUtils.userNotVanished(targetEvent.getTarget())) return;
        if (targetingEnabled((Player) targetEvent.getTarget())) return;
        targetEvent.setCancelled(true);
    }

    private boolean targetingEnabled(Player player) {
        PlayerVanishSettings vanishSettings = Cache.getVanishSettings(player.getUniqueId());
        return player.hasPermission(VanishPermission.MOBS_TARGET) && vanishSettings.shouldMobsTarget();
    }
}
