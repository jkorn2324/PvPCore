package pvpcore.forms;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementLabel;
import cn.nukkit.form.element.ElementToggle;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.utils.TextFormat;
import pvpcore.PvPCore;
import pvpcore.forms.def.types.CallbackCustomForm;
import pvpcore.forms.def.types.CallbackSimpleForm;
import pvpcore.player.PvPCorePlayer;
import pvpcore.utils.PvPCKnockback;
import pvpcore.utils.Utils;
import pvpcore.worlds.PvPCWorld;
import pvpcore.worlds.areas.PvPCArea;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class contains all of the forms we are using.
 */
public class PvPCoreForms {

    /**
     * Gets the PvPCore Menu form.
     *
     * @param player - The player input.
     * @return - The PvPCore menu form.
     */
    public static CallbackSimpleForm getPvPCoreMenu(Player player) {

        CallbackSimpleForm form = new CallbackSimpleForm((responsePlayer, response, extraData) -> {

            if (response instanceof FormResponseSimple) {
                int button = ((FormResponseSimple) response).getClickedButtonId();
                FormWindow window = null;
                switch (button) {
                    case 0:
                        window = PvPCoreForms.getWorldsMenu(responsePlayer);
                        break;
                    case 1:
                        window = PvPCoreForms.getAreasMenu(responsePlayer);
                        break;
                }

                if (window != null) {
                    responsePlayer.showFormWindow(window);
                }
            }
        });

        form.setTitle(TextFormat.BOLD.toString() + "PvPCore Menu");
        form.setContent("The menu used to configure the PvPCore plugin.");

        form.addButton("Configure Worlds", ElementButtonImageData.IMAGE_DATA_TYPE_PATH, "textures/ui/dev_glyph_color.png");
        form.addButton("Configure Areas", ElementButtonImageData.IMAGE_DATA_TYPE_PATH, "textures/ui/dev_glyph_color.png");

        return form;
    }

    /**
     * Gets the worlds menu form.
     *
     * @param player - The input player for the worlds menu.
     * @return - The CallbackSimpleForm.
     */
    public static CallbackSimpleForm getWorldsMenu(Player player) {

        CallbackSimpleForm form = new CallbackSimpleForm((responsePlayer, response, extraData) -> {

            if (response instanceof FormResponseSimple) {
                int clickedID = ((FormResponseSimple) response).getClickedButtonId();
                FormWindow window = null;
                switch (clickedID) {
                    case 0:
                    case 1:
                        window = PvPCoreForms.getWorldSelectorForm(responsePlayer, Boolean.parseBoolean(Integer.toString(clickedID)));
                        break;
                    case 2:
                        window = PvPCoreForms.getPvPCoreMenu(responsePlayer);
                        break;
                }

                if (window != null) {
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
     *
     * @param player - The player we are sending the form to.
     * @return - The areas menu form.
     */
    public static CallbackSimpleForm getAreasMenu(Player player) {

        CallbackSimpleForm form = new CallbackSimpleForm((responsePlayer, response, extraData) -> {

            if (response instanceof FormResponseSimple) {
                int buttonID = ((FormResponseSimple) response).getClickedButtonId();
                FormWindow window = null;

                switch (buttonID) {
                    case 0:
                        window = PvPCoreForms.getAreaSelectorMenu(responsePlayer, Utils.ACTION_EDIT_AREA);
                        break;
                    case 1:
                        window = PvPCoreForms.getAreaSelectorMenu(responsePlayer, Utils.ACTION_VIEW_AREA);
                        break;
                    case 2:
                        window = PvPCoreForms.getCreateAreaForm(responsePlayer);
                        break;
                    case 3:
                        window = PvPCoreForms.getAreaSelectorMenu(responsePlayer, Utils.ACTION_DELETE_AREA);
                        break;
                    case 4:
                        window = PvPCoreForms.getPvPCoreMenu(responsePlayer);
                        break;
                }

                if (window != null) {
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

    /**
     * Gets the world selector form window.
     *
     * @param player   - The player we are sending the form window to.
     * @param viewInfo - Determines whether we are viewing or editing the kb world values.
     * @return - The Form Window.
     */
    public static CallbackSimpleForm getWorldSelectorForm(Player player, boolean viewInfo) {

        CallbackSimpleForm form = new CallbackSimpleForm((responsePlayer, response, extraData) -> {

            if (response instanceof FormResponseSimple) {

                /* ArrayList worlds = (ArrayList) ((HashMap) extraData).get("worlds");
                if (worlds.size() <= 0) {
                    return;
                }

                boolean dataViewInfo = (boolean) ((HashMap) extraData).get("viewInfo");
                PvPCWorld world = (PvPCWorld) worlds.get(((FormResponseSimple) response).getClickedButtonId());
                FormWindow menu = PvPCoreForms.getWorldMenu(responsePlayer, world, dataViewInfo);
                responsePlayer.showFormWindow(menu); */
            }
        });

        form.setTitle("Select World");
        form.setContent("Select the world that you want to view/configure the knockback for.");

        ArrayList<PvPCWorld> worlds = PvPCore.getWorldHandler().getWorlds();
        if(worlds.size() <= 0) {
            form.addButton("None", ElementButtonImageData.IMAGE_DATA_TYPE_PATH, "textures/ui/redX1.png");
            return form;
        }

        for(PvPCWorld world : worlds)
        {
            form.addButton(
                    world.getLevelName(),
                    ElementButtonImageData.IMAGE_DATA_TYPE_PATH,
                    "textures/ui/op.png"
            );
        }

        return form;
    }

    /**
     * Gets the area selector form.
     *
     * @param player - The player we are sending the form to.
     * @param type   - The type of form we are sending.
     * @return - A Callback Simple Form.
     */
    public static CallbackSimpleForm getAreaSelectorMenu(Player player, int type) {

        CallbackSimpleForm form = new CallbackSimpleForm((responsePlayer, response, extraData) -> {

            if (response instanceof FormResponseSimple) {
                /* ArrayList areas = (ArrayList) ((HashMap) extraData).get("areas");
                if (areas.size() <= 0) {
                    return;
                }

                PvPCArea area = (PvPCArea) areas.get(((FormResponseSimple) response).getClickedButtonId());
                int typeValue = (int) ((HashMap) extraData).get("type");
                FormWindow window = null;

                switch (typeValue) {
                    case Utils.ACTION_EDIT_AREA:
                    case Utils.ACTION_VIEW_AREA:
                        window = PvPCoreForms.getAreaMenu(responsePlayer, area, typeValue);
                        break;
                    case Utils.ACTION_DELETE_AREA:
                        window = PvPCoreForms.getDeleteMenu(responsePlayer, area);
                        break;
                }

                if (window != null) {
                    responsePlayer.showFormWindow(window);
                } */
            }

        });

        form.setTitle("Select Area");
        type %= 3;

        String description = null;
        switch (type) {
            case Utils.ACTION_DELETE_AREA:
                description = "Select the area that you want to delete.";
                break;
            case Utils.ACTION_EDIT_AREA:
                description = "Select the area that you want to edit.";
                break;
            case Utils.ACTION_VIEW_AREA:
                description = "Select the area that you want to view.";
                break;
        }

        if (description != null) {
            form.setContent(description);
        }

        ArrayList<PvPCArea> areas = PvPCore.getAreaHandler().getAreas();
        if (areas.size() <= 0) {
            form.addButton("None", ElementButtonImageData.IMAGE_DATA_TYPE_PATH, "textures/ui/redX1.png");
            return form;
        }

        for (PvPCArea area : areas) {
            form.addButton(area.getName(), ElementButtonImageData.IMAGE_DATA_TYPE_PATH, "textures/ui/deop.png");
        }

        return form;
    }

    /**
     * Gets the world knockback editor/display.
     *
     * @param player - The player we are sending the display to.
     * @param world  - The PvPCWorld the player is editing.
     * @param view   - The boolean that determines if we are viewing or editing the world.
     * @return - A Custom Form instance.
     */
    public static CallbackCustomForm getWorldMenu(Player player, PvPCWorld world, boolean view) {

        CallbackCustomForm form = new CallbackCustomForm((responsePlayer, response, extraData) -> {

            /* boolean viewInfo = (boolean) ((HashMap) extraData).get("view");
            if (viewInfo) {
                FormWindow window = PvPCoreForms.getWorldSelectorForm(responsePlayer, true);
                responsePlayer.showFormWindow(window);
                return;
            }

            if (response instanceof FormResponseCustom) {
                PvPCWorld pvpWorld = (PvPCWorld) ((HashMap) extraData).get("world");
                PvPCKnockback knockback = pvpWorld.getKnockback();

                int previousSpeed = knockback.getAttackDelay();
                float previousXKB = knockback.getHorizontalKB(), previousYKB = knockback.getVerticalKB();
                boolean previousEnabled = pvpWorld.isKBEnabled();

                try {

                    boolean kbEnabled = ((FormResponseCustom) response).getToggleResponse(2);
                    if (previousEnabled != kbEnabled) {
                        pvpWorld.setKBEnabled(kbEnabled);
                    }

                    Float horizontalKB = Float.parseFloat(((FormResponseCustom) response).getInputResponse(3));
                    if (!horizontalKB.equals(previousXKB)) {
                        knockback.update(PvPCKnockback.HORIZONTAL_KB, horizontalKB);
                    }

                    Float verticalKB = Float.parseFloat(((FormResponseCustom) response).getInputResponse(4));
                    if (!verticalKB.equals(previousYKB)) {
                        knockback.update(PvPCKnockback.VERTICAL_KB, verticalKB);
                    }

                    Integer speed = Integer.parseInt(((FormResponseCustom) response).getInputResponse(5));
                    if (!speed.equals(previousSpeed)) {
                        knockback.update(PvPCKnockback.SPEED_KB, speed);
                    }

                } catch (Exception e) {

                    responsePlayer.sendMessage(Utils.getPrefix() + TextFormat.RED + " Failed to update the kb of the world.");
                    pvpWorld.setKBEnabled(previousEnabled);
                    knockback.update(PvPCKnockback.SPEED_KB, previousSpeed);
                    knockback.update(PvPCKnockback.HORIZONTAL_KB, previousXKB);
                    knockback.update(PvPCKnockback.VERTICAL_KB, previousYKB);
                    return;
                }

                responsePlayer.sendMessage(Utils.getPrefix() + TextFormat.GREEN + " The kb has been successfully updated.");
            } */
        });

        String title = view ? "World Information" : "Edit World Configuration";
        form.setTitle(title);

        String desc = view ? "Displays the knockback information of the world." : "Edit the knockback configuration of the world.";
        form.addElement(new ElementLabel(desc));

        if (view) {
            form.addElement(new ElementLabel(TextFormat.WHITE.toString() + "World Name: " + world.getLevelName()));
            form.addElement(new ElementLabel(TextFormat.WHITE.toString() + "Knockback-Enabled: " + world.isKBEnabled()));
            form.addElement(new ElementLabel(TextFormat.WHITE.toString() + "Horizontal (X) Knockback: " + world.getKnockback().getHorizontalKB()));
            form.addElement(new ElementLabel(TextFormat.WHITE.toString() + "Vertical (Y) Knockback: " + world.getKnockback().getVerticalKB()));
            form.addElement(new ElementLabel(TextFormat.WHITE.toString() + "Attack Delay: " + world.getKnockback().getAttackDelay()));
            return form;
        }

        PvPCKnockback knockback = world.getKnockback();
        form.addElement(new ElementLabel(TextFormat.WHITE.toString() + "World Name: " + world.getLevelName()));
        form.addElement(new ElementToggle(TextFormat.WHITE.toString() + "Knockback-Enabled", world.isKBEnabled()));
        form.addElement(new ElementInput(TextFormat.WHITE + "Horizontal (X) Knockback: ", "Default = 0.4", Float.toString(knockback.getHorizontalKB())));
        form.addElement(new ElementInput(TextFormat.WHITE + "Vertical (Y) Knockback: ", "Default = 0.4", Float.toString(knockback.getVerticalKB())));
        form.addElement(new ElementInput(TextFormat.WHITE + "Attack Delay: ", "Default = 10", Integer.toString(knockback.getAttackDelay())));
        return form;
    }

    /**
     * Gets the area edit/view menu.
     *
     * @param player - The player we are sending the form to.
     * @param area   - The area we are editing/viewing.
     * @param type   - The type of action we are doing.
     * @return - A new Callback Custom Form instance.
     */
    public static CallbackCustomForm getAreaMenu(Player player, PvPCArea area, int type) {

        CallbackCustomForm form = new CallbackCustomForm((responsePlayer, response, extraData) -> {

            /* int typeInfo = (int) ((HashMap) extraData).get("type");
            if (typeInfo == Utils.ACTION_VIEW_AREA) {
                FormWindow window = PvPCoreForms.getWorldSelectorForm(responsePlayer, true);
                responsePlayer.showFormWindow(window);
                return;
            }

            if (response instanceof FormResponseCustom) {

                PvPCArea pvpWorld = (PvPCArea) ((HashMap) extraData).get("area");
                PvPCKnockback knockback = pvpWorld.getKnockback();

                int previousSpeed = knockback.getAttackDelay();
                float previousXKB = knockback.getHorizontalKB(), previousYKB = knockback.getVerticalKB();
                boolean previousEnabled = pvpWorld.isEnabled();

                try {

                    boolean kbEnabled = ((FormResponseCustom) response).getToggleResponse(2);
                    if (previousEnabled != kbEnabled) {
                        pvpWorld.setEnabled(kbEnabled);
                    }

                    Float horizontalKB = Float.parseFloat(((FormResponseCustom) response).getInputResponse(3));
                    if (!horizontalKB.equals(previousXKB)) {
                        knockback.update(PvPCKnockback.HORIZONTAL_KB, horizontalKB);
                    }

                    Float verticalKB = Float.parseFloat(((FormResponseCustom) response).getInputResponse(4));
                    if (!verticalKB.equals(previousYKB)) {
                        knockback.update(PvPCKnockback.VERTICAL_KB, verticalKB);
                    }

                    Integer speed = Integer.parseInt(((FormResponseCustom) response).getInputResponse(5));
                    if (!speed.equals(previousSpeed)) {
                        knockback.update(PvPCKnockback.SPEED_KB, speed);
                    }

                } catch (Exception e) {

                    responsePlayer.sendMessage(Utils.getPrefix() + TextFormat.RED + " Failed to update the kb of the area.");
                    pvpWorld.setEnabled(previousEnabled);
                    knockback.update(PvPCKnockback.SPEED_KB, previousSpeed);
                    knockback.update(PvPCKnockback.HORIZONTAL_KB, previousXKB);
                    knockback.update(PvPCKnockback.VERTICAL_KB, previousYKB);
                    return;
                }

                responsePlayer.sendMessage(Utils.getPrefix() + TextFormat.GREEN + " The kb has been successfully updated.");
            } */
        });

        String title = type == Utils.ACTION_VIEW_AREA ? "Area Information" : "Edit Area Configuration";
        form.setTitle(title);

        String description = type == Utils.ACTION_VIEW_AREA ? "Displays the knockback information of the area." : "Edit the knockback configuration of the area.";
        form.setTitle(description);

        if(type == Utils.ACTION_VIEW_AREA)
        {
            form.addElement(new ElementLabel(TextFormat.WHITE.toString() + "Area Name: " + area.getName()));
            form.addElement(new ElementLabel(TextFormat.WHITE.toString() + "Knockback-Enabled: " + area.isEnabled()));
            form.addElement(new ElementLabel(TextFormat.WHITE.toString() + "Horizontal (X) Knockback: " + area.getKnockback().getHorizontalKB()));
            form.addElement(new ElementLabel(TextFormat.WHITE.toString() + "Vertical (Y) Knockback: " + area.getKnockback().getVerticalKB()));
            form.addElement(new ElementLabel(TextFormat.WHITE.toString() + "Attack Delay: " + area.getKnockback().getAttackDelay()));
            return form;
        }

        PvPCKnockback knockback = area.getKnockback();
        form.addElement(new ElementLabel(TextFormat.WHITE.toString() + "Area Name: " + area.getName()));
        form.addElement(new ElementToggle(TextFormat.WHITE.toString() + "Knockback-Enabled", area.isEnabled()));
        form.addElement(new ElementInput(TextFormat.WHITE + "Horizontal (X) Knockback: ", "Default = 0.4", Float.toString(knockback.getHorizontalKB())));
        form.addElement(new ElementInput(TextFormat.WHITE + "Vertical (Y) Knockback: ", "Default = 0.4", Float.toString(knockback.getVerticalKB())));
        form.addElement(new ElementInput(TextFormat.WHITE + "Attack Delay: ", "Default = 10", Integer.toString(knockback.getAttackDelay())));
        return form;
    }

    /**
     * Gets the form used to create a new area.
     *
     * @param player - The player we are sending the form to.
     * @return - A new form instance.
     */
    public static CallbackCustomForm getCreateAreaForm(Player player) {

        if(!(player instanceof PvPCorePlayer))
        {
            return PvPCoreForms.getPvPAreaHelpForm(player);
        }

        HashMap<String, Object> areaInfo = ((PvPCorePlayer) player).getAreaInfo();
        if(!areaInfo.containsKey("firstPos") || !areaInfo.containsKey("secondPos"))
        {
            return PvPCoreForms.getPvPAreaHelpForm(player);
        }

        CallbackCustomForm form = new CallbackCustomForm((responsePlayer, response, extraData) -> {

            if(response instanceof FormResponseCustom && responsePlayer instanceof PvPCorePlayer)
            {
                String name = ((FormResponseCustom) response).getInputResponse(0);
                if(name.trim().equals(""))
                {
                    responsePlayer.sendMessage(Utils.getPrefix() + TextFormat.RED.toString() + " Invalid area name.");
                    return;
                }

                ((PvPCorePlayer) responsePlayer).createArea(name);
            }

        });

        form.setTitle("Create New Area");
        form.addElement(new ElementInput("Provide the name of the area that you want to create: "));
        return form;
    }

    /**
     * Gets the PvPArea help form.
     *
     * @param player - The player we are sending the form to.
     * @return - The help custom form.
     */
    public static CallbackCustomForm getPvPAreaHelpForm(Player player) {

        CallbackCustomForm form = new CallbackCustomForm();

        form.setTitle("PvPArea Creation Help");
        form.addElement(new ElementLabel("To create a new PvPArea, you must provide the following things:\n"
                + "  => The First Position Boundary of the Area\n"
                + "  => The Second Position Boundary of the Area\n"
                + "  => The name of the PvPArea."));
        form.addElement(new ElementLabel("To set the first position boundary of the area, type: "
                + TextFormat.AQUA.toString() + "/pvparea pos1"));
        form.addElement(new ElementLabel("To set the second position boundary of the area, type: "
                + TextFormat.AQUA.toString() + "/pvparea pos2"));
        form.addElement(new ElementLabel("You provide the name of the PvPArea upon creation in the PvPArea menu."));
        form.addElement(new ElementLabel("You MUST provide the first position AND second position boundary before you create the PvPArea in the form menu."));

        if (!(player instanceof PvPCorePlayer)) {
            return form;
        }

        HashMap<String, Object> areaInfo = ((PvPCorePlayer) player).getAreaInfo();
        String information = null;

        if (areaInfo.size() == 0) {
            information = " => The First Position Boundary\n => The Second Position Boundary";
        } else {
            if (!areaInfo.containsKey("firstPos")) {
                information = " => The First Position Boundary";
            }

            if (!areaInfo.containsKey("secondPos")) {
                if (information == null) {
                    information = " => The Second Position Boundary";
                } else {
                    information = "\n => The Second Position Boundary";
                }
            }
        }

        if(information != null) {
            form.addElement(new ElementLabel(TextFormat.RED.toString() + "You still need to set the following information before you can create a PvPArea:" + TextFormat.WHITE.toString() + "\n" + information));
        } else {
            form.addElement(new ElementLabel(TextFormat.GREEN + "You have successfully provided all of the necessary information."));
        }

        return form;
    }

    /**
     * Gets the delete menu.
     *
     * @param player - The player we are sending the form to.
     * @param area   - The PvPCArea form.
     * @return - A new instance of the delete menu form.
     */
    public static CallbackSimpleForm getDeleteMenu(Player player, PvPCArea area) {

        CallbackSimpleForm form = new CallbackSimpleForm((responsePlayer, response, extraData) -> {

            if (response instanceof FormResponseSimple) {

                int buttonID = ((FormResponseSimple) response).getClickedButtonId();
                switch (buttonID) {
                    case 0:
                        /* PvPCore.getAreaHandler().deleteArea(pvpArea);
                        responsePlayer.sendMessage(Utils.getPrefix() + TextFormat.RED.toString() + " You have successfully deleted the area."); */
                        break;
                    case 1:
                        FormWindow window = PvPCoreForms.getAreaSelectorMenu(responsePlayer, Utils.ACTION_DELETE_AREA);
                        responsePlayer.showFormWindow(window);
                        break;
                }
            }
        });

        form.setTitle("Delete Area");
        form.setContent("Are you sure you want to delete the PvPArea? If you accept, you can't undo this action."
                + " Select 'Yes' if you want to delete the area, or 'No' if you don't want to delete the area.");

        form.addButton("Yes", ElementButtonImageData.IMAGE_DATA_TYPE_PATH, "textures/ui/check.png");
        form.addButton("No", ElementButtonImageData.IMAGE_DATA_TYPE_PATH, "textures/ui/cancel.png");

        return form;
    }
}
