package simplexity.simplevanish.handling;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import simplexity.simplevanish.SimpleVanish;
import simplexity.simplevanish.config.ConfigHandler;
import simplexity.simplevanish.config.Message;
import simplexity.simplevanish.saving.Cache;

public class ScheduleHandler {
    private BukkitTask task;
    private final BukkitScheduler scheduler = SimpleVanish.getInstance().getServer().getScheduler();
    private final MiniMessage miniMessage = SimpleVanish.getMiniMessage();
    private ScheduleHandler(){}
    private static ScheduleHandler instance;
    public static ScheduleHandler getInstance(){
        if(instance == null) instance = new ScheduleHandler();
        return instance;
    }

    public void startScheduler(){
        if (task != null) task.cancel();
        task = scheduler.runTaskTimer(SimpleVanish.getInstance(), this::displayActionBar, 0L, ConfigHandler.getInstance().getRemindInterval() * 20L);
    }

    public void stopScheduler(){
        if (task != null) task.cancel();
    }

    private void displayActionBar(){
        if (Cache.getVanishedPlayers().isEmpty()) return;
        for (Player player : Cache.getVanishedPlayers()) {
            player.sendActionBar(miniMessage.deserialize(Message.VANISH_REMINDER.getMessage()));
        }
    }

}
