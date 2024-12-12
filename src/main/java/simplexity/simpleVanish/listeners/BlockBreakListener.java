package simplexity.simpleVanish.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import simplexity.simpleVanish.objects.PlayerVanishSettings;
import simplexity.simpleVanish.objects.VanishPermission;
import simplexity.simpleVanish.saving.Cache;

public class BlockBreakListener implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent breakEvent) {
        Player player = breakEvent.getPlayer();
        if (ListenerUtils.shouldEarlyReturn(player)) return;
        if (blockBreakEnabled(player)) return;
        breakEvent.setCancelled(true);
    }

    private boolean blockBreakEnabled(Player player) {
        PlayerVanishSettings vanishSettings = Cache.getVanishSettings(player.getUniqueId());
        return !player.hasPermission(VanishPermission.BREAK_BLOCKS) || !vanishSettings.canBreakBlocks();
    }
}
