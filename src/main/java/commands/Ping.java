package commands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import main.CommandTracker;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

public class Ping extends SlashCommand
{
	private static final String BEFORE_MESSAGE = ":ping_pong: | %s, %s! ...";
	private static final String AFTER_MESSAGE = ":ping_pong: | %s, %s! **%d ms**";

	public Ping()
	{
		this.name = "ping";
		this.aliases = new String[]{ "pong" };
		this.help = ":ping_pong: Pong! Check the latency of the Animal Crossing Bot.";
		this.cooldown = 3;
	}

	@Override
	protected void execute(SlashCommandEvent event)
	{
		CommandTracker.incrementSlashCount("ping");

		long time = System.currentTimeMillis();

		event.deferReply().queue();

		event.getHook()
		     .sendMessageFormat(BEFORE_MESSAGE, event.getUser().getAsMention(), "Pong!")
		     .queue(
			     cb -> cb.editMessageFormat(AFTER_MESSAGE,
				     event.getUser().getAsMention(), "Pong!", System.currentTimeMillis() - time).queue()
		     );
	}

	@Override
	protected void execute(CommandEvent event)
	{
		CommandTracker.incrementTextCount("ping");

		MessageChannel channel = event.getChannel();
		long time = System.currentTimeMillis();

		if(event.getMessage().getContentRaw().contains("ping"))
		{
			channel.sendMessageFormat(BEFORE_MESSAGE, event.getAuthor().getAsMention(), "Pong!").queue(
				response -> response.editMessageFormat(AFTER_MESSAGE, event.getAuthor().getAsMention(), "Pong!", System.currentTimeMillis() - time).queue());
		}

		else if(event.getMessage().getContentRaw().contains("pong"))
		{
			channel.sendMessageFormat(BEFORE_MESSAGE, event.getAuthor().getAsMention(), "Ping!").queue(
				response -> response.editMessageFormat(AFTER_MESSAGE, event.getAuthor().getAsMention(), "Ping!", System.currentTimeMillis() - time).queue());
		}
	}
}