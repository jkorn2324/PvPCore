package pvpcore;

import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import pvpcore.commands.AreaCommand;
import pvpcore.commands.PvPCommand;
import pvpcore.misc.PvPCListener;
import pvpcore.utils.Utils;
import pvpcore.worlds.WorldHandler;
import pvpcore.worlds.areas.AreaHandler;

import java.util.*;

public class PvPCore extends PluginBase implements Listener {

    /* public static WorldHandler worldHandler;
    public static AreaHandler areaHandler; */
    //public InAirTask airTask;

    /** The worldHandler accessor. */
    private static WorldHandler worldHandler;
    /** The areaHandler accessor. */
    private static AreaHandler areaHandler;

    /**
     * Called when the plugin is enabled.
     */
    @Override
    public void onEnable()
    {

        // Initializes the Commands.
        this.initCommands();

        worldHandler = new WorldHandler(this);
        areaHandler = new AreaHandler(this);

        // Initializes the listener.
        new PvPCListener(this);


        /* initConfig();
        worldHandler = new WorldHandler(this);
        areaHandler = new AreaHandler(this);
        initCommands();
        this.getServer().getLogger().alert(PvPCoreUtil.getEnabledString(this.getName(), true));
        this.getServer().getPluginManager().registerEvents(new PvPCListener(this), this);

        coreTask = new PvPCoreTask(this);
        getServer().getScheduler().scheduleRepeatingTask(coreTask, 1); */
    }

    /**
     * Called when the plugin is disabled.
     */
    @Override
    public void onDisable()
    {
        // Saves the worlds to the world Handler.
        if(worldHandler != null)
        {
            worldHandler.save();
        }
        // TODO: Save updated managers.
        // this.getServer().getLogger().alert(PvPCoreUtil.getEnabledString(this.getName(), false));
    }

    /**
     * Gets the world handler.
     * @return The world handler class.
     */
    public static WorldHandler getWorldHandler()
    {
        return worldHandler;
    }

    /**
     * Gets the area handler.
     * @return The area handler class.
     */
    public static AreaHandler getAreaHandler()
    {
        return areaHandler;
    }

    private void initConfig()
    {
        // TODO: Remove.
        this.saveDefaultConfig();
        HashMap levelMap = (HashMap) getConfig().get("levels");
        if (levelMap == null) {
            getConfig().set("levels", new HashMap<String, Object>());
            getConfig().save();
        }

        HashMap areaMap = (HashMap)getConfig().get("areas");
        if(areaMap == null){
            getConfig().set("areas", new HashMap<String, Object>());
            getConfig().save();
        }
    }

    /**
     * Initializes the PvPCore commands.
     */
    private void initCommands()
    {
        // TODO: Implement Commands.
        Utils.registerCommand(new PvPCommand());
        Utils.registerCommand(new AreaCommand());
    }
}
