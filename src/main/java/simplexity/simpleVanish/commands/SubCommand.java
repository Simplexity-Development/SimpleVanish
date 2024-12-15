package simplexity.simpleVanish.commands;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import simplexity.simpleVanish.config.Message;
import simplexity.simpleVanish.objects.PlayerVanishSettings;
import simplexity.simpleVanish.saving.Cache;

import java.util.UUID;

public abstract class SubCommand {
    public final Permission commandPermission;
    public final String commandName;
    public final String settingName;

    public SubCommand(Permission commandPermission, String commandName, String settingName) {
        this.commandPermission = commandPermission;
        this.commandName = commandName;
        this.settingName = settingName;
    }

    public Permission getCommandPermission() {
        return commandPermission;
    }

    public String getCommandName() {
        return commandName;
    }

    public String getSettingName() {
        return settingName;
    }

    public abstract void execute(Player player, boolean enabled);

    public abstract boolean isEnabled(Player player);

    public void sendMessage(Player player, boolean enabled) {
        if (enabled) {
            player.sendRichMessage(Message.SETTING_CHANGED.getMessage(),
                    Placeholder.parsed("value", Message.SETTING_INSERT_ENABLED.getMessage()),
                    Placeholder.parsed("setting", getSettingName()));
            return;
        }
        player.sendRichMessage(Message.SETTING_CHANGED.getMessage(),
                Placeholder.parsed("value", Message.SETTING_INSERT_DISABLED.getMessage()),
                Placeholder.parsed("setting", getSettingName()));
    }

    public PlayerVanishSettings getSettings(Player player) {
        UUID uuid = player.getUniqueId();
        return Cache.getVanishSettings(uuid);
    }


}
