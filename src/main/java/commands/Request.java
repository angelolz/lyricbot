package commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import commands.request.*;

public class Request extends SlashCommand
{
    public Request()
    {
        this.name = "request";
        this.help = "Commands used for requesting songs";
        this.children = new SlashCommand[]{ new Set(), new List(), new Winners() };
    }

    @Override
    protected void execute(SlashCommandEvent event) { /* ignored */ }
}
