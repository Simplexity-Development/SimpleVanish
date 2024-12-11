package simplexity.simpleVanish.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import simplexity.simpleVanish.objects.PlayerVanishSettings;
import simplexity.simpleVanish.objects.VanishPermission;
import simplexity.simpleVanish.saving.Cache;

public class PlayerJoinListener implements Listener {


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

    }

    private boolean shouldVanish(Player player) {
        if (!player.hasPermission(VanishPermission.VANISH_COMMAND)) return false;
        if (!player.hasPermission(VanishPermission.PERSIST)) return false;
        PlayerVanishSettings vanishSettings = Cache.getVanishSettings(player.getUniqueId());
        if (!vanishSettings.shouldVanishPersist()) return false;
        return (vanishSettings.isVanished());
    }





}
