package pvpcore.forms;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.utils.TextFormat;
import pvpcore.forms.types.CallbackSimpleForm;

/**
 * This class contains all of the forms we are using.
 */
public class PvPCoreForms
{

    /**
     * Gets the PvPCore Menu form.
     * @param player - The player input.
     * @return - The PvPCore menu form.
     */
    public static CallbackSimpleForm getPvPCoreMenu(Player player)
    {
        CallbackSimpleForm form = new CallbackSimpleForm((responsePlayer, response, extraData) -> {

            if(response instanceof FormResponseSimple)
            {
                int button = ((FormResponseSimple) response).getClickedButtonId();
                FormWindow window = null;
                switch(button)
                {
                    case 0:
                        window = PvPCoreForms.getWorldsMenu(responsePlayer);
                        break;
                    case 1:
                        window = PvPCoreForms.getAreasMenu(responsePlayer);
                        break;
                }

                if(window != null)
                {
                    responsePlayer.showFormWindow(window);
                }
            }
        });

        form.setTitle(TextFormat.BOLD.toString() + "PvPCore Menu");
        form.setContent("The menu used to configure the PvPCore plugin.");

        form.addButton("Configure Worlds", ElementButtonImageData.IMAGE_DATA_TYPE_PATH,"textures/ui/dev_glyph_color.png");
        form.addButton("Configure Areas", ElementButtonImageData.IMAGE_DATA_TYPE_PATH, "textures/ui/dev_glyph_color.png");

        return form;
    }

    /**
     * Gets the worlds menu form.
     * @param player - The input player for the worlds menu.
     * @return - The CallbackSimpleForm.
     */
    public static CallbackSimpleForm getWorldsMenu(Player player)
    {
        CallbackSimpleForm form = new CallbackSimpleForm((responsePlayer, response, extraData) -> {

            if(response instanceof FormResponseSimple)
            {
                int clickedID = ((FormResponseSimple) response).getClickedButtonId();
                FormWindow window = null;
                switch(clickedID)
                {
                    case 0:
                    case 1:
                        // window = PvPCoreForms.getWorldSelectorMenu(responsePlayer, Boolean.parseBoolean(clickedID));
                        break;
                    case 2:
                        window = PvPCoreForms.getPvPCoreMenu(responsePlayer);
                        break;
                }

                if(window != null)
                {
                    responsePlayer.showFormWindow(window);
                }
            }
        });

        form.setTitle("Worlds Configuration");
        form.setContent("The worlds configuration menu.");

        form.addButton("Edit World Knockback Settings", ElementButtonImageData.IMAGE_DATA_TYPE_PATH, "textures/ui/debug_glyph_color.png");
        form.addButton("View World Knockback Settings", ElementButtonImageData.IMAGE_DATA_TYPE_PATH, "textures/ui/magnifyingGlass.png");
        form.addButton("Go Back");

        return form;
    }

    /**
     * Gets the areas form menu.
     * @param player - The player we are sending the form to.
     * @return - The areas menu form.
     */
    public static CallbackSimpleForm getAreasMenu(Player player)
    {
        CallbackSimpleForm form = new CallbackSimpleForm((responsePlayer, response, extraData) -> {

            if(response instanceof FormResponseSimple)
            {
                int buttonID = ((FormResponseSimple) response).getClickedButtonId();
                FormWindow window = null;

                switch(buttonID)
                {
                    // TODO: Create, Edit, View, Delete
                    case 4:
                        window = PvPCoreForms.getPvPCoreMenu(responsePlayer);
                        break;
                }

                if(window != null)
                {
                    responsePlayer.showFormWindow(window);
                }
            }
        });

        form.setTitle("Areas Configuration");
        form.setContent("The areas configuration menu.");

        form.addButton("Edit Area Knockback Settings", ElementButtonImageData.IMAGE_DATA_TYPE_PATH, "textures/ui/debug_glyph_color.png");
        form.addButton("View Area Knockback Settings", ElementButtonImageData.IMAGE_DATA_TYPE_PATH, "textures/ui/magnifyingGlass.png");
        form.addButton("Create New Area", ElementButtonImageData.IMAGE_DATA_TYPE_PATH, "textures/ui/color_plus.png");
        form.addButton("Delete Existing Area", ElementButtonImageData.IMAGE_DATA_TYPE_PATH, "textures/ui/realms_red_x.png");
        form.addButton("Go Back");

        return form;
    }
}
