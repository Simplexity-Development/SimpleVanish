package simplexity.simplevanish.saving;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import simplexity.simplevanish.SimpleVanish;
import simplexity.simplevanish.config.ConfigHandler;
import simplexity.simplevanish.objects.PlayerVanishSettings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.logging.Logger;

@SuppressWarnings({"FieldCanBeLocal", "CallToPrintStackTrace"})

public class SqlHandler {
    private static final HikariConfig hikariConfig = new HikariConfig();
    private static HikariDataSource dataSource;

    Connection connection;
    Logger logger = SimpleVanish.getInstance().getLogger();
    private final String initStatement = """
            CREATE TABLE IF NOT EXISTS vanish_settings (
                player_uuid VARCHAR(36) PRIMARY KEY,
                is_vanished BOOLEAN NOT NULL,
                vanish_persists BOOLEAN NOT NULL,
                night_vision BOOLEAN NOT NULL,
                break_blocks BOOLEAN NOT NULL,
                open_containers BOOLEAN NOT NULL,
                attack_entities BOOLEAN NOT NULL,
                mobs_target BOOLEAN NOT NULL,
                pickup_items BOOLEAN NOT NULL,
                invulnerability BOOLEAN NOT NULL,
                leave_silently BOOLEAN NOT NULL,
                join_silently BOOLEAN NOT NULL,
                vanish_notifications BOOLEAN NOT NULL
            );
            """;
    private final String updateStatement = """
            REPLACE INTO vanish_settings (player_uuid,
            is_vanished, vanish_persists, night_vision,
            break_blocks, open_containers, attack_entities,
            mobs_target, pickup_items, invulnerability,
            leave_silently, join_silently, vanish_notifications)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
            """;
    private final String selectionStatement = """
            SELECT is_vanished, vanish_persists, night_vision,
            break_blocks, open_containers, attack_entities,
            mobs_target, pickup_items, invulnerability,
            leave_silently, join_silently, vanish_notifications
            FROM vanish_settings WHERE player_uuid = ?
            """;

    private SqlHandler() {
    }

    private static SqlHandler instance;

    public static SqlHandler getInstance() {
        if (instance == null) {
            instance = new SqlHandler();
        }
        return instance;
    }


    public void init() {
        setupConfig();
        try {
            connection = getConnection();
            try (Statement statement = connection.createStatement()) {
                statement.execute(initStatement);
            }
        } catch (SQLException e) {
            logger.severe("Failed to connect to database");
            logger.severe("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void savePlayerSettings(UUID uuid, PlayerVanishSettings settings) {
        try (PreparedStatement statement = connection.prepareStatement(updateStatement)) {
            statement.setString(1, uuid.toString());
            statement.setBoolean(2, settings.isVanished());
            statement.setBoolean(3, settings.shouldVanishPersist());
            statement.setBoolean(4, settings.giveNightvision());
            statement.setBoolean(5, settings.canBreakBlocks());
            statement.setBoolean(6, settings.shouldContainersOpen());
            statement.setBoolean(7, settings.canAttackEntities());
            statement.setBoolean(8, settings.shouldMobsTarget());
            statement.setBoolean(9, settings.shouldPickupItems());
            statement.setBoolean(10, settings.shouldGiveInvulnerability());
            statement.setBoolean(11, settings.shouldLeaveSilently());
            statement.setBoolean(12, settings.shouldJoinSilently());
            statement.setBoolean(13, settings.viewVanishNotifications());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.severe("Failed to save player settings to database");
            logger.severe("Error: " + e.getMessage());
            e.printStackTrace();
        }
        Cache.updateSettingsCache(uuid, settings);
    }

    public void updateSettings(UUID uuid) {
        try (PreparedStatement statement = connection.prepareStatement(selectionStatement)) {
            statement.setString(1, uuid.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    boolean isVanished = resultSet.getBoolean("is_vanished");
                    boolean vanishPersists = resultSet.getBoolean("vanish_persists");
                    boolean nightVision = resultSet.getBoolean("night_vision");
                    boolean breakBlocks = resultSet.getBoolean("break_blocks");
                    boolean openContainers = resultSet.getBoolean("open_containers");
                    boolean attackEntities = resultSet.getBoolean("attack_entities");
                    boolean mobsTarget = resultSet.getBoolean("mobs_target");
                    boolean pickupItems = resultSet.getBoolean("pickup_items");
                    boolean invulnerability = resultSet.getBoolean("invulnerability");
                    boolean leaveSilently = resultSet.getBoolean("leave_silently");
                    boolean joinSilently = resultSet.getBoolean("join_silently");
                    boolean vanishNotifications = resultSet.getBoolean("vanish_notifications");
                    PlayerVanishSettings settings = new PlayerVanishSettings(isVanished, vanishPersists, nightVision,
                            breakBlocks, openContainers, attackEntities, mobsTarget, pickupItems, invulnerability,
                            leaveSilently, joinSilently, vanishNotifications);
                    Cache.updateSettingsCache(uuid, settings);
                } else {
                    PlayerVanishSettings settings = new PlayerVanishSettings();
                    Cache.updateSettingsCache(uuid, settings);
                    savePlayerSettings(uuid, settings);
                }
            }
        } catch (SQLException e) {
            logger.severe("Failed to get vanish settings from database");
            logger.severe("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }


    private void setupConfig() {
        if (ConfigHandler.getInstance().isMysqlEnabled()) {
            hikariConfig.setJdbcUrl("jdbc:mysql://" + ConfigHandler.getInstance().getMysqlIP() + "/"
                                    + ConfigHandler.getInstance().getDatabaseName());
            hikariConfig.setUsername(ConfigHandler.getInstance().getDatabaseUsername());
            hikariConfig.setPassword(ConfigHandler.getInstance().getDatabasePassword());
            dataSource = new HikariDataSource(hikariConfig);
            return;
        }
        hikariConfig.setJdbcUrl("jdbc:sqlite:" + SimpleVanish.getInstance().getDataFolder() + "/vanish-settings.db");
        hikariConfig.setConnectionTestQuery("PRAGMA journal_mode = WAL;");
        dataSource = new HikariDataSource(hikariConfig);
    }

}
