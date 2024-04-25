package listeners;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubmissionListener extends ListenerAdapter
{
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if(event.getAuthor().isBot()) return;
        if(event.getGuild().getId().equals("1114273768660017172")
            && event.getChannel().getType().name().equals("TEXT")
            && event.getChannel().getName().startsWith("submission"))
        {
            final String PATTERN = "\\bhttps?:\\/\\/(?:www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b(?:[-a-zA-Z0-9()@:%_\\+.~#?&\\/=]*)\\b";
            Pattern p = Pattern.compile(PATTERN);
            Matcher m = p.matcher(event.getMessage().getContentRaw());

            List<Message.Attachment> attachments = event.getMessage().getAttachments();
            
            //can't find link AND (no attachments OR attachment doesn't have a video)
            if(!m.find() && (attachments.isEmpty() || !attachments.get(0).getContentType().toLowerCase().contains("video")))
            {
                //if an admin breaks these rules, dw about it
                if(event.getMember().getRoles().stream().anyMatch(role -> role.getId().equals("1114276515857833994") || role.getId().equals("1114390059572015204")))
                    return;

                event.getMessage().delete().queue();
                event.getChannel().sendMessageFormat("❌ | %s, please submit a video or a valid link for your submission.", event.getAuthor().getAsMention()).delay(8, TimeUnit.SECONDS).flatMap(Message::delete).queue();
                return;
            }

            event.getMessage().createThreadChannel(event.getAuthor().getName() + "'s submission").queue();
        }
    }
}
