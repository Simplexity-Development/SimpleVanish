package simplexity.simplevanish.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import simplexity.simplevanish.config.ConfigHandler;
import simplexity.simplevanish.config.Message;
import simplexity.simplevanish.saving.SqlHandler;

public class VanishReload implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        ConfigHandler.getInstance().loadConfigValues();
        SqlHandler.getInstance().closeConnection();
        SqlHandler.getInstance().init();
        sender.sendRichMessage(Message.MESSAGE_CONFIG_RELOADED.getMessage());
        return false;
    }
}
