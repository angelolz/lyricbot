package listeners;


import commands.admin.Clear;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ButtonListener extends ListenerAdapter
{
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event)
    {
        String[] args = event.getComponentId().split(":");

        if(!args[0].contains(event.getUser().getId())) return;

        switch(args[1].toLowerCase())
        {
            case "clear" -> Clear.clearList(event, args[2].equalsIgnoreCase("yes"));
        }
    }
}
