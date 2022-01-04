package commands;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Hello extends Command
{
	public Hello()
	{
		this.name = "hello";
		this.help = "Says hello to you!";
		this.cooldown = 2;
	}
	@Override
	protected void execute(CommandEvent event)
	{
		try
		{
			Random rng = new Random();
			ArrayList<String> lines = new ArrayList<>();

			BufferedReader br = new BufferedReader(new FileReader("hello.txt"));
			String line = br.readLine();
			while (line != null)
			{
				lines.add(line);
				line = br.readLine();
			}

			event.reply(lines.get(rng.nextInt(lines.size())));
			br.close();
		}

		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
