package simplexity.simplevanish.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import simplexity.simplevanish.objects.PlayerVanishSettings;

public class PlayerVanishEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final PlayerVanishSettings settings;
    private boolean cancelled;


    public PlayerVanishEvent(@NotNull final Player player, @NotNull PlayerVanishSettings settings) {
        this.player = player;
        this.settings = settings;
    }

    /**
     * Gets the player who is vanishing
     *
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

    public PlayerVanishSettings getSettings() {
        return settings;
    }
}
