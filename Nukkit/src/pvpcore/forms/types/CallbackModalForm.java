package pvpcore.forms.types;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.window.FormWindowModal;
import pvpcore.forms.ICallbackForm;
import pvpcore.forms.IResponseCallback;

public class CallbackModalForm extends FormWindowModal implements ICallbackForm {

    /** The callback callback. */
    private IResponseCallback callback;
    /** The extra data that we are setting the callback to. */
    private Object extraData;

    /**
     * The Callback Modal form constructor.
     * @param callback - The callback that we are setting the modal form to.
     */
    public CallbackModalForm(IResponseCallback callback)
    {
        super("", "", "", "");
        this.callback = callback;
        this.extraData = null;
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
     * Sets the extra data of the modal form.
     * @param extraData - The extra data object.
     */
    public void setExtraData(Object extraData)
    {
        this.extraData = extraData;
    }
}
