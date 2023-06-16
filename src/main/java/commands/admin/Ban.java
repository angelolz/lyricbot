package commands.admin;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import main.LoggerManager;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import repo.LyricerRepo;
import repo.RequestRepo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Ban extends SlashCommand
{
    public Ban()
    {
        this.name = "ban";
        this.help = "[ANGEL/DOUGLAS ONLY] Ban a user from requesting a song.";

        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "user", "User to ban", true));

        this.options = options;
    }

    @Override
    protected void execute(SlashCommandEvent event)
    {
        if(!event.getGuild().getId().equals("1114273768660017172") && !event.getGuild().getId().equals("695074147071557632"))
            return;

        if(!event.getUser().getId().equals("189690228292845568") && !event.getUser().getId().equals("465271897978961921"))
            return;

        try
        {
            event.deferReply().queue();

            User user = event.optUser("user");

            if(!LyricerRepo.doesExist(user.getIdLong()))
                LyricerRepo.addLyricer(user.getIdLong());

            LyricerRepo.updateBanned(user.getIdLong(), true);
            RequestRepo.deleteRequest(user.getIdLong());

            event.getHook().sendMessage("✅ | User can no longer make song requests.").queue();
        }

        catch(SQLException e)
        {
            event.getHook().setEphemeral(true).sendMessage("❌ | Unable to ban user.").queue();
            LoggerManager.logError(e, event.getCommandString(), event.getUser().getName(), event.getGuild());
        }
    }
}
