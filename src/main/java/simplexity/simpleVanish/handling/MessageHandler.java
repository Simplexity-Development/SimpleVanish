package simplexity.simpleVanish.handling;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import simplexity.simpleVanish.SimpleVanish;
import simplexity.simpleVanish.config.ConfigHandler;
import simplexity.simpleVanish.config.LocaleHandler;
import simplexity.simpleVanish.events.FakeJoinEvent;
import simplexity.simpleVanish.events.FakeLeaveEvent;
import simplexity.simpleVanish.objects.PlayerVanishSettings;
import simplexity.simpleVanish.objects.VanishPermission;
import simplexity.simpleVanish.saving.Cache;

public class MessageHandler {
    private static MessageHandler instance;

    public static MessageHandler getInstance() {
        if (instance == null) instance = new MessageHandler();
        return instance;
    }

    public MessageHandler() {
    }

    private final MiniMessage miniMessage = SimpleVanish.getMiniMessage();

    public Component parsePlayerMessage(Player player, String message) {
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

    private TagResolver papiTag(final Player player) {
        if (player == null) return TagResolver.empty();
        return TagResolver.resolver("papi", (argumentQueue, context) -> {
            final String papiPlaceholder = argumentQueue.popOr("PLACEHOLDER API NEEDS ARGUMENT").value();
            final String parsedPlaceholder = PlaceholderAPI.setPlaceholders(player, '%' + papiPlaceholder + '%');
            final Component componentPlaceholder = LegacyComponentSerializer.legacySection().deserialize(parsedPlaceholder);
            return Tag.inserting(componentPlaceholder);
        });
    }

    public void sendAdminNotification(Player player, String message) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!shouldSendVanishNotification(onlinePlayer, player)) continue;
            onlinePlayer.sendMessage(parsePlayerMessage(player, message));
        }
    }

    public boolean shouldSendVanishNotification(Player onlinePlayer, Player player) {
        if (!onlinePlayer.hasPermission(VanishPermission.VIEW_VANISHED)) return false;
        if (onlinePlayer.equals(player)) return false;
        PlayerVanishSettings vanishSettings = Cache.getVanishSettings(onlinePlayer.getUniqueId());
        return !vanishSettings.viewVanishNotifications();
    }

    public void sendFakeJoinMessage(Player player) {
        Component message;
        if (ConfigHandler.getInstance().isCustomJoinLeave()) {
            message = parsePlayerMessage(player, ConfigHandler.getInstance().getCustomJoinMessage());
        } else {
            message = miniMessage.deserialize(LocaleHandler.Message.MESSAGE_FAKE_JOIN.getMessage(),
                    Placeholder.parsed("username", player.getName()));
        }
        FakeJoinEvent event = new FakeJoinEvent(player, message);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) return;
        Bukkit.getServer().sendMessage(message);
    }

    public void sendFakeLeaveMessage(Player player) {
        Component message;
        if (ConfigHandler.getInstance().isCustomJoinLeave()) {
            message = parsePlayerMessage(player, ConfigHandler.getInstance().getCustomLeaveMessage());
        } else {
            message = miniMessage.deserialize(LocaleHandler.Message.MESSAGE_FAKE_LEAVE.getMessage(),
                    Placeholder.parsed("username", player.getName()));
        }
        FakeLeaveEvent event = new FakeLeaveEvent(player, message);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) return;
        Bukkit.getServer().sendMessage(message);
    }

    public void changeTablist(Player player) {
        if (!ConfigHandler.getInstance().shouldChangeTablistFormat()) return;
        Component message = parsePlayerMessage(player, LocaleHandler.Message.VIEW_TABLIST_FORMAT.getMessage());
        player.playerListName(message);
    }

    public void removeChangedTablist(Player player) {
        if (!ConfigHandler.getInstance().shouldChangeTablistFormat()) return;
        player.playerListName(Component.text(player.getName()));
    }

}
