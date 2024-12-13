package simplexity.simpleVanish;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.java.JavaPlugin;
import simplexity.simpleVanish.commands.Vanish;
import simplexity.simpleVanish.commands.VanishSettings;
import simplexity.simpleVanish.commands.settings.AttackEntities;
import simplexity.simpleVanish.commands.settings.BreakBlocks;
import simplexity.simpleVanish.commands.settings.Invulnerability;
import simplexity.simpleVanish.commands.settings.JoinSilently;
import simplexity.simpleVanish.commands.settings.LeaveSilently;
import simplexity.simpleVanish.commands.settings.MobsTarget;
import simplexity.simpleVanish.commands.settings.NightVision;
import simplexity.simpleVanish.commands.settings.OpenAnimation;
import simplexity.simpleVanish.commands.settings.Persist;
import simplexity.simpleVanish.commands.settings.PickupItems;
import simplexity.simpleVanish.commands.settings.VanishNotifications;
import simplexity.simpleVanish.config.ConfigHandler;
import simplexity.simpleVanish.config.LocaleHandler;
import simplexity.simpleVanish.listeners.AttackListener;
import simplexity.simpleVanish.listeners.BlockBreakListener;
import simplexity.simpleVanish.listeners.ContainerOpenListener;
import simplexity.simpleVanish.listeners.ItemPickupListener;
import simplexity.simpleVanish.listeners.PingServerListener;
import simplexity.simpleVanish.listeners.PlayerJoinListener;
import simplexity.simpleVanish.listeners.PlayerLeaveListener;
import simplexity.simpleVanish.listeners.TargetListener;
import simplexity.simpleVanish.objects.VanishPermission;
import simplexity.simpleVanish.saving.Cache;
import simplexity.simpleVanish.saving.SqlHandler;

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
        // Plugin startup logic

    }

    private void registerCommands() {
        this.getCommand("vanish").setExecutor(new Vanish());
        this.getCommand("vsettings").setExecutor(new VanishSettings());
    }

    private void registerSubCommands() {
        Cache.getSubCommands().clear();
        Cache.getSubCommands().add(new AttackEntities(VanishPermission.ATTACK_ENTITIES, "attack-entities",
                LocaleHandler.Message.SETTING_INSERT_ATTACK_ENTITIES.getMessage()));
        Cache.getSubCommands().add(new BreakBlocks(VanishPermission.BREAK_BLOCKS, "break-blocks",
                LocaleHandler.Message.SETTING_INSERT_BREAK_BLOCKS.getMessage()));
        Cache.getSubCommands().add(new Invulnerability(VanishPermission.INVULNERABLE, "invulnerability",
                LocaleHandler.Message.SETTING_INSERT_INVULNERABLE.getMessage()));
        Cache.getSubCommands().add(new JoinSilently(VanishPermission.SILENT_JOIN, "silent-join",
                LocaleHandler.Message.SETTING_INSERT_SILENT_JOIN.getMessage()));
        Cache.getSubCommands().add(new LeaveSilently(VanishPermission.SILENT_LEAVE, "silent-leave",
                LocaleHandler.Message.SETTING_INSERT_SILENT_LEAVE.getMessage()));
        Cache.getSubCommands().add(new MobsTarget(VanishPermission.MOBS_TARGET, "mobs-target",
                LocaleHandler.Message.SETTING_INSERT_MOBS_TARGET.getMessage()));
        Cache.getSubCommands().add(new NightVision(VanishPermission.NIGHT_VISION, "night-vision",
                LocaleHandler.Message.SETTING_INSERT_NIGHT_VISION.getMessage()));
        Cache.getSubCommands().add(new OpenAnimation(VanishPermission.OPEN_CONTAINERS, "container-animation",
                LocaleHandler.Message.SETTING_INSERT_OPEN_CONTAINERS.getMessage()));
        Cache.getSubCommands().add(new Persist(VanishPermission.PERSIST, "vanish-persist",
                LocaleHandler.Message.SETTING_INSERT_PERSIST.getMessage()));
        Cache.getSubCommands().add(new PickupItems(VanishPermission.PICK_UP_ITEMS, "pick-up-items",
                LocaleHandler.Message.SETTING_INSERT_PICK_UP_ITEMS.getMessage()));
        Cache.getSubCommands().add(new VanishNotifications(VanishPermission.VIEW_MESSAGES, "notifications",
                LocaleHandler.Message.SETTING_INSERT_VANISH_NOTIFICATIONS.getMessage()));
    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new AttackListener(), this);
        this.getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        this.getServer().getPluginManager().registerEvents(new ContainerOpenListener(),  this);
        this.getServer().getPluginManager().registerEvents(new ItemPickupListener(), this);
        this.getServer().getPluginManager().registerEvents(new PingServerListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerLeaveListener(), this);
        this.getServer().getPluginManager().registerEvents(new TargetListener(), this);
    }

    private void checkForPapi() {
        if (this.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            papiEnabled = true;
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
