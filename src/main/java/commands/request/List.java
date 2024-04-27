package commands.request;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import dataobjects.Request;
import main.LoggerManager;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import repo.RequestRepo;
import utils.Statics;

import java.sql.SQLException;
import java.util.ArrayList;

public class List extends SlashCommand
{
    public List()
    {
        this.name = "list";
        this.help = "List all the songs that have been requested.";
    }

    @Override
    protected void execute(SlashCommandEvent event)
    {
        if(event.getGuild() == null && !event.getGuild().getId().equals(Statics.ONEHR_SERVER_ID) && !event.getGuild().getId().equals(Statics.DEV_SERVER_ID))
            return;

        event.deferReply().queue();

        try
        {
            java.util.List<Request> requests = RequestRepo.getRequests();

            if(requests.isEmpty())
            {
                event.getHook().sendMessageFormat("❌ | There are no songs in the list!").queue();
                return;
            }

            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < requests.size(); i++)
            {
                Request request = requests.get(i);
                String title = request.getTitle().length() > 75 ? request.getTitle().substring(0, 72) + "..." : request.getTitle();
                String link = request.getLink();
                String mention = event.getJDA().retrieveUserById(request.getUserId()).complete().getAsMention();
                sb.append(String.format("%d. **[%s](%s)** from %s%n", i+1, title, link, mention));
            }

            MessageCreateBuilder msgBuilder = new MessageCreateBuilder()
                .setContent(sb.toString())
                .setSuppressEmbeds(true)
                .setSuppressedNotifications(true);

            event.getHook().sendMessage(msgBuilder.build()).queue();
        }

        catch(SQLException e)
        {
            event.getHook().setEphemeral(true).sendMessage("❌ | There was an error retrieving the request list.").queue();
            LoggerManager.logError(e, event.getCommandString(), event.getUser().getName(), event.getGuild());
        }
    }
}
