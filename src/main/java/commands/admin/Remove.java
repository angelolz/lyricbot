package commands.admin;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import main.LoggerManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import repo.RequestRepo;
import utils.Statics;
import utils.Utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Remove extends SlashCommand
{
    private static final String USER_ID = "user_id";

    public Remove()
    {
        this.name = "remove";
        this.help = "Remove a song request that a member has made.";
        this.userPermissions = new Permission[]{ Permission.MANAGE_SERVER };

        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "user", "User to remove request of", false));
        options.add(new OptionData(OptionType.STRING, USER_ID, "Remove request using user id", false));

        this.options = options;
    }

    @Override
    protected void execute(SlashCommandEvent event)
    {
        event.deferReply().queue();

        if(event.getGuild() == null && !event.getGuild().getId().equals(Statics.ONEHR_SERVER_ID) && !event.getGuild().getId().equals(Statics.DEV_SERVER_ID))
            return;

        if(event.optUser("user") == null && event.optString(USER_ID) == null)
        {
            event.getHook().sendMessageFormat("❌ | Please provide a user or a user ID.").queue();
            return;
        }

        long userId;
        if(event.getOption(USER_ID) == null)
            userId = event.optUser("user").getIdLong();
        else
        {
            if(Utils.isValidLong(event.optString(USER_ID)))
                userId = event.optLong(USER_ID);
            else
            {
                event.getHook().sendMessageFormat("❌ | Invalid user ID.").queue();
                return;
            }
        }

        try
        {
            RequestRepo.deleteRequest(userId);
            event.getHook().sendMessage("✅ | Removed request.").queue();
        }

        catch(SQLException e)
        {
            event.getHook().setEphemeral(true).sendMessage("❌ | Unable to remove request.").queue();
            LoggerManager.logError(e, event.getCommandString(), event.getUser().getName(), event.getGuild());
        }
    }
}
