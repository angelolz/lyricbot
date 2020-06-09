package commands;

import java.awt.Color;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;

public class Round extends Command{
	public Round() {
		this.name = "round";
		this.help = "Shows you the current round information.";
		this.cooldown = 5;
	}
	@Override
	protected void execute(CommandEvent event) {
		long sysTime = System.currentTimeMillis();
		long timeEnd = 1592524800000L;
		long timeLeft = timeEnd - sysTime;
		
		if(timeLeft < 0) {
			timeLeft = 0;
		}
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle("Current Round Information");
		embed.setColor(0x5892FD);
		embed.addField("Round", "2", true);
		embed.addField("Genre", "Jazzstep", true);
		embed.addField("Song", "Haywyre - Insight", true);
		
		if(timeLeft <= 0) {
			embed.setColor(Color.RED);
			embed.addField("Time left for the round", "Time is up!", false);
		}else {
			embed.addField("Time left for the round", convertTime(timeLeft), false);
		}
		
		event.reply(embed.build());
		
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
