package listeners;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.gson.Gson;

import jsonObjects.Video;
import lyricbot.LyricBot;
import methods.ReadURL;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegFormat;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Listener extends ListenerAdapter
{
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event)
	{
		if(event.getChannel().getId().equals("560985408150831126") && !event.getAuthor().getId().equals(event.getJDA().getSelfUser().getId()))
		{
			try
			{
				final String PATTERN = "https?:\\/\\/(www\\.)?([-a-zA-Z0-9@:%._\\\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6})\\b([-a-zA-Z0-9()@:%_\\+.~#?&\\/=]*)";
				Pattern p = Pattern.compile(PATTERN, Pattern.CASE_INSENSITIVE);
				Matcher m = p.matcher(event.getMessage().getContentRaw());
				String url = "";

				//if there is an attachment, get the url of it
				if(event.getMessage().getAttachments().size() > 0)
				{
					url = event.getMessage().getAttachments().get(0).getUrl();
				}

				//if there is no attachment, check if there is a url given
				else if(m.find())
				{
					if(m.group(2).contains("streamable.com"))
					{
						Document document = Jsoup.connect(m.group()).get();
						url = document.select("meta[property=og:video]").get(0).attr("content");
					}

					else if(m.group(2).contains("youtu.be") || m.group(2).contains("youtube.com"))
					{
						final String YOUTUBE_PATTERN = "(.*?)(^|\\/|v=)([a-z0-9_-]{11})(.*)?";
						Pattern yt_p = Pattern.compile(YOUTUBE_PATTERN, Pattern.CASE_INSENSITIVE);
						Matcher yt_m = yt_p.matcher(event.getMessage().getContentRaw());

						if(yt_m.find())
						{
							Properties prop = new Properties();
							FileInputStream propFile = new FileInputStream("config.properties");
							prop.load(propFile);
							String apiKey = prop.getProperty("yt_api_key");

							String ytUrl = "https://youtube.googleapis.com/youtube/v3/videos?part=snippet%2CcontentDetails&id=" + yt_m.group(3) + "&key=" + apiKey;

							String json = ReadURL.readURL(ytUrl);
							Gson gson = new Gson();
							Video result = gson.fromJson(json, Video.class);

							final String DURATION_PATTERN = "PT([1-9]+)S";
							Pattern d_p = Pattern.compile(DURATION_PATTERN);
							Matcher d_m = d_p.matcher(result.getVideo().contentDetails.duration);

							if(d_m.find())
							{
								if(Integer.parseInt(d_m.group(1)) < 8)
								{
									event.getMessage().delete().queue();
									event.getChannel().sendMessage(":x: | " + event.getAuthor().getAsMention() + ", your video is shorter than 8 seconds. Please post a longer preview of your video.")
									.delay(10, TimeUnit.SECONDS).flatMap(Message::delete).queue();;
								}
							}
						}
					}

					else
					{
						url = m.group();
					}
				}

				if(!url.isEmpty())
				{
					FFprobe ffprobe;
					if(new File("programs/ffprobe.exe").exists())
					{
						ffprobe = new FFprobe("programs/ffprobe.exe");
					}
					
					else
					{
						ffprobe = new FFprobe("programs/ffmpeg/ffprobe");
					}
					
					FFmpegProbeResult probeResult = ffprobe.probe(url);
					FFmpegFormat format = probeResult.getFormat();

					if(format.format_long_name.equalsIgnoreCase("QuickTime / MOV"))
					{
						if(format.duration < 8.5)
						{
							event.getMessage().delete().queue();
							event.getChannel().sendMessage(":x: | " + event.getAuthor().getAsMention() + ", your video is shorter than 8 seconds. Please post a longer preview of your video.")
							.delay(10, TimeUnit.SECONDS).flatMap(Message::delete).queue();;
						}

						else
						{
							if(format.duration > 3600)
							{
								event.getMessage().delete().queue();
								event.getChannel().sendMessage(":x: | " + event.getAuthor().getAsMention() + ", did you modify your mp4's duration?")
								.delay(10, TimeUnit.SECONDS).flatMap(Message::delete).queue();;
							}
						}
					}
				}
			}

			catch(Exception e)
			{
				if(!e.toString().contains("ffprobe"))
				{
					LyricBot.getLogger().warn(e.toString());
				}
			}
		}
	}
}
