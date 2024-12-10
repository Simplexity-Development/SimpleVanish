package simplexity.simpleVanish.handling.vanish;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import simplexity.simpleVanish.objects.PlayerVanishSettings;
import simplexity.simpleVanish.saving.Cache;

public class Invulnerable extends AbstractSettingHandler {

    public Invulnerable(Permission settingPermission) {
        super(settingPermission);
    }

    @Override
    public boolean shouldPreventSetting(Player player) {
        PlayerVanishSettings vanishSettings = Cache.getVanishSettings(player.getUniqueId());
        return !vanishSettings.canBeInvulnerable() || !player.hasPermission(getSettingPermission());
    }

    @Override
    public void handleVanish(Player player) {
        if (shouldPreventSetting(player)) return;
        if (player.isInvulnerable()) return;


    }

    @Override
    public void handleUnvanish(Player player) {

    }
}
