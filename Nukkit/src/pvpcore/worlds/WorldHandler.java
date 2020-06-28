package pvpcore.worlds;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Level;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import pvpcore.PvPCore;
import pvpcore.utils.Utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class WorldHandler {

    /** The accessor to the PvPCore. */
    private PvPCore core;
    /** The accessor to the server. */
    private Server server;

    /** The path to the worlds.json file. */
    private File worldsFile;

    /** The ConcurrentMap containing all of the worlds */
    private ConcurrentHashMap<String, PvPCWorld> worlds;

    /**
     * The world handler constructor.
     * @param core - The main plugin core.
     */
    public WorldHandler(PvPCore core)
    {
        this.core = core;
        this.server = core.getServer();

        this.worlds = new ConcurrentHashMap<>();

        this.worldsFile = new File(core.getDataFolder(), "worlds.json");
        this.init();
    }

    /**
     * Initializes all of the worlds.
     */
    private void init() {

        try
        {
            // Creates a new file.
            if(!this.worldsFile.exists())
            {
                this.worldsFile.createNewFile();

                // Reads the file and gets the outputted data.
                File configFile = new File(this.core.getDataFolder(), "config.yml");
                if(!configFile.exists())
                {
                    return;
                }

                String content = cn.nukkit.utils.Utils.readFile(configFile);

                // Dumps contents from yaml file and creates a hash map.
                DumperOptions dumperOptions = new DumperOptions();
                dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

                Yaml yaml = new Yaml(dumperOptions);
                LinkedHashMap dataMap = yaml.loadAs(content, LinkedHashMap.class);

                if(!dataMap.containsKey("levels"))
                {
                    return;
                }

                // Iterates through the levels map & decodes.
                HashMap levelsMap = (HashMap) dataMap.get("levels");
                for(Object levelName : levelsMap.keySet())
                {
                    Object value = levelsMap.get(levelName);
                    if(levelName instanceof String)
                    {
                        PvPCWorld world = PvPCWorld.decodeLegacy((String) levelName, value);
                        if(world != null)
                        {
                            this.worlds.put(world.getLevelName(), world);
                        }
                    }
                }
                return;
            }

            // Reads the JSON file.
            FileReader reader = new FileReader(this.worldsFile);
            JSONParser parser = new JSONParser();

            Object worldsData = parser.parse(reader);
            if(worldsData instanceof JSONObject)
            {
                Iterator iterator = ((JSONObject) worldsData).keySet().iterator();
                while(iterator.hasNext())
                {
                    String worldName = (String)iterator.next();
                    Object worldData = ((JSONObject) worldsData).get(worldName);
                    PvPCWorld world = PvPCWorld.decode(worldName, worldData);
                    if(world != null)
                    {
                        this.worlds.put(world.getLevelName(), world);
                    }
                }
            }
            reader.close();

        } catch (Exception e) {}
    }

    /**
     * Gets the world from the world manager.
     * @param level - The level to get the corresponding information.
     * @return PvPCWorld if successful or null.
     */
    public PvPCWorld getWorld(Object level)
    {
        if(level instanceof Level)
        {
            if(!this.worlds.containsKey(((Level) level).getName()))
            {
                return this.worlds.put(((Level) level).getName(), new PvPCWorld((Level) level));
            }

            PvPCWorld world = this.worlds.get(((Level) level).getName());
            if(!Utils.levelsEqual(world.getLevel(), level))
            {
                world.setLevel((Level) level);
            }
            return world;
        }
        else if (level instanceof String)
        {
            boolean loaded = true;
            if(this.server.isLevelLoaded((String)level))
            {
                loaded = this.server.loadLevel((String)level);
            }

            return loaded ? this.getWorld(this.server.getLevelByName((String)level)) : null;
        }

        return null;
    }

    /**
     * Gets the knockback from the world the players are in.
     * @param player1 - The first player.
     * @param player2 - The second player.
     * @return PvPCWorld or null
     */
    public PvPCWorld getWorldKnockback(Player player1, Player player2)
    {
        for(PvPCWorld world : this.worlds.values())
        {
            if(world.canUseKnockback(player1, player2))
            {
                return world;
            }
        }

        if(Utils.levelsEqual(player1.level, player2.level))
        {
            return this.getWorld(player1.level);
        }

        return null;
    }

    /**
     * Gets all of the worlds in the handler.
     * @return ArrayList of the worlds.
     */
    public ArrayList<PvPCWorld> getWorlds()
    {
        ArrayList<PvPCWorld> worlds = new ArrayList<>();
        Map<Integer, Level> levels = this.server.getLevels();
        for(Level level : levels.values())
        {
            PvPCWorld world = this.getWorld(level);
            if(world != null)
            {
                worlds.add(world);
            }
        }
        return worlds;
    }

    /**
     * Saves the worlds to the file.
     */
    public void save()
    {
        try
        {
            JSONObject object = new JSONObject();
            for(PvPCWorld world : this.worlds.values())
            {
                object.put(world.getLevelName(), world.export());
            }

            FileWriter writer = new FileWriter(this.worldsFile);
            object.writeJSONString(writer);
            writer.close();

        } catch (Exception e)
        {

        }
    }
}
