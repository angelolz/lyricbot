package commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Test extends Command{
	public Test() {
		this.name = "test";
		this.help = "For testing purposes.";
		this.ownerCommand = true;
		this.hidden = true;
		this.guildOnly = true;
	}
	
	@Override
	protected void execute(CommandEvent event) {
		// TODO Auto-generated method stub
		
	}

}
