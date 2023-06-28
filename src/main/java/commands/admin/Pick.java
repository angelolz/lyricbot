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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Pick extends SlashCommand
{
    public Pick()
    {
        this.name = "pick";
        this.help = "[ANGEL/DOUGLAS ONLY] Puts a user in the winners list and prevents them from sending a request.";
        this.userPermissions = new Permission[]{ Permission.MANAGE_SERVER };

        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "user", "This user will be marked as a winner.", true));
        this.options = options;
    }

    @Override
    protected void execute(SlashCommandEvent event)
    {
        if(!event.getGuild().getId().equals("1114273768660017172") && !event.getGuild().getId().equals("695074147071557632"))
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

            WinnerRepo.addWinner(request);
            LyricerRepo.updateBanned(request.getUserId(), true);
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
