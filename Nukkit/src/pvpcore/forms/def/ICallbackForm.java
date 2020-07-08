package pvpcore.forms.def;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;

public interface ICallbackForm {


    /**
     * Called to handle the response of the form.
     * @param player - The player that sent the response.
     * @param response - The response set to the form.
     */
    void handleResponse(Player player, FormResponse response);

    void setCallback(IResponseCallback callable);

    void addExtraData(String key, Object value);
}

