package simplexity.simpleVanish.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class FakeJoinEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final String message;

    public FakeJoinEvent(@NotNull final Player player, @NotNull final String message) {
        this.player = player;
        this.message = message;
    }

    /**
     * The player who triggered this event, either by un-vanishing or running a command
     * @return Player
     */
    public @NotNull Player getPlayer() {
        return player;
    }

    /**
     * The message that will be sent to the server on the 'fake join'
     * @return String (message)
     */
    public @NotNull String getMessage() {
        return message;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

