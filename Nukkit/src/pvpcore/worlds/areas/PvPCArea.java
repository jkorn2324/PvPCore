package pvpcore.worlds.areas;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import pvpcore.PvPCore;
import pvpcore.misc.PvPCoreUtil;
import pvpcore.worlds.PvPCWorld;

import java.util.HashMap;

public class PvPCArea {

    private Position firstPos;

    private Position secondPos;

    private float xKB, yKB;

    private String name;

    private int attackDelay;

    private boolean areaKBEnabled;

    private String world;

    public PvPCArea(String theName, String world){
        firstPos = new Position();
        secondPos = new Position();
        name = theName;
        xKB = 0.4f;
        yKB = 0.4f;
        attackDelay = PvPCoreUtil.DEFAULT_ATTACK_DELAY;
        areaKBEnabled = false;
        this.world = world;
    }

    public PvPCArea(String name, Double xKnockback, Double yKnockback, Position pos1, Position pos2){
        firstPos = pos1;
        secondPos = pos2;
        xKB = xKnockback.floatValue();
        yKB = yKnockback.floatValue();
        this.name = name;
        attackDelay = PvPCoreUtil.DEFAULT_ATTACK_DELAY;
        areaKBEnabled = false;
        world = Server.getInstance().getDefaultLevel().getFolderName();
    }

    public PvPCArea setWorld(String s){
        this.world = s;
        if(PvPCore.worldHandler.doesWorldExist(s)){
            PvPCWorld world = PvPCore.worldHandler.get(s);
            Level lvl = world.getLevel();
            firstPos.level = lvl;
            secondPos.level = lvl;
        }
        return this;
    }

    public String getWorld(){
        return world;
    }

    public PvPCArea setAreaKBEnabled(boolean en){
        this.areaKBEnabled = en;
        return this;
    }

    public PvPCArea setAttackDelay(int del){
        this.attackDelay = del;
        return this;
    }

    public float getXZKB(){
        return xKB;
    }

    public float getYKB(){
        return yKB;
    }

    public Position getFirstPos(){
        return firstPos;
    }

    public Position getSecondPos(){
        return secondPos;
    }

    private Double getMinX(){
        double firstX = firstPos.x, secondX = secondPos.x;
        return PvPCoreUtil.getMinimumValue(firstX, secondX);
    }

    private Double getMaxX(){
        double firstX = firstPos.x, secondX = secondPos.x;
        return PvPCoreUtil.getMaximumValue(firstX, secondX);
    }

    private Double getMinY(){
        double firstY = firstPos.y, secondY = secondPos.y;
        return PvPCoreUtil.getMinimumValue(firstY, secondY);
    }

    private Double getMaxY(){
        double firstY = firstPos.y, secondY = secondPos.y;
        return PvPCoreUtil.getMaximumValue(firstY, secondY);
    }

    private Double getMinZ(){
        double firstZ = firstPos.z, secondZ = secondPos.z;
        return PvPCoreUtil.getMinimumValue(firstZ, secondZ);
    }

    private Double getMaxZ(){
        double firstZ = firstPos.z, secondZ = secondPos.z;
        return PvPCoreUtil.getMaximumValue(firstZ, secondZ);
    }

    public boolean isPlayerWithinBounds(Player player){

        Level lvl = player.getLevel();

        boolean cont = lvl.equals(getLevel()), result = false;

        if(cont) {

            int x = PvPCoreUtil.doubleToInt(player.x), y = PvPCoreUtil.doubleToInt(player.y), z = PvPCoreUtil.doubleToInt(player.z);

            int maxX = PvPCoreUtil.doubleToInt(getMaxX()), minX = PvPCoreUtil.doubleToInt(getMinX());

            int maxY = PvPCoreUtil.doubleToInt(getMaxY()), minY = PvPCoreUtil.doubleToInt(getMinY());

            int maxZ = PvPCoreUtil.doubleToInt(getMaxZ()), minZ = PvPCoreUtil.doubleToInt(getMinZ());

            result = PvPCoreUtil.isWithinBounds(x, minX, maxX) && PvPCoreUtil.isWithinBounds(y, minY, maxY) && PvPCoreUtil.isWithinBounds(z, minZ, maxZ);
        }

        return result;
    }

    public int getAttackDelay(){
        return attackDelay;
    }

    public String getName(){
        return name;
    }

    public Level getLevel(){
        return this.firstPos.getLevel();
    }

    public HashMap<String, Object> toMap(){
        HashMap<String, Object> res = new HashMap<>();
        res.put("knockback-xz", xKB);
        res.put("knockback-y", yKB);
        res.put("first-pos", PvPCoreUtil.positionToString(firstPos, false));
        res.put("second-pos", PvPCoreUtil.positionToString(secondPos, false));
        res.put("attack-delay", attackDelay);
        res.put("kb-enabled", areaKBEnabled);
        res.put("world", world);
        return res;
    }

    public static String[] getKeys(){
        return new String[]{"knockback-xz", "knockback-y", "first-pos", "second-pos", "attack-delay", "kb-enabled", "world"};
    }

    public PvPCArea setPositions(Position first, Position sec) {
        this.firstPos = first;
        this.secondPos = sec;
        return this;
    }

    public boolean isPlayerClose(Player player){

        int x = PvPCoreUtil.doubleToInt(player.x), y = PvPCoreUtil.doubleToInt(player.y), z = PvPCoreUtil.doubleToInt(player.z);

        int maxX = PvPCoreUtil.doubleToInt(getMaxX()), minX = PvPCoreUtil.doubleToInt(getMinX());

        int maxY = PvPCoreUtil.doubleToInt(getMaxY()), minY = PvPCoreUtil.doubleToInt(getMinY());

        int maxZ = PvPCoreUtil.doubleToInt(getMaxZ()), minZ = PvPCoreUtil.doubleToInt(getMinZ());

        boolean close;

        if(!isPlayerWithinBounds(player)){

            int dist = 15;

            close = PvPCoreUtil.isClose(x, minX, maxX, dist) && PvPCoreUtil.isClose(y, minY, maxY, dist) && PvPCoreUtil.isClose(z, minZ, maxZ, dist);

        } else {

            close = true;

        }

        return close;
    }

    public boolean equals(Object o){
        boolean result = false;
        if(o instanceof PvPCArea){
            if(((PvPCArea) o).getName().equalsIgnoreCase(this.name)){
                if(((PvPCArea) o).getFirstPos().equals(this.getFirstPos()) && ((PvPCArea) o).getSecondPos().equals(this.getSecondPos())){
                    result = true;
                }
            }
        }
        return result;
    }

    public boolean canUseKB() {
        return areaKBEnabled;
    }

    public PvPCArea setKB(float xKb, float yKb) {
        this.xKB = xKb;
        this.yKB = yKb;
        return this;
    }

    public void setFirstPosition(Position p) {
        firstPos = p;
    }

    public void setSecondPosition(Position p){
        secondPos = p;
    }
}
