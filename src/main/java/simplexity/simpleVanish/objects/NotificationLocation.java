package simplexity.simpleVanish.objects;

public enum NotificationLocation {
    ACTION_BAR("action_bar"),
    BOSS_BAR("boss_bar");

    private final String name;

    NotificationLocation(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static NotificationLocation fromString(String string) {
        for (NotificationLocation location : NotificationLocation.values()) {
            if (location.getName().equalsIgnoreCase(string)) {
                return location;
            }
        }
        throw new IllegalArgumentException(string + " is not a valid notification location");
    }
}
