package simplexity.simplevanish.listeners;

import io.papermc.paper.event.block.VaultChangeStateEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class VaultStateListener implements Listener {

    @EventHandler
    public void onVaultStateChange(VaultChangeStateEvent changeEvent){
        Player player = changeEvent.getPlayer();
        if (!ListenerUtils.userNotVanished(player)) changeEvent.setCancelled(true);
    }
}
