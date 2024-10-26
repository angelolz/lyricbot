package main;

import lombok.Getter;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager
{
    private static final String PREFIX = "lb!";
    private static final String VERSION = "v1.9.2";
    @Getter
    private static String token;
    @Getter
    private static String ownerId;
    @Getter
    private static String youtubeApiKey;
    @Getter
    private static String urlMetaApiKey;
    @Getter
    private static String logChannel;
    @Getter
    private static String exceptionChannel;

    private ConfigManager() {}

    public static void init() throws IOException
    {
        try(FileInputStream propFile = new FileInputStream("config.properties"))
        {
            Properties prop = new Properties();
            prop.load(propFile);
            token = prop.getProperty("bot_token");
            ownerId = prop.getProperty("owner_id");
            youtubeApiKey = prop.getProperty("yt_api_key");
            urlMetaApiKey = prop.getProperty("url_api_key");
            logChannel = prop.getProperty("log_channel_id", "0");
            exceptionChannel = prop.getProperty("exception_channel_id", "0");
        }
    }

    public static String getPrefix() { return PREFIX; }

    public static String getVersion() { return VERSION; }
}
