package simplexity.simpleVanish.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import simplexity.simpleVanish.SimpleVanish;
import simplexity.simpleVanish.config.Message;
import simplexity.simpleVanish.saving.Cache;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class VanishSettings implements TabExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendRichMessage(Message.ERROR_MUST_BE_PLAYER.getMessage());
            return false;
        }
        HashSet<SubCommand> subCommands = getCommandsUserCanAccess(player);
        if (subCommands.isEmpty()) {
            player.sendRichMessage(Message.ERROR_NO_PERMISSION.getMessage());
            return false;
        }
        if (args.length == 0) {
            player.sendMessage(displayCurrentSettings(player));
            return true;
        }
        String commandName = args[0];
        iterateSubCommands(player, subCommands, commandName, args);
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) return List.of();
        if (strings.length > 1) return List.of();
        List<String> subCommands = new ArrayList<>();
        for (SubCommand subCommand : getCommandsUserCanAccess(player)) {
            subCommands.add(subCommand.getCommandName());
        }
        return subCommands;
    }

    private HashSet<SubCommand> getCommandsUserCanAccess(Player player) {
        HashSet<SubCommand> commands = new HashSet<>();
        for (SubCommand subCommand : Cache.getSubCommands()) {
            if (player.hasPermission(subCommand.getCommandPermission())) {
                commands.add(subCommand);
            }
        }
        return commands;
    }

    private Component displayCurrentSettings(Player player) {
        MiniMessage miniMessage = SimpleVanish.getMiniMessage();
        Component settingsMessage = miniMessage.deserialize(Message.SETTING_LIST_HEADER.getMessage());
        for (SubCommand subCommand : Cache.getSubCommands()) {
            if (!player.hasPermission(subCommand.getCommandPermission())) continue;
            Component valueComponent;
            if (subCommand.isEnabled(player)) {
                valueComponent = miniMessage.deserialize(Message.SETTING_INSERT_ENABLED.getMessage());
            } else {
                valueComponent = miniMessage.deserialize(Message.SETTING_INSERT_DISABLED.getMessage());
            }
            Component appendableMessage = miniMessage.deserialize(Message.SETTING_LIST_ITEM.getMessage(),
                    Placeholder.parsed("setting", toTitleCase(subCommand.getCommandName())),
                    Placeholder.component("value", valueComponent));
            settingsMessage = settingsMessage.append(appendableMessage);
        }
        return settingsMessage;
    }

    private void iterateSubCommands(Player player, HashSet<SubCommand> subCommands, String suppliedCommand, String[] args) {
        for (SubCommand subCommand : subCommands) {
            if (!subCommand.getCommandName().equalsIgnoreCase(suppliedCommand)) continue;
            boolean setEnabled = !subCommand.isEnabled(player);
            subCommand.execute(player, setEnabled);
            return;
        }
        player.sendRichMessage(Message.ERROR_INVALID_SUBCOMMAND.getMessage(),
                Placeholder.unparsed("value", suppliedCommand));
    }

    private String toTitleCase(String string) {
        if (string == null || string.isEmpty()) return string;
        String replaceDashes = string.replace("-", " ");
        String[] words = replaceDashes.split(" ");
        StringBuilder stringBuilder = new StringBuilder();
        for (String word : words) {
            if (word.isEmpty()) continue;
            stringBuilder.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1).toLowerCase())
                    .append(" ");
        }
        return stringBuilder.toString().trim();
    }


}
