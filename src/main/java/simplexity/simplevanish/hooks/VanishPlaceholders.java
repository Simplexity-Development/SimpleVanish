package simplexity.simplevanish.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import simplexity.simplevanish.config.Message;
import simplexity.simplevanish.saving.Cache;

public class VanishPlaceholders extends PlaceholderExpansion {

    public VanishPlaceholders() {
    }

    @Override
    public @NotNull String getIdentifier() {
        return "simplevanish";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Simplexity";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.equalsIgnoreCase("total-online")) {
            int count = Bukkit.getServer().getOnlinePlayers().size();
            return String.valueOf(count);
        }
        if (params.equalsIgnoreCase("current-vanished")) {
            int count = Cache.getVanishedPlayers().size();
            return String.valueOf(count);
        }
        if (params.equalsIgnoreCase("current-visible")) {
            int vanishedCount = Cache.getVanishedPlayers().size();
            int onlineCount = Bukkit.getOnlinePlayers().size();
            int visibleCount = onlineCount - vanishedCount;
            return String.valueOf(visibleCount);
        }
        if (!(player instanceof Player onlinePlayer)) return null;
        boolean isVanished = Cache.getVanishedPlayers().contains(onlinePlayer);
        if (params.equalsIgnoreCase("is-vanished")) {
            return String.valueOf(isVanished);
        }
        if (params.equalsIgnoreCase("vanished-prefix")) {
            if (isVanished) {
                return Message.VIEW_PLACEHOLDER_FORMAT.getMessage();
            } else {
                return "";
            }
        }
        return null;
    }
}
