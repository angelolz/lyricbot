package commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Close extends Command
{
	public Close()
	{
		this.name = "close";
		this.ownerCommand = true;
		this.hidden = true;
	}

	@Override
	protected void execute(CommandEvent event)
	{
		try
		{
			System.out.println("Shutting down bot.");
			event.reply(":wave: | Goodbye!");
			Thread.sleep(3000);
			event.getJDA().shutdown();
			System.out.println("Bot successfully off.");
		}

		catch (InterruptedException e)
		{
			System.out.println("Something went wrong while shutting down the bot.");
		}
	}

}
