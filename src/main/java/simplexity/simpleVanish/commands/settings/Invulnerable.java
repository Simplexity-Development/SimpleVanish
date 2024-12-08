package simplexity.simpleVanish.commands.settings;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import simplexity.simpleVanish.commands.SubCommand;
import simplexity.simpleVanish.config.LocaleHandler;
import simplexity.simpleVanish.objects.PlayerVanishSettings;
import simplexity.simpleVanish.saving.SqlHandler;

import java.util.UUID;

public class Invulnerable extends SubCommand {

    public Invulnerable(Permission commandPermission, String commandName) {
        super(commandPermission, commandName);
    }

    @Override
    public void execute(Player player, boolean enabled) {
        UUID playerUuid = player.getUniqueId();
        PlayerVanishSettings settings = SqlHandler.getInstance().getVanishSettings(playerUuid);
        settings.setInvulnerable(enabled);
        SqlHandler.getInstance().savePlayerSettings(playerUuid, settings);
        sendMessage(player, LocaleHandler.Message.SETTING_INSERT_INVULNERABLE.getMessage(), enabled);
    }

    @Override
    public boolean isEnabled(Player player) {
        return getSettings(player).isInvulnerable();
    }
}
