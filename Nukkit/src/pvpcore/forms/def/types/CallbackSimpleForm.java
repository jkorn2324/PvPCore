package pvpcore.forms.def.types;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.window.FormWindowSimple;
import pvpcore.forms.def.ICallbackForm;
import pvpcore.forms.def.IResponseCallback;

import java.util.ArrayList;
import java.util.HashMap;

public class CallbackSimpleForm extends FormWindowSimple implements ICallbackForm {

    /** The callable to run when the response is called. */
    private IResponseCallback callback;
    /** The extra data of the simple form. */
    private HashMap<String, Object> extraData;

    /**
     * The default simple form constructor.
     */
    public CallbackSimpleForm()
    {
        this(null);
        this.extraData = new HashMap<>();
    }

    /**
     * The default constructor
     * @param callable - The callback.
     */
    public CallbackSimpleForm(IResponseCallback callable)
    {
        super("", "", new ArrayList<>());
        this.callback = callable;
        this.extraData = new HashMap<>();
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
     * Adds an extra value to the map.
     * @param key - The input key for the extra data.
     * @param value - The input value for the extra data.
     */
    @Override
    public void addExtraData(String key, Object value)
    {
        this.extraData.put(key, value);
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
        super.addButton(button);
    }
}
