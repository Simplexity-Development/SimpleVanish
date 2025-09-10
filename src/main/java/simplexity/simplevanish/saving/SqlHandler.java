package simplexity.simplevanish.saving;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import simplexity.simplevanish.SimpleVanish;
import simplexity.simplevanish.config.ConfigHandler;
import simplexity.simplevanish.objects.PlayerVanishSettings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@SuppressWarnings("FieldCanBeLocal")

public class SqlHandler {
    private static final HikariConfig hikariConfig = new HikariConfig();
    private static HikariDataSource dataSource;
    private static final int SCHEMA_VERSION = 1;

    private final Logger logger = SimpleVanish.getInstance().getSLF4JLogger();

    private final String mysqlUpdateStatement = """
            INSERT INTO vanish_settings (
                player_uuid, is_vanished, vanish_persists, night_vision,
                break_blocks, open_containers, attack_entities, mobs_target,
                pickup_items, invulnerability, leave_silently, join_silently,
                vanish_notifications
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
                is_vanished = VALUES(is_vanished),
                vanish_persists = VALUES(vanish_persists),
                night_vision = VALUES(night_vision),
                break_blocks = VALUES(break_blocks),
                open_containers = VALUES(open_containers),
                attack_entities = VALUES(attack_entities),
                mobs_target = VALUES(mobs_target),
                pickup_items = VALUES(pickup_items),
                invulnerability = VALUES(invulnerability),
                leave_silently = VALUES(leave_silently),
                join_silently = VALUES(join_silently),
                vanish_notifications = VALUES(vanish_notifications);
            """;

    private final String selectionStatement = """
            SELECT is_vanished, vanish_persists, night_vision,
            break_blocks, open_containers, attack_entities,
            mobs_target, pickup_items, invulnerability,
            leave_silently, join_silently, vanish_notifications
            FROM vanish_settings WHERE player_uuid = ?
            """;

    private final String sqliteUpdateStatement = """
            INSERT INTO vanish_settings (
                player_uuid, is_vanished, vanish_persists, night_vision,
                break_blocks, open_containers, attack_entities, mobs_target,
                pickup_items, invulnerability, leave_silently, join_silently,
                vanish_notifications
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            ON CONFLICT(player_uuid) DO UPDATE SET
                is_vanished = excluded.is_vanished,
                vanish_persists = excluded.vanish_persists,
                night_vision = excluded.night_vision,
                break_blocks = excluded.break_blocks,
                open_containers = excluded.open_containers,
                attack_entities = excluded.attack_entities,
                mobs_target = excluded.mobs_target,
                pickup_items = excluded.pickup_items,
                invulnerability = excluded.invulnerability,
                leave_silently = excluded.leave_silently,
                join_silently = excluded.join_silently,
                vanish_notifications = excluded.vanish_notifications;
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
        debug("Setting up Hikari config");
        setupConfig();
        try {
            debug("Initializing tables");
            initializeTables();
            debug("Doing version checks");
            versionChecks();
        } catch (SQLException e) {
            logger.warn("Failed to connect to database. Error: {}", e.getMessage(), e);
        }
    }

    public void savePlayerSettings(UUID uuid, PlayerVanishSettings settings) {
        String updateStatement;
        if (ConfigHandler.getInstance().isMysqlEnabled()) {
            updateStatement = mysqlUpdateStatement;
        } else {
            updateStatement = sqliteUpdateStatement;
        }
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(updateStatement)) {
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
            logger.warn("Failed to save player settings to database. Error: {}", e.getMessage(), e);
        }

        Cache.updateSettingsCache(uuid, settings);
    }

    public void updateSettings(UUID uuid) {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(selectionStatement)) {

            statement.setString(1, uuid.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    PlayerVanishSettings settings = new PlayerVanishSettings(
                            resultSet.getBoolean("is_vanished"),
                            resultSet.getBoolean("vanish_persists"),
                            resultSet.getBoolean("night_vision"),
                            resultSet.getBoolean("break_blocks"),
                            resultSet.getBoolean("open_containers"),
                            resultSet.getBoolean("attack_entities"),
                            resultSet.getBoolean("mobs_target"),
                            resultSet.getBoolean("pickup_items"),
                            resultSet.getBoolean("invulnerability"),
                            resultSet.getBoolean("leave_silently"),
                            resultSet.getBoolean("join_silently"),
                            resultSet.getBoolean("vanish_notifications")
                    );
                    Cache.updateSettingsCache(uuid, settings);
                } else {
                    PlayerVanishSettings settings = new PlayerVanishSettings();
                    Cache.updateSettingsCache(uuid, settings);
                    savePlayerSettings(uuid, settings);
                }
            }
        } catch (SQLException e) {
            logger.warn("Failed to get vanish settings from database. Error: {}", e.getMessage(), e);
        }
    }

    private void initializeTables() throws SQLException {
        Connection connection = getConnection();
        PreparedStatement initializationStatement = connection.prepareStatement("""
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
                );""");
        initializationStatement.execute();
    }


    /* Yes I did just copy this from my other plugin, leave me be */

    private void versionChecks() throws SQLException {
        debug("Creating schema_version table...");
        Connection connection = getConnection();
        PreparedStatement versionStatement = connection.prepareStatement("""
                CREATE TABLE IF NOT EXISTS schema_version (
                    version INTEGER NOT NULL
                );
                """);
        versionStatement.execute();
        PreparedStatement checkVersion = connection.prepareStatement("SELECT COUNT(*) FROM schema_version;");
        ResultSet versionCheck = checkVersion.executeQuery();
        if (versionCheck.next() && versionCheck.getInt(1) == 0) {
            PreparedStatement insertVersion = connection.prepareStatement("INSERT INTO schema_version (version) VALUES (?);");
            insertVersion.setInt(1, SCHEMA_VERSION);
            insertVersion.executeUpdate();
            debug("Inserted initial schema version {}", SCHEMA_VERSION);
        } else {
            int currentVersion = getSchemaVersion();
            debug("Existing schema version found: {}", currentVersion);
            if (currentVersion < SCHEMA_VERSION) {
                runMigrations(connection, currentVersion);
            }
        }
    }

    public int getSchemaVersion() {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT version FROM schema_version LIMIT 1;");
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("version");
            }
        } catch (SQLException e) {
            logger.warn("Failed to get schema version", e);
        }
        return -1;
    }

    private void runMigrations(Connection connection, int fromVersion) {
        try {
            debug("Running migrations from schema version {}", fromVersion);
            PreparedStatement updateVersion = connection.prepareStatement("UPDATE schema_version SET version = ?;");
            updateVersion.setInt(1, SCHEMA_VERSION);
            updateVersion.executeUpdate();
            debug("Schema upgraded to version {}", SCHEMA_VERSION);
        } catch (SQLException e) {
            logger.error("Error running schema migrations", e);
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

    public void closeConnection() {
        if (dataSource != null && !dataSource.isClosed()) dataSource.close();
    }

    private void debug(String message, Object... args) {
        if (ConfigHandler.getInstance().isDebug()) {
            logger.info("[SQL DEBUG] {}, {}", message, args);
        }
    }
}
