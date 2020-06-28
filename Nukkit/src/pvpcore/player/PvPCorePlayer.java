package pvpcore.player;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.SourceInterface;
import pvpcore.utils.PvPCKnockback;
import pvpcore.utils.Utils;

public class PvPCorePlayer extends Player {

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
            PvPCKnockback knockback = Utils.getKnockback(this, (Player) attacker);
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
}
