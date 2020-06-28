package pvpcore.commands.parameters;

public class BaseParameter implements Parameter {

    private String permission;

    private String name;

    private String description;

    public BaseParameter(String name, String description){
        this.description = description;
        this.name = name;
        this.permission = null;
    }

    public BaseParameter(String name, String basePerm, String description){
        this(name, basePerm, description, false);
    }

    public BaseParameter(String name, String perm, String desc, boolean fullPerm){
        this(name, desc);
        this.permission = (fullPerm ? perm : perm + "." + name);
    }

    public String getDescription(){
        return description;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean hasPermission() {
        return permission != null;
    }

    @Override
    public String getPermission(){
        return permission;
    }
}
