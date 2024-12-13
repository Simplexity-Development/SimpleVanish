package simplexity.simpleVanish.objects;

import org.bukkit.permissions.Permission;

public class VanishPermission {
    public static final Permission ATTACK_ENTITIES = new Permission("vanish.settings.interaction.attack-entities");
    public static final Permission BREAK_BLOCKS = new Permission("vanish.settings.interaction.break-blocks");
    public static final Permission INVULNERABLE = new Permission("vanish.settings.admin.invulnerable");
    public static final Permission MOBS_TARGET = new Permission("vanish.settings.interaction.mobs-target");
    public static final Permission NIGHT_VISION = new Permission("vanish.settings.core.night-vision");
    public static final Permission OPEN_CONTAINERS = new Permission("vanish.settings.interaction.open-containers");
    public static final Permission PERSIST = new Permission("vanish.settings.core.persist");
    public static final Permission PICK_UP_ITEMS = new Permission("vanish.settings.interaction.pick-up-items");
    public static final Permission SILENT_JOIN = new Permission("vanish.settings.core.silent-join");
    public static final Permission SILENT_LEAVE = new Permission("vanish.settings.core.silent-leave");
    public static final Permission VIEW_MESSAGES = new Permission("vanish.view.messages");
    public static final Permission VIEW_TABLIST = new Permission("vanish.view.tablist");
    public static final Permission VIEW_VANISHED = new Permission("vanish.view.vanished");
    public static final Permission VANISH_COMMAND = new Permission("vanish.command");
}
