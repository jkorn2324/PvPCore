package pvpcore.forms.types;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.window.FormWindowSimple;
import pvpcore.forms.ICallbackForm;
import pvpcore.forms.IResponseCallback;

import java.util.ArrayList;

public class CallbackSimpleForm extends FormWindowSimple implements ICallbackForm {

    /** The extraData to the callback form. */
    private Object extraData;
    /** The callable to run when the response is called. */
    private IResponseCallback callback;

    /**
     * The default constructor
     * @param callable - The callback.
     */
    public CallbackSimpleForm(IResponseCallback callable)
    {
        super("", "", new ArrayList<>());
        this.callback = callable;
        this.extraData = null;
    }

    @Override
    public void handleResponse(Player player, FormResponse response)
    {
        if(this.callback != null)
        {
            this.callback.call(player, response, this.extraData);
        }
    }

    /**
     * Sets the callback used when the player handles the response.
     * @param callback - The callback used for the response.
     */
    public void setCallback(IResponseCallback callback)
    {
        this.callback = callback;
    }

    /**
     * Sets the extra data of the Simple Form.
     * @param extraData - The extra data.
     */
    public void setExtraData(Object extraData)
    {
        this.extraData = extraData;
    }

    /**
     * Adds the button to the arraylist.
     * @param text - The button text.
     */
    public void addButton(String text)
    {
        this.addButton(text, "", "");
    }

    /**
     * Adds the button to the arraylist.
     * @param text - The button text.
     * @param imageType - The button image type.
     * @param path - The path of the image.
     */
    public void addButton(String text, String imageType, String path)
    {
        ElementButton button = new ElementButton(text);
        if(
                imageType.equals(ElementButtonImageData.IMAGE_DATA_TYPE_PATH)
                || imageType.equals(ElementButtonImageData.IMAGE_DATA_TYPE_URL)
        )
        {
            button.addImage(new ElementButtonImageData(imageType, path));
        }
    }
}
