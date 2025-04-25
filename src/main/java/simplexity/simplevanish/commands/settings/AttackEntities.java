package simplexity.simplevanish.commands.settings;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import simplexity.simplevanish.commands.SubCommand;
import simplexity.simplevanish.objects.PlayerVanishSettings;
import simplexity.simplevanish.saving.Cache;

public class AttackEntities extends SubCommand {
    public AttackEntities(Permission commandPermission, String commandName, String settingName) {
        super(commandPermission, commandName, settingName);
    }

    @Override
    public void execute(Player player, boolean enabled) {
        PlayerVanishSettings settings = getSettings(player);
        settings.setAttackEntities(enabled);
        Cache.saveSettings(player.getUniqueId(), settings);
        sendMessage(player, enabled);
    }

    @Override
    public boolean isEnabled(Player player) {
        return getSettings(player).canAttackEntities();
    }
}
