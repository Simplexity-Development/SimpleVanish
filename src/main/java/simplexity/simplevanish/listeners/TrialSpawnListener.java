package simplexity.simplevanish.listeners;

import org.bukkit.block.TrialSpawner;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.TrialSpawnerSpawnEvent;

import java.util.Optional;

@SuppressWarnings("UnstableApiUsage")
public class TrialSpawnListener implements Listener {

    @EventHandler
    public void onTrialSpawn(TrialSpawnerSpawnEvent spawnEvent) {
        TrialSpawner spawner = spawnEvent.getTrialSpawner();
        Optional<Player> anyModifiedPlayer = spawner.getTrackedPlayers().stream()
                .filter(ListenerUtils::userNotVanished)
                .findAny();
        if (anyModifiedPlayer.isPresent()) return;
        spawner.setNextSpawnAttempt(0L);
        spawnEvent.setCancelled(true);
    }
}
