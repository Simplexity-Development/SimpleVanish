package simplexity.simplevanish.handling;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import simplexity.simplevanish.SimpleVanish;
import simplexity.simplevanish.config.ConfigHandler;
import simplexity.simplevanish.objects.PlayerVanishSettings;
import simplexity.simplevanish.objects.VanishPermission;
import simplexity.simplevanish.saving.Cache;

public class MessageHandler {

    private final static MiniMessage miniMessage = SimpleVanish.getMiniMessage();

    @NotNull
    public static Component parsePlayerMessage(@NotNull Player player, @NotNull String message) {
        if (!SimpleVanish.isPapiEnabled()) {
            return miniMessage.deserialize(
                    message,
                    Placeholder.component("displayname", player.displayName()),
                    Placeholder.unparsed("username", player.getName())
            );
        }
        return miniMessage.deserialize(
                message,
                papiTag(player),
                Placeholder.component("displayname", player.displayName()),
                Placeholder.unparsed("username", player.getName())
        );
    }

    @NotNull
    private static TagResolver papiTag(@NotNull final Player player) {
        return TagResolver.resolver("papi", (argumentQueue, context) -> {
            final String papiPlaceholder = argumentQueue.popOr("PLACEHOLDER API NEEDS ARGUMENT").value();
            final String parsedPlaceholder = PlaceholderAPI.setPlaceholders(player, '%' + papiPlaceholder + '%');
            final Component componentPlaceholder = LegacyComponentSerializer.legacySection().deserialize(parsedPlaceholder);
            return Tag.inserting(componentPlaceholder);
        });
    }

    public static void sendAdminNotification(@NotNull Player player, @Nullable String message) {
        if (message == null || message.isEmpty()) return;
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!shouldSendVanishNotification(onlinePlayer, player)) continue;
            onlinePlayer.sendMessage(MessageHandler.parsePlayerMessage(player, message));
        }
    }

    public static boolean shouldSendVanishNotification(@NotNull Player onlinePlayer, @NotNull Player player) {
        if (!onlinePlayer.hasPermission(VanishPermission.VIEW_VANISHED)) return false;
        if (onlinePlayer.equals(player)) return false;
        PlayerVanishSettings vanishSettings = Cache.getVanishSettings(onlinePlayer.getUniqueId());
        return !vanishSettings.viewVanishNotifications();
    }

    public static void debug(@NotNull String prefix, @NotNull String message, @Nullable Object... args) {
        if (ConfigHandler.getInstance().isDebug()) {
            message = prefix + message;
            message = String.format(message, args);
            SimpleVanish.getInstance().getSLF4JLogger().info(message);
        }
    }


}
