package simplexity.simpleVanish.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import simplexity.simpleVanish.SimpleVanish;
import simplexity.simpleVanish.handling.VanishHandler;

import java.util.List;

public class Vanish implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("You must be a player to use this command.");
            return true;
        }
        if (SimpleVanish.getVanishedPlayers().contains(player)) {
            VanishHandler.runUnvanishEvent(player);
            player.sendMessage("VANISH DISABLED");
        } else {
            VanishHandler.runVanishEvent(player);
            player.sendMessage("VANISH ENABLED");
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return List.of();
    }
}
