package commands.admin;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import main.LoggerManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import repo.LyricerRepo;
import repo.RequestRepo;
import utils.Statics;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Ban extends SlashCommand
{
    public Ban()
    {
        this.name = "ban";
        this.help = "Ban a user from requesting a song.";
        this.userPermissions = new Permission[]{ Permission.MANAGE_SERVER };
        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "user", "User to ban", true));

        this.options = options;
    }

    @Override
    protected void execute(SlashCommandEvent event)
    {
        if(event.getGuild() == null && !event.getGuild().getId().equals(Statics.ONEHR_SERVER_ID) && !event.getGuild().getId().equals(Statics.DEV_SERVER_ID))
            return;

        try
        {
            event.deferReply().queue();

            User user = event.optUser("user");

            if(!LyricerRepo.doesExist(user.getIdLong()))
                LyricerRepo.addLyricer(user.getIdLong());

            LyricerRepo.updateBanned(user.getIdLong(), true);
            RequestRepo.deleteRequest(user.getIdLong());

            event.getHook().sendMessage("✅ | User can no longer make song requests.").queue();
        }

        catch(SQLException e)
        {
            event.getHook().setEphemeral(true).sendMessage("❌ | Unable to ban user.").queue();
            LoggerManager.logError(e, event.getCommandString(), event.getUser().getName(), event.getGuild());
        }
    }
}
