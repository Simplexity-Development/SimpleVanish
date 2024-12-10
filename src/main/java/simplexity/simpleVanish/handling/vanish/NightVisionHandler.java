package simplexity.simpleVanish.handling.vanish;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import simplexity.simpleVanish.objects.PlayerVanishSettings;
import simplexity.simpleVanish.objects.VanishSettingsPermission;
import simplexity.simpleVanish.saving.Cache;

public class NightVisionHandler extends AbstractSettingHandler{

    public NightVisionHandler(Permission settingPermission) {
        super(settingPermission);
    }

    @Override
    public boolean shouldPreventSetting(Player player) {
        PlayerVanishSettings vanishSettings = Cache.getVanishSettings(player.getUniqueId());
        return !vanishSettings.shouldGetNightVision() || !player.hasPermission(getSettingPermission());
    }

    @Override
    public void handleVanish(Player player) {
        if (shouldPreventSetting(player)) return;
        Cache.getNightVisionPlayers().add(player);


    }

    @Override
    public void handleUnvanish(Player player) {

    }
}
