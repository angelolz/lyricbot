package commands.lyricerinfo;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import main.LoggerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.FileUpload;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class View extends SlashCommand
{
    public View()
    {
        this.name = "view";
        this.help = "View you (or another person's) link and/or watermark.";

        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "user", "See another person's link and watermark.", false));

        this.options = options;
    }

    @Override
    protected void execute(SlashCommandEvent event)
    {
        event.deferReply().queue();

        User user = event.optUser("user", event.getUser());
        
        File lyricerFolder = new File("lyricers/" + user.getId());

        if(!lyricerFolder.exists())
        {
            event.getHook()
                 .sendMessageFormat("❌ | There is no lyricer info saved from %s.",
                     user.getId().equals(event.getUser().getId()) ? "you" : "that user")
                 .queue();
            return;
        }

        File linkFile = new File("lyricers/" + user.getId() + "/link.txt");
        File watermarkFile = new File("lyricers/" + user.getId() + "/watermark.png");

        try
        {
            EmbedBuilder embed = new EmbedBuilder()
                .setColor(0x32CD32)
                .setTitle("Lyricer Info: " + user.getName())
                .addField("Link", getLink(linkFile), false);

            if(!watermarkFile.exists())
            {
                embed.addField("Watermark", "**None**", false);
                event.getHook().sendMessageEmbeds(embed.build()).queue();
            }

            else
            {
                FileUpload imgUpload = FileUpload.fromData(watermarkFile, "watermark.png");
                embed.addField("Watermark:", "", true);
                embed.setImage("attachment://watermark.png");
                event.getHook().sendMessageEmbeds(embed.build()).addFiles(imgUpload).queue();
            }
        }

        catch(IOException e)
        {
            LoggerManager.logError(e, event.getCommandString(), event.getUser().getName(), event.getGuild());
            event.getHook().sendMessage("❌ | Unable to get lyricer info.").queue();
        }
    }

    private String getLink(File linkFile) throws IOException
    {
        if(!linkFile.exists())
            return "*None*";

        List<String> lines = Files.readAllLines(Paths.get(linkFile.toURI()), StandardCharsets.UTF_8);
        return String.format("[Link Here](%1$s)", lines.get(0));
    }
}
