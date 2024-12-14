package simplexity.simpleVanish.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import simplexity.simpleVanish.config.ConfigHandler;
import simplexity.simpleVanish.config.LocaleHandler;
import simplexity.simpleVanish.handling.UnvanishHandler;
import simplexity.simpleVanish.handling.VanishHandler;
import simplexity.simpleVanish.saving.Cache;

public class Vanish implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("You must be a player to use this command.");
            return true;
        }

        if (Cache.getVanishedPlayers().contains(player)) {
            boolean fakeJoin = ConfigHandler.getInstance().shouldChatFakeJoin();
            UnvanishHandler.getInstance().runUnvanishEvent(player, fakeJoin, LocaleHandler.Message.VIEW_USER_UNVANISHED.getMessage());
            player.sendRichMessage(LocaleHandler.Message.VANISH_DISABLED.getMessage());
        } else {
            boolean fakeLeave = ConfigHandler.getInstance().shouldChatFakeLeave();
            VanishHandler.getInstance().runVanishEvent(player, fakeLeave, LocaleHandler.Message.VIEW_USER_VANISHED.getMessage());
            player.sendRichMessage(LocaleHandler.Message.VANISH_ENABLED.getMessage());
        }
        return false;
    }
}
