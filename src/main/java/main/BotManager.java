package main;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import commands.*;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import schedulers.NotificationListener;
import schedulers.ScheduledTasks;
import schedulers.SubmissionListener;

public class BotManager
{
    public static void init()
    {
        //create command builders and listeners
        CommandClientBuilder client = new CommandClientBuilder();

        //bot client config
        client.useHelpBuilder(false);
        client.setActivity(Activity.playing("bwao wub wub | lb!help"));
        client.setOwnerId(ConfigManager.getOwnerId());
        client.setEmojis("✅", "⚠️", "❌");
        client.setPrefix(ConfigManager.getPrefix());

        //slash commands
        client.addSlashCommands(
            new LyricerInfo(),
            new Admin(),
            new Request()
        );

        //non-hidden commands
        client.addCommands(
            new Help(),
            new Ping(),
            new Search(),
            new Random(),
            new ServerInfo(),
            new Support()
        );

        //hidden commands
        client.addCommands(
            new CheckRoles(),
            new Close(),
            new Rules());

        // ONLY FOR TESTING
        //        client.forceGuildOnly("695074147071557632");

        JDABuilder.createLight(ConfigManager.getToken())
                  .setStatus(OnlineStatus.DO_NOT_DISTURB)
                  .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
                  .setActivity(Activity.playing("loading!! | " + ConfigManager.getPrefix() + "help"))
                  .addEventListeners(client.build(), new ScheduledTasks(), new SubmissionListener(), new NotificationListener())
                  .build();
    }
}
