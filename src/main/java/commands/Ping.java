package commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;

public class Ping extends Command{
	public Ping() {
		this.name = "ping";
		this.help = ":ping_pong: Pong!";
		this.aliases = new String[] {"pong"};
		this.botPermissions = new Permission[] {Permission.MESSAGE_WRITE, Permission.MESSAGE_READ};
		this.guildOnly = true;
		this.cooldown = 3;
	}
	@Override
	protected void execute(CommandEvent event) {
		MessageChannel channel = event.getChannel();
		long time = System.currentTimeMillis();
		if(event.getMessage().getContentRaw().contains("ping")) {
			channel.sendMessage(":ping_pong: | **Pong!** ...") /* => RestAction<Message> */
			.queue(response /* => Message */ -> {
				response.editMessageFormat(":ping_pong: | **Pong!** %d ms", System.currentTimeMillis() - time).queue();
			});
		}else if(event.getMessage().getContentRaw().contains("pong")) {
			channel.sendMessage(":ping_pong: | **Ping!** ...") /* => RestAction<Message> */
			.queue(response /* => Message */ -> {
				response.editMessageFormat(":ping_pong: | **Ping!** %d ms", System.currentTimeMillis() - time).queue();
			});
		}

	}

}