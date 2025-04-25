package simplexity.simplevanish.listeners;

import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import simplexity.simplevanish.objects.PlayerVanishSettings;
import simplexity.simplevanish.objects.VanishPermission;
import simplexity.simplevanish.saving.Cache;

public class AttackListener implements Listener {
    @EventHandler
    public void onAttack(PrePlayerAttackEntityEvent attackEvent) {
        Player player = attackEvent.getPlayer();
        if (ListenerUtils.shouldEarlyReturn(player)) return;
        if (attackEnabled(player)) return;
        attackEvent.setCancelled(true);
    }

    private boolean attackEnabled(Player player) {
        PlayerVanishSettings vanishSettings = Cache.getVanishSettings(player.getUniqueId());
        return player.hasPermission(VanishPermission.ATTACK_ENTITIES) && vanishSettings.canAttackEntities();
    }
}
