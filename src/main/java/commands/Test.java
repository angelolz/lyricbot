package commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;

public class Test extends Command
{
	public Test()
	{
		this.name = "rules";
		this.help = "For testing purposes.";
		this.ownerCommand = true;
		this.hidden = true;
		this.guildOnly = true;
	}
	
	@Override
	protected void execute(CommandEvent event)
	{
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle("Rules of the Server");
		embed.setColor(0x2E86C1);
		embed.setThumbnail(event.getGuild().getIconUrl());
		embed.setDescription("By staying in this server, you agree to follow "
				+ "[**Discord's Guidelines**](https://discord.com/guidelines) and "
				+ "[**TOS**](https://discord.com/terms).");
		embed.addField(":one: | Do not spam links/invite links anywhere in the server.",
				"Posting a server link is allowed (in <#560984800895303680>), but spamming it will result in a"
				+ " kick and a ban.", false);
		embed.addField(":two: | Please, no NSFW in the server.", "i will eviscerate all horny people on sight", false);
		embed.addField(":three: | Do not spam bot commands outside of the bot channels.",
				"Light bot usage to make a haha moment in general is fine but if it starts getting excessive, please move.", false);
		embed.addField(":four: | If issues arise, please contact an admin.", "Admins try their best to mitigate issues, so we'd appreciate it"
				+ " if you avoid backseat modding. Thank you.", false);
		embed.addField(":five: | When an admin tells you to stop, just stop.", "If they tell you to stop, there's an"
				+ " 11/10 chance that you're being annoying and you need to calm down.", false);
		embed.addField(":six: | Don't be mean.", "yeah just don't be an asshole and we'll be alright", false);
		embed.addField(":seven: | Do not ask for free lyric videos.", "Lyric videos take a while to make."
				+ " Unfortunately, we cannot do them for free.", false);
		embed.addBlankField(false);
		embed.addField(":zero: | yo sub to me and ivan lol", "it would be sick af if you sub to "
				+ "[**Angelolz**](http://youtube.com/c/angelolz1) and [**Ivan**](https://www.youtube.com/user/TSalvaChannel) :)", false);
		embed.setFooter("Failure to follow any of the above rules will result in a kick or ban.");
		
		EmbedBuilder embed2 = new EmbedBuilder();
		embed2.setTitle("Roles and Other Things");
		embed2.setColor(0xF39C12);
		embed2.setDescription(
				  "<@&560978295320084492> - haha that's me and ivan\n"
				+ "<@&560983352815910913> - People that watch over the server and enforce the rules.\n"
				+ "<@&585536432039264334> - People that have boosted this server.\n"
				+ "<@&740370691328835584> - People that won top 3 in the Lyric Competition #2.\n"
				+ "<@&560995013945851929> - People that have **2.5k subs and 10k views**. Please let me know if you meet these requirements.\n"
				+ "<@&747067845392728134> - People that have donated to the fundraiser for **Ivan's new PC.**\n\n");
		
		embed2.addField("Gaining access to the server:", "To gain access to the server, please <#560991785053192248> and an admin will let you"
				+ " in the server as soon as possible. Thank you!", false);
		embed2.addField("Announcing your video:", "If you want your lyric video posted in <#560986455174938646>, please DM <@189690228292845568> "
				+ "or any other admin, and they'll be happy to promote your video. We only accept finished, full-length lyric videos.", false);
		
		event.reply(embed.build());
		event.reply(embed2.build());
		event.reply("If you want to invite people to this server, feel free to do so! Here's the link:\n"
				+ "https://discord.io/edmlyrics");
		
	}

}
