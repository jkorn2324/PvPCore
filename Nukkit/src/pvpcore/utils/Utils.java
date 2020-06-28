package pvpcore.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.TextFormat;
import pvpcore.PvPCore;
import pvpcore.worlds.PvPCWorld;
import pvpcore.worlds.areas.PvPCArea;

import java.util.HashMap;
import java.util.Map;

/**
 * The Utils class. This contains all the useful functions.
 */
public class Utils {

    /**
     * Gets the maximum value based on a list of values.
     * @param values - The list of number values.
     * @return - Number, the maximum value.
     */
    public static Number getMaxValue(Number...values)
    {
        Number maxNumber = null;
        for(Number num : values)
        {
            if(maxNumber == null || maxNumber.doubleValue() < num.doubleValue())
            {
                maxNumber = num;
            }
        }
        return maxNumber;
    }

    /**
     * Gets the minimum value based on a list of values.
     * @param values - The list of number values.
     * @return - Number, the minimum value.
     */
    public static Number getMinValue(Number...values)
    {
        Number minNumber = null;
        for(Number num : values)
        {
            if(minNumber == null || minNumber.doubleValue() > num.doubleValue())
            {
                minNumber = num;
            }
        }
        return minNumber;
    }

    /**
     * Converts the vector3 to a map object.
     * @param input - The input object.
     * @return - A new hashmap object.
     */
    public static HashMap<String, Double> vec3ToMap(Vector3 input)
    {
        HashMap<String, Double> output = new HashMap<>();
        output.put("x", input.x);
        output.put("y", input.y);
        output.put("z", input.z);
        return output;
    }


    /**
     * Converts a map to a vector3 object.
     * @param map - The input map.
     * @return - Vector3 or null if failed.
     */
    public static Vector3 mapToVec3(Map map)
    {
        if(
                map.containsKey("x")
                && map.containsKey("y")
                && map.containsKey("z")
        )
        {
            Object x = map.get("x");
            Object y = map.get("y");
            Object z = map.get("z");

            if(
                    x instanceof Number
                    && y instanceof Number
                    && z instanceof Number
            )
            {
                return new Vector3(
                        ((Number) x).doubleValue(),
                        ((Number) y).doubleValue(),
                        ((Number) z).doubleValue()
                );
            }
        }

        return null;
    }

    /**
     * Gets the knockback of the player based on level and area.
     * @param player1 - The player being attacked.
     * @param player2 - The player attacking player1.
     * @return PvPCKnockback or Null.
     */
    public static PvPCKnockback getKnockbackFor(Player player1, Player player2)
    {
        PvPCArea area = PvPCore.getAreaHandler().getAreaKnockback(player1, player2);
        if(area != null)
        {
            return area.getKnockback();
        }

        PvPCWorld world = PvPCore.getWorldHandler().getWorldKnockback(player1, player2);
        if(world != null)
        {
            return world.getKnockback();
        }

        return null;
    }


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

    /**
     * Gets the prefix of the server name.
     * @return - The String containing the prefix.
     */
    public static String getPrefix()
    {
        return TextFormat.BOLD.toString() + TextFormat.DARK_GRAY.toString() +
                "[" + TextFormat.BLUE.toString() + "PvPCore" + TextFormat.DARK_GRAY.toString() +
                "]" + TextFormat.RESET.toString();
    }

}
