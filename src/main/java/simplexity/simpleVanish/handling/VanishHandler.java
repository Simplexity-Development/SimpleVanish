package simplexity.simpleVanish.handling;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import simplexity.simpleVanish.SimpleVanish;
import simplexity.simpleVanish.commands.settings.NightVision;
import simplexity.simpleVanish.config.ConfigHandler;
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
        }
        provideNightVision(player, settings);
        setInvulnerable(player, settings);
        removeFromSleepingPlayers(player);
        MessageHandler.getInstance().changeTablist(player);
        MessageHandler.getInstance().sendAdminNotification(player, notificationMessage);
        giveGlow(player);
        Cache.getVanishedPlayers().add(player);
        settings.setVanished(true);
        SqlHandler.getInstance().savePlayerSettings(player.getUniqueId(), settings);
        if (fakeLeave) FakeLeaveHandler.getInstance().sendFakeLeaveMessage(player);
    }

    public void handlePlayerLeave(Player player) {
        if (!Cache.getVanishSettings(player.getUniqueId()).shouldVanishPersist()) {
            UnvanishHandler.getInstance().runUnvanishEvent(player, false, "");
        } else {
            Cache.getVanishedPlayers().remove(player);
            Cache.removePlayerFromCache(player.getUniqueId());
        }
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
        if (!ConfigHandler.getInstance().shouldRemoveFromTablist()) return;
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

    private void removeFromSleepingPlayers(Player player) {
        if (!ConfigHandler.getInstance().shouldRemoveFromSleepingPlayers()) return;
        player.setSleepingIgnored(true);
    }

    private void giveGlow(Player player) {
        if (!ConfigHandler.getInstance().shouldGlowWhileVanished()) return;
        player.setGlowing(true);
    }

}
