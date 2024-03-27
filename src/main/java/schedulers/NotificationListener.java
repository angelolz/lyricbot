package schedulers;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import utils.Statics;

import java.util.List;

public class NotificationListener extends ListenerAdapter
{
    private static String messageId;

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event)
    {
        if((messageId == null || !messageId.equals(event.getMessageId())) || (!event.getGuild().getId().equals(Statics.ONEHR_SERVER_ID) && !event.getGuild().getId().equals(Statics.DEV_SERVER_ID)))
            return;

        Role notificationRole = event.getGuild().getRoleById(Statics.NOTIFICATION_ROLE_ID);
        List<Role> roles = event.getMember().getRoles();
        if(!roles.stream().anyMatch(r -> r.getId().equals(Statics.NOTIFICATION_ROLE_ID)))
            event.getGuild().addRoleToMember(event.getUser(), notificationRole).queue();
    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event)
    {
        if((messageId == null || !messageId.equals(event.getMessageId())) || !event.getGuild().getId().equals(Statics.ONEHR_SERVER_ID) && !event.getGuild().getId().equals(Statics.DEV_SERVER_ID))
            return;

        Role notificationRole = event.getGuild().getRoleById(Statics.NOTIFICATION_ROLE_ID);
        List<Role> roles = event.getMember().getRoles();

        if(roles.stream().anyMatch(r -> r.getId().equals(Statics.NOTIFICATION_ROLE_ID)))
            event.getGuild().removeRoleFromMember(event.getUser(), notificationRole);
    }

    public static void setMessageId(String messageId)
    {
        NotificationListener.messageId = messageId;
    }
}