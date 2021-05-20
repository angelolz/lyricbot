package commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Random;
import java.util.Scanner;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Hello extends Command
{
	public Hello()
	{
		this.name = "hello";
		this.help = "Says hello to you!";
		this.guildOnly = true;
		this.cooldown = 3;
	}
	@Override
	protected void execute(CommandEvent event)
	{
		try
		{
			File helloTxt = new File("src/main/resources/hello.txt");
			Random rng = new Random();
			int lineCount = 0;
			FileReader fr = new FileReader(helloTxt);
			LineNumberReader lnr = new LineNumberReader(fr);
			lnr.skip(Long.MAX_VALUE);
			String[] lines = new String[lnr.getLineNumber() + 1];

			Scanner scanner = new Scanner(helloTxt);
			while(scanner.hasNextLine())
			{
				lines[lineCount] = scanner.nextLine();
				lineCount++;
			}

			lnr.close();
			scanner.close();

			event.reply(lines[rng.nextInt(lineCount)]);
		}

		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
