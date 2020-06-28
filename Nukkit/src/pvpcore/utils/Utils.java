package pvpcore.utils;

import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.level.Level;

/**
 * The Utils class. This contains all the useful functions.
 */
public class Utils {


    /**
     * Determines if two levels are equal to each other.
     * @param level1 - The first level.
     * @param level2 - The second level.
     * @return - true if the levels are equal, false otherwise.
     */
    public static boolean levelsEqual(Object level1, Object level2)
    {
        if(level1 == null || level2 == null)
        {
            return false;
        }

        // The final check to determine if two levels are equivalent.
        if(level1 instanceof Level && level2 instanceof Level)
        {
            return ((Level) level1).getId() == ((Level) level2).getId();
        }

        Server server = Server.getInstance();
        Level firstLevel = null;

        if(level1 instanceof String)
        {
            firstLevel = server.getLevelByName((String) level1);
        }
        else if (level1 instanceof Level)
        {
            firstLevel = (Level) level1;
        }

        Level secondLevel = null;
        if(level2 instanceof String)
        {
            secondLevel = server.getLevelByName((String) level2);
        }
        else if (level2 instanceof Level)
        {
            secondLevel = (Level)level2;
        }

        return levelsEqual(firstLevel, secondLevel);
    }

    /**
     * Registers the command to the server.
     * @param command - The command to be registered.
     */
    public static void registerCommand(Command command)
    {
        Server.getInstance().getCommandMap().register(command.getName(), command);
    }


}
