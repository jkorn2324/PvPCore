package pvpcore.forms.def.types;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.window.FormWindowModal;
import pvpcore.forms.def.ICallbackForm;
import pvpcore.forms.def.IResponseCallback;

import java.util.HashMap;

public class CallbackModalForm extends FormWindowModal implements ICallbackForm {

    /** The callback callback. */
    private IResponseCallback callback;
    /** The extra data of the form. */
    private HashMap<String, Object> extraData;

    /**
     * The Callback Modal form constructor.
     * @param callback - The callback that we are setting the modal form to.
     */
    public CallbackModalForm(IResponseCallback callback)
    {
        super("", "", "", "");
        this.callback = callback;
        this.extraData = new HashMap<>();
    }

    /**
     * Called to handle the response of the modal form.
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
     * Sets the callback to the given callable.
     * @param callable - The callable that we are setting the callback to.
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
