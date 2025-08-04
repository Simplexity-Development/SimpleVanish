package simplexity.simplevanish.listeners;

import org.bukkit.block.BlockState;
import org.bukkit.block.TrialSpawner;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.TrialSpawnerSpawnEvent;
import org.slf4j.Logger;
import simplexity.simplevanish.SimpleVanish;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class TrialSpawnListener implements Listener {

    @EventHandler
    public void onTrialSpawn(TrialSpawnerSpawnEvent spawnEvent) {
        TrialSpawner spawner = spawnEvent.getTrialSpawner();
        List<Player> trackedPlayers = new ArrayList<>(spawner.getTrackedPlayers().stream().toList());
        List<Player> modifiedPlayers = new ArrayList<>();
        for (Player player : trackedPlayers) {
            if (ListenerUtils.userNotVanished(player)) {
                modifiedPlayers.add(player);
            }
        }
        if (!modifiedPlayers.isEmpty()) return;
        spawner.setNextSpawnAttempt(0L);
        spawnEvent.setCancelled(true);
    }


}
