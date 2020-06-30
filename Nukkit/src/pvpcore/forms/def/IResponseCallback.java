package pvpcore.forms.def;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;

public interface IResponseCallback
{

    /**
     * The function used to call the callback.
     * @param player - The player that responded.
     * @param response - The form response input.
     * @param extraData - The extra data of the response.
     */
    void call(Player player, FormResponse response, Object extraData);
}
