package simplexity.simpleVanish.handling;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import simplexity.simpleVanish.SimpleVanish;
import simplexity.simpleVanish.config.ConfigHandler;
import simplexity.simpleVanish.config.Message;
import simplexity.simpleVanish.events.FakeLeaveEvent;

public class FakeLeaveHandler {
    private static FakeLeaveHandler instance;

    public FakeLeaveHandler() {
    }

    public static FakeLeaveHandler getInstance() {
        if (instance == null) instance = new FakeLeaveHandler();
        return instance;
    }

    private final MiniMessage miniMessage = SimpleVanish.getMiniMessage();

    public void sendFakeLeaveMessage(Player player) {
        Component message;
        if (ConfigHandler.getInstance().isCustomJoinLeave()) {
            message = MessageHandler.getInstance().parsePlayerMessage(player, ConfigHandler.getInstance().getCustomLeaveMessage());
        } else {
            message = miniMessage.deserialize(Message.MESSAGE_FAKE_LEAVE.getMessage(),
                    Placeholder.parsed("username", player.getName()));
        }
        FakeLeaveEvent event = new FakeLeaveEvent(player, message);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) return;
        Bukkit.getServer().sendMessage(event.getMessage());
    }
}
