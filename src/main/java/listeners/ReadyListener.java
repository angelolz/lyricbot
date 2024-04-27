package listeners;

import lombok.Getter;
import lombok.Setter;
import main.LoggerManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.channel.concrete.TextChannelManager;
import org.jetbrains.annotations.NotNull;
import utils.Statics;

public class ReadyListener extends ListenerAdapter
{
    @Getter
    @Setter
    private static boolean requestsOpen;

    @Override
    public void onReady(@NotNull ReadyEvent event)
    {
        //set jda to logger
        LoggerManager.setJda(event.getJDA());

        //check what status the request channel is in
        ReadyListener.setRequestsOpen(checkRequestStatus(event.getJDA()));
    }

    public boolean checkRequestStatus(JDA jda)
    {
        TextChannel textChannel = jda.getTextChannelById(Statics.REQUEST_CHANNEL_ID);
        if(textChannel == null)
            return false;

        String topic = textChannel.getTopic();
        if(topic == null)
            return false;

        return topic.contains("OPEN");
    }
}
