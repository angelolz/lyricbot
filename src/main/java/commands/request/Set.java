package commands.request;

import com.google.gson.Gson;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import dataobjects.Metadata;
import dataobjects.Request;
import enums.LogLevel;
import listeners.ReadyListener;
import main.LoggerManager;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import repo.LyricerRepo;
import repo.RequestRepo;
import utils.Statics;
import utils.Utils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Set extends SlashCommand
{
    public Set()
    {
        this.name = "set";
        this.help = "Give a song request.";

        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING, "link", "Link to your song request.", true));

        this.options = options;
    }

    @Override
    protected void execute(SlashCommandEvent event)
    {
        if(event.getGuild() == null && !event.getGuild().getId().equals(Statics.ONEHR_SERVER_ID) && !event.getGuild().getId().equals(Statics.DEV_SERVER_ID))
            return;

        event.deferReply().queue();

        if(event.optString("link").length() > 512)
        {
            event.getHook().sendMessage("❌ | Your link is too long. Please shorten it.").queue();
            return;
        }

        Request request = new Request()
            .setUserId(event.getUser().getIdLong());

        try
        {
            if(!ReadyListener.isRequestsOpen())
            {
                event.getHook().sendMessage("❌ | Song requests are closed.").queue();
                return;
            }

            if(!LyricerRepo.doesExist(event.getUser().getIdLong()))
                LyricerRepo.addLyricer(event.getUser().getIdLong());

            if(LyricerRepo.getLyricer(event.getUser().getIdLong()).isBanned())
            {
                event.getHook().sendMessage("❌ | You are not allowed to request a song.").queue();
                return;
            }

            if(!Utils.isValidUrl(event.optString("link")))
            {
                event.getHook().sendMessage("❌ | That's not a valid link.").queue();
                return;
            }

            request.setLink(event.optString("link"))
                   .setTitle(getTitle(event.optString("link")));

            if(RequestRepo.hasRequest(request.getUserId()))
            {
                RequestRepo.updateRequest(request);
                event.getHook().sendMessageFormat("✅ | Your request has been **updated**.\n%s", request.getLink()).queue();
            }

            else
            {
                RequestRepo.addRequest(request);

                event.getHook().sendMessageFormat("✅ | Your request has been **added**.\n%s", request.getLink()).queue();
            }
        }

        catch(Exception e)
        {
            event.getHook().sendMessage("❌ | Unable to add your request.").queue();
            LoggerManager.logError(e, event.getCommandString(), event.getUser().getName(), event.getGuild());
        }
    }

    private String getTitle(String url)
    {
        try
        {
            String json = Utils.readURL(String.format("https://api.urlmeta.org/meta?url=%s", URLEncoder.encode(url, StandardCharsets.UTF_8)), false);
            Metadata metadata = new Gson().fromJson(json, Metadata.class);
            String title = metadata.getMeta().getTitle();
            if(title.length() > 256)
                return metadata.getMeta().getTitle().substring(0, 253) + "...";
            else
                return metadata.getMeta().getTitle();
        }

        catch(Exception e)
        {
            LoggerManager.sendLogMessage(LogLevel.WARN, String.format("Unable to get title for URL %s: %s", url, e));
            return url;
        }
    }
}
