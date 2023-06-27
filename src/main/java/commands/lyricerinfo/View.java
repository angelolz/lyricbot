package commands.lyricerinfo;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import dataobjects.Lyricer;
import main.LoggerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.FileUpload;
import repo.LyricerRepo;

import java.io.File;
import java.sql.SQLException;
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
        File watermarkFile = new File("watermarks/" + user.getId() + ".png");

        try
        {
            if(!LyricerRepo.doesExist(user.getIdLong()))
                LyricerRepo.addLyricer(user.getIdLong());

            Lyricer lyricer = LyricerRepo.getLyricer(user.getIdLong());

            EmbedBuilder embed = new EmbedBuilder()
                .setColor(0x32CD32)
                .setTitle("Lyricer Info: " + user.getName())
                .addField("YouTube/Social Link", lyricer.getLink() == null ? "*None*" : lyricer.getLink(), false);

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

        catch(SQLException e)
        {
            LoggerManager.logError(e, event.getCommandString(), event.getUser().getName(), event.getGuild());
            event.getHook().sendMessage("❌ | Unable to get lyricer info.").queue();
        }
    }
}
