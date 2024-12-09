package simplexity.simpleVanish.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import simplexity.simpleVanish.SimpleVanish;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LocaleHandler {

    public enum Message {
        SETTING_CHANGED("settings.message.updated", "<gray><setting> is now <value>"),
        SETTING_LIST_HEADER("settings.list.header", "<gray>Vanish settings</gray>:"),
        SETTING_LIST_ITEM("settings.list.item", "\n<white><setting>:</white> <gold><value></gold>"),
        SETTING_INSERT_ENABLED("settings.insert.enabled", "<gold>enabled</gold>"),
        SETTING_INSERT_DISABLED("settings.insert.disabled", "<dark_gray>disabled</dark_gray>"),
        SETTING_INSERT_INVULNERABLE("settings.insert.invulnerable", "<green>Invulnerable</green>"),
        SETTING_INSERT_FLIGHT("settings.insert.flight", "<green>Flight</green>"),
        SETTING_INSERT_BREAK_BLOCKS("settings.insert.break-blocks", "<green>Break Blocks</green>"),
        SETTING_INSERT_OPEN_CONTAINERS("settings.insert.open-containers", "<green>Open Containers</green>"),
        SETTING_INSERT_ATTACK_ENTITIES("settings.insert.attack-entities", "<green>Attack Entities</green>"),
        SETTING_INSERT_PICK_UP_ITEMS("settings.insert.pick-up-items", "<green>Pick Up Items</green>"),
        SETTING_INSERT_MOBS_TARGET("settings.insert.vanish-persist", "<green>Mobs Target</green>"),
        SETTING_INSERT_PERSIST("settings.insert.vanish-persist", "<green>Persist</green>"),
        SETTING_INSERT_NIGHT_VISION("settings.insert.night-vision", "<green>Night Vision</green>"),
        SETTING_INSERT_SILENT_JOIN("settings.insert.silent-join", "<green>Silent Join</green>"),
        SETTING_INSERT_SILENT_LEAVE("settings.insert.silent-leave", "<green>Silent Leave</green>"),
        VIEW_USER_JOINED_SILENTLY("view.user-joined-silently", "<gray> <username> joined the server silently</gray>"),
        VIEW_USER_LEFT_SILENTLY("view.user-left-silently", "<gray> <username> left silently</gray>"),
        VIEW_USER_VANISHED("view.user-vanished", "<gray> <username> vanished</gray>"),
        VIEW_USER_UNVANISHED("view.user-unvanished", "<gray> <username> unvanished</gray>"),
        ERROR_MUST_BE_PLAYER("error.must-be-player", "<red>Sorry, only a player can run this command!</red>"),
        ERROR_NO_PERMISSION("error.no-permission", "<red>Sorry, you do not have permission to run this command</red>"),
        ERROR_INVALID_SUBCOMMAND("error.invalid-subcommand", "<red>Sorry, <gray><value></gray> is an invalid subcommand!</red>"),
        SOMETHING_WENT_WRONG("error.something-wrong", "<red>Sorry, something went wrong, please check console for more information</red>");

        private final String path;
        private String message;

        Message(String path, String message) {
            this.path = path;
            this.message = message;
        }

        public String getPath() {
            return path;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

    }

    private static LocaleHandler instance;
    private final String fileName = "locale.yml";
    private final File dataFile = new File(SimpleVanish.getInstance().getDataFolder(), fileName);
    private FileConfiguration locale = new YamlConfiguration();

    private LocaleHandler() {
        try {
            dataFile.getParentFile().mkdirs();
            dataFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        reloadLocale();
    }

    public static LocaleHandler getInstance() {
        if (instance == null) {
            instance = new LocaleHandler();
        }
        return instance;
    }

    public void reloadLocale() {
        try {
            locale.load(dataFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        populateLocale();
        sortLocale();
        saveLocale();
    }


    private void populateLocale() {
        Set<Message> missing = new HashSet<>(Arrays.asList(Message.values()));
        for (Message message : Message.values()) {
            if (locale.contains(message.getPath())) {
                message.setMessage(locale.getString(message.getPath()));
                missing.remove(message);
            }
        }

        for (Message message : missing) {
            locale.set(message.getPath(), message.getMessage());
        }


    }

    private void sortLocale() {
        FileConfiguration newLocale = new YamlConfiguration();
        List<String> keys = new ArrayList<>();
        keys.addAll(locale.getKeys(true));
        Collections.sort(keys);
        for (String key : keys) {
            newLocale.set(key, locale.getString(key));
        }
        locale = newLocale;
    }

    private void saveLocale() {
        try {
            locale.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
