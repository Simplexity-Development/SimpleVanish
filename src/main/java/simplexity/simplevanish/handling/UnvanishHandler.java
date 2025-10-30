package simplexity.simplevanish.handling;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import simplexity.simplevanish.SimpleVanish;
import simplexity.simplevanish.config.ConfigHandler;
import simplexity.simplevanish.events.PlayerUnvanishEvent;
import simplexity.simplevanish.hooks.PlexmapIntegration;
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

    private UnvanishHandler() {
    }

    public void runUnvanishEvent(@NotNull Player player, boolean fakeJoin, @Nullable String notificationMessage) {
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
        MessageHandler.sendAdminNotification(player, notificationMessage);
        removeChangedTablist(player);
        removeGlow(player);
        player.setAffectsSpawning(true);
        Cache.getVanishedPlayers().remove(player);
        settings.setVanished(false);
        SqlHandler.getInstance().savePlayerSettings(player.getUniqueId(), settings);
        if (fakeJoin) FakeJoinHandler.getInstance().sendFakeJoinMessage(player);
        if (SimpleVanish.isPlexmapEnabled()) PlexmapIntegration.unHideVanishedPlayer(player);
    }

    public void removeNightVision(@NotNull Player player, @NotNull PlayerVanishSettings settings) {
        if (!player.hasPermission(VanishPermission.NIGHT_VISION)) return;
        if (!settings.giveNightvision()) return;
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
    }

    public void removeInvulnerability(@NotNull Player player, @NotNull PlayerVanishSettings settings) {
        if (!player.hasPermission(VanishPermission.INVULNERABLE)) return;
        if (!settings.shouldGiveInvulnerability()) return;
        player.setInvulnerable(false);
    }

    public void addBackToSleepingPlayers(@NotNull Player player) {
        if (!ConfigHandler.getInstance().shouldRemoveFromSleepingPlayers()) return;
        player.setSleepingIgnored(false);
    }

    public void removeGlow(@NotNull Player player) {
        if (!ConfigHandler.getInstance().shouldGlowWhileVanished()) return;
        player.setGlowing(false);
    }

    public void removeChangedTablist(@NotNull Player player) {
        if (!ConfigHandler.getInstance().shouldChangeTablistFormat()) return;
        player.playerListName(Component.text(player.getName()));
    }
}
