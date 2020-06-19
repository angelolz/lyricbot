package commands;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import lyricbot.LyricBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

public class Round extends Command{
	public Round() {
		this.name = "round";
		this.help = "Shows you the current round information.";
		this.cooldown = 5;
	}
	@Override
	protected void execute(CommandEvent event) {
		String[] args = event.getArgs().split("\\s+");
		
		try {
			if(args[0].equalsIgnoreCase("")) {
				Message message = event.getChannel().sendMessage(":x: | " + event.getAuthor().getAsMention() + ", "
						+ "please tell me what round you would like info on. Ex: ``" + LyricBot.prefix + "round 2``").complete();
				message.delete().queueAfter(5, TimeUnit.SECONDS);
			}else if(Integer.parseInt(args[0]) > 4 || Integer.parseInt(args[0]) < 1){
				Message message = event.getChannel().sendMessage(":x: | " + event.getAuthor().getAsMention() + ", that's not a valid round!").complete();
				message.delete().queueAfter(5, TimeUnit.SECONDS);
			}else {
				List<String> list = Files.readAllLines(Paths.get("rounds/" + args[0] + ".txt"));
				EmbedBuilder embed = new EmbedBuilder();
				
				String genre = list.get(0);
				String song = list.get(1);
				long roundEnd = Long.parseLong(list.get(2));
				long sysTime = System.currentTimeMillis();
				long timeLeft = roundEnd - sysTime;
				String dateEnd = list.get(3);
				
				if(timeLeft < 0) {
					timeLeft = 0;
				}
				
				embed.setTitle("Round " + args[0] + " Information");
				embed.setColor(0x5892FD);
				embed.addField("Round", args[0], true);
				embed.addField("Genre", genre, true);
				embed.addField("Song", song, true);
				
				if(roundEnd == 0) {
					embed.setColor(Color.BLACK);
					embed.addField("Time left for the round", "TBA", false);
				}else if(timeLeft <= 0) {
					embed.setColor(Color.RED);
					embed.addField("Time left for the round", "Time is up!", false);
				}else {
					embed.addField("Time left for the round", convertTime(timeLeft), false);
				}
				
				embed.addField("Round End Date & Time", dateEnd, false);
				event.reply(embed.build());
			}
		}catch(NumberFormatException e) {
			Message message = event.getChannel().sendMessage(":x: | " + event.getAuthor().getAsMention() + ", that's not a valid round!").complete();
			message.delete().queueAfter(10, TimeUnit.SECONDS);
		}catch(IOException e) {
			Message message = event.getChannel().sendMessage(":x: | Fuck, something went wrong lol; if this problem persists, please @Angelolz. Thank you :)").complete();
			message.delete().queueAfter(10, TimeUnit.SECONDS);
		}catch(Exception e) {
			Message message = event.getChannel().sendMessage(":x: | Unhandled exception, please @Angelolz so he can look at it. Thank you!").complete();
			message.delete().queueAfter(10, TimeUnit.SECONDS);
			System.out.println(e.toString());
		}
	}
	
	private String convertTime(Long ms) {
		int days, hours, minutes, seconds;
		String result = "";

		seconds = (int) ((ms / 1000) % 60);
		minutes = (int) ((ms / (1000*60)) % 60);
		hours = (int) ((ms /(1000*60*60)) % 24);
		days = (int) ((ms /(1000*60*60*24)));
		
		result = days + " days, " + hours + " hours, " + minutes + " minutes, and " + seconds + " seconds";
		return result;
	}

}
