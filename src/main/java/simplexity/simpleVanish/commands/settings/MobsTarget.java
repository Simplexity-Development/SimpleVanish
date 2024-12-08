package simplexity.simpleVanish.commands.settings;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import simplexity.simpleVanish.commands.SubCommand;
import simplexity.simpleVanish.config.LocaleHandler;
import simplexity.simpleVanish.objects.PlayerVanishSettings;
import simplexity.simpleVanish.saving.SqlHandler;

import java.util.UUID;

public class MobsTarget extends SubCommand {
    public MobsTarget(Permission commandPermission, String commandName) {
        super(commandPermission, commandName);
    }

    @Override
    public void execute(Player player, boolean enabled) {
        UUID playerUuid = player.getUniqueId();
        PlayerVanishSettings vanishSettings = SqlHandler.getInstance().getVanishSettings(playerUuid);
        vanishSettings.setShouldMobsTarget(enabled);
        SqlHandler.getInstance().savePlayerSettings(playerUuid, vanishSettings);
        sendMessage(player, LocaleHandler.Message.SETTING_INSERT_MOBS_TARGET.getMessage(), enabled);
    }

    @Override
    public boolean isEnabled(Player player) {
        return getSettings(player).shouldMobTarget();
    }


}
