package commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import lyricbot.LyricBot;
import net.dv8tion.jda.api.EmbedBuilder;

public class Help extends Command{
	public Help() {
		this.name = "help";
		this.help = "Shows this help command.";
		this.guildOnly = true;
		this.cooldown = 3;
	}
	
	@Override
	protected void execute(CommandEvent event) {
		EmbedBuilder info = new EmbedBuilder();
		info.setAuthor(event.getSelfUser().getName());
		info.setThumbnail(event.getJDA().getUserById("701038771776520222").getAvatarUrl());
		info.setColor(0x228B22);
		info.setFooter("Created by Angelolz#6969 | Version " + LyricBot.getVersion(), event.getJDA().getUserById(event.getClient().getOwnerId()).getAvatarUrl());
		for(Command commands : event.getClient().getCommands()) {
			if(!commands.isHidden() && !commands.isOwnerCommand()) {
				info.addField(event.getClient().getPrefix() + commands.getName(), commands.getHelp(), true);
			}
		}

		event.reply(info.build());
	}

}
