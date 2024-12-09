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
import java.util.Locale;
import java.util.UUID;
import java.util.logging.Logger;

public class SqlHandler {
    Connection connection;
    Logger logger = SimpleVanish.getInstance().getLogger();

    private SqlHandler() {
    }

    private static final HashMap<UUID, PlayerVanishSettings> cachedSettings = new HashMap<>();
    public static PlayerVanishSettings defaultSettings = new PlayerVanishSettings(
            false, false, false,
            false, false, false,
            false, false, false,
            false, false, false,
            NotificationLocation.ACTION_BAR);

    private static SqlHandler instance;

    public static SqlHandler getInstance() {
        if (instance == null) {
            instance = new SqlHandler();
        }
        return instance;
    }

    public void init(){
        try {
            connection = getConnection();
            try (Statement statement = connection.createStatement()) {
                statement.execute("""
                CREATE TABLE IF NOT EXISTS vanish_settings (
                    player_uuid VARCHAR(36) PRIMARY KEY,
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
            updateSettings(uuid);
        }
        return cachedSettings.get(uuid);
    }

    public void savePlayerSettings(UUID uuid, PlayerVanishSettings settings){
        String query = """
                REPLACE INTO vanish_settings (player_uuid, toggle_bitmask, notification_location)
                VALUES (?, ?, ?);
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
        updateSettingsCache(uuid, settings);
    }

    private void updateSettings(UUID uuid){
        String query = "SELECT toggle_bitmask, notification_location FROM vanish_settings WHERE player_uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, uuid.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int toggleBitMask = resultSet.getInt("toggle_bitmask");
                    NotificationLocation notificationLocation = NotificationLocation.valueOf(resultSet.getString("notification_location").toUpperCase(Locale.ROOT));
                    PlayerVanishSettings settings = newSettingsFromBitmask(toggleBitMask, notificationLocation);
                    updateSettingsCache(uuid, settings);
                } else {
                    updateSettingsCache(uuid, defaultSettings);
                    savePlayerSettings(uuid, defaultSettings);
                }
            }
        } catch (SQLException e) {
            logger.severe("Failed to get vanish settings from database");
            logger.severe("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateSettingsCache(UUID uuid, PlayerVanishSettings settings){
        cachedSettings.remove(uuid);
        cachedSettings.put(uuid, settings);
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
                (bitmask & (1 << 4)) != 0, // canOpenContainers
                (bitmask & (1 << 5)) != 0, // canAttackEntities
                (bitmask & (1 << 6)) != 0, // preventMobTargeting
                (bitmask & (1 << 7)) != 0, // canPickupItems
                (bitmask & (1 << 8)) != 0, // isInvulnerable
                (bitmask & (1 << 9)) != 0, // shouldAllowFlight
                (bitmask & (1 << 10)) != 0, // shouldJoinSilently
                (bitmask & (1 << 11)) != 0, // shouldLeaveSilently
                notificationLocation
        );
    }

    public void removePlayerFromCache(UUID uuid){
        cachedSettings.remove(uuid);
    }
}
