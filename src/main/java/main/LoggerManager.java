package main;

import enums.LogLevel;
import lombok.Cleanup;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.FileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;

public class LoggerManager
{
    private static Logger logger;
    private static JDA jda;


    public static void init()
    {
        logger = LoggerFactory.getLogger(LyricBot.class);
    }

    public static void setJda(JDA jda)
    {
        LoggerManager.jda = jda;
    }

    public static void logError(Exception e, String event)
    {
        logError(e, event, "", null);
    }

    public static void logError(Exception e, String commandSent, String userTag, Guild guild)
    {
        EmbedBuilder embed = new EmbedBuilder()
            .setColor(Color.red)
            .setTitle(e.getClass().getName() + " caught!")
            .addField("Command sent:", commandSent, false)
            .setDescription("```" + e.getMessage() + "```");

        if(!userTag.isEmpty() || guild != null)
            embed.setFooter("Command sent by " + userTag + " in " + guild.getName() + " (" + guild.getId() + ")");

        StringWriter sw = new StringWriter();
        @Cleanup PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);

        FileUpload fu = FileUpload.fromData(sw.toString().getBytes(), "stacktrace.txt");

        TextChannel exceptionChannel = jda.getTextChannelById(ConfigManager.getExceptionChannel());
        if(exceptionChannel != null)
        {
            exceptionChannel.sendMessageEmbeds(embed.build()).addFiles(fu).queue();
            logger.error("{}: {}", e.getClass().getName(), e.getMessage());
        }
    }

    public static void sendLogMessage(LogLevel level, String message)
    {
        if(jda != null)
        {
            TextChannel logChannel = jda.getTextChannelById(ConfigManager.getLogChannel());
            if(logChannel != null)
                logChannel.sendMessageFormat("```%n[%s] %s%n```", level.name().toUpperCase(), message).queue();
        }

        switch(level)
        {
            case INFO -> logger.info(message);
            case WARN -> logger.warn(message);
            case ERROR -> logger.error(message);
            case DEBUG -> logger.debug(message);
        }
    }
}
