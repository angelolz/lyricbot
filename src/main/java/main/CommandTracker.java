package main;

import java.util.HashMap;
import java.util.Map;

public class CommandTracker
{
    protected static final Map<String, Integer> textTracker = new HashMap<>();
    protected static final Map<String, Integer> slashTracker = new HashMap<>();
    protected static final Map<String, Integer> lookupTracker = new HashMap<>();


    public static String getUsageCounts(String commandName)
    {
        return String.format("%d / %d",
            textTracker.getOrDefault(commandName, 0),
            slashTracker.getOrDefault(commandName, 0)
        );
    }

    public static void incrementTextCount(String commandName)
    {
        int count = textTracker.getOrDefault(commandName, 0) + 1;
        textTracker.put(commandName, count);
    }

    public static void incrementSlashCount(String commandName)
    {
        int count = slashTracker.getOrDefault(commandName, 0) + 1;
        slashTracker.put(commandName, count);
    }
}
