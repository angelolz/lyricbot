package lyricbot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.Random;
import java.util.Scanner;

import lyricbot.LyricBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Commands extends ListenerAdapter{
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		try {
			String[] args = event.getMessage().getContentRaw().split("\\s+");

			//test command
			if(args[0].equalsIgnoreCase(LyricBot.prefix + "test")) {
				//TODO: test shit here
			}

			//help command
			if(args[0].equalsIgnoreCase(LyricBot.prefix + "help")) {
				EmbedBuilder info = new EmbedBuilder();
				info.setTitle("Lyric Bot", event.getGuild().getIconUrl());
				info.addField(LyricBot.prefix + "hello", "Says hello to you :)", false);
				info.addField(LyricBot.prefix + "help", "Displays this help message", false);
				info.addField(LyricBot.prefix + "ping", "Pong!", false);
				info.addField(LyricBot.prefix + "serverinfo", "Displays info about the server", false);
				info.addField(LyricBot.prefix + "support", "Please support me, thank you <3", false);

				event.getChannel().sendTyping().queue();
				event.getChannel().sendMessage(info.build()).queue();
			}

			//hello command
			if(args[0].equalsIgnoreCase(LyricBot.prefix + "hello")) {
				File helloTxt = new File("src/main/resources/hello.txt");
				Random rng = new Random();
				int lineCount = 0;
				try {
					FileReader fr = new FileReader(helloTxt);
					LineNumberReader lnr = new LineNumberReader(fr);
					lnr.skip(Long.MAX_VALUE);
					String[] lines = new String[lnr.getLineNumber() + 1];

					Scanner scanner = new Scanner(helloTxt);
					while(scanner.hasNextLine()) {
						lines[lineCount] = scanner.nextLine();
						lineCount++;
					}

					lnr.close();
					scanner.close();

					event.getChannel().sendTyping().queue();
					event.getChannel().sendMessage(lines[rng.nextInt(lineCount)]).queue();
				}catch (FileNotFoundException e){
					e.printStackTrace();
				}

			}

			//server info command
			if(args[0].equalsIgnoreCase(LyricBot.prefix + "serverinfo")) {
				EmbedBuilder info = new EmbedBuilder();
				info.setColor(0x3789cc);
				//Header
				info.setTitle(event.getGuild().getName());
				info.setThumbnail(event.getGuild().getIconUrl());

				//Body
				info.addField("Owner", "<:angel:561290194951864341> " + event.getGuild().getOwner().getUser().getName() + "#" + event.getGuild().getOwner().getUser().getDiscriminator(), true);
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
				if(event.getGuild().getBannerUrl() == null) {
					info.addField("Server Banner", "none", true);
				}else {
					info.addField("Server Banner", event.getGuild().getBannerUrl(), true);
				}

				if(event.getGuild().getSplashUrl() == null) {
					info.addField("Invite Screen", "none", true);
				}else {
					info.addField("Invite Screen", event.getGuild().getSplashUrl(), true);
				}

				//Footer
				info.setFooter("Created by Angelolz#6969 | Version " + LyricBot.getVersion(), LyricBot.jda.getUserById("189690228292845568").getAvatarUrl());

				event.getChannel().sendTyping().queue();
				event.getChannel().sendMessage(info.build()).queue();
			}


			if(args[0].equalsIgnoreCase(LyricBot.prefix + "ping")) {
				MessageChannel channel = event.getChannel();
				long time = System.currentTimeMillis();
				channel.sendMessage(":ping_pong: | **Pong!** ...") /* => RestAction<Message> */
				.queue(response /* => Message */ -> {
					response.editMessageFormat(":ping_pong: | **Pong!** %d ms", System.currentTimeMillis() - time).queue();
				});
			}

			//TODO: random lyric video command
			if(args[0].equalsIgnoreCase(LyricBot.prefix + "random")) {
				event.getChannel().sendMessage("This command is still under development!").queue();;
			}

			//support command
			if(args[0].equalsIgnoreCase(LyricBot.prefix + "support")) {
				event.getChannel().sendTyping().queue();
				event.getChannel().sendMessage(":blue_heart: | If you wanna spare a buck or two towards my creator, you could check out their Ko-Fi! :) "
						+ "https://ko-fi.com/angelolz\n\n**Thank you very much for your support!**").queue();
			}

			//shutdown command
			if(args[0].equalsIgnoreCase(LyricBot.prefix + "close")) {
				if(event.getAuthor().getId().equalsIgnoreCase("189690228292845568")) {
					System.out.println("Shutting down bot.");
					event.getChannel().sendMessage(":wave: | Goodbye!").queue();
					event.getJDA().shutdownNow();
					System.out.println("Bot successfully off.");
				}else {
					System.out.println(event.getAuthor().getId());
					event.getChannel().sendMessage(":x: | Only the bot owner can do this!").queue();;
				}
			}
		}
		catch (Exception e){
			System.out.println("An error has occurred.");
			e.printStackTrace();
		}
	}

	//helper methods
	private String getBoostLevel(GuildMessageReceivedEvent event) {
		if(event.getGuild().getBoostCount() <= 1) {
			return "0";
		}else if(event.getGuild().getBoostCount() <= 14) {
			return "1";
		}else {
			return "2";
		}
	}

}