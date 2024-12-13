package simplexity.simpleVanish.commands.settings;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import simplexity.simpleVanish.commands.SubCommand;
import simplexity.simpleVanish.objects.PlayerVanishSettings;
import simplexity.simpleVanish.saving.Cache;

public class OpenContainer extends SubCommand {
    public OpenContainer(Permission commandPermission, String commandName, String settingName) {
        super(commandPermission, commandName, settingName);
    }

    @Override
    public void execute(Player player, boolean enabled) {
        PlayerVanishSettings settings = getSettings(player);
        settings.setContainerOpen(enabled);
        Cache.saveSettings(player.getUniqueId(), settings);
        sendMessage(player, enabled);
    }

    @Override
    public boolean isEnabled(Player player) {
        return getSettings(player).doesContainerOpenAnimation();
    }
}
