package utils;

import main.ConfigManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utils
{
    private static final Pattern YOUTUBE_LINK_PATTERN = Pattern.compile(
        "^(?:https?://)?(?:www\\.)?(?:youtube\\.com/(?:watch\\?v=|embed/|v/|.*?v=)|youtu\\.be/)([\\w-]{11})(?:\\S+)?$",
        Pattern.CASE_INSENSITIVE
    );

    public static String readURL(String string, boolean youtube) throws Exception
    {
        URL url = new URL(string);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setConnectTimeout(10000);
        if(!youtube) con.setRequestProperty("authorization", ConfigManager.getUrlMetaApiKey());
        con.connect();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream())))
        {
            StringBuilder buffer = new StringBuilder();
            int read;
            char[] chars = new char[1024];
            while((read = reader.read(chars)) != -1)
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

    public static boolean isValidUrl(String url)
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

    public static boolean isValidLong(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidYouTubeLink(String url) {
        return YOUTUBE_LINK_PATTERN.matcher(url).matches();
    }
}
