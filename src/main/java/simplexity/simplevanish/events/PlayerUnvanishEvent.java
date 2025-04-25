package simplexity.simplevanish.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerUnvanishEvent extends Event implements Cancellable {


    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private boolean cancelled;

    public PlayerUnvanishEvent(@NotNull final Player player) {
        this.player = player;
    }

    /**
     * Gets the player who is unvanishing
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

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
