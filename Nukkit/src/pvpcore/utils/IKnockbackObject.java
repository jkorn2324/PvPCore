package pvpcore.utils;

import cn.nukkit.Player;

public interface IKnockbackObject extends IExportedObject {

    /**
     * Determines if each player can use the knockback.
     * @param player - The player object.
     * @param attacker - The player who is attacking the player.
     * @return true if the players can use the knockback.
     */
    public boolean canUseKnockback(Player player, Player attacker);

}
