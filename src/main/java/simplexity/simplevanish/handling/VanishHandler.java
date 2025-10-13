package simplexity.simplevanish.handling;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import simplexity.simplevanish.SimpleVanish;
import simplexity.simplevanish.commands.settings.NightVision;
import simplexity.simplevanish.config.ConfigHandler;
import simplexity.simplevanish.config.Message;
import simplexity.simplevanish.events.PlayerVanishEvent;
import simplexity.simplevanish.hooks.Pl3xmapIntegration;
import simplexity.simplevanish.objects.PlayerVanishSettings;
import simplexity.simplevanish.objects.VanishPermission;
import simplexity.simplevanish.saving.Cache;
import simplexity.simplevanish.saving.SqlHandler;

import java.util.List;

public class VanishHandler {
    private static VanishHandler instance;

    public static VanishHandler getInstance() {
        if (instance == null) instance = new VanishHandler();
        return instance;
    }

    private VanishHandler() {
    }

    public void runVanishEvent(@NotNull Player player, boolean fakeLeave, @Nullable String notificationMessage) {
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
        stopMobsTracking(player);
        changeTablist(player);
        giveGlow(player);
        player.setAffectsSpawning(false);
        Cache.getVanishedPlayers().add(player);
        settings.setVanished(true);
        SqlHandler.getInstance().savePlayerSettings(player.getUniqueId(), settings);
        if (fakeLeave) FakeLeaveHandler.getInstance().sendFakeLeaveMessage(player);
        if (SimpleVanish.isPl3xmapEnabled()) Pl3xmapIntegration.hideVanishedPlayer(player);
        MessageHandler.sendAdminNotification(player, notificationMessage);
    }

    public void handlePlayerLeave(@NotNull Player player) {
        if (!Cache.getVanishSettings(player.getUniqueId()).shouldVanishPersist()) {
            UnvanishHandler.getInstance().runUnvanishEvent(player, false, "");
        } else {
            Cache.getVanishedPlayers().remove(player);
            Cache.removePlayerFromCache(player.getUniqueId());
        }
    }

    public void hideCurrentlyVanishedUsers(@NotNull Player player) {
        for (Player vanishedPlayer : Cache.getVanishedPlayers()) {
            hidePlayer(player, vanishedPlayer);
            removeFromTabList(player, vanishedPlayer);
        }
    }

    public void changeTablist(@NotNull Player player) {
        if (!ConfigHandler.getInstance().shouldChangeTablistFormat()) return;
        Component message = MessageHandler.parsePlayerMessage(player, Message.VIEW_TABLIST_FORMAT.getMessage());
        player.playerListName(message);
    }

    public void hidePlayer(@NotNull Player onlinePlayer, @NotNull Player playerToHide) {
        if (onlinePlayer.equals(playerToHide)) return;
        if (onlinePlayer.hasPermission(VanishPermission.VIEW_VANISHED)) return;
        onlinePlayer.hidePlayer(SimpleVanish.getInstance(), playerToHide);
    }

    public void removeFromTabList(@NotNull Player onlinePlayer, @NotNull Player playerToRemove) {
        if (!ConfigHandler.getInstance().shouldRemoveFromTablist()) return;
        if (onlinePlayer.equals(playerToRemove)) return;
        if (onlinePlayer.hasPermission(VanishPermission.VIEW_TABLIST)) return;
        onlinePlayer.unlistPlayer(playerToRemove);
    }

    public void provideNightVision(@NotNull Player player, @NotNull PlayerVanishSettings settings) {
        if (!player.hasPermission(VanishPermission.NIGHT_VISION)) return;
        if (!settings.giveNightvision()) return;
        player.addPotionEffect(NightVision.nightVision);
    }

    public void setInvulnerable(@NotNull Player player, @NotNull PlayerVanishSettings settings) {
        if (!player.hasPermission(VanishPermission.INVULNERABLE)) return;
        if (!settings.shouldGiveInvulnerability()) return;
        player.setInvulnerable(true);
    }

    public void removeFromSleepingPlayers(@NotNull Player player) {
        if (!ConfigHandler.getInstance().shouldRemoveFromSleepingPlayers()) return;
        player.setSleepingIgnored(true);
    }

    public void giveGlow(@NotNull Player player) {
        if (!ConfigHandler.getInstance().shouldGlowWhileVanished()) return;
        player.setGlowing(true);
    }

    public void stopMobsTracking(@NotNull Player player) {
        List<Entity> nearbyEntities = player.getNearbyEntities(16, 16, 16);
        for (Entity entity : nearbyEntities) {
            if (!(entity instanceof Mob mob)) continue;
            LivingEntity targetEntity = mob.getTarget();
            if (targetEntity != null && targetEntity.equals(player)) mob.setTarget(null);
        }
    }

}
