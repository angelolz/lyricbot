package commands;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import lyricbot.LyricBot;
import org.apache.commons.io.FileUtils;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public class CheckRoles extends Command
{
	public CheckRoles()
	{
		this.name = "checkroles";
		this.aliases = new String[] {"cr"};
		this.ownerCommand = true;
		this.hidden = true;
	}

	@Override
	protected void execute(CommandEvent event) 
	{
		//gets all members of the server
		event.getGuild().loadMembers().onSuccess((members) -> {
			try
			{
				long startTime = System.currentTimeMillis();

				//gets guild
				Guild guild = event.getGuild();

				//puts each role group into an array
				List<String> specialRoles = FileUtils.readLines(new File("roles/special.txt"), StandardCharsets.UTF_8);
				List<String> videoEditorRoles = FileUtils.readLines(new File("roles/video.txt"), StandardCharsets.UTF_8);
				List<String> eventsRoles = FileUtils.readLines(new File("roles/events.txt"), StandardCharsets.UTF_8);

				//assign the separator roles to be given
				Role specialSeparator = event.getGuild().getRoleById("594557478923010048");
				Role videoSeparator = event.getGuild().getRoleById("665624812390973481");
				Role eventSeparator = event.getGuild().getRoleById("600533152049528835");

				//iterates through each member of the server
				for(Member member : members)
				{
					//gets the roles of a member
					List<Role> roles = member.getRoles();

					//boolean for role separators
					boolean hasSpecial = false, hasVideo = false, hasEvents = false;


					//for each role that the member has
					for(Role role : roles)
					{
						//checks if they have any of the role separators
						if(role.getId().equals("594557478923010048"))
							hasSpecial = true;

						else if(role.getId().equals("665624812390973481"))
							hasVideo = true;

						else if(role.getId().equals("600533152049528835"))
							hasEvents = true;

						if(!hasSpecial)
						{
							for(String specialRole : specialRoles)
							{
								if(role.getId().equals(specialRole))
								{
									guild.addRoleToMember(member, specialSeparator).queue();
									hasSpecial = true;
								}
							}
						}

						if(!hasVideo)
						{
							for(String videoRole : videoEditorRoles)
							{
								if(role.getId().equals(videoRole))
								{
									guild.addRoleToMember(member, videoSeparator).queue();
									hasVideo = true;
								}
							}
						}

						if(!hasEvents)
						{
							for(String eventRole : eventsRoles)
							{
								if(role.getId().equals(eventRole))
								{
									guild.addRoleToMember(member, eventSeparator).queue();
									hasEvents = true;
								}
							}
						}
					}
				}

				long endTime = System.currentTimeMillis() - startTime;
				event.reply(":white_check_mark: | Finished in " + convertTime(endTime));
			}

			catch(IOException e)
			{
				event.reply(":x: | There was an error in getting list of roles.");
				LyricBot.getLogger().error("Error: {}", e.getMessage());
			}
		});
	}

	private String convertTime(long ms)
	{
		int minutes, seconds;
		String result;

		seconds = (int) ((ms / 1000) % 60);
		minutes = (int) ((ms / (1000*60)));

		result = minutes + " minutes, and " + seconds + " seconds.";
		return result;
	}
}
