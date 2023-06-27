package schedulers;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class SubmissionListener extends ListenerAdapter
{
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if(event.getAuthor().isBot()) return;
        if(event.getMember().getRoles().stream().anyMatch(role -> role.getId().equals("1114390059572015204") || role.getId().equals("1114390059572015204")))
            return;
        if(event.getGuild().getId().equals("1114273768660017172")
            && event.getChannel().getType().name().equals("TEXT")
            && event.getChannel().getName().startsWith("submission"))
        {
            List<Message.Attachment> attachments = event.getMessage().getAttachments();
            if(attachments.isEmpty() || !attachments.get(0).getContentType().toLowerCase().contains("video"))
            {
                event.getMessage().delete().queue();
                return;
            }

            event.getMessage().createThreadChannel(event.getAuthor().getName() + "'s submission").queue();
        }
    }
}
