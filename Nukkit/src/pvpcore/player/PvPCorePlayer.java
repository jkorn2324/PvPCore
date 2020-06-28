package pvpcore.player;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Position;
import cn.nukkit.network.SourceInterface;

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

    /* private String player;

    private int damageDelay;

    public PvPPlayer(String name){
        this(name, 0);
    }

    public PvPPlayer(Player p){
        this(p, 0);
    }

    public PvPPlayer(String name, int time) {
        damageDelay = time;
        player = name;
    }

    public PvPPlayer(Player player, int time) {
        this.player = player.getName();
        damageDelay = time;
    }

    public void removeTick(int a){
        damageDelay -= a;
    }

    public void setNoDamageTicks(int a){
        damageDelay = a;
    }

    public int getNoDamageTicks(){
        return damageDelay;
    }

    public boolean canBeHit(){
        boolean result = false;
        if(isOnline()){
            Player p = getPlayer();
            if(!p.isCreative() && Server.getInstance().getPropertyBoolean("pvp")){
                if(damageDelay <= 0){
                    result = true;
                }
            }
        }
        return result;
    }

    public Player getPlayer(){
        return Server.getInstance().getPlayer(player);
    }

    public boolean isOnline(){
        boolean result = false;
        if(getPlayer() != null){
            Player p = getPlayer();
            result = p.isOnline();
        }
        return result;
    }

    public Position getPosition(){
        return getPlayer().getPosition();
    } */
}
