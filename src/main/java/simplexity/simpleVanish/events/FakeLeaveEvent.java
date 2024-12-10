package simplexity.simpleVanish.events;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class FakeLeaveEvent extends Event implements Cancellable {


    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private Component message;
    private boolean cancelled;

    public FakeLeaveEvent(@NotNull final Player player, @NotNull final Component message) {
        this.player = player;
        this.message = message;
    }

    /**
     * The player who triggered this event, either by vanishing or running a command
     * @return Player
     */
    public @NotNull Player getPlayer() {
        return player;
    }

    /**
     * The message that will be sent to the server on the 'fake leave'
     * @return Component form of message to be sent to the server.
     */
    public @NotNull Component getMessage() {
        return message;
    }

    /**
     * The message that will be sent to the server on the 'fake leave'
     * @param message Component form of a message to be used.
     */
    public void setMessage(Component message) {
        this.message = message;
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
