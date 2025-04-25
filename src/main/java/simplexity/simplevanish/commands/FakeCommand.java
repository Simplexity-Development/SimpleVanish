package simplexity.simplevanish.commands;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import simplexity.simplevanish.config.Message;
import simplexity.simplevanish.handling.FakeJoinHandler;
import simplexity.simplevanish.handling.FakeLeaveHandler;

import java.util.List;
import java.util.Set;

public class FakeCommand implements TabExecutor {

    private final Set<String> allowedArgs = Set.of("leave", "join");


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length < 1) {
            sender.sendRichMessage(Message.ERROR_NOT_ENOUGH_ARGUMENTS.getMessage(),
                    Placeholder.parsed("args", "<leave|join>"));
            return false;
        }
        if (!(sender instanceof Player player)) {
            handleConsoleSender(sender, args);
            return true;
        }
        String leaveOrJoin = args[0];
        if (!allowedArgs.contains(leaveOrJoin.toLowerCase())) {
            player.sendRichMessage(Message.ERROR_INVALID_SUBCOMMAND.getMessage(),
                    Placeholder.parsed("value", leaveOrJoin));
            return false;
        }
        if (leaveOrJoin.equalsIgnoreCase("leave")) {
            FakeLeaveHandler.getInstance().sendFakeLeaveMessage(player);
            return false;
        }
        if (leaveOrJoin.equalsIgnoreCase("join")) {
            FakeJoinHandler.getInstance().sendFakeJoinMessage(player);
            return true;
        }
        return false;
    }

    private void handleConsoleSender(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendRichMessage(Message.ERROR_NOT_ENOUGH_ARGUMENTS.getMessage(),
                    Placeholder.parsed("args", "<leave|join> <player>"));
            return;
        }
        String leaveOrJoin = args[0];
        if (!allowedArgs.contains(leaveOrJoin.toLowerCase())) {
            sender.sendRichMessage(Message.ERROR_INVALID_SUBCOMMAND.getMessage(),
                    Placeholder.parsed("value", leaveOrJoin));
            return;
        }
        Player player = Bukkit.getPlayer(args[1]);
        if (player == null) {
            sender.sendRichMessage(Message.ERROR_NO_TARGET_PLAYER_FOUND.getMessage(),
                    Placeholder.parsed("name", args[1]));
            return;
        }
        if (leaveOrJoin.equalsIgnoreCase("leave")) {
            FakeLeaveHandler.getInstance().sendFakeLeaveMessage(player);
            return;
        }
        if (leaveOrJoin.equalsIgnoreCase("join")) {
            FakeJoinHandler.getInstance().sendFakeJoinMessage(player);
        }
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length < 1) {
            return List.of("leave", "join");
        }
        return null;
    }
}
