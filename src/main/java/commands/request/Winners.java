package commands.request;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import dataobjects.Request;
import main.LoggerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import repo.WinnerRepo;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Winners extends SlashCommand
{
    public Winners()
    {
        this.name = "winners";
        this.help = "List all the winners of the wheel and their winning song.";

        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.INTEGER, "season", "View winners of a certain season", true));

        this.options = options;
    }

    @Override
    protected void execute(SlashCommandEvent event)
    {
        if(!event.getGuild().getId().equals("1114273768660017172") && !event.getGuild().getId().equals("695074147071557632"))
            return;

        event.deferReply().queue();

        try
        {
            java.util.List<Request> winners = WinnerRepo.getWinners((int) event.optLong("season"));

            if(winners.isEmpty())
            {
                event.getHook().sendMessageFormat("❌ | There are no winners for that season!").queue();
                return;
            }

            EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Winner List for Season " + event.optLong("season"))
                .setColor(Color.YELLOW);

            for(Request request : winners)
            {
                String name = event.getJDA().retrieveUserById(request.getUserId()).complete().getEffectiveName();
                embed.appendDescription(String.format("**%s** | %s%n", name, request.getTitle()));
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
