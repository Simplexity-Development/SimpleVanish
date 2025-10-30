package simplexity.simplevanish.handling;

import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import simplexity.simplevanish.SimpleVanish;
import simplexity.simplevanish.config.ConfigHandler;
import simplexity.simplevanish.config.Message;
import simplexity.simplevanish.saving.Cache;

import java.util.concurrent.TimeUnit;

public class ScheduleHandler {
    private ScheduledTask task;
    private final AsyncScheduler scheduler = SimpleVanish.getInstance().getServer().getAsyncScheduler();
    private final MiniMessage miniMessage = SimpleVanish.getMiniMessage();
    private ScheduleHandler(){}
    private static ScheduleHandler instance;
    public static ScheduleHandler getInstance(){
        if(instance == null) instance = new ScheduleHandler();
        return instance;
    }

    public void startScheduler(){
        if (task != null) task.cancel();
        task = scheduler.runAtFixedRate(SimpleVanish.getInstance(), this::displayActionBar, 0L, ConfigHandler.getInstance().getRemindInterval(), TimeUnit.SECONDS);
    }

    public void stopScheduler(){
        if (task != null) task.cancel();
    }

    private void displayActionBar(ScheduledTask scheduledTask){
        if (Cache.getVanishedPlayers().isEmpty()) return;
        for (Player player : Cache.getVanishedPlayers()) {
            player.getScheduler().run(SimpleVanish.getInstance(), task1 -> player.sendActionBar(miniMessage.deserialize(Message.VANISH_REMINDER.getMessage())), () -> {});
        }
    }

}
