package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager
{
    private static final String PREFIX = "lb!";
    private static final String VERSION = "v1.5.1";
    private static String token;
    private static String ownerId;
    private static String youtubeApiKey;

    public static void init() throws IOException
    {
        try(FileInputStream propFile = new FileInputStream("config.properties"))
        {
            Properties prop = new Properties();
            prop.load(propFile);
            token = prop.getProperty("bot_token");
            ownerId = prop.getProperty("owner_id");
            youtubeApiKey = prop.getProperty("yt_api_key");
        }
    }

    public static String getPrefix()
    {
        return PREFIX;
    }

    public static String getVersion()
    {
        return VERSION;
    }

    public static String getToken()
    {
        return token;
    }

    public static String getOwnerId()
    {
        return ownerId;
    }

    public static String getYoutubeApiKey() { return youtubeApiKey; }
}
