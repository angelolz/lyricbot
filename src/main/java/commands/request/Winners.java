package commands.request;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import dataobjects.Request;
import main.LoggerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import repo.WinnerRepo;

import java.awt.*;
import java.sql.SQLException;

public class Winners extends SlashCommand
{
    public Winners()
    {
        this.name = "winners";
        this.help = "List all the winners of the wheel and their winning song.";
    }

    @Override
    protected void execute(SlashCommandEvent event)
    {
        if(!event.getGuild().getId().equals("1114273768660017172") && !event.getGuild().getId().equals("695074147071557632"))
            return;

        event.deferReply().queue();

        try
        {
            java.util.List<Request> winners = WinnerRepo.getWinners();

            if(winners.isEmpty())
            {
                event.getHook().sendMessageFormat("❌ | There are no winners!").queue();
                return;
            }

            EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Winner List")
                .setColor(Color.YELLOW);

            for(Request request : winners)
            {
                String name = request.getName().length() > 16 ? request.getName().substring(0, 13) + "..." : request.getName();
                embed.appendDescription(String.format("**%s** | %s\n", name, request.getTitle()));
            }

            event.getHook().sendMessageEmbeds(embed.build()).queue();
        }

        catch(SQLException e)
        {
            event.getHook().setEphemeral(true).sendMessage("❌ | There was an error retrieving the winner list.").queue();
            LoggerManager.logError(e, event.getCommandString(), event.getUser().getName(), event.getGuild());
        }
    }
}
