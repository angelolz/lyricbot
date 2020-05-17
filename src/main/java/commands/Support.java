package commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Support extends Command{
	public Support() {
		this.name = "support";
		this.help = "Gives you a link to my creator's Ko-Fi :)";
		this.guildOnly = true;
		this.cooldown = 3;
	}
	
	@Override
	protected void execute(CommandEvent event) {
		event.reply(":blue_heart: | If you wanna spare a buck or two towards my creator, you could check out their Ko-Fi! :) "
				+ "https://ko-fi.com/angelolz\n\n**Thank you very much for your support!**");
	
	}

}
