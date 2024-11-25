package simplexity.simpleVanish.listeners;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import simplexity.simpleVanish.SimpleVanish;
import simplexity.simpleVanish.config.ConfigHandler;
import simplexity.simpleVanish.events.PlayerVanishEvent;

import java.util.UUID;

public class PlayerVanishListener implements Listener {
    @EventHandler
    public void onPlayerVanish(PlayerVanishEvent event) {
        Player player = event.getPlayer();
        for (UUID uuid : ConfigHandler.getInstance().getPlayersToHideFrom()) {
            Player listedPlayer = SimpleVanish.getInstance().getServer().getPlayer(uuid);
            if (listedPlayer == null) continue;
            listedPlayer.hidePlayer(SimpleVanish.getInstance(), player);
            if (ConfigHandler.getInstance().shouldRemoveFromTablist()) {
                listedPlayer.unlistPlayer(player);
            }
        }
        if (ConfigHandler.getInstance().shouldChatFakeLeave()) {
            SimpleVanish.getInstance().getServer().sendMessage(MiniMessage.miniMessage().deserialize("<yellow><lang:multiplayer.player.left:" + player.getName() + "></yellow>"));
        }
    }


}
