package schedulers;

import main.LoggerManager;
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
        if(roles.stream().noneMatch(r -> r.getId().equals(Statics.NOTIFICATION_ROLE_ID)))
            event.getGuild().addRoleToMember(event.getUser(), notificationRole).queue(s -> {
            }, e -> LoggerManager.logError((Exception) e, String.format("couldn't add role to user | name: %s, id: %s", event.getUser().getName(), event.getUser().getId())));
    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event)
    {
        if((messageId == null || !messageId.equals(event.getMessageId())) || !event.getGuild().getId().equals(Statics.ONEHR_SERVER_ID) && !event.getGuild().getId().equals(Statics.DEV_SERVER_ID))
            return;

        Role notificationRole = event.getGuild().getRoleById(Statics.NOTIFICATION_ROLE_ID);
        List<Role> roles = event.getMember().getRoles();

        if(roles.stream().anyMatch(r -> r.getId().equals(Statics.NOTIFICATION_ROLE_ID)))
            event.getGuild().removeRoleFromMember(event.getUser(), notificationRole).queue(s -> {
            }, e -> LoggerManager.logError((Exception) e, String.format("couldn't remove role from user | name: %s, id: %s", event.getUser().getName(), event.getUser().getId())));
    }

    public static void setMessageId(String messageId)
    {
        NotificationListener.messageId = messageId;
    }
}
