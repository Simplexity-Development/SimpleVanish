package simplexity.simplevanish.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import simplexity.simplevanish.objects.PlayerVanishSettings;
import simplexity.simplevanish.objects.VanishPermission;
import simplexity.simplevanish.saving.Cache;

public class ItemPickupListener implements Listener {
    @EventHandler
    public void onItemPickup(EntityPickupItemEvent pickupEvent) {
        if (ListenerUtils.userNotVanished(pickupEvent.getEntity())) return;
        if (pickupEnabled((Player) pickupEvent.getEntity())) return;
        pickupEvent.setCancelled(true);
    }

    private boolean pickupEnabled(Player player) {
        PlayerVanishSettings vanishSettings = Cache.getVanishSettings(player.getUniqueId());
        return player.hasPermission(VanishPermission.PICK_UP_ITEMS) && vanishSettings.shouldPickupItems();
    }
}
