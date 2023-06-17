package schedulers;

import enums.LogLevel;
import main.LoggerManager;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.events.session.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import repo.StatusRepo;

import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ScheduledTasks extends ListenerAdapter
{
    private final ScheduledExecutorService channelDescriptionScheduler = Executors.newSingleThreadScheduledExecutor();
    ScheduledFuture<?> channelDescriptionUpdater = null;

    @Override
    public void onReady(@NotNull ReadyEvent event)
    {
        //set jda to logger
        LoggerManager.setJda(event.getJDA());

        Runnable updateChannelDescription = () -> updateChannelDescription(event);

        channelDescriptionUpdater = channelDescriptionScheduler.scheduleAtFixedRate(updateChannelDescription, 0, 10, TimeUnit.MINUTES);
    }

    @Override
    public void onShutdown(@NotNull ShutdownEvent event)
    {
        channelDescriptionScheduler.shutdown();
    }

    private void updateChannelDescription(GenericEvent event)
    {
        try
        {
            long time = StatusRepo.getTime();
            boolean open = StatusRepo.isOpen();
            String topic = event.getJDA().getTextChannelById("1118658436708708484").getTopic();

            if((!open && topic.contains("OPEN")) || (open && topic.contains("CLOSED")) || (open && topic.contains(String.valueOf(time))))
            {
                String message;
                if(open)
                    message = String.format("Song requests are **OPEN**. Should be closed in: %s", time <= 0 ? "TBA" : "<t:" + time + ":F>");
                else
                    message = "Song requests are **CLOSED**.";

                //                event.getJDA().getTextChannelById("928247257730514984").getManager()
                event.getJDA().getTextChannelById("1118658436708708484").getManager()
                     .setTopic(message).queue();
                LoggerManager.sendLogMessage(LogLevel.INFO, "Updated channel description to: " + message);
            }
        }

        catch(SQLException e)
        {
            LoggerManager.logError(e, "trying to update channel description");
        }
    }
}
