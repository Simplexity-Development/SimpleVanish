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
import simplexity.simpleVanish.commands.settings.NightVision;
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

    public void runVanishEvent(Player player, boolean fakeLeave, String notificationMessage) {
        PlayerVanishSettings settings = Cache.getVanishSettings(player.getUniqueId());
        PlayerVanishEvent vanishEvent = new PlayerVanishEvent(player, settings);
        Bukkit.getServer().getPluginManager().callEvent(vanishEvent);
        if (vanishEvent.isCancelled()) return;
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            hidePlayer(onlinePlayer, player);
            removeFromTabList(onlinePlayer, player);
            if (MessageHandler.getInstance().shouldSendVanishNotification(onlinePlayer, player)) {
                onlinePlayer.sendMessage(MessageHandler.getInstance().parsePlayerMessage(player, notificationMessage));
            }
        }
        provideNightVision(player, settings);
        setInvulnerable(player, settings);
        Cache.getVanishedPlayers().add(player);
        settings.setVanished(true);
        SqlHandler.getInstance().savePlayerSettings(player.getUniqueId(), settings);
        if (fakeLeave) MessageHandler.getInstance().sendFakeLeaveMessage(player);
    }

    public void handlePlayerLeave(Player player) {
        Cache.getVanishedPlayers().remove(player);
        Cache.removePlayerFromCache(player.getUniqueId());
    }

    public void hideCurrentlyVanishedUsers(Player player) {
        for (Player vanishedPlayer : Cache.getVanishedPlayers()) {
            hidePlayer(player, vanishedPlayer);
            removeFromTabList(player, vanishedPlayer);
        }
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

    private void provideNightVision(Player player, PlayerVanishSettings settings) {
        if (!player.hasPermission(VanishPermission.NIGHT_VISION) || !settings.giveNightvision()) return;
        player.addPotionEffect(NightVision.nightVision);
    }

    private void setInvulnerable(Player player, PlayerVanishSettings settings) {
        if (!player.hasPermission(VanishPermission.INVULNERABLE) || !settings.shouldGiveInvulnerability()) return;
        player.setInvulnerable(true);
    }

}
