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
    private final ScheduledExecutorService requestChannelTopicScheduler = Executors.newSingleThreadScheduledExecutor();

    ScheduledFuture<?> requestChannelTopicUpdater = null;

    @Override
    public void onReady(@NotNull ReadyEvent event)
    {
        //set jda to logger
        LoggerManager.setJda(event.getJDA());

        Runnable updateRequestChannelTopic = () -> updateSongRequestTopic(event);

        requestChannelTopicUpdater = requestChannelTopicScheduler.scheduleAtFixedRate(updateRequestChannelTopic, 0, 10, TimeUnit.MINUTES);
    }

    @Override
    public void onShutdown(@NotNull ShutdownEvent event)
    {
        requestChannelTopicScheduler.shutdown();
    }

    private void updateSongRequestTopic(GenericEvent event)
    {
        try
        {
            long time = StatusRepo.getTime();
            boolean open = StatusRepo.isOpen();
            String topic = event.getJDA().getTextChannelById("1118582068914442250").getTopic();

            if(topic == null || (!open && topic.contains("OPEN")) || (open && topic.contains("CLOSED")) || (open && !topic.contains(String.valueOf(time))))
            {
                String message;
                if(open)
                    message = String.format("Song requests are **OPEN**. Should be closed in: %s", time <= 0 ? "TBA" : "<t:" + time + ":F>");
                else
                    message = "Song requests are **CLOSED**.";

                event.getJDA().getTextChannelById("1123274001993715823").getManager().setTopic(message).queue();
                LoggerManager.sendLogMessage(LogLevel.INFO, "Updated channel description to: " + message);
            }
        }

        catch(SQLException e)
        {
            LoggerManager.logError(e, "trying to update channel description");
        }
    }
}
