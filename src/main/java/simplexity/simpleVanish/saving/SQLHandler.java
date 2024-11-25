package simplexity.simpleVanish.saving;

import simplexity.simpleVanish.objects.NotificationLocation;
import simplexity.simpleVanish.objects.PlayerVanishSettings;
import simplexity.simpleVanish.SimpleVanish;
import simplexity.simpleVanish.config.ConfigHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

public class SQLHandler {
    Connection connection;
    Logger logger = SimpleVanish.getInstance().getLogger();

    private SQLHandler() {
    }

    private static final HashMap<UUID, PlayerVanishSettings> cachedSettings = new HashMap<>();
    public static PlayerVanishSettings defaultSettings = new PlayerVanishSettings(false, false, false, false, false, false, false, false, NotificationLocation.ACTION_BAR);

    private static SQLHandler instance;

    public static SQLHandler getInstance() {
        if (instance == null) {
            instance = new SQLHandler();
        }
        return instance;
    }

    public void init(){
        try {
            connection = getConnection();
            try (Statement statement = connection.createStatement()) {
                statement.execute("""
                CREATE TABLE IF NOT EXISTS vanish_settings (
                    player_uuid VARCHAR(36),
                    toggle_bitmask INT NOT NULL,
                    notification_location VARCHAR(128) NOT NULL
                );""");
            }
        } catch (SQLException e) {
            logger.severe("Failed to connect to database");
            logger.severe("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public PlayerVanishSettings getVanishSettings(UUID uuid){
        if (!cachedSettings.containsKey(uuid)) {
            updateCache(uuid);
        }
        return cachedSettings.get(uuid);
    }

    public void savePlayerSettings(UUID uuid, PlayerVanishSettings settings){
        String query = """
                INSERT INTO vanish_settings (player_uuid, toggle_bitmask, notification_location)
                VALUES (?, ?, ?)
                ON DUPLICATE KEY UPDATE
                toggle_bitmask = VALUES(toggle_bitmask),
                notification_location = VALUES(notification_location);
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, uuid.toString());
            statement.setInt(2, settings.toBitmask());
            statement.setString(3, settings.getNotificationLocation().getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.severe("Failed to save player settings to database");
            logger.severe("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateCache(UUID uuid){
        String query = "SELECT toggle_bitmask, notification_location FROM vanish_settings WHERE player_uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, uuid.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int toggleBitMask = resultSet.getInt("toggle_bitmask");
                    NotificationLocation notificationLocation = NotificationLocation.valueOf(resultSet.getString("notification_location"));
                    PlayerVanishSettings settings = newSettingsFromBitmask(toggleBitMask, notificationLocation);
                    cachedSettings.put(uuid, settings);
                }
            }
        } catch (SQLException e) {
            logger.severe("Failed to get vanish settings from database");
            logger.severe("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        if (ConfigHandler.getInstance().isMysqlEnabled()){
            return DriverManager.getConnection("jdbc:mysql://" + ConfigHandler.getInstance().getMysqlIP() + "/"
                    + ConfigHandler.getInstance().getDatabaseName(),
                    ConfigHandler.getInstance().getDatabaseUsername(),
                    ConfigHandler.getInstance().getDatabasePassword());
        }
        return DriverManager.getConnection("jdbc:sqlite:"
                + SimpleVanish.getInstance().getDataFolder()
                + "/vanish-settings.db");
    }

    public PlayerVanishSettings newSettingsFromBitmask(int bitmask, NotificationLocation notificationLocation) {
        return new PlayerVanishSettings(
                (bitmask & (1)) != 0, // isVanished
                (bitmask & (1 << 1)) != 0, // shouldVanishPersist
                (bitmask & (1 << 2)) != 0, // isNightVisionEnabled
                (bitmask & (1 << 3)) != 0, // canBreakBlocks
                (bitmask & (1 << 4)) != 0, // canAttackPlayers
                (bitmask & (1 << 5)) != 0, // canAttackEntities
                (bitmask & (1 << 6)) != 0, // isInvulnerable
                (bitmask & (1 << 7)) != 0, // shouldAllowFlight
                notificationLocation
        );
    }
}
