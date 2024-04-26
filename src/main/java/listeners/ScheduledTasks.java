package listeners;

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
    @Override
    public void onReady(@NotNull ReadyEvent event)
    {
        //set jda to logger
        LoggerManager.setJda(event.getJDA());
    }
}
