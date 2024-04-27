package commands.admin;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import listeners.ReadyListener;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import utils.Statics;

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
        if(event.getGuild() == null && !event.getGuild().getId().equals(Statics.ONEHR_SERVER_ID) && !event.getGuild().getId().equals(Statics.DEV_SERVER_ID))
            return;

        event.deferReply().queue();

        boolean newOpenStatus = !ReadyListener.isRequestsOpen();
        ReadyListener.setRequestsOpen(newOpenStatus);

        String message = String.format("Song requests are **%s**.", newOpenStatus ? "OPEN" : "CLOSED");
        TextChannel textChannel = event.getJDA().getTextChannelById(Statics.REQUEST_CHANNEL_ID);
        if(textChannel != null)
            textChannel.getManager().setTopic(message).queue();

        event.getHook().sendMessage("✅ | " + message).queue();
    }
}
