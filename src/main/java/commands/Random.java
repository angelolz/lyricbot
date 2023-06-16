package commands;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import dataobjects.*;
import main.ConfigManager;
import utils.Utils;
import net.dv8tion.jda.api.EmbedBuilder;

public class Random extends Command
{
    private final java.util.Random rng;

    public Random()
    {
        this.name = "random";
        this.help = "Gives you a random lyric video.";
        this.cooldown = 5;
        this.rng = new java.util.Random();
    }

    @Override
    protected void execute(CommandEvent event)
    {
        try
        {
            String url = String.format("https://youtube.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=100&pageToken=&playlistId=PLIMIDL2lIgpA4VzVBrDvfIGsj9kaP22cO&key=%s",
                ConfigManager.getYoutubeApiKey());

            String json = Utils.readURL(url, true);
            Gson gson = new Gson();
            PlaylistResults result = gson.fromJson(json, PlaylistResults.class);
            List<dataobjects.PlaylistResults.VideoInfo> videos = result.getItems();

            while(!result.getNextPageToken().isEmpty())
            {
                String nextUrl = String.format("https://youtube.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=100&pageToken=%s&playlistId=PLIMIDL2lIgpA4VzVBrDvfIGsj9kaP22cO&key=%s",
                    result.getNextPageToken(), ConfigManager.getYoutubeApiKey());
                String nextJson = Utils.readURL(nextUrl, true);
                PlaylistResults nextPageResult = gson.fromJson(nextJson, PlaylistResults.class);
                videos.addAll(nextPageResult.getItems());

                result.setNextPage(nextPageResult.getNextPageToken());
            }

            int index = rng.nextInt(videos.size());

            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            SimpleDateFormat format2 = new SimpleDateFormat("MMMM dd, yyyy hh:mm:ss a");
            Date date = format1.parse(result.getVideo(index).getSnippet().getPublishedAt());
            String convertedDate = format2.format(date);

            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(0xc4302b);
            embed.addField("Title", result.getVideo(index).getSnippet().getTitle(), false);
            embed.addField("Description", Utils.trimForEmbedDescription(result.getVideo(index).getSnippet().getDescription()), false);
            embed.addField("Posted on", convertedDate, true);
            embed.setFooter("This video was randomly chosen from a playlist made by specific curators.");

            String message = String.format("Here's a random lyric video:\n"
                + "**https://youtu.be/%s**", videos.get(index).getSnippet().getResourceId().getVideoId());

            event.reply(message);
            event.reply(embed.build());
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
                event.reply(":x: | There was an error when trying to get a random lyric video.");
                e.printStackTrace();
            }
        }
    }
}
