package commands.admin;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import main.LoggerManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import repo.RequestRepo;
import utils.Statics;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Remove extends SlashCommand
{
    public Remove()
    {
        this.name = "remove";
        this.help = "Remove a song request that a member has made.";
        this.userPermissions = new Permission[]{ Permission.MANAGE_SERVER };

        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "user", "User to remove request of", false));
        options.add(new OptionData(OptionType.INTEGER, "user_id", "Remove request using user id", false));

        this.options = options;
    }

    @Override
    protected void execute(SlashCommandEvent event)
    {
        event.deferReply().queue();

        if(event.getGuild() == null && !event.getGuild().getId().equals(Statics.ONEHR_SERVER_ID) && !event.getGuild().getId().equals(Statics.DEV_SERVER_ID))
            return;

        if(event.optUser("user") == null && event.getOption("user_id") == null)
        {
            event.getHook().sendMessageFormat("❌ | Please provide a user or a user ID.").queue();
            return;
        }

        long userId;
        if(event.getOption("user_id") == null)
            userId = event.optUser("user").getIdLong();
        else
            userId = event.optLong("user_id");

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
