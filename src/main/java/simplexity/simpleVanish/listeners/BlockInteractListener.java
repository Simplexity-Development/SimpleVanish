package simplexity.simpleVanish.listeners;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import simplexity.simpleVanish.config.ConfigHandler;
import simplexity.simpleVanish.objects.PlayerVanishSettings;
import simplexity.simpleVanish.objects.VanishPermission;
import simplexity.simpleVanish.saving.Cache;

public class BlockInteractListener implements Listener {


    @EventHandler
    public void onContainerOpen(PlayerInteractEvent interactEvent) {
        if (!interactEvent.hasBlock()) return;
        if (interactEvent.getHand() == null) return;
        if (interactEvent.getHand().equals(EquipmentSlot.OFF_HAND)) return;
        Player player = interactEvent.getPlayer();
        if (ListenerUtils.shouldEarlyReturn(player)) return;
        Block block = interactEvent.getClickedBlock();
        if (block == null) return;
        if (!ConfigHandler.getInstance().getContainersToBlock().contains(block.getType())) return;
        if (interactionEnabled(player)) return;
        interactEvent.setCancelled(true);
    }

    private boolean interactionEnabled(Player player) {
        PlayerVanishSettings vanishSettings = Cache.getVanishSettings(player.getUniqueId());
        if (player.hasPermission(VanishPermission.OPEN_CONTAINERS) && vanishSettings.shouldContainersOpen()) {
            return true;
        }
        if (player.hasPermission(VanishPermission.OPEN_CONTAINERS) && player.isSneaking()) {
            return true;
        }
        return false;
    }


}
