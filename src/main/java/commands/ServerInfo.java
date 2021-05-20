package commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import lyricbot.LyricBot;
import net.dv8tion.jda.api.EmbedBuilder;

public class ServerInfo extends Command
{
	public ServerInfo()
	{
		this.name = "serverinfo";
		this.help = "Displays info about this server.";
		this.guildOnly = true;
		this.cooldown = 3;
	}

	@Override
	protected void execute(CommandEvent event)
	{
		EmbedBuilder info = new EmbedBuilder();
		info.setColor(0x3789cc);
		//Header
		info.setTitle(event.getGuild().getName());
		info.setThumbnail(event.getGuild().getIconUrl());

		//Body
		info.addField("Owner", event.getGuild().getOwner().getUser().getName() + "#" + event.getGuild().getOwner().getUser().getDiscriminator(), true);
		info.addField("Server ID", event.getGuild().getId(), true);
		info.addField("Region", event.getGuild().getRegionRaw(), true);
		info.addField("Nitro Boosters", event.getGuild().getBoostCount() + " boosts at Level " + getBoostLevel(event), true);
		info.addField("Members", Integer.toString(event.getGuild().getMemberCount()), true);
		info.addField("Emojis", Integer.toString(event.getGuild().getEmotes().size()), true);
		info.addField("Categories", Integer.toString(event.getGuild().getCategories().size()), true);
		info.addField("Channels", 
				Integer.toString(event.getGuild().getChannels().size()) + " text / " + 
						Integer.toString(event.getGuild().getVoiceChannels().size()) + " voice", true);
		info.addField("Roles", Integer.toString(event.getGuild().getRoles().size()), true);
		info.addField("Date Created",
				event.getGuild().getTimeCreated().getMonthValue() + "/" + 
						event.getGuild().getTimeCreated().getDayOfMonth() + "/" + 
						event.getGuild().getTimeCreated().getYear() + " " +
						event.getGuild().getTimeCreated().getHour() + ":" +
						event.getGuild().getTimeCreated().getMinute() + ":" +
						event.getGuild().getTimeCreated().getSecond(), true);

		if(event.getGuild().getBannerUrl() == null)
		{
			info.addField("Server Banner", "none", true);
		}

		else
		{
			info.addField("Server Banner", "[Link](" + event.getGuild().getBannerUrl() + ")", true);
		}

		if(event.getGuild().getSplashUrl() == null)
		{
			info.addField("Invite Screen", "none", true);
		}

		else
		{
			info.addField("Invite Screen", "[Link](" + event.getGuild().getSplashUrl() + ")", true);
		}

		//Footer
		info.setFooter("Created by Angelolz#6969 | Version " + LyricBot.getVersion(), event.getJDA().getUserById("189690228292845568").getAvatarUrl());

		event.reply(info.build());
	}

	private String getBoostLevel(CommandEvent event)
	{
		if(event.getGuild().getBoostCount() <= 1)
		{
			return "0";
		}

		else if(event.getGuild().getBoostCount() <= 14)
		{
			return "1";
		}

		else
		{
			return "2";
		}
	}
}
