package lyricbot;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import commands.*;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class LyricBot
{
	private static Logger logger;
	public static final String prefix = "lb!";
	static final String version = "v1.4.7";

	//main method
	public static void main(String[] args) throws IOException, IllegalArgumentException
	{
		//logger
		logger = LoggerFactory.getLogger(LyricBot.class);

		Properties prop = new Properties();
		FileInputStream propFile = new FileInputStream("config.properties");
		prop.load(propFile);
		String token = prop.getProperty("bot_token");
		String ownerId = prop.getProperty("owner_id");
		EventWaiter waiter = new EventWaiter();
		CommandClientBuilder client = new CommandClientBuilder();

		client.useHelpBuilder(false);
		client.setActivity(Activity.playing("bwao wub wub | lb!help"));
		client.setOwnerId(ownerId);
		client.setEmojis("\uD83D\uDE03", "\uD83D\uDE2E", "\uD83D\uDE26");
		client.setPrefix(prefix);

		//non-hidden commands
		client.addCommands(
				new Hello(),
				new Help(),
				new Ping(),
				new Search(),
				new Random(),
				new ServerInfo(),
				new Support());

		//hidden commands
		client.addCommands(
				new CheckRoles(),
				new Close(),
				new Rules());

		try
		{
			JDABuilder.createDefault(token)
			.enableIntents(GatewayIntent.GUILD_MEMBERS)
			.setStatus(OnlineStatus.DO_NOT_DISTURB)
			.setActivity(Activity.playing("loading.."))
			.addEventListeners(waiter, client.build())
			.build();
		}

		catch(LoginException e)
		{
			System.out.println("Unable to login with bot token.");
			e.printStackTrace();
		}

		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static String getVersion()
	{
		return version;
	}

	public static String getPrefix()
	{
		return prefix;
	}
	
	public static Logger getLogger() 
	{
		return logger;
	}
}
