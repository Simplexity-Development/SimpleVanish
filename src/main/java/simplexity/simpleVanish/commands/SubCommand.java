package simplexity.simpleVanish.commands;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import simplexity.simpleVanish.config.LocaleHandler;
import simplexity.simpleVanish.objects.PlayerVanishSettings;
import simplexity.simpleVanish.saving.Cache;

import java.util.UUID;

public abstract class SubCommand {
    public final Permission commandPermission;
    public final String commandName;

    public SubCommand(Permission commandPermission, String commandName) {
        this.commandPermission = commandPermission;
        this.commandName = commandName;
    }

    public Permission getCommandPermission() {
        return commandPermission;
    }

    public String getCommandName() {
        return commandName;
    }

    public abstract void execute(Player player, boolean enabled);

    public abstract boolean isEnabled(Player player);

    public void sendMessage(Player player, String setting, boolean enabled) {
        if (enabled) {
            player.sendRichMessage(LocaleHandler.Message.SETTING_CHANGED.getMessage(),
                    Placeholder.parsed("value", LocaleHandler.Message.SETTING_INSERT_ENABLED.getMessage()),
                    Placeholder.parsed("setting", setting));
            return;
        }
        player.sendRichMessage(LocaleHandler.Message.SETTING_CHANGED.getMessage(),
                Placeholder.parsed("value", LocaleHandler.Message.SETTING_INSERT_DISABLED.getMessage()),
                Placeholder.parsed("setting", setting));
    }

    public PlayerVanishSettings getSettings(Player player) {
        UUID uuid = player.getUniqueId();
        return Cache.getVanishSettings(uuid);
    }


}
