package commands;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.google.gson.Gson;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import jsonObjects.*;
import methods.ReadURL;
import net.dv8tion.jda.api.EmbedBuilder;

public class Random extends Command
{
	public Random()
	{
		this.name = "random";
		this.help = "Gives you a random lyric video.";
		this.cooldown = 5;
	}

	@Override
	protected void execute(CommandEvent event)
	{
		try
		{
			Properties prop = new Properties();
			FileInputStream propFile = new FileInputStream("config.properties");
			prop.load(propFile);
			String apiKey = prop.getProperty("yt_api_key");
			String url = String.format("https://youtube.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=50&pageToken=&playlistId=PLllRHMFuhhnicr4P7nSF-1vetLmRgPPrI&key=%s",
					apiKey);

			String json = ReadURL.readURL(url);
			Gson gson = new Gson();
			PlaylistResults result = gson.fromJson(json, PlaylistResults.class);
			List<jsonObjects.PlaylistResults.VideoInfo> videos = result.items;

			while(!result.nextPageToken.isEmpty())
			{
				String nextUrl = String.format("https://youtube.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=50&pageToken=%s&playlistId=PLllRHMFuhhnicr4P7nSF-1vetLmRgPPrI&key=%s",
						result.nextPageToken, apiKey);
				String nextJson = ReadURL.readURL(nextUrl);
				PlaylistResults nextPageResult = gson.fromJson(nextJson, PlaylistResults.class);
				for(jsonObjects.PlaylistResults.VideoInfo video: nextPageResult.items)
				{
					videos.add(video);
				}

				result.setNextPage(nextPageResult.nextPageToken);
			}

			java.util.Random rng = new java.util.Random();
			int index = rng.nextInt(videos.size());

			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			SimpleDateFormat format2 = new SimpleDateFormat("MMMM dd, yyyy hh:mm:ss a");
			Date date = format1.parse(result.getVideo(index).snippet.publishedAt);
			String convertedDate = format2.format(date);
			String thumbnailString = "";

			if(!result.getThumbnails(index).defaultThumb.url.isEmpty())
			{
				thumbnailString += String.format("[default](%s)\n", result.getThumbnails(index).defaultThumb.url);
			}

			if(!result.getThumbnails(index).standard.url.isEmpty())
			{
				thumbnailString += String.format("[standard](%s)\n", result.getThumbnails(index).standard.url);
			}

			if(!result.getThumbnails(index).medium.url.isEmpty())
			{
				thumbnailString += String.format("[medium](%s)\n", result.getThumbnails(index).medium.url);
			}

			if(!result.getThumbnails(index).high.url.isEmpty())
			{
				thumbnailString += String.format("[high](%s)\n", result.getThumbnails(index).high.url);
			}

			if(!result.getThumbnails(index).maxres.url.isEmpty())
			{
				thumbnailString += String.format("[maxres](%s)\n", result.getThumbnails(index).maxres.url);
			}

			EmbedBuilder embed = new EmbedBuilder();
			embed.setColor(0xc4302b);
			embed.addField("Title", result.getVideo(index).snippet.title, false);
			embed.addField("Description", result.getVideo(index).snippet.description, false);
			embed.addField("Posted on", convertedDate, true);
			embed.addField("Thumbnail(s)", thumbnailString.isEmpty() ? "*None*" : thumbnailString, true);
			embed.setThumbnail(result.getThumbnails(index).defaultThumb.url);
			embed.setFooter("This video was randomly chosen from the \"Lyrics I hate\" playlist.");

			String message = String.format("Here's a random lyric video:\n"
					+ "**https://www.youtube.com/watch?v=%s**", videos.get(index).snippet.resourceId.videoId);

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
			{
				event.reply(":x: | Sorry, we've reached the quota limit for searching videos today. Please try again tomorrow.");
			}

			else
			{
				event.reply(":x: | There was an error when trying to get a random lyric video.");
				e.printStackTrace();
			}
		}
	}
}
