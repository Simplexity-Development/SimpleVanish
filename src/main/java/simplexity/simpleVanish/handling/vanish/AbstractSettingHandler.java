package simplexity.simpleVanish.handling.vanish;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public abstract class AbstractSettingHandler {
    private final Permission settingPermission;

    public AbstractSettingHandler(Permission settingPermission) {
        this.settingPermission = settingPermission;
    }

    public Permission getSettingPermission() {
        return settingPermission;
    }

    public abstract boolean shouldPreventSetting(Player player);

    public abstract void handleVanish(Player player);

    public abstract void handleUnvanish(Player player);


}
