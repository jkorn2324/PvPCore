package pvpcore.player;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.SourceInterface;
import cn.nukkit.utils.TextFormat;
import pvpcore.PvPCore;
import pvpcore.utils.PvPCKnockback;
import pvpcore.utils.Utils;

import java.util.HashMap;

public class PvPCorePlayer extends Player {


    /** Determines whether the player is viewing a form. */
    private boolean lookingAtForm = false;
    /** Stores the area information for the player. */
    private HashMap<String, Object> areaInfo;

    /**
     * The default PvPPlayer constructor.
     * @param interfaz - The input interface instance.
     * @param clientID - The input client ID.
     * @param ip - The player's ip.
     * @param port - The player's port.
     */
    public PvPCorePlayer(SourceInterface interfaz, Long clientID, String ip, int port)
    {
        super(interfaz, clientID, ip, port);
        this.areaInfo = new HashMap<>();
    }

    /**
     * Sets the first position in the area information.
     */
    public void setFirstPos()
    {
        this.areaInfo.put("firstPos", new Vector3(this.x, this.y, this.z));
        this.sendMessage(Utils.getPrefix() + TextFormat.GREEN + " Successfully set the first position of the PvPArea.");
    }

    /**
     * Sets the second position in the area information.
     */
    public void setSecondPos()
    {
        this.areaInfo.put("secondPos", new Vector3(this.x, this.y, this.z));
        this.sendMessage(Utils.getPrefix() + TextFormat.GREEN + " Successfully set the second position of the PvPArea.");
    }

    /**
     * Gets the player's area information.
     * @return - The area information.
     */
    public HashMap<String, Object> getAreaInfo()
    {
        return this.areaInfo;
    }

    /**
     * Creates the new area based on the given name.
     * @param name - The input name.
     */
    public void createArea(String name)
    {
        if(PvPCore.getAreaHandler().createArea(this.areaInfo, name, this))
        {
            this.sendMessage(Utils.getPrefix() + TextFormat.GREEN + " Successfully created a new PvPArea.");
            this.areaInfo.clear();
        }
    }

    /**
     * Forces the player to take knockback, overriden to account for PvPAreas.
     * @param attacker - The Entity attacking the player.
     * @param damage - The amount of damage dealt.
     * @param x - The x coordinate.
     * @param z - The y coordinate.
     * @param base - The base knockback.
     */
    public void knockBack(Entity attacker, double damage, double x, double z, double base)
    {
        double xzKB = base, yKB = base;

        if(attacker instanceof Player)
        {
            PvPCKnockback knockback = Utils.getKnockbackFor(this, (Player) attacker);
            if(knockback != null)
            {
                xzKB = knockback.getHorizontalKB();
                yKB = knockback.getVerticalKB();
            }
        }

        double f = Math.sqrt(x * x + z * z);
        if (f > 0.0D)
        {
            f = 1.0D / f;
            Vector3 motion = new Vector3(this.motionX, this.motionY, this.motionZ);
            motion.x /= 2.0D;
            motion.y /= 2.0D;
            motion.z /= 2.0D;
            motion.x += x * f * xzKB;
            motion.y += yKB;
            motion.z += z * f * xzKB;

            if (motion.y > base)
            {
                motion.y = base;
            }

            this.setMotion(motion);
        }
    }

    /**
     * Sends the form window to the player, but checks whether or not the player is viewing a form.
     * @param window - The Form Window input.
     * @return - The input form window.
     */
    public int showFormWindow(FormWindow window)
    {
        int id = (this.lookingAtForm ? this.formWindowCount : this.formWindowCount++);
        return this.showFormWindow(window, id);
    }

    /**
     * Sends the form window to the player, but checks whether or not the player is viewing a form.
     * @param window - The Form Window input.
     * @param id - The form id.
     * @return - The input form window id.
     */
    @Override
    public int showFormWindow(FormWindow window, int id)
    {
        if(!this.lookingAtForm)
        {
            this.lookingAtForm = true;
            return super.showFormWindow(window, id);
        }

        return id;
    }

    /**
     * Determines whether or not the player is looking at a form.
     * @param lookingAtForm - The looking at form input boolean.
     */
    public void setLookingAtForm(boolean lookingAtForm)
    {
        this.lookingAtForm = lookingAtForm;
    }

    /**
     * Determines if the player is viewing a form.
     * @return - True if looking at a form, false otherwise.
     */
    public boolean isLookingAtForm()
    {
        return this.lookingAtForm;
    }
}
