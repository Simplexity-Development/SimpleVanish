package simplexity.simpleVanish.handling;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import simplexity.simpleVanish.SimpleVanish;
import simplexity.simpleVanish.config.ConfigHandler;
import simplexity.simpleVanish.events.PlayerUnvanishEvent;
import simplexity.simpleVanish.events.PlayerVanishEvent;
import simplexity.simpleVanish.objects.PlayerVanishSettings;
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

    public void runVanishEvent(Player player, boolean sendMessage) {
        PlayerVanishSettings settings = SqlHandler.getInstance().getVanishSettings(player.getUniqueId());
        PlayerVanishEvent vanishEvent = new PlayerVanishEvent(player, settings);
        SimpleVanish.getInstance().getServer().getPluginManager().callEvent(vanishEvent);
        if (vanishEvent.isCancelled()) return;
        for (Player hideFromPlayer : SimpleVanish.getPlayersToHideFrom()) {
            hideFromPlayer.hidePlayer(SimpleVanish.getInstance(), player);
            hideFromPlayer.unlistPlayer(player);
        }
        SimpleVanish.getVanishedPlayers().add(player);
        settings.setVanished(true);
        SqlHandler.getInstance().savePlayerSettings(player.getUniqueId(), settings);
        if (sendMessage) sendLeaveMessage(player);
    }

    public void runUnvanishEvent(Player player, boolean sendMessage) {
        PlayerUnvanishEvent unvanishEvent = new PlayerUnvanishEvent(player);
        SimpleVanish.getInstance().getServer().getPluginManager().callEvent(unvanishEvent);
        if (unvanishEvent.isCancelled()) return;
        PlayerVanishSettings settings = SqlHandler.getInstance().getVanishSettings(player.getUniqueId());
        for (Player hideFromPlayer : SimpleVanish.getPlayersToHideFrom()) {
            hideFromPlayer.showPlayer(SimpleVanish.getInstance(), player);
            hideFromPlayer.listPlayer(player);
        }
        SimpleVanish.getVanishedPlayers().remove(player);
        settings.setVanished(false);
        SqlHandler.getInstance().savePlayerSettings(player.getUniqueId(), settings);
        if (sendMessage) sendJoinMessage(player);
    }

    public void handlePlayerLeave(Player player) {
        SimpleVanish.getVanishedPlayers().remove(player);
        SimpleVanish.getPlayersToHideFrom().remove(player);
        SimpleVanish.getViewingPlayers().remove(player);
        SqlHandler.getInstance().removePlayerFromCache(player.getUniqueId());
    }

    private void sendLeaveMessage(Player player) {
        Component message;
        if (ConfigHandler.getInstance().shouldCustomizeFormat()) {
            message = parsePlayerMessage(player, ConfigHandler.getInstance().getCustomLeave());
        } else {
            message = miniMessage.deserialize("<yellow><lang:multiplayer.player.left:" + player.getName() + "></yellow>");
        }
        SimpleVanish.getInstance().getServer().sendMessage(message);
    }

    private void sendJoinMessage(Player player) {
        Component message;
        if (ConfigHandler.getInstance().shouldCustomizeFormat()) {
            message = parsePlayerMessage(player, ConfigHandler.getInstance().getCustomJoin());
        } else {
            message = miniMessage.deserialize("<yellow><lang:multiplayer.player.joined:" + player.getName() + "></yellow>");
        }
        SimpleVanish.getInstance().getServer().sendMessage(message);
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

    private TagResolver papiTag(final Player player) {
        if (player == null) return TagResolver.empty();
        return TagResolver.resolver("papi", (argumentQueue, context) -> {
            final String papiPlaceholder = argumentQueue.popOr("PLACEHOLDER API NEEDS ARGUMENT").value();
            final String parsedPlaceholder = PlaceholderAPI.setPlaceholders(player, '%' + papiPlaceholder + '%');
            final Component componentPlaceholder = LegacyComponentSerializer.legacySection().deserialize(parsedPlaceholder);
            return Tag.inserting(componentPlaceholder);
        });
    }

}
