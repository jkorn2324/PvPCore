package pvpcore.worlds;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Level;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import pvpcore.PvPCore;
import pvpcore.utils.Utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
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

                // TODO: Get information from config file.
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

    /* public void add(PvPCWorld world)
    {
        Config config = core.getConfig();
        ConfigSection section = new ConfigSection();
        HashMap<String, Object> areaMap = PvPCore.getAreaHandler().toHashMap();
        HashMap<String, Object> levelMap = toHashMap();
        levelMap.put(world.getWorldName(), world.toHashMap());
        section.set("areas", areaMap);
        section.set("levels", levelMap);
        config.setAll(section);
        config.save();
    }

    public void update(PvPCWorld world)
    {
        Config cfg = core.getConfig();
        ConfigSection section = new ConfigSection();
        String name = world.getWorldName();
        HashMap<String, Object> worldsMap = toHashMap(getWorlds(name));
        worldsMap.put(world.getWorldName(), world.toHashMap());
        HashMap<String, Object> areaMap = PvPCore.getAreaHandler().toHashMap();
        section.set("levels", worldsMap);
        section.set("areas", areaMap);
        cfg.setAll(section);
        cfg.save();
    }

    public PvPCWorld get(String name){

        HashMap list = (HashMap) core.getConfig().get("levels");

        PvPCWorld world = null;

        for(Object v : list.keySet()){

            String worldName = v + "";

            if(worldName.equals(name)) {

                Object value = list.get(v);

                if (value instanceof HashMap) {

                    HashMap object = (HashMap) value;
                    int delay = -1;
                    Float kbY = null, kbXZ = null;
                    Boolean isCustomKB = null;

                    if (object.containsKey("attack-delay")) {
                        delay = (Integer)object.get("attack-delay");
                    }
                    if (object.containsKey("knockback-y")) {
                        kbY = PvPCoreUtil.getKnockbackFromObj(object.get("knockback-y"));
                    }
                    if (object.containsKey("knockback-xz")) {
                        kbXZ = PvPCoreUtil.getKnockbackFromObj(object.get("knockback-xz"));
                    }
                    if (object.containsKey("customkb")) {
                        isCustomKB = (boolean) object.get("customkb");
                    }

                    if (kbY != null && kbXZ != null && isCustomKB != null && delay != -1) {
                        world = new PvPCWorld(isCustomKB, worldName, delay, new float[]{kbY, kbXZ});
                    }
                }
                break;
            }
        }
        return world;
    }

    private ArrayList<PvPCWorld> getWorlds(String update)
    {
        Config config = core.getConfig();
        HashMap list = (HashMap)config.get("levels");
        ArrayList<PvPCWorld> worlds = new ArrayList<>();
        for(Object key : list.keySet()){
            String name = key + "";
            if(!name.contains(".")){
                boolean exec = true;
                if(update != null){
                    if(name.equalsIgnoreCase(update)){
                        exec = false;
                    }
                }
                if(exec) {
                    PvPCWorld world = get(name);
                    if (world != null) worlds.add(world);
                }
            }
        }
        return worlds;
    }

    private HashMap<String, Object> toHashMap(ArrayList<PvPCWorld> worlds)
    {
        HashMap<String, Object> map = new HashMap<>();
        for(PvPCWorld w : worlds){
            map.put(w.getWorldName(), w.toHashMap());
        }
        return map;
    }

    public HashMap<String, Object> toHashMap(){
        HashMap<String, Object> map = new HashMap<>();
        for(PvPCWorld w : getWorlds()){
            map.put(w.getWorldName(), w.toHashMap());
        }
        return map;
    }

    public ArrayList<PvPCWorld> getWorlds(){
        return getWorlds(null);
    }


    public boolean doesWorldExist(String name){
        PvPCWorld world = get(name);
        return world != null;
    }

    /* public PvPCWorld createDefault(String name){
        return new PvPCWorld(false, name, PvPCoreUtil.DEFAULT_ATTACK_DELAY, new float[]{0.4f, 0.4f});
    } */
}
