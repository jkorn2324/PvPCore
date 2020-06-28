package pvpcore.commands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.TextFormat;
import pvpcore.PvPCore;
import pvpcore.commands.parameters.BaseParameter;
import pvpcore.commands.parameters.Parameter;
import pvpcore.commands.parameters.SimpleParameter;
import pvpcore.misc.PvPCoreUtil;
import pvpcore.worlds.areas.PvPCArea;

public class AreaCommand extends BaseCommand {

    public AreaCommand() {
        super("area", "The base area command.", "/area help");
        Parameter[][] params = new Parameter[][]{
                {new BaseParameter("help", super.getPermission(), "Lists all of the area commands.")},
                {new BaseParameter("create", super.getPermission(), "Creates a new area at the current position."), new SimpleParameter("name", Parameter.PARAMTYPE_STRING)},
                {new BaseParameter("delete", super.getPermission(), "Deletes an area."), new SimpleParameter("name", Parameter.PARAMTYPE_STRING)},
                {new BaseParameter("setpos", super.getPermission() + ".pos", "Sets the min/max position bounds of the arena based on the current position."), new SimpleParameter("min:max", Parameter.PARAMTYPE_STRING).setExact(true), new SimpleParameter("areaName", Parameter.PARAMTYPE_STRING)},
                {new BaseParameter("list", super.getPermission(), "Lists all of the areas on the server.")}
        };
        setParameters(params);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        String msg = null;
        if(canExecute(sender, label, args)){
            String name = args[0];
            switch(name){
                case "help":
                    msg = getFullUsage();
                    break;
                case "create":
                    if(PvPCoreUtil.isPlayer(sender)) createArea((Player)sender, args[1]);
                    break;
                case "delete":
                    deleteArea(sender, args[1]);
                    break;
                case "setpos":
                    if(PvPCoreUtil.isPlayer(sender)) setPosition((Player)sender, args[1].equalsIgnoreCase("max"), args[2]);
                    break;
                case "list":
                    msg = PvPCore.areaHandler.listAreas();
                    break;
                default:
            }
        }
        if(msg != null) sender.sendMessage(msg);
        return true;
    }

    private void setPosition(Player sender, boolean isMax, String name) {
        String msg;
        if(PvPCore.areaHandler.doesAreaExist(name)) {
            PvPCArea area = PvPCore.areaHandler.getAreaByName(name);
            Level lvl = sender.getLevel();
            if(lvl.getName().equalsIgnoreCase(area.getLevel().getName())){
                if (isMax) {
                    Position p = sender.getPosition();
                    area.setFirstPosition(p);
                    PvPCore.areaHandler.update(area);
                } else {
                    Position p = sender.getPosition();
                    area.setSecondPosition(p);
                    PvPCore.areaHandler.update(area);
                }
                msg = TextFormat.GRAY + area.getName() + " has been successfully" + TextFormat.GREEN + " updated" + TextFormat.GRAY + "!";
            } else {
                msg = TextFormat.RED + "You must be in the same level as the area (" + TextFormat.GRAY + area.getName() + TextFormat.WHITE + " = " + TextFormat.GOLD + area.getLevel().getName() + TextFormat.RED + ")!";
            }
        } else {
            msg = TextFormat.RED + "The area '" + name + "' does not exist!";
        }

        sender.sendMessage(msg);
    }

    private void createArea(Player sender, String name) {
        String msg;
        if(!PvPCore.areaHandler.doesAreaExist(name)){
            Position center = sender.getPlayer().getPosition();
            Level lvl = sender.getLevel();

            Position first = new Position(center.x + 20,  0, center.z + 20, lvl);
            Position sec = new Position(center.x - 20, PvPCoreUtil.MAX_WORLD_HEIGHT, center.z - 20, lvl);

            PvPCArea area = new PvPCArea(name, lvl.getName()).setPositions(first, sec);
            PvPCore.areaHandler.add(area);
            msg = TextFormat.GRAY + area.getName() + " has been successfully" + TextFormat.GREEN + " created" + TextFormat.GRAY + "!";
        } else {
            msg = TextFormat.RED + "The area '" + name + "' already exists!";
        }
        sender.sendMessage(msg);
    }

    private void deleteArea(CommandSender sender, String name){
        String msg;
        if(PvPCore.areaHandler.doesAreaExist(name)){
            PvPCArea area = PvPCore.areaHandler.getAreaByName(name);
            PvPCore.areaHandler.delete(area);
            msg = TextFormat.GRAY + area.getName() + " has been successfully" + TextFormat.RED + " deleted" + TextFormat.GRAY + "!";
        } else {
            msg = TextFormat.RED + "The area '" + name + "' does not exist!";
        }
        sender.sendMessage(msg);
    }
}
