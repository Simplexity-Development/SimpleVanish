package simplexity.simplevanish.handling;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import simplexity.simplevanish.SimpleVanish;
import simplexity.simplevanish.config.ConfigHandler;
import simplexity.simplevanish.config.Message;
import simplexity.simplevanish.events.FakeJoinEvent;

public class FakeJoinHandler {
    private static FakeJoinHandler instance;

    public FakeJoinHandler() {
    }

    public static FakeJoinHandler getInstance() {
        if (instance == null) instance = new FakeJoinHandler();
        return instance;
    }

    private final MiniMessage miniMessage = SimpleVanish.getMiniMessage();

    public void sendFakeJoinMessage(@NotNull Player player) {
        Component message;
        if (ConfigHandler.getInstance().isCustomJoinLeave()) {
            message = MessageHandler.parsePlayerMessage(player,
                    ConfigHandler.getInstance().getCustomJoinMessage());
        } else {
            message = miniMessage.deserialize(Message.MESSAGE_FAKE_JOIN.getMessage(),
                    Placeholder.parsed("username", player.getName()));
        }
        FakeJoinEvent event = new FakeJoinEvent(player, message);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) return;
        Bukkit.getServer().sendMessage(event.getMessage());
    }
}
