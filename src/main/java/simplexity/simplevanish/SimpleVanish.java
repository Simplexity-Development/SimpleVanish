package simplexity.simplevanish;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.java.JavaPlugin;
import simplexity.simplevanish.commands.FakeCommand;
import simplexity.simplevanish.commands.Vanish;
import simplexity.simplevanish.commands.VanishReload;
import simplexity.simplevanish.commands.VanishSettings;
import simplexity.simplevanish.commands.settings.AttackEntities;
import simplexity.simplevanish.commands.settings.BreakBlocks;
import simplexity.simplevanish.commands.settings.Invulnerability;
import simplexity.simplevanish.commands.settings.JoinSilently;
import simplexity.simplevanish.commands.settings.LeaveSilently;
import simplexity.simplevanish.commands.settings.MobsTarget;
import simplexity.simplevanish.commands.settings.NightVision;
import simplexity.simplevanish.commands.settings.OpenContainer;
import simplexity.simplevanish.commands.settings.Persist;
import simplexity.simplevanish.commands.settings.PickupItems;
import simplexity.simplevanish.commands.settings.VanishNotifications;
import simplexity.simplevanish.config.ConfigHandler;
import simplexity.simplevanish.config.Message;
import simplexity.simplevanish.hooks.VanishPlaceholders;
import simplexity.simplevanish.listeners.AttackListener;
import simplexity.simplevanish.listeners.BlockBreakListener;
import simplexity.simplevanish.listeners.BlockInteractListener;
import simplexity.simplevanish.listeners.ItemPickupListener;
import simplexity.simplevanish.listeners.PingServerListener;
import simplexity.simplevanish.listeners.PlayerJoinListener;
import simplexity.simplevanish.listeners.PlayerLeaveListener;
import simplexity.simplevanish.listeners.PreCommandListener;
import simplexity.simplevanish.listeners.TargetListener;
import simplexity.simplevanish.listeners.TrialSpawnListener;
import simplexity.simplevanish.listeners.VaultStateListener;
import simplexity.simplevanish.objects.VanishPermission;
import simplexity.simplevanish.saving.Cache;
import simplexity.simplevanish.saving.SqlHandler;

public final class SimpleVanish extends JavaPlugin {

    private static SimpleVanish instance;
    private static boolean papiEnabled = false;
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();


    public static boolean isPapiEnabled() {
        return papiEnabled;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        ConfigHandler.getInstance().loadConfigValues();
        SqlHandler.getInstance().init();
        registerCommands();
        registerSubCommands();
        registerListeners();
        checkForPapi();
    }

    private void registerCommands() {
        this.getCommand("vanish").setExecutor(new Vanish());
        this.getCommand("vsettings").setExecutor(new VanishSettings());
        this.getCommand("vanish-reload").setExecutor(new VanishReload());
        this.getCommand("fake").setExecutor(new FakeCommand());
    }

    private void registerSubCommands() {
        Cache.getSubCommands().clear();
        Cache.getSubCommands().add(new AttackEntities(VanishPermission.ATTACK_ENTITIES, "attack-entities",
                Message.SETTING_INSERT_ATTACK_ENTITIES.getMessage()));
        Cache.getSubCommands().add(new BreakBlocks(VanishPermission.BREAK_BLOCKS, "break-blocks",
                Message.SETTING_INSERT_BREAK_BLOCKS.getMessage()));
        Cache.getSubCommands().add(new Invulnerability(VanishPermission.INVULNERABLE, "invulnerability",
                Message.SETTING_INSERT_INVULNERABLE.getMessage()));
        Cache.getSubCommands().add(new JoinSilently(VanishPermission.SILENT_JOIN, "silent-join",
                Message.SETTING_INSERT_SILENT_JOIN.getMessage()));
        Cache.getSubCommands().add(new LeaveSilently(VanishPermission.SILENT_LEAVE, "silent-leave",
                Message.SETTING_INSERT_SILENT_LEAVE.getMessage()));
        Cache.getSubCommands().add(new MobsTarget(VanishPermission.MOBS_TARGET, "mobs-target",
                Message.SETTING_INSERT_MOBS_TARGET.getMessage()));
        Cache.getSubCommands().add(new NightVision(VanishPermission.NIGHT_VISION, "night-vision",
                Message.SETTING_INSERT_NIGHT_VISION.getMessage()));
        Cache.getSubCommands().add(new OpenContainer(VanishPermission.OPEN_CONTAINERS, "open-container",
                Message.SETTING_INSERT_OPEN_CONTAINERS.getMessage()));
        Cache.getSubCommands().add(new Persist(VanishPermission.PERSIST, "vanish-persist",
                Message.SETTING_INSERT_PERSIST.getMessage()));
        Cache.getSubCommands().add(new PickupItems(VanishPermission.PICK_UP_ITEMS, "pick-up-items",
                Message.SETTING_INSERT_PICK_UP_ITEMS.getMessage()));
        Cache.getSubCommands().add(new VanishNotifications(VanishPermission.VIEW_MESSAGES, "notifications",
                Message.SETTING_INSERT_VANISH_NOTIFICATIONS.getMessage()));
    }


    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new AttackListener(), this);
        this.getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        this.getServer().getPluginManager().registerEvents(new BlockInteractListener(), this);
        this.getServer().getPluginManager().registerEvents(new ItemPickupListener(), this);
        this.getServer().getPluginManager().registerEvents(new PingServerListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerLeaveListener(), this);
        this.getServer().getPluginManager().registerEvents(new TargetListener(), this);
        this.getServer().getPluginManager().registerEvents(new PreCommandListener(), this);
        this.getServer().getPluginManager().registerEvents(new TrialSpawnListener(), this);
        this.getServer().getPluginManager().registerEvents(new VaultStateListener(), this);
    }

    private void checkForPapi() {
        if (this.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            papiEnabled = true;
            new VanishPlaceholders().register();
        } else {
            this.getLogger().info("You do not have PlaceholderAPI loaded on your server. Any PlaceholderAPI placeholders used in this plugin's messages, will not work.");
        }
    }

    public static SimpleVanish getInstance() {
        return instance;
    }

    public static MiniMessage getMiniMessage() {
        return miniMessage;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
