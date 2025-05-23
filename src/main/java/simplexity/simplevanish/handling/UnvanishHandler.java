package simplexity.simplevanish.handling;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import simplexity.simplevanish.SimpleVanish;
import simplexity.simplevanish.config.ConfigHandler;
import simplexity.simplevanish.events.PlayerUnvanishEvent;
import simplexity.simplevanish.objects.PlayerVanishSettings;
import simplexity.simplevanish.objects.VanishPermission;
import simplexity.simplevanish.saving.Cache;
import simplexity.simplevanish.saving.SqlHandler;

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
        if (!(notificationMessage == null || notificationMessage.isEmpty())) {
            MessageHandler.getInstance().sendAdminNotification(player, notificationMessage);
        }
        MessageHandler.getInstance().removeChangedTablist(player);
        removeGlow(player);
        Cache.getVanishedPlayers().remove(player);
        settings.setVanished(false);
        SqlHandler.getInstance().savePlayerSettings(player.getUniqueId(), settings);
        if (fakeJoin) FakeJoinHandler.getInstance().sendFakeJoinMessage(player);
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

    private void removeGlow(Player player) {
        if (!ConfigHandler.getInstance().shouldGlowWhileVanished()) return;
        player.setGlowing(false);
    }
}
