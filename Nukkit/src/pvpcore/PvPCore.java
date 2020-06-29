package pvpcore;

import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import pvpcore.commands.CreateAreaCommand;
import pvpcore.commands.EditWorldCommand;
import pvpcore.commands.PvPAreaCommand;
import pvpcore.commands.PvPCoreCommand;
import pvpcore.utils.Utils;
import pvpcore.worlds.WorldHandler;
import pvpcore.worlds.areas.AreaHandler;

import java.io.File;

public class PvPCore extends PluginBase implements Listener {

    /**
     * The worldHandler accessor.
     */
    private static WorldHandler worldHandler;
    /**
     * The areaHandler accessor.
     */
    private static AreaHandler areaHandler;

    /**
     * Called when the plugin is enabled.
     */
    @Override
    public void onEnable() {
        // Initializes the data folder.
        this.initDataFolder();
        // Initializes the Commands.
        this.initCommands();

        worldHandler = new WorldHandler(this);
        areaHandler = new AreaHandler(this);

        // Initializes the listener.
        new PvPCListener(this);

        this.deleteConfig();
    }

    /**
     * Called when the plugin is disabled.
     */
    @Override
    public void onDisable() {
        // Saves the worlds to the worlds file.
        if (worldHandler != null) {
            worldHandler.save();
        }

        // Saves the areas to the areas file.
        if (areaHandler != null) {
            areaHandler.save();
        }
    }

    /**
     * Gets the world handler.
     *
     * @return The world handler class.
     */
    public static WorldHandler getWorldHandler() {
        return worldHandler;
    }

    /**
     * Gets the area handler.
     *
     * @return The area handler class.
     */
    public static AreaHandler getAreaHandler() {
        return areaHandler;
    }

    /**
     * Initializes the data folder.
     */
    private void initDataFolder() {
        File file = this.getDataFolder();
        if (!file.exists() || !file.isDirectory()) {
            file.mkdir();
        }
    }


    /**
     * Deletes the config from the plugin data if it exists.
     */
    private void deleteConfig() {
        File configFile = new File(this.getDataFolder(), "config.yml");
        if (configFile.exists()) {
            configFile.delete();
        }
    }

    /**
     * Initializes the PvPCore commands.
     */
    private void initCommands() {
        Utils.registerCommand(new PvPCoreCommand());
        Utils.registerCommand(new PvPAreaCommand());
        Utils.registerCommand(new CreateAreaCommand());
        Utils.registerCommand(new EditWorldCommand());
    }
}
