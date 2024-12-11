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
import simplexity.simpleVanish.events.FakeJoinEvent;
import simplexity.simpleVanish.events.FakeLeaveEvent;
import simplexity.simpleVanish.events.PlayerUnvanishEvent;
import simplexity.simpleVanish.events.PlayerVanishEvent;
import simplexity.simpleVanish.objects.PlayerVanishSettings;
import simplexity.simpleVanish.objects.VanishPermission;
import simplexity.simpleVanish.saving.Cache;
import simplexity.simpleVanish.saving.SqlHandler;

public class VanishHandler {
    private static VanishHandler instance;

    public static VanishHandler getInstance() {
        if (instance == null) instance = new VanishHandler();
        return instance;
    }

    public VanishHandler() {
    }

    private final MiniMessage miniMessage = SimpleVanish.getMiniMessage();

    public void runVanishEvent(Player player, boolean fakeLeave, String notificationMessage) {
        PlayerVanishSettings settings = Cache.getVanishSettings(player.getUniqueId());
        PlayerVanishEvent vanishEvent = new PlayerVanishEvent(player, settings);
        Bukkit.getServer().getPluginManager().callEvent(vanishEvent);
        if (vanishEvent.isCancelled()) return;
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            hidePlayer(onlinePlayer, player);
            removeFromTabList(onlinePlayer, player);
            if (shouldSendVanishNotification(onlinePlayer, player)) {
                onlinePlayer.sendMessage(parsePlayerMessage(player, notificationMessage));
            }
        }
        Cache.getVanishedPlayers().add(player);
        settings.setVanished(true);
        SqlHandler.getInstance().savePlayerSettings(player.getUniqueId(), settings);
        if (fakeLeave) sendFakeLeaveMessage(player);
    }

    public void runUnvanishEvent(Player player, boolean fakeJoin, String notificationMessage) {
        PlayerUnvanishEvent unvanishEvent = new PlayerUnvanishEvent(player);
        Bukkit.getServer().getPluginManager().callEvent(unvanishEvent);
        if (unvanishEvent.isCancelled()) return;
        PlayerVanishSettings settings = Cache.getVanishSettings(player.getUniqueId());
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.showPlayer(SimpleVanish.getInstance(), player);
            onlinePlayer.listPlayer(player);
        }
        sendAdminNotification(player, notificationMessage);
        Cache.getVanishedPlayers().remove(player);
        settings.setVanished(false);
        SqlHandler.getInstance().savePlayerSettings(player.getUniqueId(), settings);
        if (fakeJoin) sendFakeJoinMessage(player);
    }

    public void handlePlayerLeave(Player player) {
        Cache.getVanishedPlayers().remove(player);
        Cache.removePlayerFromCache(player.getUniqueId());
    }

    private void sendFakeLeaveMessage(Player player) {
        Component message;
        if (ConfigHandler.getInstance().shouldCustomizeFormat()) {
            message = parsePlayerMessage(player, ConfigHandler.getInstance().getCustomLeave());
        } else {
            message = miniMessage.deserialize("<yellow><lang:multiplayer.player.left:" + player.getName() + "></yellow>");
        }
        FakeLeaveEvent event = new FakeLeaveEvent(player, message);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) return;
        Bukkit.getServer().sendMessage(message);
    }

    private void sendFakeJoinMessage(Player player) {
        Component message;
        if (ConfigHandler.getInstance().shouldCustomizeFormat()) {
            message = parsePlayerMessage(player, ConfigHandler.getInstance().getCustomJoin());
        } else {
            message = miniMessage.deserialize("<yellow><lang:multiplayer.player.joined:" + player.getName() + "></yellow>");
        }
        FakeJoinEvent event = new FakeJoinEvent(player, message);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) return;
        Bukkit.getServer().sendMessage(message);
    }

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

    public void hideCurrentlyVanishedUsers(Player player) {
        for (Player vanishedPlayer : Cache.getVanishedPlayers()) {
            hidePlayer(player, vanishedPlayer);
            removeFromTabList(player, vanishedPlayer);
        }
    }

    public void sendAdminNotification(Player player, String message){
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!shouldSendVanishNotification(onlinePlayer, player)) continue;
            onlinePlayer.sendMessage(parsePlayerMessage(player, message));
        }
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

    private void hidePlayer(Player onlinePlayer, Player playerToHide) {
        if (onlinePlayer == null || playerToHide == null || onlinePlayer.equals(playerToHide)) return;
        if (onlinePlayer.hasPermission(VanishPermission.VIEW_VANISHED)) return;
        onlinePlayer.hidePlayer(SimpleVanish.getInstance(), playerToHide);
    }

    private void removeFromTabList(Player onlinePlayer, Player playerToRemove) {
        if (onlinePlayer == null || playerToRemove == null || onlinePlayer.equals(playerToRemove)) return;
        if (onlinePlayer.hasPermission(VanishPermission.VIEW_TABLIST)) return;
        onlinePlayer.unlistPlayer(playerToRemove);
    }

    private boolean shouldSendVanishNotification(Player onlinePlayer, Player player) {
        if (!onlinePlayer.hasPermission(VanishPermission.VIEW_VANISHED)) return false;
        if (onlinePlayer.equals(player)) return false;
        PlayerVanishSettings vanishSettings = Cache.getVanishSettings(onlinePlayer.getUniqueId());
        return !vanishSettings.viewVanishNotifications();
    }

}
