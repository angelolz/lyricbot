package utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class Utils
{
	public static String readURL(String string) throws Exception
	{
		URL url = new URL(string);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.connect();
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream())))
		{
			StringBuilder buffer = new StringBuilder();
			int read;
			char[] chars = new char[1024];
			while ((read = reader.read(chars)) != -1)
				buffer.append(chars, 0, read);
			return buffer.toString();
		}
	}

	public static String trimForEmbedDescription(String desc)
	{
		if(desc.length() > 400)
			return desc.substring(0, 397).concat("...");

		else
			return desc;
	}
}