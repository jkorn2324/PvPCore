package pvpcore.worlds.areas;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.TextFormat;
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

public class AreaHandler {

    /**
     * The PvPCore class accessor.
     */
    private PvPCore core;
    /**
     * The Server accessor.
     */
    private Server server;

    /**
     * The areas json file.
     */
    private File areaFile;
    /**
     * The areas concurrent hash map.
     */
    private ConcurrentHashMap<String, PvPCArea> areas;

    /**
     * The AreaHandler constructor.
     *
     * @param core - The PvPCore class.
     */
    public AreaHandler(PvPCore core) {
        this.core = core;
        this.server = core.getServer();

        this.areas = new ConcurrentHashMap<>();

        this.areaFile = new File(core.getDataFolder(), "areas.json");
        this.init();
    }

    /**
     * Initializes the areas to the ConcurrentHashMap.
     */
    private void init() {
        try {
            if (!this.areaFile.exists()) {
                this.areaFile.createNewFile();

                File configFile = new File(this.core.getDataFolder(), "config.yml");
                if (!configFile.exists()) {
                    return;
                }

                // Reads the content from the Yaml.
                String content = cn.nukkit.utils.Utils.readFile(configFile);

                DumperOptions options = new DumperOptions();
                options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

                Yaml yaml = new Yaml(options);
                LinkedHashMap dataMap = yaml.loadAs(content, LinkedHashMap.class);

                if (!dataMap.containsKey("areas")) {
                    return;
                }

                HashMap areasMap = (HashMap) dataMap.get("areas");
                for (Object areaName : areasMap.keySet()) {
                    Object value = areasMap.get(areaName);
                    if (areaName instanceof String) {
                        PvPCArea area = PvPCArea.decodeLegacy((String) areaName, value);
                        if (area != null) {
                            this.areas.put(area.getName(), area);
                        }
                    }
                }
                return;
            }

            FileReader reader = new FileReader(this.areaFile);
            JSONParser parser = new JSONParser();

            // Decodes the areas data from the JSON file and stores them in the concurrent hashmap.
            Object areasData = parser.parse(reader);
            if (areasData instanceof JSONObject) {

                Set set = ((JSONObject) areasData).keySet();
                for(Object key : set)
                {
                    String areaName = (String)key;
                    Object worldData = ((JSONObject) areasData).get(key);
                    PvPCArea area = PvPCArea.decode(areaName, worldData);
                    if (area != null) {
                        this.areas.put(area.getName(), area);
                    }
                }
            }
            reader.close();

        } catch (Exception e) {
            // TODO: Print
        }
    }

    /**
     * Gets the PvPCArea from the name.
     *
     * @param name - The String name of the area.
     * @return - The PvPCArea if it exists, null otherwise.
     */
    public PvPCArea getArea(String name) {
        if (this.areas.containsKey(name)) {
            return this.areas.get(name);
        }

        return null;
    }

    /**
     * Gets the area knockback from two players.
     *
     * @param player1 - The first player.
     * @param player2 - The second player.
     * @return - PvPCArea if players are within it, otherwise null.
     */
    public PvPCArea getAreaKnockback(Player player1, Player player2) {
        for (PvPCArea area : this.areas.values()) {
            if (area.canUseKnockback(player1, player2)) {
                return area;
            }
        }
        return null;
    }

    /**
     * Creates a new area if the player contains all of the given information.
     *
     * @param map    - The input map containing all the information.
     * @param name   - The name of the area.
     * @param player - The player that is creating the area.
     * @return - true if the player successfully created the area, false otherwise.
     */
    public boolean createArea(Map map, String name, Player player) {
        if (this.areas.containsKey(name)) {
            player.sendMessage(Utils.getPrefix() + TextFormat.RED.toString() + " The area already exists!");
            return false;
        }

        if (
                map != null
                        && map.containsKey("firstPos")
                        && map.containsKey("secondPos")
        ) {
            Object firstPos = map.get("firstPos");
            Object secondPos = map.get("secondPos");
            if (firstPos instanceof Vector3 && secondPos instanceof Vector3) {
                PvPCArea area = new PvPCArea(
                        name,
                        player.getLevel(),
                        (Vector3) firstPos,
                        (Vector3) secondPos
                );
                this.areas.put(area.getName(), area);
                return true;
            }
        }

        player.sendMessage(Utils.getPrefix() + TextFormat.RED + " Failed to create the area.");
        return false;
    }

    /**
     * Deletes the input area object.
     * @param area - The input area object.
     * @return - true if deletion is successful, not true otherwise.
     */
    public boolean deleteArea(PvPCArea area)
    {
        if(this.areas.containsKey(area.getName()))
        {
            this.areas.remove(area.getName());
            return true;
        }

        return false;
    }

    /**
     * Gets the areas in the collection.
     * @return - The areas.
     */
    public ArrayList<PvPCArea> getAreas()
    {
        return new ArrayList<>(this.areas.values());
    }

    /**
     * Saves the area to the file.
     */
    public void save()
    {
        try
        {
            JSONObject object = new JSONObject();
            for(PvPCArea area : this.areas.values())
            {
                object.put(area.getName(), area.export());
            }
            FileWriter writer = new FileWriter(this.areaFile);
            object.writeJSONString(writer);
            writer.close();

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
