package pvpcore.utils;

import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PvPCKnockback implements IExportedObject {

    // Declares the constant variables.
    public static final String HORIZONTAL_KB = "kb-x";
    public static final String VERTICAL_KB = "kb-y";
    public static final String SPEED_KB = "kb-speed";

    /** The horizontal & vertical KB values. */
    private float horizontalKB, verticalKB;
    /** The attack delay value. */
    private int attackDelay;

    /**
     * The default PvPCKnockback constructor.
     */
    public PvPCKnockback()
    {
        this.horizontalKB = 0.4f;
        this.verticalKB = 0.4f;
        this.attackDelay = 10;
    }

    /**
     * The PvPCKnockback constructor.
     * @param xzKB - The horizontal input KB.
     * @param yKB - The vertical input KB.
     * @param attackDelay - The input attack delay.
     */
    public PvPCKnockback(float xzKB, float yKB, int attackDelay)
    {
        this.horizontalKB = xzKB;
        this.verticalKB = yKB;
        this.attackDelay = attackDelay;
    }

    /**
     * Gets the horizontal KB.
     * @return - The horizontal KB.
     */
    public float getHorizontalKB()
    {
        return this.horizontalKB;
    }

    /**
     * Gets the vertical KB.
     * @return - The vertical KB.
     */
    public float getVerticalKB()
    {
        return this.verticalKB;
    }

    /**
     * Gets the attack delay.
     * @return - The attack delay.
     */
    public int getAttackDelay()
    {
        return this.attackDelay;
    }

    /**
     * Updates the knockback values accordingly.
     * @param key - The key of the value you want to update.
     * @param value - The value to update.
     */
    public void update(String key, Object value)
    {
        switch(key)
        {
            case HORIZONTAL_KB:
                this.horizontalKB = (float)value;
                break;
            case VERTICAL_KB:
                this.verticalKB = (float)value;
                break;
            case SPEED_KB:
                this.attackDelay = (int)value;
                break;
        }
    }

    /**
     * Exports the value to a HashMap.
     * @return The hashmap containing the data.
     */
    @Override
    public HashMap<String, Object> export()
    {
        HashMap<String, Object> output = new HashMap<>();
        output.put(HORIZONTAL_KB, this.horizontalKB);
        output.put(VERTICAL_KB, this.verticalKB);
        output.put(SPEED_KB, this.attackDelay);
        return output;
    }

    /**
     * Decodes the object and turns it into a PVPCKnockback instance.
     * @param object - The input object value.
     * @return PvPCKnockback instance or a null value.
     */
    public static PvPCKnockback decode(Object object)
    {
        if(
                object instanceof Map
                && ((Map) object).containsKey(HORIZONTAL_KB)
                && ((Map) object).containsKey(VERTICAL_KB)
                && ((Map) object).containsKey(SPEED_KB)
        )
        {
            Object horizontalKB = ((Map) object).get(HORIZONTAL_KB);
            Object verticalKB = ((Map) object).get(VERTICAL_KB);
            Object speedKB = ((Map) object).get(SPEED_KB);

            // Checks whether or not the PvP Knockback value could be decoded.
            if(
                    horizontalKB instanceof Number
                    && verticalKB instanceof Number
                    && speedKB instanceof Number
            )
            {
                return new PvPCKnockback(
                        ((Number) horizontalKB).floatValue(),
                        ((Number) verticalKB).floatValue(),
                        ((Number) speedKB).intValue()
                );
            }
        }

        return null;
    }
}
