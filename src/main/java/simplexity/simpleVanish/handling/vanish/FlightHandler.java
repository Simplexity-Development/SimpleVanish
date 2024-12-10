package simplexity.simpleVanish.handling.vanish;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import simplexity.simpleVanish.objects.PlayerVanishSettings;
import simplexity.simpleVanish.saving.Cache;

public class FlightHandler extends AbstractSettingHandler {


    public FlightHandler(Permission settingPermission) {
        super(settingPermission);
    }

    @Override
    public boolean shouldPreventSetting(Player player) {
        PlayerVanishSettings vanishSettings = Cache.getVanishSettings(player.getUniqueId());
        return !vanishSettings.canFly() || !player.hasPermission(getSettingPermission());
    }

    @Override
    public void handleVanish(Player player) {
        if (shouldPreventSetting(player)) return;
        if (player.getAllowFlight()) return;
        player.setAllowFlight(true);
        player.setFlying(true);
        Cache.getFlyingPlayers().add(player);
    }

    @Override
    public void handleUnvanish(Player player) {
        if (!Cache.getFlyingPlayers().contains(player)) return;
        Cache.getFlyingPlayers().remove(player);
        player.setAllowFlight(false);
        player.setFlying(false);
    }
}
