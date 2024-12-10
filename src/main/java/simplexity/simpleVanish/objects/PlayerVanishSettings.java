package simplexity.simpleVanish.objects;

public class PlayerVanishSettings {
    private boolean isVanished;
    private boolean shouldVanishPersist;
    private boolean shouldGetNightvision;
    private boolean canBreakBlocks;
    private boolean canOpenContainers;
    private boolean canAttackEntities;
    private boolean shouldMobsTarget;
    private boolean canPickupItems;
    private boolean canBeInvulnerable;
    private boolean canFly;
    private boolean shouldJoinSilently;
    private boolean shouldLeaveSilently;
    private NotificationLocation notificationLocation;

    public PlayerVanishSettings(boolean isVanished, boolean shouldVanishPersist, boolean shouldGetNightvision,
                                boolean canBreakBlocks, boolean canOpenContainers, boolean canAttackEntities,
                                boolean shouldMobsTarget, boolean canPickupItems, boolean canBeInvulnerable,
                                boolean canFly, boolean shouldJoinSilently, boolean shouldLeaveSilently,
                                NotificationLocation notificationLocation) {
        this.isVanished = isVanished;
        this.shouldVanishPersist = shouldVanishPersist;
        this.shouldGetNightvision = shouldGetNightvision;
        this.canBreakBlocks = canBreakBlocks;
        this.canOpenContainers = canOpenContainers;
        this.canAttackEntities = canAttackEntities;
        this.shouldMobsTarget = shouldMobsTarget;
        this.canPickupItems = canPickupItems;
        this.canBeInvulnerable = canBeInvulnerable;
        this.canFly = canFly;
        this.shouldJoinSilently = shouldJoinSilently;
        this.shouldLeaveSilently = shouldLeaveSilently;
        this.notificationLocation = notificationLocation;
    }

    public boolean isVanished() {
        return isVanished;
    }

    public boolean shouldVanishPersist() {
        return shouldVanishPersist;
    }

    public boolean shouldGetNightVision() {
        return shouldGetNightvision;
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

    public boolean canBeInvulnerable() {
        return canBeInvulnerable;
    }

    public boolean canFly() {
        return canFly;
    }

    public boolean shouldJoinSilently() {
        return shouldJoinSilently;
    }

    public boolean shouldLeaveSilently() {
        return shouldLeaveSilently;
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

    public void setShouldGetNightvision(boolean isNightVisionEnabled) {
        this.shouldGetNightvision = isNightVisionEnabled;
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

    public void setShouldMobsTarget(boolean shouldMobsTarget) {
        this.shouldMobsTarget = shouldMobsTarget;
    }

    public void setCanPickupItems(boolean canPickupItems) {
        this.canPickupItems = canPickupItems;
    }

    public void setCanBeInvulnerable(boolean isInvulnerable) {
        this.canBeInvulnerable = isInvulnerable;
    }

    public void setCanFly(boolean canFly) {
        this.canFly = canFly;
    }

    public void setShouldJoinSilently(boolean shouldJoinSilently) {
        this.shouldJoinSilently = shouldJoinSilently;
    }

    public void setShouldLeaveSilently(boolean shouldLeaveSilently) {
        this.shouldLeaveSilently = shouldLeaveSilently;
    }

    public void setNotificationLocation(NotificationLocation notificationLocation) {
        this.notificationLocation = notificationLocation;
    }

    public String toString() {
        return "PlayerVanishSettings [isVanished=" + isVanished
                + ", shouldVanishPersist=" + shouldVanishPersist
                + ", isNightVisionEnabled=" + shouldGetNightvision
                + ", canBreakBlocks=" + canBreakBlocks
                + ", canAttackPlayers=" + canOpenContainers
                + ", canAttackEntities=" + canAttackEntities
                + ", preventMobTargeting=" + shouldMobsTarget
                + ", canPickupItems=" + canPickupItems
                + ", isInvulnerable=" + canBeInvulnerable
                + ", shouldAllowFlight=" + canFly
                + ", shouldJoinSilently=" + shouldJoinSilently
                + ", shouldLeaveSilently=" + shouldLeaveSilently
                + ", notificationLocation=" + notificationLocation
                + "]";
    }

    public int toBitmask() {
        int bitmask = 0;
        bitmask |= (isVanished ? 1 : 0);
        bitmask |= (shouldVanishPersist ? 1 : 0) << 1;
        bitmask |= (shouldGetNightvision ? 1 : 0) << 2;
        bitmask |= (canBreakBlocks ? 1 : 0) << 3;
        bitmask |= (canOpenContainers ? 1 : 0) << 4;
        bitmask |= (canAttackEntities ? 1 : 0) << 5;
        bitmask |= (shouldMobsTarget ? 1 : 0) << 6;
        bitmask |= (canPickupItems ? 1 : 0) << 7;
        bitmask |= (canBeInvulnerable ? 1 : 0) << 8;
        bitmask |= (canFly ? 1 : 0) << 9;
        bitmask |= (shouldJoinSilently ? 1 : 0) << 10;
        bitmask |= (shouldLeaveSilently ? 1 : 0) << 11;
        return bitmask;
    }


}
