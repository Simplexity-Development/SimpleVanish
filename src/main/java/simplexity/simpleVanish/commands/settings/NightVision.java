package simplexity.simpleVanish.commands.settings;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import simplexity.simpleVanish.commands.SubCommand;
import simplexity.simpleVanish.objects.PlayerVanishSettings;
import simplexity.simpleVanish.saving.Cache;

public class NightVision extends SubCommand {
    public NightVision(Permission commandPermission, String commandName, String settingName) {
        super(commandPermission, commandName, settingName);
    }

    public static final PotionEffect nightVision = new PotionEffect(PotionEffectType.NIGHT_VISION,
            PotionEffect.INFINITE_DURATION, 0, false, false, false);

    @Override
    public void execute(Player player, boolean enabled) {
        PlayerVanishSettings settings = getSettings(player);
        settings.setNightVision(enabled);
        Cache.saveSettings(player.getUniqueId(), settings);
        sendMessage(player, enabled);
        updateCurrentNightVision(player, enabled);
    }

    @Override
    public boolean isEnabled(Player player) {
        return getSettings(player).giveNightvision();
    }

    private void updateCurrentNightVision(Player player, boolean enabled) {
        if (!Cache.getVanishedPlayers().contains(player)) return;
        if (enabled) {
            player.addPotionEffect(nightVision);
        } else {
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        }
    }
}
