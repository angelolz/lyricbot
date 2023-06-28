package commands.admin;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import main.LoggerManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import repo.LyricerRepo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Unban extends SlashCommand
{
    public Unban()
    {
        this.name = "unban";
        this.help = "[ANGEL/DOUGLAS ONLY] Unban a user from requesting a song.";
        this.userPermissions = new Permission[]{ Permission.MANAGE_SERVER };

        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "user", "User to unban", true));

        this.options = options;
    }

    @Override
    protected void execute(SlashCommandEvent event)
    {
        if(!event.getGuild().getId().equals("1114273768660017172") && !event.getGuild().getId().equals("695074147071557632"))
            return;

        try
        {
            event.deferReply().queue();

            User user = event.optUser("user");

            if(!LyricerRepo.doesExist(user.getIdLong()))
                LyricerRepo.addLyricer(user.getIdLong());

            LyricerRepo.updateBanned(user.getIdLong(), false);

            event.getHook().sendMessage("✅ | User is now allowed to make song requests.").queue();
        }

        catch(SQLException e)
        {
            event.getHook().setEphemeral(true).sendMessage("❌ | Unable to unban user.").queue();
            LoggerManager.logError(e, event.getCommandString(), event.getUser().getName(), event.getGuild());
        }
    }
}
