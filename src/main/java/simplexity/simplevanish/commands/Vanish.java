package simplexity.simplevanish.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import simplexity.simplevanish.config.ConfigHandler;
import simplexity.simplevanish.config.Message;
import simplexity.simplevanish.handling.UnvanishHandler;
import simplexity.simplevanish.handling.VanishHandler;
import simplexity.simplevanish.saving.Cache;

public class Vanish implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendRichMessage(Message.ERROR_MUST_BE_PLAYER.getMessage());
            return true;
        }
        if (Cache.getVanishedPlayers().contains(player)) {
            boolean fakeJoin = ConfigHandler.getInstance().shouldChatFakeJoin();
            UnvanishHandler.getInstance().runUnvanishEvent(player, fakeJoin, Message.VIEW_USER_UNVANISHED.getMessage());
            player.sendRichMessage(Message.VANISH_DISABLED.getMessage());
        } else {
            boolean fakeLeave = ConfigHandler.getInstance().shouldChatFakeLeave();
            VanishHandler.getInstance().runVanishEvent(player, fakeLeave, Message.VIEW_USER_VANISHED.getMessage());
            player.sendRichMessage(Message.VANISH_ENABLED.getMessage());
        }
        return false;
    }
}
