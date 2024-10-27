package commands.admin;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import dataobjects.Request;
import main.LoggerManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import repo.LyricerRepo;
import repo.RequestRepo;
import repo.WinnerRepo;
import utils.Statics;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Pick extends SlashCommand
{
    public Pick()
    {
        this.name = "pick";
        this.help = "Puts a user in the winners list and prevents them from sending a request.";
        this.userPermissions = new Permission[]{ Permission.MANAGE_SERVER };

        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "user", "This user will be marked as a winner.", true));
        options.add(new OptionData(OptionType.INTEGER, "season", "For what season?", true));
        this.options = options;
    }

    @Override
    protected void execute(SlashCommandEvent event)
    {
        if(event.getGuild() == null && !event.getGuild().getId().equals(Statics.ONEHR_SERVER_ID) && !event.getGuild().getId().equals(Statics.DEV_SERVER_ID))
            return;

        event.deferReply().queue();

        User user = event.optUser("user");

        try
        {
            Request request = RequestRepo.getRequest(user.getIdLong());

            if(request == null)
            {
                event.getHook().sendMessage("❌ | That user does not have a song request.").queue();
                return;
            }

            WinnerRepo.addWinner(request, (int) event.optLong("season"));
            RequestRepo.deleteRequest(request.getUserId());

            event.getHook().sendMessage("✅ | Added user to winner's list.").queue();
        }

        catch(SQLException e)
        {
            event.getHook().setEphemeral(true).sendMessage("❌ | Unable to set user as winner.").queue();
            LoggerManager.logError(e, event.getCommandString(), event.getUser().getName(), event.getGuild());
        }
    }
}
