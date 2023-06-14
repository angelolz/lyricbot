package commands.lyricerinfo;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import main.LoggerManager;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Set extends SlashCommand
{
    public Set()
    {
        this.name = "set";
        this.help = "Set your own link and/or watermarks.";

        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING, "link", "Link to your own YouTube channel or social media.", false));
        options.add(new OptionData(OptionType.ATTACHMENT, "watermarks", "Image file of your own watermarks (png only please)", false));

        this.options = options;
    }

    @Override
    protected void execute(SlashCommandEvent event)
    {
        event.deferReply().queue();

        if(event.optString("link") == null && event.optAttachment("watermarks") == null)
        {
            event.getHook().sendMessage("❌ | at least set a link **OR** a watermarks l0ser").queue();
            return;
        }

        File lyricerFolder = new File("lyricers/" + event.getUser().getId());
        if(!lyricerFolder.exists() && (!lyricerFolder.mkdir()))
        {
            event.getHook().sendMessage("❌ | Couldn't set up your lyricer info.").queue();
            return;
        }

        if(event.optString("link") != null)
        {
            if(!isValidUrl(event.optString("link")))
                event.getHook().sendMessage("❌ | The link you provided isn't valid!").queue();
            else
                event.getHook().sendMessage(setLink(event.getUser().getId(), event.optString("link"))).queue();
        }

        if(event.optAttachment("watermarks") != null)
        {
            Message.Attachment attachment = event.optAttachment("watermarks");
            System.out.println();
            if(attachment.getSize() > 2097152)
                event.getHook().sendMessage("❌ | Your image is over 2mb. Please try again.").queue();
            else if(!attachment.getContentType().equalsIgnoreCase("image/png"))
                event.getHook().sendMessage("❌ | Only PNGs are accepted. Please try again").queue();
            else
                setImage(event.getHook(), event.getUser().getId(), event.optAttachment("watermarks"));
        }
    }

    private String setLink(String userId, String link)
    {
        String fileName = "lyricers/" + userId + "/link.txt";

        try(FileWriter fw = new FileWriter(fileName))
        {
            boolean exists = new File(fileName).exists();
            fw.write(link);

            if(exists)
                return "✅ | Your link has been updated.\n";
            else
                return "✅ | Your link has been saved.\n";
        }

        catch(IOException e)
        {
            LoggerManager.logError(e, "setting link");
            return "❌ | There was an error saving your link.";
        }
    }

    private void setImage(InteractionHook hook, String userId, Message.Attachment watermark)
    {
        String fileName = "lyricers/" + userId + "/watermark.png";
        boolean exists = new File(fileName).exists();

        watermark.getProxy().downloadToFile(new File(fileName))
                 .thenAccept(file -> {
                     if(exists)
                         hook.sendMessage("✅ | Your watermarks has been updated.").queue();
                     else
                         hook.sendMessage("✅ | Your watermarks has been saved.").queue();
                 })
                 .exceptionally(t -> {
                     LoggerManager.logError(new Exception(t), "setting watermarks");
                     hook.sendMessage("❌ | There was an error saving your watermarks.").queue();
                     return null;
                 });
    }

    private static boolean isValidUrl(String url)
    {
        try
        {
            new URL(url).toURI();
            return true;
        }

        catch(Exception e)
        {
            return false;
        }
    }
}

