package commands.admin;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import main.LoggerManager;
import repo.StatusRepo;

import java.sql.SQLException;

public class Toggle extends SlashCommand
{
    public Toggle()
    {
        this.name = "toggle";
        this.help = "[ANGEL/DOUGLAS ONLY] Toggles the song request's open/closed status.";
    }

    @Override
    protected void execute(SlashCommandEvent event)
    {
        if(!event.getGuild().getId().equals("1114273768660017172") && !event.getGuild().getId().equals("695074147071557632"))
            return;

        if(!event.getUser().getId().equals("189690228292845568") && !event.getUser().getId().equals("465271897978961921"))
            return;

        event.deferReply().queue();

        try
        {
            boolean newOpenStatus = !StatusRepo.isOpen();
            StatusRepo.setOpen(newOpenStatus);

            String message;
            if(newOpenStatus)
                message = String.format("Song requests are now **OPEN**. Should be closed in: <t:%s:F>", StatusRepo.getTime());
            else
                message = "Song requests are now **CLOSED**.";

            event.getHook().sendMessage("✅ | " + message + " (will reflect in channel description within 10 minutes)").queue();
        }

        catch(SQLException e)
        {
            event.getHook().setEphemeral(true).sendMessage("❌ | Unable to change song request status.").queue();
            LoggerManager.logError(e, event.getCommandString(), event.getUser().getName(), event.getGuild());
        }
    }
}
