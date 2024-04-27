package commands.ownercommands;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import main.LoggerManager;
import net.dv8tion.jda.api.entities.ISnowflake;
import org.apache.commons.io.FileUtils;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

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
		event.getGuild().loadMembers().onSuccess(members -> {
			try
			{
				long startTime = System.currentTimeMillis();

				processRoles(event, members);

				long endTime = System.currentTimeMillis() - startTime;
				event.replySuccess("Finished in " + endTime + "ms.");
			}

			catch(IOException e)
			{
				event.reply(":x: | There was an error in getting list of roles.");
				LoggerManager.logError(e, "checking roles");
			}
		});
	}

	private void processRoles(CommandEvent event, List<Member> members) throws IOException
	{
		//puts each role group into an array
		List<String> specialRoles = FileUtils.readLines(new File("roles/special.txt"), StandardCharsets.UTF_8);
		List<String> videoEditorRoles = FileUtils.readLines(new File("roles/video.txt"), StandardCharsets.UTF_8);
		List<String> eventsRoles = FileUtils.readLines(new File("roles/events.txt"), StandardCharsets.UTF_8);

		//assign the separator roles to be given
		Role specialRoleSeparator = event.getGuild().getRoleById("594557478923010048");
		Role videoEditorRoleSeparator = event.getGuild().getRoleById("665624812390973481");
		Role eventRoleSeparator = event.getGuild().getRoleById("600533152049528835");

		for(Member member : members)
		{
			//gets the roles of a member
			List<Role> roles = member.getRoles();
			List<String> roleIds = roles.stream().map(ISnowflake::getId).toList();

			//boolean for role separators
			boolean hasSpecialRoles = roles.stream().anyMatch(r -> r.getId().equals("594557478923010048"));
			boolean hasVideoEditorRoles = roles.stream().anyMatch(r -> r.getId().equals("665624812390973481"));
			boolean hasEventsRoles = roles.stream().anyMatch(r -> r.getId().equals("600533152049528835"));

			if(!hasSpecialRoles && !Collections.disjoint(roleIds, specialRoles))
				event.getGuild().addRoleToMember(member, specialRoleSeparator).queue();

			if(!hasVideoEditorRoles && !Collections.disjoint(roleIds, videoEditorRoles))
				event.getGuild().addRoleToMember(member, videoEditorRoleSeparator).queue();

			if(!hasEventsRoles && !Collections.disjoint(roleIds, eventsRoles))
				event.getGuild().addRoleToMember(member, eventRoleSeparator).queue();
		}
	}
}
