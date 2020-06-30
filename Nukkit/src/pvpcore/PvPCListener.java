package pvpcore;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerCreationEvent;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.window.FormWindow;
import pvpcore.forms.def.ICallbackForm;
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

    /**
     * Called when the player receives a response from the form.
     * @param event - The form response event.
     */
    @EventHandler
    public void onFormResponse(PlayerFormRespondedEvent event)
    {
        // Updates the player's looking at form status.
        Player player = event.getPlayer();
        if(
                player instanceof PvPCorePlayer
                && ((PvPCorePlayer) player).isLookingAtForm()
        )
        {
            ((PvPCorePlayer) player).setLookingAtForm(false);
        }

        FormWindow window = event.getWindow();
        if(window instanceof ICallbackForm)
        {
            ((ICallbackForm) window).handleResponse(event.getPlayer(), event.getResponse());
        }
    }
}
