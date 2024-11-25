package simplexity.simpleVanish.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerVanishEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;

    public PlayerVanishEvent(@NotNull final Player player) {
        this.player = player;
    }

    /**
     * Gets the player who is vanishing
     * @return Player
     */
    public @NotNull Player getPlayer() {
        return player;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
