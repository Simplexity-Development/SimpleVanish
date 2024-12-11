package simplexity.simpleVanish.objects;

public enum ReminderLocation {
    ACTION_BAR("action_bar"),
    BOSS_BAR("boss_bar");

    private final String name;

    ReminderLocation(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ReminderLocation fromString(String string) {
        for (ReminderLocation location : ReminderLocation.values()) {
            if (location.getName().equalsIgnoreCase(string)) {
                return location;
            }
        }
        throw new IllegalArgumentException(string + " is not a valid notification location");
    }
}
