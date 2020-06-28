package pvpcore;

import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerCreationEvent;
import pvpcore.player.PvPCorePlayer;

public class PvPCListener implements Listener {

    /** The core accessor. */
    private PvPCore core;
    /** The server accessor */
    private Server server;

    /**
     * Constructor for the PvPCore Listener.
     * @param core
     */
    public PvPCListener(PvPCore core)
    {
        this.core = core;
        this.server = core.getServer();

        this.server.getPluginManager().registerEvents(this, core);
    }

    /**
     * Called when the player class is created.
     * @param event - The player creation event object.
     */
    @EventHandler
    public void onPlayerCreation(PlayerCreationEvent event)
    {
        event.setPlayerClass(PvPCorePlayer.class);
    }

}
