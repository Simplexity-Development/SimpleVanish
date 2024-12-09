package simplexity.simpleVanish.listeners;

import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import simplexity.simpleVanish.SimpleVanish;
import simplexity.simpleVanish.objects.PlayerVanishSettings;
import simplexity.simpleVanish.objects.VanishSettingsPermission;
import simplexity.simpleVanish.saving.SqlHandler;

public class AttackListener implements Listener {
    @EventHandler
    public void onAttack(PrePlayerAttackEntityEvent attackEvent){
        Player player = attackEvent.getPlayer();
        if (!SimpleVanish.getVanishedPlayers().contains(player)) return;
        PlayerVanishSettings vanishSettings = SqlHandler.getInstance().getVanishSettings(player.getUniqueId());
        if (player.hasPermission(VanishSettingsPermission.ATTACK_ENTITIES) && vanishSettings.canAttackEntities()) return;
        attackEvent.setCancelled(true);
    }
}
