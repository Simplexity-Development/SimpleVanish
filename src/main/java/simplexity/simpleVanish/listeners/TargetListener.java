package simplexity.simpleVanish.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import simplexity.simpleVanish.SimpleVanish;

public class TargetListener implements Listener {
    @EventHandler
    public void onTarget(EntityTargetEvent targetEvent) {
        if (!(targetEvent.getTarget() instanceof Player target)) return;
        if (!SimpleVanish.getVanishedPlayers().contains(target)) return;
    }
}
