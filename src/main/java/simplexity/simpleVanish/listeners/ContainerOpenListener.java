package simplexity.simpleVanish.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import simplexity.simpleVanish.config.ConfigHandler;
import simplexity.simpleVanish.objects.PlayerVanishSettings;
import simplexity.simpleVanish.objects.VanishPermission;
import simplexity.simpleVanish.saving.Cache;

public class ContainerOpenListener implements Listener {
    @EventHandler
    public void onContainerOpen(InventoryOpenEvent openEvent) {
        if (ListenerUtils.shouldEarlyReturn(openEvent.getPlayer())) return;
        Player player = (Player) openEvent.getPlayer();
        if (interactionEnabled(player)) return;
        Inventory inventory = openEvent.getInventory();
        if (!shouldPreventOpen(inventory)) return;
        openEvent.setCancelled(true);
        // Note to future or other versions of me: Do not attempt to open the same inventory you literally just prevented opening
        // It will just make this be called recursively
    }

    private boolean interactionEnabled(Player player) {
        PlayerVanishSettings vanishSettings = Cache.getVanishSettings(player.getUniqueId());
        if (player.hasPermission(VanishPermission.OPEN_CONTAINERS) && vanishSettings.doesContainerOpenAnimation()) return true;
        if (player.hasPermission(VanishPermission.OPEN_CONTAINERS) && player.isSneaking()) return true;
        return false;
    }

    private boolean shouldPreventOpen(Inventory inventory) {
        Location location = inventory.getLocation();
        if (location == null) return false;
        Material material = location.getBlock().getType();
        return ConfigHandler.getInstance().getContainersToBlock().contains(material);
    }
}
