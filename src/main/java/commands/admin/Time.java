package commands.admin;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import main.LoggerManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import repo.StatusRepo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Time extends SlashCommand
{
    public Time()
    {
        this.name = "time";
        this.help = "[ANGEL/DOUGLAS ONLY] Change the time when song requests should be closed. (Will not auto close)";
        this.userPermissions = new Permission[]{ Permission.MANAGE_SERVER };

        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.INTEGER, "time", "Date (in epoch timestamp) when the song request should close", true));

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
            StatusRepo.setTime(event.optLong("time"));
            event.getHook().sendMessageFormat("✅ | New song request deadline is set to <t:%s:F> (will reflect in channel description within 10 minutes). Do **NOT** forget to use `/admin toggle` when the deadline is near.", event.optLong("time")).queue();
        }

        catch(SQLException e)
        {
            event.getHook().setEphemeral(true).sendMessage("❌ | Unable to change song request close time.").queue();
            LoggerManager.logError(e, event.getCommandString(), event.getUser().getName(), event.getGuild());
        }
    }
}
