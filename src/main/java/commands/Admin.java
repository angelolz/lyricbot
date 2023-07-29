package commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import commands.admin.*;

public class Admin extends SlashCommand
{
    public Admin()
    {
        this.name = "admin";
        this.help = "Commands used for angel/doublar";
        this.children = new SlashCommand[]{ new Toggle(), new Time(), new Pick(), new Ban(), new Unban(), new Remove() };
    }

    @Override
    protected void execute(SlashCommandEvent event) { /* ignored */ }
}
