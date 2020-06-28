package pvpcore.commands;

import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import pvpcore.PvPCore;
import pvpcore.commands.parameters.BaseParameter;
import pvpcore.commands.parameters.Parameter;
import pvpcore.commands.parameters.SimpleParameter;
import pvpcore.misc.PvPCoreUtil;
import pvpcore.worlds.areas.PvPCArea;
import pvpcore.worlds.PvPCWorld;

public class PvPCommand extends BaseCommand {

    public PvPCommand() {
        super("pvp", "The base pvp core command.", "/pvp help");
        Parameter[][] params = new Parameter[][]{
                {new BaseParameter("help", super.getPermission(),"Lists all of the pvp commands.")},
                {new BaseParameter("set", super.getPermission(), "Configures the knockback settings of a level."), new SimpleParameter("level:area", Parameter.PARAMTYPE_STRING).setExact(true), new SimpleParameter("name", Parameter.PARAMTYPE_STRING), new SimpleParameter("knockback:attackdelay", Parameter.PARAMTYPE_STRING).setExact(true), new SimpleParameter("delay:xz-KB", Parameter.PARAMTYPE_ANY, false), new SimpleParameter("y-KB", Parameter.PARAMTYPE_FLOAT, true)},
                {new BaseParameter("enable", super.getPermission(), "Enables custom knockback in a level or area."), new SimpleParameter("level:area", Parameter.PARAMTYPE_STRING).setExact(true), new SimpleParameter("name", Parameter.PARAMTYPE_STRING)},
                {new BaseParameter("disable", super.getPermission(), "Disables custom knockback in a level or area."), new SimpleParameter("level:area", Parameter.PARAMTYPE_STRING).setExact(true), new SimpleParameter("name", Parameter.PARAMTYPE_STRING)}
        };
        setParameters(params);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        String msg = null;
        if(super.canExecute(sender, label, args)){
            String name = args[0];
            switch(name){
                case "help":
                    msg = getFullUsage();
                    break;
                case "enable":
                    String type = args[1], theName = args[2];
                    executeEnableKB(sender, type, theName, true);
                    break;
                case "disable":
                    type = args[1];
                    theName = args[2];
                    executeEnableKB(sender, type, theName, false);
                    break;
                case "set":
                    int len = args.length;
                    String levelOfArea = args[1], placeName = args[2], valType = args[3];
                    if(len == 6){
                        String xKB = args[4], yKB = args[5];
                        float y = Float.parseFloat(yKB);
                        if(PvPCoreUtil.canParse(xKB, true)){
                            float x = Float.parseFloat(xKB);
                            executeSetKB(sender, levelOfArea, placeName, x, y);
                        } else {
                            msg = getUsageOf(getParamGroupFrom(name), false);
                        }
                    } else {
                        boolean sendMsg = false;
                        if(valType.equalsIgnoreCase("attackdelay")){
                            String attackDelay = args[4];
                            if(PvPCoreUtil.canParse(attackDelay, false)){
                                executeSetDelay(sender, levelOfArea, placeName, Integer.parseInt(attackDelay));
                            } else {
                                sendMsg = true;
                            }
                        } else {
                            sendMsg = true;
                        }

                        if(sendMsg) msg = getUsageOf(getParamGroupFrom(name), false);
                    }
                    break;
                default:
                    msg = getFullUsage();
            }
        }

        if(msg != null) sender.sendMessage(msg);

        return super.execute(sender, label, args);
    }

    private void executeEnableKB(CommandSender sender, String type, String theName, boolean enable) {

        String msg;

        String e = (enable ? TextFormat.GREEN + "enabled" + TextFormat.GRAY : TextFormat.RED + "disabled" + TextFormat.GRAY);
        if (type.equalsIgnoreCase("level")) {
            if(PvPCore.worldHandler.doesWorldExist(theName)){
                PvPCWorld world = PvPCore.worldHandler.get(theName);
                world = world.setKBEnabled(enable);
                PvPCore.worldHandler.update(world);
                msg = TextFormat.GRAY + "Custom KB for the level '" + TextFormat.BLUE + world.getWorldName() + TextFormat.GRAY + "' has been successfully " + e + "!";
            } else {
                msg = TextFormat.RED + "The level '" + theName + "' does not exist!";
            }
        } else {
            if(PvPCore.areaHandler.doesAreaExist(theName)){
                PvPCArea area = PvPCore.areaHandler.getAreaByName(theName);
                area = area.setAreaKBEnabled(enable);
                PvPCore.areaHandler.update(area);
                msg = TextFormat.GRAY + "Custom KB for the area '" + TextFormat.BLUE + area.getName() + TextFormat.GRAY + "' has been successfully " + e + "!";
            } else {
                msg = TextFormat.RED + "The arena '" + theName + "' does not exist!";
            }
        }

        sender.sendMessage(msg);

    }

    protected void executeSetDelay(CommandSender sender, String levelOrArea, String placeName, int attackDelay) {

        String msg;

        if(levelOrArea.equalsIgnoreCase("level")){
            if(PvPCore.worldHandler.doesWorldExist(placeName)){
                if(attackDelay <= 0){
                    msg = TextFormat.RED + "The value has to be greater than 0!";
                } else {
                    PvPCWorld world = PvPCore.worldHandler.get(placeName);
                    world = world.setAttackDelay(attackDelay);
                    PvPCore.worldHandler.update(world);
                    msg = TextFormat.GRAY + world.getWorldName() + " has been successfully" + TextFormat.GREEN + " updated" + TextFormat.GRAY + "!";
                }
            } else {
                msg = TextFormat.RED + "The level '" + placeName + "' does not exist!";
            }
        } else {
            if(PvPCore.areaHandler.doesAreaExist(placeName)){
                if(attackDelay <= 0){
                    msg = TextFormat.RED + "The value has to be greater than 0!";
                } else {
                    PvPCArea area = PvPCore.areaHandler.getAreaByName(placeName);
                    area = area.setAttackDelay(attackDelay);
                    PvPCore.areaHandler.update(area);
                    msg = TextFormat.GRAY + area.getName() + " has been successfully" + TextFormat.GREEN + " updated" + TextFormat.GRAY + "!";
                }
            } else {
                msg = TextFormat.RED + "The arena '" + placeName + "' does not exist!";
            }
        }

        sender.sendMessage(msg);
    }

    protected void executeSetKB(CommandSender sender, String levelOfArea, String name, float xKb, float yKb){
        String msg = null;
        if(levelOfArea.equalsIgnoreCase("level")){
            if(PvPCore.worldHandler.doesWorldExist(name)){
                if(xKb <= 0 || yKb <= 0){
                    msg = TextFormat.RED + "The value has to be greater than 0!";
                } else {
                    PvPCWorld world = PvPCore.worldHandler.get(name);
                    world = world.setKB(xKb, yKb);
                    PvPCore.worldHandler.update(world);
                    msg = TextFormat.GRAY + world.getWorldName() + " has been successfully" + TextFormat.GREEN + " updated" + TextFormat.GRAY + "!";
                }
            } else {
                msg = TextFormat.RED + "The level '" + name + "' does not exist!";
            }
        } else {
            if(PvPCore.areaHandler.doesAreaExist(name)){
                if(xKb <= 0 || yKb <= 0){

                } else {
                    PvPCArea area = PvPCore.areaHandler.getAreaByName(name);
                    area = area.setKB(xKb, yKb);
                    PvPCore.areaHandler.update(area);
                    msg = TextFormat.GRAY + area.getName() + " has been successfully" + TextFormat.GREEN + " updated" + TextFormat.GRAY + "!";
                }
            } else {
                msg = TextFormat.RED + "The arena '" + name + "' does not exist!";
            }
        }
        if(msg != null) sender.sendMessage(msg);
    }

}
