package commands.lyricerinfo;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import enums.LogLevel;
import main.LoggerManager;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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

        File lyricerFolder = new File("lyricers/" + event.getUser().getId());

        if(!lyricerFolder.exists())
        {
            event.getHook().sendMessageFormat("❌ | There is no lyricer info saved from you. use `/lyricerinfo set` to save your %s.", event.optString("remove")).queue();
            return;
        }

        event.getHook().sendMessage(remove(event.getUser().getId(), event.optString("remove"))).queue();

        if(lyricerFolder.listFiles().length == 0 && !lyricerFolder.delete())
            LoggerManager.sendLogMessage(LogLevel.ERROR, "unable to delete folder: " + lyricerFolder.getPath());
    }

    private String remove(String userId, String removeType)
    {
        String fileName;
        if(removeType.equals("link"))
            fileName = "lyricers/" + userId + "/link.txt";
        else
            fileName = "lyricers/" + userId + "/watermark.png";

        File linkFile = new File(fileName);

        try
        {
            if(Files.deleteIfExists(linkFile.toPath()))
                return String.format("✅ | Removed your %s.", removeType);
            else
                return String.format("❌ | you don't even have a %s saved bozo", removeType);
        }

        catch(IOException e)
        {
            return String.format("❌ | There was a problem removing your %s.", removeType);
        }
    }
}
