package pvpcore.commands.parameters;

public interface Parameter {

    int PARAMTYPE_STRING = 0, PARAMTYPE_INTEGER = 1, PARAMTYPE_TARGET = 2, PARAMTYPE_BOOLEAN = 3, PARAMTYPE_FLOAT = 4, PARAMTYPE_ANY = 5;

    String NO_PERMISSION = "None";

    String getName();

    boolean hasPermission();

    String getPermission();

}
