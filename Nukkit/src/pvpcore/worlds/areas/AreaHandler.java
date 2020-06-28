package pvpcore.worlds.areas;

import cn.nukkit.Player;
import cn.nukkit.level.Position;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import cn.nukkit.utils.TextFormat;
import pvpcore.PvPCore;
import pvpcore.misc.PvPCoreUtil;
import pvpcore.worlds.PvPCWorld;

import java.util.ArrayList;
import java.util.HashMap;

public class AreaHandler {

    private PvPCore p;

    public AreaHandler(PvPCore a){
        p = a;
    }

    public void add(PvPCArea area){
        Config config = p.getConfig();
        ConfigSection section = new ConfigSection();
        HashMap<String, Object> areaMap = toHashMap();
        areaMap.put(area.getName(), area.toMap());
        HashMap<String, Object> levelMap = PvPCore.worldHandler.toHashMap();
        section.set("areas", areaMap);
        section.set("levels", levelMap);
        config.setAll(section);
        config.save();
        /*p.getConfig().set("areas." + world.getWorldName(), world.toHashMap());
        p.getConfig().save();*/
    }

    public ArrayList<PvPCArea> getAreas(){
        return getAreas(null);
    }

    private ArrayList<PvPCArea> getAreas(String s){
        ArrayList<PvPCArea> list = new ArrayList<>();
        HashMap map = (HashMap)p.getConfig().get("areas");
        for(Object o : map.keySet()){
            String name = o + "";
            boolean b = true;

            if(s != null){
                if(s.equalsIgnoreCase(name)){
                    b = false;
                }
            }

            if(b){
                if(doesAreaExist(name)){
                    PvPCArea area = getAreaByName(name);
                    list.add(area);
                }
            }
        }
        return list;
    }

    public PvPCArea getAreaByName(String name){

        PvPCArea area = null;

        HashMap theMap = (HashMap)p.getConfig().get("areas");

        if(theMap.containsKey(name)) {

            HashMap map = (HashMap)theMap.get(name);

            String[] keys = PvPCArea.getKeys().clone();

            Float xKB = null, yKB = null;

            Position firstPos = null, secondPos = null;

            Integer attackDel = null;

            Boolean isEnabled = null;

            String world = null;

            if (map.containsKey(keys[0])) {
                xKB = PvPCoreUtil.getKnockbackFromObj(map.get(keys[0]));
            }

            if (map.containsKey(keys[1])) {
                yKB = PvPCoreUtil.getKnockbackFromObj(map.get(keys[1]));
            }

            if (map.containsKey(keys[2])) {
                firstPos = PvPCoreUtil.readPosFrom(map.get(keys[2]));
            }

            if (map.containsKey(keys[3])) {
                secondPos = PvPCoreUtil.readPosFrom(map.get(keys[3]));
            }

            if (map.containsKey(keys[4]) && map.get(keys[4]) instanceof Integer) {
                attackDel = (Integer) map.get(keys[4]);
            }

            if (map.containsKey(keys[5]) && map.get(keys[5]) instanceof Boolean) {
                isEnabled = (Boolean) map.get(keys[5]);
            }

            if (map.containsKey(keys[6])) {
                world = map.get(keys[6]) + "";
            }

            if (xKB != null && yKB != null && firstPos != null && secondPos != null && attackDel != null && isEnabled != null && world != null) {
                area = new PvPCArea(name, xKB.doubleValue(), yKB.doubleValue(), firstPos, secondPos).setAreaKBEnabled(isEnabled).setAttackDelay(attackDel).setWorld(world);
            }
        }
        return area;
    }

    public boolean doesAreaExist(String name){
        return getAreaByName(name) != null;
    }

    public void update(PvPCArea area){
        Config config = p.getConfig();
        ConfigSection section = new ConfigSection();
        String name = area.getName();
        HashMap<String, Object> areaMap = toHashMap(getAreas(name));
        areaMap.put(area.getName(), area.toMap());
        HashMap<String, Object> levelMap = PvPCore.worldHandler.toHashMap();
        section.set("areas", areaMap);
        section.set("levels", levelMap);
        config.setAll(section);
        config.save();
    }

    public void delete(PvPCArea area){
        Config config = p.getConfig();
        ConfigSection section = new ConfigSection();
        String name = area.getName();
        HashMap<String, Object> areaMap = toHashMap(getAreas(name));
        HashMap<String, Object> levelMap = PvPCore.worldHandler.toHashMap();
        section.set("areas", areaMap);
        section.set("levels", levelMap);
        config.setAll(section);
        config.save();
    }

    public HashMap<String, Object> toHashMap(ArrayList<PvPCArea> list){
        HashMap<String, Object> map = new HashMap<>();
        for(PvPCArea area : list){
            map.put(area.getName(), area.toMap());
        }
        return map;
    }

    public HashMap<String, Object> toHashMap(){
        HashMap<String, Object> map = new HashMap<>();
        for(PvPCArea area : getAreas()){
            map.put(area.getName(), area.toMap());
        }
        return map;
    }

    public PvPCArea getAreaFrom(Player...players){
        int len = players.length;
        PvPCArea area = null;
        if(len != 0) {
            if (len == 1) {
                Player p = players[0];
                if (isPlayerInArea(p)) {
                    area = getAreaAtPos(p);
                }
            } else {
                Player p = players[0];
                if(isPlayerInArea(p)){
                    area = getAreaAtPos(p);
                }

                if(area != null) {
                    for (int i = 1; i < players.length; i++) {
                        Player pl = players[i];
                        if (pl != null && isPlayerInArea(pl)) {
                            PvPCArea theArea = getAreaAtPos(pl);
                            if (!theArea.equals(area)) {
                                area = null;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return area;
    }

    public boolean isPlayerInArea(Player p){
        return getAreaAtPos(p) != null;
    }

    public PvPCArea getAreaAtPos(Player...players){
        PvPCArea res = null;
        for(Player player : players) {
            for (PvPCArea area : getAreas()) {
                if (area.isPlayerWithinBounds(player)) {
                    res = area;
                    break;
                }
            }
        }
        return res;
    }

    public boolean arePlayersInSameArea(Player...players) {
        return getAreaFrom(players) != null;
    }

    public boolean hasWorld(PvPCArea area) {
        String world = area.getWorld();
        return PvPCore.worldHandler.doesWorldExist(world);
    }

    public PvPCWorld getWorld(PvPCArea area){
        return PvPCore.worldHandler.get(area.getWorld());
    }

    public String listAreas() {

        String areaList = TextFormat.GOLD + "Area List" + TextFormat.GRAY + ": " + TextFormat.RESET;

        int len = getAreas().size() - 1, count = 0;
        for(PvPCArea area : getAreas()){
            String comma = TextFormat.GRAY + ", " + TextFormat.BLUE;
            if(count == len){
                comma = "";
            }
            String areaName = TextFormat.BLUE + area.getName() + comma;
            areaList += areaName;
            count++;
        }
        return areaList;
    }
}
