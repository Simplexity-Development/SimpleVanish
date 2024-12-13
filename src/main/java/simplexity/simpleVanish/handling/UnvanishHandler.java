package simplexity.simpleVanish.handling;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import simplexity.simpleVanish.SimpleVanish;
import simplexity.simpleVanish.config.ConfigHandler;
import simplexity.simpleVanish.events.PlayerUnvanishEvent;
import simplexity.simpleVanish.objects.PlayerVanishSettings;
import simplexity.simpleVanish.objects.VanishPermission;
import simplexity.simpleVanish.saving.Cache;
import simplexity.simpleVanish.saving.SqlHandler;

public class UnvanishHandler {
    private static UnvanishHandler instance;

    public static UnvanishHandler getInstance() {
        if (instance == null) instance = new UnvanishHandler();
        return instance;
    }

    public UnvanishHandler() {
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
        removeNightVision(player, settings);
        removeInvulnerability(player, settings);
        addBackToSleepingPlayers(player);
        MessageHandler.getInstance().sendAdminNotification(player, notificationMessage);
        Cache.getVanishedPlayers().remove(player);
        settings.setVanished(false);
        SqlHandler.getInstance().savePlayerSettings(player.getUniqueId(), settings);
        if (fakeJoin) MessageHandler.getInstance().sendFakeJoinMessage(player);
    }

    private void removeNightVision(Player player, PlayerVanishSettings settings) {
        if (!player.hasPermission(VanishPermission.NIGHT_VISION) || !settings.giveNightvision()) return;
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
    }

    private void removeInvulnerability(Player player, PlayerVanishSettings settings) {
        if (!player.hasPermission(VanishPermission.INVULNERABLE) || !settings.shouldGiveInvulnerability()) return;
        player.setInvulnerable(false);
    }

    private void addBackToSleepingPlayers(Player player) {
        if (!ConfigHandler.getInstance().shouldRemoveFromSleepingPlayers()) return;
        player.setSleepingIgnored(false);
    }
}
