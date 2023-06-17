package commands.lyricerinfo;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import main.LoggerManager;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import repo.LyricerRepo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Remove extends SlashCommand
{
    public Remove()
    {
        this.name = "remove";
        this.help = "Remove your own link and/or watermark.";

        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING, "remove", "What do you want to remove?", true)
            .addChoice("link", "link")
            .addChoice("watermark", "watermark"));

        this.options = options;
    }

    @Override
    protected void execute(SlashCommandEvent event)
    {
        event.deferReply().queue();

        if(event.optString("remove").equals("link"))
        {
            try
            {
                if(!LyricerRepo.doesExist(event.getUser().getIdLong()))
                {
                    event.getHook().sendMessage("❌ | you don't even have any lyricer info saved bozo").queue();
                    return;
                }

                LyricerRepo.updateLink(event.getUser().getIdLong(), null);
                event.getHook().sendMessage("✅ | Removed your link.").queue();
            }

            catch(SQLException e)
            {
                event.getHook().sendMessage("❌ | There was a problem removing your link.").queue();
                LoggerManager.logError(e, event.getCommandString(), event.getUser().getName(), event.getGuild());
            }
        }

        else
        {
            String fileName = "watermarks/" + event.getUser().getId() + ".png";
            try
            {
                if(!Files.deleteIfExists(Paths.get(fileName)))
                    event.getHook().sendMessage("✅ | Removed your link.").queue();
                else
                    event.getHook().sendMessage("❌ | There is no watermark saved.").queue();
            }

            catch(IOException e)
            {
                event.getHook().sendMessage("❌ | There was a problem removing your watermark.").queue();
                LoggerManager.logError(e, event.getCommandString(), event.getUser().getName(), event.getGuild());
            }
        }
    }
}
