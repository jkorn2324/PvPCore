package pvpcore.forms.def.types;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.window.FormWindowCustom;
import pvpcore.forms.def.ICallbackForm;
import pvpcore.forms.def.IResponseCallback;

import java.util.ArrayList;

public class CallbackCustomForm extends FormWindowCustom implements ICallbackForm {

    /** The callback instance of the form. */
    private IResponseCallback callback;

    /**
     * The Default callback custom form constructor.
     */
    public CallbackCustomForm()
    {
        this(null);
    }


    /**
     * The Callback custom form constructor.
     * @param callback - The callback for the form.
     */
    public CallbackCustomForm(IResponseCallback callback)
    {
        super("", new ArrayList<>());
        this.callback = callback;
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
            this.callback.call(player, response, null);
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
}
