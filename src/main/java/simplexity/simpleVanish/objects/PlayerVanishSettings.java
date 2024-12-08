package simplexity.simpleVanish.objects;

public class PlayerVanishSettings {
    private boolean isVanished;
    private boolean shouldVanishPersist;
    private boolean isNightVisionEnabled;
    private boolean canBreakBlocks;
    private boolean canOpenContainers;
    private boolean canAttackEntities;
    private boolean shouldMobsTarget;
    private boolean canPickupItems;
    private boolean isInvulnerable;
    private boolean shouldAllowFlight;
    private NotificationLocation notificationLocation;

    public PlayerVanishSettings(boolean isVanished, boolean shouldVanishPersist, boolean isNightVisionEnabled,
                                boolean canBreakBlocks, boolean canOpenContainers, boolean canAttackEntities,
                                boolean shouldMobsTarget, boolean canPickupItems, boolean isInvulnerable, boolean shouldAllowFlight,
                                NotificationLocation notificationLocation) {
        this.isVanished = isVanished;
        this.shouldVanishPersist = shouldVanishPersist;
        this.isNightVisionEnabled = isNightVisionEnabled;
        this.canBreakBlocks = canBreakBlocks;
        this.canOpenContainers = canOpenContainers;
        this.canAttackEntities = canAttackEntities;
        this.shouldMobsTarget = shouldMobsTarget;
        this.canPickupItems = canPickupItems;
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

    public boolean canOpenContainers() {
        return canOpenContainers;
    }

    public boolean canAttackEntities() {
        return canAttackEntities;
    }

    public boolean shouldMobTarget() {
        return shouldMobsTarget;
    }

    public boolean canPickupItems() {
        return canPickupItems;
    }

    public boolean isInvulnerable() {
        return isInvulnerable;
    }

    public boolean canFly() {
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

    public void setCanOpenContainers(boolean canOpenContainers) {
        this.canOpenContainers = canOpenContainers;
    }

    public void setCanAttackEntities(boolean canAttackEntities) {
        this.canAttackEntities = canAttackEntities;
    }

    public void setShouldMobsTarget(boolean shouldMobsTarget){
        this.shouldMobsTarget = shouldMobsTarget;
    }

    public void setCanPickupItems(boolean canPickupItems){
        this.canPickupItems = canPickupItems;
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
                + ", canAttackPlayers=" + canOpenContainers
                + ", canAttackEntities=" + canAttackEntities
                + ", preventMobTargeting=" + shouldMobsTarget
                + ", canPickupItems=" + canPickupItems
                + ", isInvulnerable=" + isInvulnerable
                + ", shouldAllowFlight=" + shouldAllowFlight
                + ", notificationLocation=" + notificationLocation
                + "]";
    }

    public int toBitmask() {
        int bitmask = 0;
        bitmask |= (isVanished ? 1 : 0);
        bitmask |= (shouldVanishPersist ? 1 : 0) << 1;
        bitmask |= (isNightVisionEnabled ? 1 : 0) << 2;
        bitmask |= (canBreakBlocks ? 1 : 0) << 3;
        bitmask |= (canOpenContainers ? 1 : 0) << 4;
        bitmask |= (canAttackEntities ? 1 : 0) << 5;
        bitmask |= (shouldMobsTarget ? 1 : 0) << 6;
        bitmask |= (canPickupItems ? 1 : 0) << 7;
        bitmask |= (isInvulnerable ? 1 : 0) << 8;
        bitmask |= (shouldAllowFlight ? 1 : 0) << 9;
        return bitmask;
    }
}
