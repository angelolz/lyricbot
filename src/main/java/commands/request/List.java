package commands.request;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import dataobjects.Request;
import main.LoggerManager;
import repo.RequestRepo;

import java.sql.SQLException;

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
        if(!event.getGuild().getId().equals("1114273768660017172") && !event.getGuild().getId().equals("695074147071557632"))
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
            for(Request request : requests)
            {
                String title = request.getTitle().length() > 75 ? request.getTitle().substring(0, 72) + "..." : request.getTitle();
                String name = event.getJDA().retrieveUserById(request.getUserId()).complete().getEffectiveName();
                sb.append(String.format("%-75s {%s}%n", title, name));
            }

            event.getHook().sendMessage("```\n" + sb + "\n```").queue();
        }

        catch(SQLException e)
        {
            event.getHook().setEphemeral(true).sendMessage("❌ | There was an error retrieving the request list.").queue();
            LoggerManager.logError(e, event.getCommandString(), event.getUser().getName(), event.getGuild());
        }
    }
}
