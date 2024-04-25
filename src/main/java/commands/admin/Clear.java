package commands.admin;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import main.LoggerManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import repo.RequestRepo;
import utils.Statics;

import java.sql.SQLException;

public class Clear extends SlashCommand
{
    private static final String DISABLED = "disabled";

    public Clear()
    {
        this.name = "clear";
        this.help = "Clears the song request list.";
        this.userPermissions = new Permission[]{ Permission.MANAGE_SERVER };
    }

    @Override
    protected void execute(SlashCommandEvent event)
    {
        if(event.getGuild() == null && !event.getGuild().getId().equals(Statics.ONEHR_SERVER_ID) && !event.getGuild().getId().equals(Statics.DEV_SERVER_ID))
            return;

        event.deferReply().queue();
        event.getHook().sendMessage("Are you sure you want to clear the list?").addActionRow(
            Button.success(String.format("%s:clear:yes", event.getUser().getId()), "Yes"),
            Button.danger(String.format("%s:clear:no", event.getUser().getId()), "No")
        ).queue();
    }

    public static void clearList(ButtonInteractionEvent event, boolean clear)
    {
        event.deferEdit().queue();

        try
        {
            if(clear)
            {
                RequestRepo.deleteAllRequests();
                event.getHook().editOriginalComponents(ActionRow.of(Button.primary(DISABLED, "Cleared the list.").asDisabled().withEmoji(Emoji.fromFormatted("✅")))).queue();
            }

            else
                event.getHook().editOriginalComponents(ActionRow.of(Button.primary(DISABLED, "List is not cleared.").asDisabled().withEmoji(Emoji.fromFormatted("❌")))).queue();
        }

        catch(SQLException e)
        {
            LoggerManager.logError(e, "unable to clear request list");
            event.getHook().editOriginalComponents(ActionRow.of(Button.primary(DISABLED, "Unable to clear the list.").asDisabled().withEmoji(Emoji.fromFormatted("❌")))).queue();
        }
    }
}
