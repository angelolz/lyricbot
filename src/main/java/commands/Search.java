package commands;

import java.io.FileNotFoundException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.Gson;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import dataobjects.*;
import main.ConfigManager;
import utils.Utils;
import net.dv8tion.jda.api.EmbedBuilder;

public class Search extends Command
{
    public Search()
    {
        this.name = "search";
        this.help = "Searches a youtube video.";
        this.cooldown = 5;
    }

    @Override
    protected void execute(CommandEvent event)
    {
        try
        {
            String q = URLEncoder.encode(event.getArgs(), StandardCharsets.UTF_8);
            String url = String.format("https://youtube.googleapis.com/youtube/v3/search?part=snippet&maxResults=1&q=%s&type=video&videoDimension=any&key=%s",
                q, ConfigManager.getYoutubeApiKey());

            String json = Utils.readURL(url, true);
            Gson gson = new Gson();
            SearchResults result = gson.fromJson(json, SearchResults.class);

            if(result.getPageInfo().getTotalResults() == 0)
                event.reply(":x: | Sorry, there weren't any videos that matched your search.");

            else
            {
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                SimpleDateFormat format2 = new SimpleDateFormat("MMMM dd, yyyy hh:mm:ss a");
                Date date = format1.parse(result.getVideo().getSnippet().getPublishedAt());
                String convertedDate = format2.format(date);
                String thumbnailString = "";

                if(!result.getThumbnails().getDefaultThumb().getUrl().isEmpty())
                    thumbnailString += String.format("[default](%s)\n", result.getThumbnails().getDefaultThumb().getUrl());

                if(!result.getThumbnails().getMedium().getUrl().isEmpty())
                    thumbnailString += String.format("[medium](%s)\n", result.getThumbnails().getMedium().getUrl());

                if(!result.getThumbnails().getHigh().getUrl().isEmpty())
                    thumbnailString += String.format("[high](%s)\n", result.getThumbnails().getHigh().getUrl());

                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(0xc4302b);
                embed.addField("Title", result.getVideo().getSnippet().getTitle(), false);
                embed.addField("Description", Utils.trimForEmbedDescription(result.getVideo().getSnippet().getDescription()), false);
                embed.addField("Posted on", convertedDate, true);
                embed.addField("Thumbnail(s)", thumbnailString.isEmpty() ? "*None*" : thumbnailString, true);
                embed.setThumbnail(result.getThumbnails().getDefaultThumb().getUrl());

                String message = String.format("Here's the video you requested:\n"
                    + "**https://www.youtube.com/watch?v=%s**", result.getVideo().getId().getVideoId());

                event.reply(message);
                event.reply(embed.build());
            }

        }

        catch(FileNotFoundException e)
        {
            event.reply(":x: | Couldn't find YouTube API Key. Please let Angel know.");
        }

        catch(Exception e)
        {
            if(e.toString().contains("403"))
                event.reply(":x: | Sorry, we've reached the quota limit for searching videos today. Please try again tomorrow.");

            else
            {
                event.reply(":x: | There was an error when trying to search for a YouTube video.");
                e.printStackTrace();
            }
        }
    }
}
