package pvpcore.commands;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import pvpcore.commands.parameters.BaseParameter;
import pvpcore.commands.parameters.Parameter;
import pvpcore.commands.parameters.SimpleParameter;
import pvpcore.misc.PvPCoreUtil;

import java.util.ArrayList;

public class BaseCommand extends Command {

    private Parameter[][] parameters;

    public BaseCommand(String name, String description, String usageMessage) {
        super(name, description, usageMessage);
        setPermission("pvpcore.permission." + name);
        super.commandParameters.clear();
        super.commandParameters.put("args", new CommandParameter[]{new CommandParameter("args", CommandParamType.TEXT, false)});
    }

    public void setParameters(Parameter[][] params) {
        parameters = params;
    }

    protected Parameter[] getParamGroupFrom(String name) {
        Parameter[] group = null;
        for (int i = 0; i < parameters.length; i++) {
            Parameter[] paramGroup = parameters[i];
            if (paramGroup.length > 0) {
                String theName = paramGroup[0].getName();
                if (theName.equalsIgnoreCase(name)) {
                    group = paramGroup;
                    break;
                }
            }
        }
        return group;
    }

    private boolean hasParamGroup(String name) {
        return getParamGroupFrom(name) != null;
    }

    protected boolean checkPermission(CommandSender sender, String name) {
        boolean result = false;
        if (hasParamGroup(name)) {
            Parameter[] group = getParamGroupFrom(name);
            int len = group.length;
            if (len > 0) {
                Parameter p = group[0];
                if (p.hasPermission()) {
                    String perm = p.getPermission();
                    if (sender.hasPermission(perm)) {
                        result = true;
                    }
                } else {
                    result = true;
                }
            } else {
                if (sender.hasPermission(this.getPermission())) {
                    result = true;
                }
            }
        }
        return result;
    }

    protected boolean hasProperParamTypes(String[] args, Parameter[] group) {
        boolean result = true;
        for(int i = 0; i < args.length; i++){
            Parameter p = group[i];
            String argument = args[i];
            if (p instanceof BaseParameter) {
                String name = p.getName();
                if (!argument.equalsIgnoreCase(name)) {
                    result = false;
                    break;
                }
            } else if (p instanceof SimpleParameter) {
                if (!hasProperParamType(argument, (SimpleParameter) p)) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    private boolean hasProperParamType(String argument, SimpleParameter p) {

        boolean result = false;

        switch (p.getParamType()) {
            case Parameter.PARAMTYPE_INTEGER:
                result = PvPCoreUtil.canParse(argument, false);
                break;
            case Parameter.PARAMTYPE_FLOAT:
                result = PvPCoreUtil.canParse(argument, true);
                break;
            case Parameter.PARAMTYPE_BOOLEAN:
                result = PvPCoreUtil.isBoolean(argument);
                break;
            case Parameter.PARAMTYPE_TARGET:
                result = true;
                break;
            case Parameter.PARAMTYPE_STRING:
                result = true;
                break;
            case Parameter.PARAMTYPE_ANY:
                result = true;
                break;
            default:
        }

        if (result) {
            if (p.hasExactValues()) {
                if (!p.isExactValue(argument)) {
                    result = false;
                }
            }
        }
        return result;
    }

    protected String getParameterType(SimpleParameter p) {
        String name = p.getName();
        String result = name;
        switch (p.getParamType()) {
            case Parameter.PARAMTYPE_INTEGER:
                result = "[int | " + name + "]";
                break;
            case Parameter.PARAMTYPE_FLOAT:
                result = "[float | " + name + "]";
                break;
            case Parameter.PARAMTYPE_BOOLEAN:
                result = "[boolean | " + name + "]";
                break;
            case Parameter.PARAMTYPE_TARGET:
                result = "[target | " + name + "]";
                break;
            case Parameter.PARAMTYPE_STRING:
                result = "[string | " + name + "]";
                break;
            case Parameter.PARAMTYPE_ANY:
                result = "[any | " + name + "]";
                break;
            default:
        }
        return result;
    }

    protected boolean hasProperParamLength(String[] args, Parameter[] params){
        boolean result = false;
        int argsLen = args.length, minLen = 0, maxLen = minLen;
        for(Parameter p : params) {
            boolean add = true;
            if(p instanceof SimpleParameter){
                if(((SimpleParameter) p).isOptional()){
                    add = false;
                }
            }
            if(add) minLen++;
            maxLen++;
        }

        if(minLen == maxLen){
            result = argsLen == maxLen;
        } else {
            result = PvPCoreUtil.isWithinBounds(argsLen, minLen, maxLen);
        }
        return result;
    }

    public boolean canExecute(CommandSender sender, String s, String[] args){
        boolean result = false;
        String message = null;

        boolean exec = args.length > 0 && hasParamGroup(args[0]);
        if(exec) {
            if(checkPermission(sender, args[0])){
                Parameter[] group = this.getParamGroupFrom(args[0]);
                result = this.hasProperParamLength(args, group) && this.hasProperParamTypes(args, group);
                if(!result){
                    message = getUsageOf(group, false);
                }
            } else {
                message = getPermissionMessage();
            }
        } else {
            message = this.getFullUsage();
        }

        if(message != null) sender.sendMessage(message);
        return result;
    }

    protected String getUsageOf(Parameter[] group, boolean fullMsg) {
        String name = super.getName();
        String str = (fullMsg ? " - /" + name + " " : "Usage: /" + name + " ");
        int count = 0;
        String desc = null;
        int len = group.length - 1;
        for(Parameter p : group){
            if(count == 0){
                String pName = p.getName();
                String space = (len == 0 ? "" : " ");
                str = str + pName + space;
                if(p instanceof BaseParameter){
                    desc = ((BaseParameter)p).getDescription();
                }
            } else {
                String space = (count == len ? "" : " ");
                if(p instanceof SimpleParameter){
                    str = str + getParameterType((SimpleParameter)p) + space;
                }
            }
            count++;
        }

        if(desc != null) str = str + " - " + desc;
        return str;
    }

    protected String getFullUsage(){
        int count = 0;
        ArrayList<String> list = new ArrayList<>();
        for(Parameter[] group : parameters){
            Parameter first = group[0];
            String name = first.getName();
            list.add(name);
        }
        int len = list.size() - 1;
        String res = "All the " + getName() + " commands:\n";
        for(String s : list){
            String line = "\n";
            if(count == len){
                line = "";
            }
            res = res + getUsageOf(getParamGroupFrom(s), true) + line;
            count++;
        }
        return res;
    }



    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        return true;
    }
}
