package commands.admin;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import schedulers.NotificationListener;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Notify extends SlashCommand
{
    private static final String MESSAGE_ID = "message_id";
    public Notify()
    {
        this.name = "notify";
        this.help = "Set the message ID to watch for adding/removing notification role.";
        this.userPermissions = new Permission[]{ Permission.MANAGE_SERVER };
        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING, MESSAGE_ID, "message id of the message to listen for reactions", true));

        this.options = options;
    }

    @Override
    protected void execute(SlashCommandEvent event)
    {
        event.deferReply().queue();
        
        if(!Utils.isValidLong(event.optString(MESSAGE_ID))) {
            event.getHook().sendMessage("❌ | Not a valid ID.").queue();
            return;
        }
        
        NotificationListener.setMessageId(event.optString(MESSAGE_ID));
        event.getHook().sendMessage("✅ | Set new message to watch.").queue();
    }
}


