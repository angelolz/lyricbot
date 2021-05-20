package listeners;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.coremedia.iso.IsoFile;

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
				final String PATTERN = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&\\/=]*)";
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
					url = m.group();
				}

				if(!url.isEmpty())
				{
					System.out.println("url is not empty");
					File file = getFileByUrl(url);

					IsoFile isoFile = new IsoFile(file.getCanonicalPath());
			        long length =
			                isoFile.getMovieBox().getMovieHeaderBox().getDuration() /
			                isoFile.getMovieBox().getMovieHeaderBox().getTimescale();

			        if(length < 5)
			        {
			        	event.getMessage().delete().queue();
			        	event.getChannel().sendMessage(":x: | Your video is less than 5 seconds. Please post a longer preview.").queue();
			        }
				}
				
				else
				{
					System.out.println("no url");
				}

			}
			
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	//helper functions
	private static File getFileByUrl(String url) throws IOException
	{
		File tmpFile = File.createTempFile ("temp", ".mp4"); // Create a temporary file
		toBDFile(url, tmpFile.getCanonicalPath());
		return tmpFile;
	}
	
	private static void toBDFile(String urlStr, String bdUrl) throws IOException, UnknownHostException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        DataInputStream in = new DataInputStream(conn.getInputStream());
        byte[] data = toByteArray(in);
        in.close();
        FileOutputStream out=new FileOutputStream(bdUrl);
        out.write(data);
        out.close();
    }
	
	private static byte[] toByteArray(InputStream in) throws IOException
	{
 
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        byte[] buffer=new byte[1024*4];
        int n=0;
        while ( (n=in.read(buffer)) !=-1) {
            out.write(buffer,0,n);
        }
        return out.toByteArray();
    }

}
