package simplexity.simpleVanish.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import simplexity.simpleVanish.config.ConfigHandler;
import simplexity.simpleVanish.config.Message;
import simplexity.simpleVanish.objects.VanishPermission;
import simplexity.simpleVanish.saving.Cache;

public class PreCommandListener implements Listener {
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (!ConfigHandler.getInstance().shouldPreventMessagingVanished()) return;
        if (!isMessageCommand(event.getMessage())) return;
        if (Cache.getVanishedPlayers().isEmpty()) return;
        if (event.getPlayer().hasPermission(VanishPermission.VIEW_VANISHED)) return;
        if (!isTryingToMessageVanishedPlayer(event.getMessage())) return;
        Player player = event.getPlayer();
        event.setCancelled(true);
        player.sendRichMessage(Message.ERROR_NO_PLAYER_FOUND_TRANSLATABLE.getMessage());
    }

    private boolean isMessageCommand(String message) {
        message = message.toLowerCase();
        if (message.startsWith("/minecraft:msg")) return true;
        if (message.startsWith("/minecraft:w")) return true;
        if (message.startsWith("/minecraft:tell")) return true;
        if (message.startsWith("/msg")) return true;
        if (message.startsWith("/w")) return true;
        if (message.startsWith("/tell")) return true;
        return false;
    }

    private boolean isTryingToMessageVanishedPlayer(String message) {
        String[] args = message.split(" ");
        if (args.length < 2) return false;
        String username = args[1];
        Player target = Bukkit.getPlayer(username);
        return Cache.getVanishedPlayers().contains(target);
    }
}
