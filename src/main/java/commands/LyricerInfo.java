package commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import commands.lyricerinfo.*;

public class LyricerInfo extends SlashCommand
{
    public LyricerInfo()
    {
        this.name = "lyricerinfo";
        this.help = "Commands used for storing lyricer info";
        this.children = new SlashCommand[]{ new Set(), new View(), new Remove() };
    }

    @Override
    protected void execute(SlashCommandEvent event) { /*ignored */ }
}
