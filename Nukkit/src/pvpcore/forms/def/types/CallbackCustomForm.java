package pvpcore.forms.def.types;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.window.FormWindowCustom;
import pvpcore.forms.def.ICallbackForm;
import pvpcore.forms.def.IResponseCallback;

import java.util.ArrayList;
import java.util.HashMap;

public class CallbackCustomForm extends FormWindowCustom implements ICallbackForm {

    /** The callback instance of the form. */
    private IResponseCallback callback;

    /** The extra data of the callback form. */
    private HashMap<String, Object> extraData;

    /**
     * The Default callback custom form constructor.
     */
    public CallbackCustomForm()
    {
        this(null);
        this.extraData = new HashMap<>();
    }


    /**
     * The Callback custom form constructor.
     * @param callback - The callback for the form.
     */
    public CallbackCustomForm(IResponseCallback callback)
    {
        super("", new ArrayList<>());
        this.callback = callback;
        this.extraData = new HashMap<>();
    }

    /**
     * The function that handles the response of the form.
     * @param player - The player that sent the response.
     * @param response - The response set to the form.
     */
    @Override
    public void handleResponse(Player player, FormResponse response)
    {
        if(this.callback != null)
        {
            this.callback.call(player, response, this.extraData);
        }
    }

    /**
     * Sets the callback of the custom form window.
     * @param callable - The callback.
     */
    @Override
    public void setCallback(IResponseCallback callable)
    {
        this.callback = callable;
    }

    /**
     * Adds an extra value to the map.
     * @param key - The input key for the extra data.
     * @param value - The input value for the extra data.
     */
    @Override
    public void addExtraData(String key, Object value)
    {
        this.extraData.put(key, value);
    }
}
