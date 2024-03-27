package commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.ConfigManager;
import net.dv8tion.jda.api.EmbedBuilder;

public class ServerInfo extends Command
{
    public ServerInfo()
    {
        this.name = "serverinfo";
        this.help = "Displays info about this server.";
        this.cooldown = 3;
    }

    @Override
    protected void execute(CommandEvent event)
    {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(0x3789cc);

        //Header
        embed.setTitle(event.getGuild().getName());
        embed.setThumbnail(event.getGuild().getIconUrl());

        //Body
        String ownerName = event.getGuild().retrieveOwner().complete().getUser().getName();
        embed.addField("Owner", ownerName, true);
        embed.addField("Server ID", event.getGuild().getId(), true);
        embed.addBlankField(true);

        embed.addField("Nitro Boosters", event.getGuild().getBoostCount() + " boosts at Level " + getBoostLevel(event), true);
        embed.addField("Members", Integer.toString(event.getGuild().getMemberCount()), true);
        embed.addField("Emojis", Integer.toString(event.getGuild().getEmojis().size()), true);

        embed.addField("Categories", Integer.toString(event.getGuild().getCategories().size()), true);
        embed.addField("Channels",
            event.getGuild().getChannels().size() + " text / " +
                event.getGuild().getVoiceChannels().size() + " voice", true);
        embed.addField("Roles", Integer.toString(event.getGuild().getRoles().size()), true);

        embed.addField("Date Created",
            String.format("%d/%d/%d %d:%d:%d",
                event.getGuild().getTimeCreated().getMonthValue(),
                event.getGuild().getTimeCreated().getDayOfMonth(),
                event.getGuild().getTimeCreated().getYear(),
                event.getGuild().getTimeCreated().getHour(),
                event.getGuild().getTimeCreated().getMinute(),
                event.getGuild().getTimeCreated().getSecond()),
            true);
        embed.addField("Server Banner", event.getGuild().getBannerUrl() == null ? "none" : "[Link](" + event.getGuild().getBannerUrl() + ")", true);
        embed.addField("Invite Screen", event.getGuild().getSplashUrl() == null ? "none" : "[Link](" + event.getGuild().getSplashUrl() + ")", true);

        //Footer
        String avatarUrl = event.getJDA().retrieveUserById("189690228292845568").complete().getAvatarUrl();
        embed.setFooter("Created by Angelolz#6969 | Version " + ConfigManager.getVersion(), avatarUrl);

        event.reply(embed.build());
    }

    private String getBoostLevel(CommandEvent event)
    {
        if(event.getGuild().getBoostCount() < 2) return "0";
        else if(event.getGuild().getBoostCount() < 7) return "1";
        else if(event.getGuild().getBoostCount() < 14) return "2";
        else return "3";
    }
}
