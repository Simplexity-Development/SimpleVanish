package simplexity.simplevanish.saving;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import simplexity.simplevanish.SimpleVanish;
import simplexity.simplevanish.config.ConfigHandler;
import simplexity.simplevanish.handling.MessageHandler;
import simplexity.simplevanish.objects.PlayerVanishSettings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;


/**
 * Handles all SQL operations for SimpleVanish.
 * <p>
 * This class is responsible for:
 * <ul>
 *   <li>Initializing database connections using HikariCP.</li>
 *   <li>Creating and maintaining the vanish_settings schema.</li>
 *   <li>Saving and loading {@link PlayerVanishSettings} from the database.</li>
 *   <li>Handling schema versioning and migrations.</li>
 * </ul>
 * <p>
 * Supports both MySQL and SQLite backends.
 */
public class SqlHandler {
    private static final HikariConfig hikariConfig = new HikariConfig();
    private static HikariDataSource dataSource;
    private static final int SCHEMA_VERSION = 1;

    private final Logger logger = SimpleVanish.getInstance().getSLF4JLogger();

    @SuppressWarnings("FieldCanBeLocal")
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

    @SuppressWarnings("FieldCanBeLocal")
    private final String selectionStatement = """
            SELECT is_vanished, vanish_persists, night_vision,
            break_blocks, open_containers, attack_entities,
            mobs_target, pickup_items, invulnerability,
            leave_silently, join_silently, vanish_notifications
            FROM vanish_settings WHERE player_uuid = ?
            """;

    @SuppressWarnings("FieldCanBeLocal")
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


    /**
     * Initializes the SQL handler by setting up HikariCP,
     * creating required tables, and running schema version checks.
     */
    public void init() {
        debug("Initializing SQL Handler...");
        setupConfig();
        try {
            debug("Creating vanish_settings table if missing.");
            initializeTables();
            debug("Verifying schema version and applying migrations if needed.");
            versionChecks();
        } catch (SQLException e) {
            logger.warn("Failed to initialize database. Error: {}", e.getMessage(), e);
        }
    }

    /**
     * Saves vanish settings for a player into the database.
     *
     * @param uuid     the UUID of the player
     * @param settings the {@link PlayerVanishSettings} object to persist
     */

    public void savePlayerSettings(@NotNull UUID uuid, @NotNull PlayerVanishSettings settings) {
        String updateStatement;
        debug("Saving vanish settings for player %s", uuid);
        if (ConfigHandler.getInstance().isMysqlEnabled()) {
            updateStatement = mysqlUpdateStatement;
        } else {
            updateStatement = sqliteUpdateStatement;
        }
        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(updateStatement)) {
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
        debug("Loading vanish settings for player %s", uuid);
        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(selectionStatement)) {

            statement.setString(1, uuid.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    PlayerVanishSettings settings = new PlayerVanishSettings(resultSet.getBoolean("is_vanished"), resultSet.getBoolean("vanish_persists"), resultSet.getBoolean("night_vision"), resultSet.getBoolean("break_blocks"), resultSet.getBoolean("open_containers"), resultSet.getBoolean("attack_entities"), resultSet.getBoolean("mobs_target"), resultSet.getBoolean("pickup_items"), resultSet.getBoolean("invulnerability"), resultSet.getBoolean("leave_silently"), resultSet.getBoolean("join_silently"), resultSet.getBoolean("vanish_notifications"));
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

    /**
     * Ensures the vanish_settings table exists in the database.
     *
     * @throws SQLException if table creation fails
     */
    public void initializeTables() throws SQLException {
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

    /**
     * Creates or verifies the schema_version table,
     * and applies migrations if the current schema is outdated.
     *
     * @throws SQLException if database operations fail
     */
    public void versionChecks() throws SQLException {
        debug("Ensuring schema_version table exists...");
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
            debug("Inserted initial schema version %d", SCHEMA_VERSION);
        } else {
            int currentVersion = getSchemaVersion();
            debug("Current schema version is: %d", currentVersion);
            if (currentVersion < SCHEMA_VERSION) {
                runMigrations(connection, currentVersion);
            }
        }
    }


    /**
     * Retrieves the current database schema version.
     *
     * <p>The schema version is stored in the {@code schema_version} table and is used
     * to track whether migrations need to be run. If the version cannot be retrieved,
     * this method returns -1.</p>
     *
     * @return the current schema version, or -1 if unavailable
     */

    public int getSchemaVersion() {
        debug("Checking schema version...");
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT version FROM schema_version LIMIT 1;");
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int version = resultSet.getInt("version");
                debug("Retrieved schema version: %d", version);
                return version;
            } else {
                debug("Schema version table exists but no version found, returning -1");
                return -1;
            }
        } catch (SQLException e) {
            logger.warn("Failed to get schema version", e);
        }
        return -1;
    }

    /**
     * Runs schema migrations from the given version to the latest schema version.
     *
     * @param connection  active database connection
     * @param fromVersion the current schema version
     */

    public void runMigrations(Connection connection, int fromVersion) {
        try {
            debug("Running migrations from schema version %d to %d", fromVersion, SCHEMA_VERSION);
            PreparedStatement updateVersion = connection.prepareStatement("UPDATE schema_version SET version = ?;");
            updateVersion.setInt(1, SCHEMA_VERSION);
            updateVersion.executeUpdate();
            debug("Schema upgraded to version %d", SCHEMA_VERSION);
        } catch (SQLException e) {
            logger.error("Error running schema migrations", e);
        }
    }

    /**
     * Gets the active connection to the database
     *
     * @return Connection
     * @throws SQLException if there's no connection or another issue
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }


    /**
     * Sets up the config with Hikari based on configured values. Controls MySQL vs Sqlite
     */
    public void setupConfig() {
        if (ConfigHandler.getInstance().isMysqlEnabled()) {
            debug("Setting Hikari Config Settings...");
            debug("Using MySQL");
            hikariConfig.setJdbcUrl("jdbc:mysql://" + ConfigHandler.getInstance().getMysqlIP() + "/" + ConfigHandler.getInstance().getDatabaseName());
            hikariConfig.setUsername(ConfigHandler.getInstance().getDatabaseUsername());
            hikariConfig.setPassword(ConfigHandler.getInstance().getDatabasePassword());
            dataSource = new HikariDataSource(hikariConfig);
            return;
        }
        debug("Setting Hikari Config Settings...");
        debug("Using SQLite");
        hikariConfig.setJdbcUrl("jdbc:sqlite:" + SimpleVanish.getInstance().getDataFolder() + "/vanish-settings.db");
        hikariConfig.setConnectionTestQuery("PRAGMA journal_mode = WAL;");
        dataSource = new HikariDataSource(hikariConfig);
    }

    /**
     * Closes the active connection to the database
     */
    public void closeConnection() {
        debug("Closing connection to database");
        if (dataSource != null && !dataSource.isClosed()) dataSource.close();
    }

    private void debug(String message, Object... args) {
        MessageHandler.debug("[SQL Handler] ", message, args);
    }
}
