package simplexity.simpleVanish;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;
import simplexity.simpleVanish.commands.Vanish;
import simplexity.simpleVanish.commands.VanishSettings;
import simplexity.simpleVanish.commands.settings.AttackEntities;
import simplexity.simpleVanish.commands.settings.BreakBlocks;
import simplexity.simpleVanish.commands.settings.Flight;
import simplexity.simpleVanish.commands.settings.Invulnerable;
import simplexity.simpleVanish.commands.settings.MobsTarget;
import simplexity.simpleVanish.commands.settings.NightVision;
import simplexity.simpleVanish.commands.settings.OpenContainers;
import simplexity.simpleVanish.commands.settings.Persist;
import simplexity.simpleVanish.commands.settings.PickUpItems;
import simplexity.simpleVanish.commands.settings.SilentJoin;
import simplexity.simpleVanish.commands.settings.SilentLeave;
import simplexity.simpleVanish.config.ConfigHandler;
import simplexity.simpleVanish.listeners.AttackListener;
import simplexity.simpleVanish.listeners.ItemPickupListener;
import simplexity.simpleVanish.listeners.PlayerJoinListener;
import simplexity.simpleVanish.listeners.PlayerLeaveListener;
import simplexity.simpleVanish.listeners.TargetListener;
import simplexity.simpleVanish.objects.VanishSettingsPermission;
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
        this.getCommand("vanish-settings").setExecutor(new VanishSettings());
    }

    private void registerSubCommands() {
        Cache.getSubCommands().clear();
        Cache.getSubCommands().add(new AttackEntities(new Permission(VanishSettingsPermission.ATTACK_ENTITIES), "attack-entities"));
        Cache.getSubCommands().add(new BreakBlocks(new Permission(VanishSettingsPermission.BREAK_BLOCKS), "break-blocks"));
        Cache.getSubCommands().add(new Flight(new Permission(VanishSettingsPermission.FLIGHT), "flight"));
        Cache.getSubCommands().add(new Invulnerable(new Permission(VanishSettingsPermission.INVULNERABLE), "invulnerable"));
        Cache.getSubCommands().add(new MobsTarget(new Permission(VanishSettingsPermission.MOBS_TARGET), "mobs-target"));
        Cache.getSubCommands().add(new NightVision(new Permission(VanishSettingsPermission.NIGHT_VISION), "night-vision"));
        Cache.getSubCommands().add(new OpenContainers(new Permission(VanishSettingsPermission.OPEN_CONTAINERS), "open-containers"));
        Cache.getSubCommands().add(new Persist(new Permission(VanishSettingsPermission.PERSIST), "persist"));
        Cache.getSubCommands().add(new PickUpItems(new Permission(VanishSettingsPermission.PICK_UP_ITEMS), "pick-up-items"));
        Cache.getSubCommands().add(new SilentJoin(new Permission(VanishSettingsPermission.SILENT_JOIN), "silent-join"));
        Cache.getSubCommands().add(new SilentLeave(new Permission(VanishSettingsPermission.SILENT_LEAVE), "silent-leave"));
    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new AttackListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerLeaveListener(), this);
        this.getServer().getPluginManager().registerEvents(new TargetListener(), this);
        this.getServer().getPluginManager().registerEvents(new ItemPickupListener(), this);
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
