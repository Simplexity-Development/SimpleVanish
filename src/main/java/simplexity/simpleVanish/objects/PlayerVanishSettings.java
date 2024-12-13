package simplexity.simpleVanish.objects;

public class PlayerVanishSettings {
    private boolean isVanished;
    private boolean vanishPersist;
    private boolean nightVision;
    private boolean breakBlocks;
    private boolean containerOpen;
    private boolean attackEntities;
    private boolean mobsTarget;
    private boolean pickupItems;
    private boolean invulnerability;
    private boolean leaveSilently;
    private boolean joinSilently;
    private boolean vanishNotifications;
    private ReminderLocation reminderLocation;

    public PlayerVanishSettings(boolean isVanished, boolean vanishPersist, boolean nightVision, boolean breakBlocks,
                                boolean containerOpen, boolean attackEntities, boolean mobsTarget,
                                boolean pickupItems, boolean invulnerability, boolean leaveSilently, boolean joinSilently,
                                boolean vanishNotifications, ReminderLocation reminderLocation) {
        this.isVanished = isVanished;
        this.vanishPersist = vanishPersist;
        this.nightVision = nightVision;
        this.breakBlocks = breakBlocks;
        this.containerOpen = containerOpen;
        this.attackEntities = attackEntities;
        this.mobsTarget = mobsTarget;
        this.pickupItems = pickupItems;
        this.invulnerability = invulnerability;
        this.leaveSilently = leaveSilently;
        this.joinSilently = joinSilently;
        this.vanishNotifications = vanishNotifications;
        this.reminderLocation = reminderLocation;
    }

    public PlayerVanishSettings() {
        this.isVanished = false;
        this.vanishPersist = false;
        this.nightVision = false;
        this.breakBlocks = false;
        this.containerOpen = false;
        this.attackEntities = false;
        this.mobsTarget = false;
        this.pickupItems = false;
        this.invulnerability = false;
        this.leaveSilently = false;
        this.joinSilently = false;
        this.vanishNotifications = false;
        this.reminderLocation = ReminderLocation.ACTION_BAR;
    }

    public String toString(){
        return "[PlayerVanishSettings: " +
                "isVanished=" + isVanished +
                ", vanishPersist=" + vanishPersist +
                ", nightVision=" + nightVision +
                ", breakBlocks=" + breakBlocks +
                ", containerOpen=" + containerOpen +
                ", attackEntities=" + attackEntities +
                ", mobsTarget=" + mobsTarget +
                ", pickupItems=" + pickupItems +
                ", invulnerability=" + invulnerability +
                ", leaveSilently=" + leaveSilently +
                ", joinSilently=" + joinSilently +
                ", vanishNotifications=" + vanishNotifications +
                ", reminderLocation=" + reminderLocation +
                "]";
    }

    public boolean isVanished() {
        return isVanished;
    }

    public void setVanished(boolean isVanished) {
        this.isVanished = isVanished;
    }

    public boolean shouldVanishPersist() {
        return vanishPersist;
    }

    public void setVanishPersist(boolean vanishPersist) {
        this.vanishPersist = vanishPersist;
    }

    public boolean giveNightvision() {
        return nightVision;
    }

    public void setNightVision(boolean nightVision) {
        this.nightVision = nightVision;
    }

    public boolean canBreakBlocks() {
        return breakBlocks;
    }

    public void setBreakBlocks(boolean breakBlocks) {
        this.breakBlocks = breakBlocks;
    }

    public boolean doesContainerOpenAnimation() {
        return containerOpen;
    }

    public void setContainerOpen(boolean containerOpen) {
        this.containerOpen = containerOpen;
    }

    public boolean canAttackEntities() {
        return attackEntities;
    }

    public void setAttackEntities(boolean attackEntities) {
        this.attackEntities = attackEntities;
    }

    public boolean shouldMobsTarget() {
        return mobsTarget;
    }

    public void setMobsTarget(boolean mobsTarget) {
        this.mobsTarget = mobsTarget;
    }

    public boolean shouldPickupItems() {
        return pickupItems;
    }

    public void setPickupItems(boolean pickupItems) {
        this.pickupItems = pickupItems;
    }

    public boolean shouldGiveInvulnerability() {
        return invulnerability;
    }

    public void setInvulnerability(boolean invulnerability) {
        this.invulnerability = invulnerability;
    }

    public boolean shouldLeaveSilently() {
        return leaveSilently;
    }

    public void setLeaveSilently(boolean leaveSilently) {
        this.leaveSilently = leaveSilently;
    }

    public boolean shouldJoinSilently() {
        return joinSilently;
    }

    public void setJoinSilently(boolean joinSilently) {
        this.joinSilently = joinSilently;
    }

    public ReminderLocation getReminderLocation() {
        return reminderLocation;
    }

    public void setReminderLocation(ReminderLocation reminderLocation) {
        this.reminderLocation = reminderLocation;
    }

    public boolean viewVanishNotifications() {
        return vanishNotifications;
    }

    public void setVanishNotifications(boolean vanishNotifications) {
        this.vanishNotifications = vanishNotifications;
    }
}
