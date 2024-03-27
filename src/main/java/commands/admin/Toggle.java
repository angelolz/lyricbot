package commands.admin;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import main.LoggerManager;
import net.dv8tion.jda.api.Permission;
import repo.StatusRepo;

import java.sql.SQLException;

public class Toggle extends SlashCommand
{
    public Toggle()
    {
        this.name = "toggle";
        this.help = "Toggles the song request's open/closed status.";
        this.userPermissions = new Permission[]{ Permission.MANAGE_SERVER };
    }

    @Override
    protected void execute(SlashCommandEvent event)
    {
        if(!event.getGuild().getId().equals("1114273768660017172") && !event.getGuild().getId().equals("695074147071557632"))
            return;

        event.deferReply().queue();

        try
        {
            boolean newOpenStatus = !StatusRepo.isOpen();
            StatusRepo.setOpen(newOpenStatus);
            event.getHook().sendMessageFormat("✅ | Song requests are now **%s**. (will reflect in channel description within 10 minutes)", newOpenStatus ? "OPEN" : "CLOSED").queue();
        }

        catch(SQLException e)
        {
            event.getHook().setEphemeral(true).sendMessage("❌ | Unable to change song request status.").queue();
            LoggerManager.logError(e, event.getCommandString(), event.getUser().getName(), event.getGuild());
        }
    }
}
