package pvpcore.forms.types;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.window.FormWindowCustom;
import pvpcore.forms.ICallbackForm;
import pvpcore.forms.IResponseCallback;

import java.util.ArrayList;

public class CallbackCustomForm extends FormWindowCustom implements ICallbackForm {

    /** The callback instance of the form. */
    private IResponseCallback callback;
    /** The extra data of the form. */
    private Object extraData;

    /**
     * The Callback custom form constructor.
     * @param callback - The callback for the form.
     */
    public CallbackCustomForm(IResponseCallback callback)
    {
        super("", new ArrayList<>());
        this.callback = callback;
        this.extraData = null;
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
     * Sets the extra data of the form.
     * @param object - The extra data object.
     */
    public void setExtraData(Object object)
    {
        this.extraData = object;
    }
}
