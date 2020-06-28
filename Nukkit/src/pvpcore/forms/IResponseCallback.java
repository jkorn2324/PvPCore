package pvpcore.forms;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;

public interface IResponseCallback
{

    /**
     * The function used to call the callback.
     * @param player - The player that sent the response.
     * @param response - The form response input.
     * @param extraData - The extra data stored in the form.
     */
    void call(Player player, FormResponse response, Object extraData);
}
