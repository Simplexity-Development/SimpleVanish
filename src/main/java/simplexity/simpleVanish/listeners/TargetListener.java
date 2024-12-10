package simplexity.simpleVanish.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import simplexity.simpleVanish.objects.PlayerVanishSettings;
import simplexity.simpleVanish.objects.VanishSettingsPermission;
import simplexity.simpleVanish.saving.Cache;

import java.util.UUID;

public class TargetListener implements Listener {
    @EventHandler
    public void onTarget(EntityTargetEvent targetEvent) {
        if (!(targetEvent.getTarget() instanceof Player target)) return;
        if (!Cache.getVanishedPlayers().contains(target)) return;
        if (targetingEnabled(target)) return;
        targetEvent.setCancelled(true);
    }

    private boolean targetingEnabled(Player target) {
        if (!target.hasPermission(VanishSettingsPermission.MOBS_TARGET)) return false;
        UUID uuid = target.getUniqueId();
        PlayerVanishSettings vanishSettings = Cache.getVanishSettings(uuid);
        return vanishSettings.shouldMobTarget();
    }
}
