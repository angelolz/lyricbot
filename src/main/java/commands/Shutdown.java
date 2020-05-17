package commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Shutdown extends Command{
	public Shutdown() {
		this.name = "shutdown";
		this.help = "Shuts down the bot.";
		this.guildOnly = true;
		this.cooldown = 3;
		this.hidden = true;
	}
	@Override
	protected void execute(CommandEvent event) {
		System.out.println("Shutting down bot.");
		event.getChannel().sendMessage(":wave: | Goodbye!").queue();
		event.getJDA().shutdown();
		System.out.println("Bot successfully off.");
	}

}
