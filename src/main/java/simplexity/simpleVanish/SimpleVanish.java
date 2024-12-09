package simplexity.simpleVanish;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;
import simplexity.simpleVanish.commands.SubCommand;
import simplexity.simpleVanish.commands.Vanish;
import simplexity.simpleVanish.commands.VanishSettings;
import simplexity.simpleVanish.commands.settings.Flight;
import simplexity.simpleVanish.commands.settings.Invulnerable;
import simplexity.simpleVanish.commands.settings.MobsTarget;
import simplexity.simpleVanish.commands.settings.NightVision;
import simplexity.simpleVanish.commands.settings.OpenContainers;
import simplexity.simpleVanish.commands.settings.Persist;
import simplexity.simpleVanish.commands.settings.AttackEntities;
import simplexity.simpleVanish.commands.settings.BreakBlocks;
import simplexity.simpleVanish.commands.settings.PickUpItems;
import simplexity.simpleVanish.commands.settings.SilentJoin;
import simplexity.simpleVanish.commands.settings.SilentLeave;
import simplexity.simpleVanish.config.ConfigHandler;
import simplexity.simpleVanish.listeners.PlayerJoinListener;
import simplexity.simpleVanish.listeners.PlayerLeaveListener;
import simplexity.simpleVanish.listeners.TargetListener;
import simplexity.simpleVanish.objects.VanishSettingsPermission;
import simplexity.simpleVanish.saving.SqlHandler;

import java.util.HashSet;

public final class SimpleVanish extends JavaPlugin {

    private static SimpleVanish instance;
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();
    private static final HashSet<Player> playersToHideFrom = new HashSet<>();
    private static final HashSet<Player> vanishedPlayers = new HashSet<>();
    private static final HashSet<SubCommand> subCommands = new HashSet<>();

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        ConfigHandler.getInstance().loadConfigValues();
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerLeaveListener(), this);
        this.getServer().getPluginManager().registerEvents(new TargetListener(), this);
        this.getCommand("vanish").setExecutor(new Vanish());
        this.getCommand("vanish-settings").setExecutor(new VanishSettings());
        SqlHandler.getInstance().init();
        registerSubCommands();
        // Plugin startup logic

    }

    private void registerSubCommands(){
        subCommands.clear();
        subCommands.add(new AttackEntities(new Permission(VanishSettingsPermission.ATTACK_ENTITIES), "attack-entities"));
        subCommands.add(new BreakBlocks(new Permission(VanishSettingsPermission.BREAK_BLOCKS), "break-blocks"));
        subCommands.add(new Flight(new Permission(VanishSettingsPermission.FLIGHT), "flight"));
        subCommands.add(new Invulnerable(new Permission(VanishSettingsPermission.INVULNERABLE), "invulnerable"));
        subCommands.add(new MobsTarget(new Permission(VanishSettingsPermission.MOBS_TARGET), "mobs-target"));
        subCommands.add(new NightVision(new Permission(VanishSettingsPermission.NIGHT_VISION), "night-vision"));
        subCommands.add(new OpenContainers(new Permission(VanishSettingsPermission.OPEN_CONTAINERS), "open-containers"));
        subCommands.add(new Persist(new Permission(VanishSettingsPermission.PERSIST), "persist"));
        subCommands.add(new PickUpItems(new Permission(VanishSettingsPermission.PICK_UP_ITEMS), "pick-up-items"));
        subCommands.add(new SilentJoin(new Permission(VanishSettingsPermission.SILENT_JOIN), "silent-join"));
        subCommands.add(new SilentLeave(new Permission(VanishSettingsPermission.SILENT_LEAVE), "silent-leave"));
    }

    public static SimpleVanish getInstance() {
        return instance;
    }

    public static MiniMessage getMiniMessage() {
        return miniMessage;
    }

    public static HashSet<Player> getPlayersToHideFrom() {
        return playersToHideFrom;
    }

    public static HashSet<Player> getVanishedPlayers() {
        return vanishedPlayers;
    }

    public static HashSet<SubCommand> getSubCommands() {
        return subCommands;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
