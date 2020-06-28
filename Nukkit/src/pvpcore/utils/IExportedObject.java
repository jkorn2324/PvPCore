package pvpcore.utils;

import java.util.HashMap;

public interface IExportedObject {

    /**
     * Exports the value to a HashMap.
     * @return The hashmap containing the data.
     */
    public HashMap<String, Object> export();
}
