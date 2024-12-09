package simplexity.simpleVanish.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import simplexity.simpleVanish.SimpleVanish;
import simplexity.simpleVanish.objects.PlayerVanishSettings;
import simplexity.simpleVanish.objects.VanishSettingsPermission;
import simplexity.simpleVanish.saving.SqlHandler;

import java.util.UUID;

public class ItemPickupListener implements Listener {
    @EventHandler
    public void onItemPickup(EntityPickupItemEvent pickupEvent) {
        if (!(pickupEvent.getEntity() instanceof Player player)) return;
        if (!SimpleVanish.getVanishedPlayers().contains(player)) return;
        if (!player.hasPermission(VanishSettingsPermission.PICK_UP_ITEMS)) return;
        UUID playerUuid = player.getUniqueId();
        PlayerVanishSettings vanishSettings = SqlHandler.getInstance().getVanishSettings(playerUuid);
        if (vanishSettings.canPickupItems()) return;
        pickupEvent.setCancelled(true);
    }
}
