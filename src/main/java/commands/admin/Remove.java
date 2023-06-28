package commands.admin;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import commands.Request;
import main.LoggerManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import repo.RequestRepo;

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
        options.add(new OptionData(OptionType.USER, "user", "User to remove request of", true));

        this.options = options;
    }

    @Override
    protected void execute(SlashCommandEvent event)
    {
        if(!event.getGuild().getId().equals("1114273768660017172") && !event.getGuild().getId().equals("695074147071557632"))
            return;

        try
        {
            RequestRepo.deleteRequest(event.optUser("user").getIdLong());
            event.getHook().sendMessage("✅ | Removed request.").queue();
        }

        catch(SQLException e)
        {
            event.getHook().setEphemeral(true).sendMessage("❌ | Unable to remove request.").queue();
            LoggerManager.logError(e, event.getCommandString(), event.getUser().getName(), event.getGuild());
        }
    }
}
