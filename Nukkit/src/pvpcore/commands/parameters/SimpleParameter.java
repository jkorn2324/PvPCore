package pvpcore.commands.parameters;

import java.util.ArrayList;
import java.util.Arrays;

public class SimpleParameter implements Parameter {

    private String name;

    private String permission;

    private boolean optional;

    private int paramType;

    private boolean hasExact;

    public SimpleParameter(String name, int type) {
        this.name = name;
        this.permission = null;
        this.paramType = type;
        this.optional = false;
        this.hasExact = false;
    }

    public SimpleParameter(String name, int type, String basePerm){
        this(name, type);
        this.permission = basePerm + "." + name;
    }

    public SimpleParameter(String name, int type, boolean optional){
        this(name, type);
        this.optional = optional;
    }

    public SimpleParameter setExact(boolean b){
        this.hasExact = b;
        return this;
    }

    public boolean hasExactValues(){
        return hasExact;
    }

    public boolean isOptional(){
        return optional;
    }

    public int getParamType(){
        return paramType;
    }

    public boolean isExactValue(String val){
        boolean result = false;
        for(String v : getExactValues()){
            if(v.equalsIgnoreCase(val)){
                result = true;
                break;
            }
        }
        return result;
    }

    private ArrayList<String> getExactValues(){
        ArrayList<String> result = new ArrayList<>();
        if(name.contains(":")){
            result.addAll(Arrays.asList(name.split(":")));
        } else {
            result.add(name);
        }
        return result;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean hasPermission() {
        return this.permission != null;
    }

    @Override
    public String getPermission() {
        return this.permission;
    }
}
