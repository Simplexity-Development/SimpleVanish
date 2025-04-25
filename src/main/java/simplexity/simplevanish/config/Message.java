package simplexity.simplevanish.config;

public enum Message {
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
    SETTING_INSERT_MOBS_TARGET("settings.insert.mobs-target", "<green>Mobs Target</green>"),
    SETTING_INSERT_PERSIST("settings.insert.vanish-persist", "<green>Persist</green>"),
    SETTING_INSERT_NIGHT_VISION("settings.insert.night-vision", "<green>Night Vision</green>"),
    SETTING_INSERT_SILENT_JOIN("settings.insert.silent-join", "<green>Silent Join</green>"),
    SETTING_INSERT_SILENT_LEAVE("settings.insert.silent-leave", "<green>Silent Leave</green>"),
    SETTING_INSERT_VANISH_NOTIFICATIONS("settings.insert.vanish-notifications", "<green>Vanish Notifications</green>"),
    VANISH_ENABLED("vanish.enabled", "<green>You are now vanished!</green>"),
    VANISH_DISABLED("vanish.disabled", "<gray>You are no longer vanished!</gray>"),
    VANISH_REMINDER("vanish.reminder", "<yellow>You are currently vanished!</yellow>"),
    VIEW_USER_JOINED_SILENTLY("view.user-joined-silently", "<gray> <username> joined the server silently</gray>"),
    VIEW_USER_LEFT_SILENTLY("view.user-left-silently", "<gray> <username> left silently</gray>"),
    VIEW_USER_VANISHED("view.user-vanished", "<gray> <username> vanished</gray>"),
    VIEW_USER_UNVANISHED("view.user-unvanished", "<gray> <username> unvanished</gray>"),
    VIEW_TABLIST_FORMAT("view.tablist-format", "<gray><i>[Hidden] <username></i></gray>"),
    MESSAGE_FAKE_JOIN("message.fake-join", "<yellow><lang:multiplayer.player.joined:<username>></yellow>"),
    MESSAGE_FAKE_LEAVE("message.fake-leave", "<yellow><lang:multiplayer.player.left:<username>></yellow>"),
    MESSAGE_CONFIG_RELOADED("message.config-reloaded", "<gold>SimpleVanish config has been reloaded</gold>"),
    ERROR_MUST_BE_PLAYER("error.must-be-player", "<red>Sorry, only a player can run this command!</red>"),
    ERROR_NO_PERMISSION("error.no-permission", "<red>Sorry, you do not have permission to run this command</red>"),
    ERROR_INVALID_SUBCOMMAND("error.invalid-subcommand", "<red>Sorry, <gray><value></gray> is an invalid subcommand!</red>"),
    ERROR_NO_PLAYER_FOUND_TRANSLATABLE("error.no-player-found", "<red><lang:argument.entity.notfound.player></red>"),
    ERROR_NO_TARGET_PLAYER_FOUND("error.no-target-player-found", "<red><name> either does not exist or is not online. Please check your syntax and try again</red>"),
    ERROR_NOT_ENOUGH_ARGUMENTS("error.not-enough-arguments", "<red>Not enough arguments provided. Required Arguments: <args></red>"),
    SOMETHING_WENT_WRONG("error.something-wrong", "<red>Sorry, something went wrong, please check console for more information</red>"),
    SETTING_CHANGED("settings.message.updated", "<gray><setting> is now <value>");

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
