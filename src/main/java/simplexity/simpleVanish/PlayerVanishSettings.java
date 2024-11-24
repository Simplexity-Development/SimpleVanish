package simplexity.simpleVanish;

public class PlayerVanishSettings {
    private boolean isVanished;
    private boolean shouldVanishPersist;
    private boolean isNightVisionEnabled;
    private boolean canBreakBlocks;
    private boolean canAttackPlayers;
    private boolean canAttackEntities;
    private boolean isInvulnerable;
    private boolean shouldAllowFlight;
    private NotificationLocation notificationLocation;

    public PlayerVanishSettings(boolean isVanished, boolean shouldVanishPersist, boolean isNightVisionEnabled,
                                boolean canBreakBlocks, boolean canAttackPlayers, boolean canAttackEntities,
                                boolean isInvulnerable, boolean shouldAllowFlight, NotificationLocation notificationLocation) {
        this.isVanished = isVanished;
        this.shouldVanishPersist = shouldVanishPersist;
        this.isNightVisionEnabled = isNightVisionEnabled;
        this.canBreakBlocks = canBreakBlocks;
        this.canAttackPlayers = canAttackPlayers;
        this.canAttackEntities = canAttackEntities;
        this.isInvulnerable = isInvulnerable;
        this.shouldAllowFlight = shouldAllowFlight;
        this.notificationLocation = notificationLocation;
    }

    public boolean isVanished() {
        return isVanished;
    }

    public boolean shouldVanishPersist() {
        return shouldVanishPersist;
    }

    public boolean isNightVisionEnabled() {
        return isNightVisionEnabled;
    }

    public boolean canBreakBlocks() {
        return canBreakBlocks;
    }

    public boolean canAttackPlayers() {
        return canAttackPlayers;
    }

    public boolean canAttackEntities() {
        return canAttackEntities;
    }

    public boolean isInvulnerable() {
        return isInvulnerable;
    }

    public boolean shouldAllowFlight() {
        return shouldAllowFlight;
    }
    public NotificationLocation getNotificationLocation() {
        return notificationLocation;
    }

    public void setVanished(boolean isVanished) {
        this.isVanished = isVanished;
    }

    public void setShouldVanishPersist(boolean shouldVanishPersist) {
        this.shouldVanishPersist = shouldVanishPersist;
    }

    public void setNightVisionEnabled(boolean isNightVisionEnabled) {
        this.isNightVisionEnabled = isNightVisionEnabled;
    }

    public void setCanBreakBlocks(boolean canBreakBlocks) {
        this.canBreakBlocks = canBreakBlocks;
    }

    public void setCanAttackPlayers(boolean canAttackPlayers) {
        this.canAttackPlayers = canAttackPlayers;
    }

    public void setCanAttackEntities(boolean canAttackEntities) {
        this.canAttackEntities = canAttackEntities;
    }

    public void setInvulnerable(boolean isInvulnerable) {
        this.isInvulnerable = isInvulnerable;
    }

    public void setShouldAllowFlight(boolean shouldAllowFlight) {
        this.shouldAllowFlight = shouldAllowFlight;
    }

    public void setNotificationLocation(NotificationLocation notificationLocation) {
        this.notificationLocation = notificationLocation;
    }

    public String toString() {
        return "PlayerVanishSettings [isVanished=" + isVanished
                + ", shouldVanishPersist=" + shouldVanishPersist
                + ", isNightVisionEnabled=" + isNightVisionEnabled
                + ", canBreakBlocks=" + canBreakBlocks
                + ", canAttackPlayers=" + canAttackPlayers
                + ", canAttackEntities=" + canAttackEntities
                + ", isInvulnerable=" + isInvulnerable
                + ", shouldAllowFlight=" + shouldAllowFlight + "]";
    }




}
