package lyricbot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.security.auth.login.LoginException;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import commands.*;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.exceptions.RateLimitedException;

public class LyricBot {
	public static JDA jda;
	public static String prefix = "lb!";
	static String version = "v1.1.2";
	
	//main method
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException, LoginException, IllegalArgumentException, RateLimitedException{
		List<String> list = Files.readAllLines(Paths.get("config.txt"));
		String token = list.get(0);
		String ownerId = list.get(1);
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
				new ServerInfo(),
				new Support(),
				new Round());
		
		//hidden commands
		client.addCommands(
				new Shutdown(),
				new Test());
		
		try {
			new JDABuilder(AccountType.BOT)
				.setToken(token)
				.setStatus(OnlineStatus.DO_NOT_DISTURB)
				.setActivity(Activity.playing("loading.."))
				.addEventListeners(waiter, client.build())
				.build();
		}catch(LoginException e){
			System.out.println("Unable to login with bot token.");
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getVersion() {
		return version;
	}

}
