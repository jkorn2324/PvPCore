<?php

declare(strict_types=1);

namespace jkorn\pvpcore\forms;


use jkorn\pvpcore\forms\types\CustomForm;
use jkorn\pvpcore\forms\types\SimpleForm;
use jkorn\pvpcore\player\PvPCPlayer;
use jkorn\pvpcore\PvPCore;
use jkorn\pvpcore\utils\PvPCKnockback;
use jkorn\pvpcore\utils\Utils;
use jkorn\pvpcore\world\areas\PvPCArea;
use jkorn\pvpcore\world\PvPCWorld;
use pocketmine\Player;
use pocketmine\utils\TextFormat;

class PvPCoreForms
{

    /**
     * @param Player $player
     * @return SimpleForm
     *
     * Gets the main PvPCore menu.
     */
    public static function getPvPCoreMenu(Player $player): SimpleForm
    {
        $form = new SimpleForm(function (Player $player, $data, $extraData) {
            if ($data !== null) {
                switch ((int)$data) {
                    case 0:
                        $form = self::getWorldsMenu($player);
                        break;
                    case 1:
                        $form = self::getAreasMenu($player);
                        break;
                }

                if (isset($form) && $form instanceof AbstractForm) {
                    $player->sendForm($form);
                }
            }
        });

        $form->setTitle(TextFormat::BOLD . "PvPCore Menu");
        $form->setContent("The menu used to configure the PvPCore plugin.");

        $form->addButton("Configure Worlds", 0, "textures/ui/dev_glyph_color.png");
        $form->addButton("Configure Areas", 0, "textures/ui/dev_glyph_color.png");

        return $form;
    }

    /**
     * @param Player $player
     * @return SimpleForm
     */
    public static function getWorldsMenu(Player $player): SimpleForm
    {
        $form = new SimpleForm(function (Player $player, $data, $extraData) {
            if ($data !== null) {
                switch ((int)$data) {
                    case 0:
                    case 1:
                        $form = self::getWorldSelectorMenu($player, boolval($data));
                        break;
                    case 2:
                        $form = self::getPvPCoreMenu($player);
                        break;
                }

                if (isset($form) && $form instanceof AbstractForm) {
                    $player->sendForm($form);
                }
            }
        });

        $form->setTitle("Worlds Configuration");
        $form->setContent("The worlds configuration menu.");

        $form->addButton("Edit World Knockback Settings", 0, "textures/ui/debug_glyph_color.png");
        $form->addButton("View World Knockback Settings", 0, "textures/ui/magnifyingGlass.png");
        $form->addButton("Go Back");

        return $form;
    }


    /**
     * @param Player $player
     * @return SimpleForm
     *
     * Gets the areas menu.
     */
    public static function getAreasMenu(Player $player): SimpleForm
    {
        $form = new SimpleForm(function (Player $player, $data, $extraData) {
            if ($data !== null) {
                switch ((int)$data) {
                    case 0:
                        $form = self::getAreaSelectorMenu($player, Utils::ACTION_EDIT_AREA);
                        break;
                    case 1:
                        $form = self::getAreaSelectorMenu($player, Utils::ACTION_VIEW_AREA);
                        break;
                    case 2:
                        $form = self::getCreateAreaForm($player);
                        break;
                    case 3:
                        $form = self::getAreaSelectorMenu($player, Utils::ACTION_DELETE_AREA);
                        break;
                    case 4:
                        $form = self::getPvPCoreMenu($player);
                        break;
                }

                if (isset($form) && $form instanceof AbstractForm) {
                    $player->sendForm($form);
                }
            }
        });

        $form->setTitle("Areas Configuration");
        $form->setContent("The areas configuration menu.");

        $form->addButton("Edit Area Knockback Settings", 0, "textures/ui/debug_glyph_color.png");
        $form->addButton("View Area Knockback Settings", 0, "textures/ui/magnifyingGlass.png");
        $form->addButton("Create New Area", 0, "textures/ui/color_plus.png");
        $form->addButton("Delete Existing Area", 0, "textures/ui/realms_red_x.png");
        $form->addButton("Go Back");

        return $form;
    }

    /**
     * @param Player $player
     * @param bool $viewInfo
     * @return SimpleForm
     *
     * Gets the pvp core world selector menu.
     */
    public static function getWorldSelectorMenu(Player $player, bool $viewInfo = false): SimpleForm
    {
        $form = new SimpleForm(function (Player $player, $data, $extraData) {
            if ($data !== null) {
                /** @var PvPCWorld[] $worlds */
                $worlds = $extraData["worlds"];

                if (count($worlds) <= 0) {
                    return;
                }

                $world = $worlds[(int)$data];
                $viewInfo = (bool)$extraData["view"];
                $form = self::getWorldMenu($player, $world, $viewInfo);
                $player->sendForm($form);
            }
        });

        $form->setTitle("Select World");
        $form->setContent("Select the world that you want to view/configure the knockback for.");

        $worlds = PvPCore::getWorldHandler()->getWorlds();
        if (count($worlds) <= 0) {
            $form->addButton("None", 0, "textures/ui/redX1.png");
            $form->setExtraData(["worlds" => [], "view" => $viewInfo]);
            return $form;
        }

        foreach ($worlds as $world) {
            $form->addButton($world->getLocalizedLevel(), 0, "textures/ui/op.png");
        }

        $form->setExtraData(["worlds" => $worlds, "view" => $viewInfo]);
        return $form;
    }

    /**
     * @param Player $player
     * @param int $type - The type of area selector form that determines the action.
     * @return SimpleForm
     *
     * Gets the pvp core area selector menu.
     */
    public static function getAreaSelectorMenu(Player $player, int $type): SimpleForm
    {
        $form = new SimpleForm(function (Player $player, $data, $extraData) {
            if ($data !== null) {
                /** @var PvPCArea[] $areas */
                $areas = $extraData["areas"];

                if (count($areas) <= 0) {
                    return;
                }

                $type = (int)$extraData["type"];
                $area = $areas[(int)$data];

                switch ($type) {
                    case Utils::ACTION_EDIT_AREA:
                        $form = self::getAreaMenu($player, $area, $type);
                        break;
                    case Utils::ACTION_VIEW_AREA:
                        $form = self::getAreaMenu($player, $area, $type);
                        break;
                    case Utils::ACTION_DELETE_AREA:
                        $form = self::getDeleteForm($player, $area);
                        break;
                }

                if(isset($form) && $form instanceof AbstractForm)
                {
                    $player->sendForm($form);
                }
            }
        });

        $form->setTitle("Select Area");

        $type %= 3;
        switch ($type) {
            case Utils::ACTION_DELETE_AREA:
                $description = "Select the area that you want to delete.";
                break;
            case Utils::ACTION_EDIT_AREA:
                $description = "Select the area that you want to edit.";
                break;
            case Utils::ACTION_VIEW_AREA:
                $description = "Select the area that you want to view.";
                break;
        }

        if (isset($description)) {
            $form->setContent($description);
        }

        /** @var PvPCArea[] $areas */
        $areas = array_values(PvPCore::getAreaHandler()->getAreas());
        if (count($areas) <= 0) {
            $form->addButton("None", 0, "textures/ui/redX1.png");
            $form->setExtraData(["areas" => [], "type" => $type]);
            return $form;
        }

        $form->setExtraData(["areas" => $areas, "type" => $type]);

        foreach ($areas as $area) {
            $form->addButton($area->getName(), 0, "textures/ui/deop.png");
        }

        return $form;
    }

    /**
     * @param Player $player
     * @param PvPCWorld $world
     * @param bool $view
     * @return CustomForm
     *
     * Gets the world editor/display.
     */
    public static function getWorldMenu(Player $player, PvPCWorld $world, bool $view): CustomForm
    {
        $form = new CustomForm(function (Player $player, $data, $extraData) {
            $view = (bool)$extraData["view"];
            if ($view) {
                $form = self::getWorldSelectorMenu($player, true);
                $player->sendForm($form);
                return;
            }

            if ($data !== null) {
                /** @var PvPCWorld $world */
                $world = $extraData["world"];
                $knockback = $world->getKnockback();

                $kbEnabled = (bool)$data[2];
                if ($world->isKBEnabled() !== $kbEnabled) {
                    $world->setKBEnabled($kbEnabled);
                }

                $xzKB = floatval($data[3]);
                if ($knockback->getXZKb() !== $xzKB) {
                    $knockback->update(Utils::X_KB, $xzKB);
                }

                $yKB = floatval($data[4]);
                if ($knockback->getYKb() !== $yKB) {
                    $knockback->update(Utils::Y_KB, $yKB);
                }

                $speedKB = intval($data[5]);
                if ($knockback->getSpeed() !== $speedKB) {
                    $knockback->update(Utils::SPEED_KB, $speedKB);
                }

                $player->sendMessage(Utils::getPrefix() . TextFormat::GREEN . " The kb has been successfully updated.");
            }
        });

        $title = $view ? "World Information" : "Edit World Configuration";
        $form->setTitle($title);

        $desc = $view ? "Displays the Knockback information of the world." : "Edit the knockback configuration of the world.";
        $form->addLabel($desc);

        if ($view) {
            $form->addLabel(TextFormat::WHITE . "World Name: " . $world->getLocalizedLevel());
            $form->addLabel(TextFormat::WHITE . "Knockback-Enabled: " . ($world->isKBEnabled() ? "true" : "false"));
            $form->addLabel(TextFormat::WHITE . "Horizontal (X) Knockback: " . $world->getKnockback()->getXZKb());
            $form->addLabel(TextFormat::WHITE . "Vertical (Y) Knockback: " . $world->getKnockback()->getYKb());
            $form->addLabel(TextFormat::WHITE . "Attack Delay: " . $world->getKnockback()->getSpeed());
            $form->setExtraData(["view" => true]);
            return $form;
        }

        $knockbackData = $world->getKnockback();
        $form->addLabel(TextFormat::WHITE . "World Name: " . $world->getLocalizedLevel());
        $form->addToggle(TextFormat::WHITE . "Knockback-Enabled", $world->isKBEnabled());
        $form->addInput(TextFormat::WHITE . "Horizontal (X) Knockback: ", "Default = 0.4", strval($knockbackData->getXZKb()));
        $form->addInput(TextFormat::WHITE . "Vertical (Y) Knockback: ", "Default = 0.4", strval($knockbackData->getYKb()));
        $form->addInput(TextFormat::WHITE . "Attack Delay: ", "Default = 10", strval($knockbackData->getSpeed()));
        $form->setExtraData(["view" => false, "world" => $world]);
        return $form;
    }

    /**
     * @param Player $player
     * @param PvPCArea $area
     * @param int $type
     * @return CustomForm
     *
     * Gets the area edit/view menu.
     */
    public static function getAreaMenu(Player $player, PvPCArea $area, int $type): CustomForm
    {
        $form = new CustomForm(function (Player $player, $data, $extraData) {

            $type = (int)$extraData["type"];
            if ($type === Utils::ACTION_VIEW_AREA) {
                $form = self::getAreaSelectorMenu($player, $type);
                $player->sendForm($form);
                return;
            }

            if ($data !== null) {
                /** @var PvPCArea $area */
                $area = $extraData["area"];
                $knockback = $area->getKnockback();

                $kbEnabled = (bool)$data[2];
                if ($area->isKBEnabled() !== $kbEnabled) {
                    $area->setKBEnabled($kbEnabled);
                }

                $xzKB = floatval($data[3]);
                if ($knockback->getXZKb() !== $xzKB) {
                    $knockback->update(Utils::X_KB, $xzKB);
                }

                $yKB = floatval($data[4]);
                if ($knockback->getYKb() !== $yKB) {
                    $knockback->update(Utils::Y_KB, $yKB);
                }

                $speedKB = intval($data[5]);
                if ($knockback->getSpeed() !== $speedKB) {
                    $knockback->update(Utils::SPEED_KB, $speedKB);
                }

                $player->sendMessage(Utils::getPrefix() . TextFormat::GREEN . " The kb has been successfully updated.");
            }
        });

        $title = $type === Utils::ACTION_VIEW_AREA ? "Area Information" : "Edit Area Configuration";
        $form->setTitle($title);

        $desc = $type === Utils::ACTION_VIEW_AREA ? "Displays the knockback information of the area." : "Edit the knockback configuration of the area.";
        $form->addLabel($desc);

        if ($type === Utils::ACTION_VIEW_AREA) {
            $form->addLabel(TextFormat::WHITE . "Area Name: " . $area->getName());
            $form->addLabel(TextFormat::WHITE . "Knockback-Enabled: " . ($area->isKBEnabled() ? "true" : "false"));
            $form->addLabel(TextFormat::WHITE . "Horizontal (X) Knockback: " . $area->getKnockback()->getXZKb());
            $form->addLabel(TextFormat::WHITE . "Vertical (Y) Knockback: " . $area->getKnockback()->getYKb());
            $form->addLabel(TextFormat::WHITE . "Attack Delay: " . $area->getKnockback()->getSpeed());
            $form->setExtraData(["type" => $type]);
            return $form;
        }

        $knockbackData = $area->getKnockback();
        $form->addLabel(TextFormat::WHITE . "Area Name: " . $area->getName());
        $form->addToggle(TextFormat::WHITE . "Knockback-Enabled", $area->isKBEnabled());
        $form->addInput(TextFormat::WHITE . "Horizontal (X) Knockback: ", "Default = 0.4", strval($knockbackData->getXZKb()));
        $form->addInput(TextFormat::WHITE . "Vertical (Y) Knockback: ", "Default = 0.4", strval($knockbackData->getYKb()));
        $form->addInput(TextFormat::WHITE . "Attack Delay: ", "Default = 10", strval($knockbackData->getSpeed()));
        $form->setExtraData(["type" => $type, "area" => $area]);
        return $form;
    }

    /**
     * @param Player $player
     * @return CustomForm
     *
     * The template for the create area form.
     */
    public static function getCreateAreaForm(Player $player): CustomForm
    {
        if (!$player instanceof PvPCPlayer)
        {
            $form = PvPCoreForms::getPvPAreaHelp($player);
            return $form;
        }

        $areaInformation = $player->getAreaInfo();
        if($areaInformation === null || !isset($areaInformation->firstPos, $areaInformation->secondPos))
        {
            $form = PvPCoreForms::getPvPAreaHelp($player);
            return $form;
        }

        $form = new CustomForm(function (Player $player, $data, $extraData)
        {
            if($data !== null && $player instanceof PvPCPlayer)
            {
                $areaName = strval($data[0]);
                if(trim($areaName) === "")
                {
                    $player->sendMessage(Utils::getPrefix() . TextFormat::RED . " Invalid area name.");
                    return;
                }


                $player->createArea($areaName);
            }
        });

        $form->setTitle("Create New Area");
        $form->addInput("Provide the name of the area that you want to create: ");
        return $form;
    }

    /**
     * @param Player $player
     * @return CustomForm
     *
     * Gets the PvPArea help form.
     */
    private static function getPvPAreaHelp(Player $player): CustomForm
    {
        $form = new CustomForm(function (Player $player, $data, $extraData) {
        });

        $form->setTitle("PvPArea Creation Help");
        $form->addLabel("To create a new PvPArea, you must provide the following things:\n"
            . "  => The First Position Boundary of the Area\n"
            . "  => The Second Position Boundary of the Area\n"
            . "  => The name of the PvPArea.");
        $form->addLabel("To set the first position boundary of the area, type: "
            . TextFormat::AQUA . "/pvparea pos1");
        $form->addLabel("To set the second position boundary of the area, type: "
            . TextFormat::AQUA . "/pvparea pos2");
        $form->addLabel("You provide the name of the PvPArea upon creation in the PvPArea menu.");
        $form->addLabel("You MUST provide the first position AND second position boundary before you create the PvPArea in the form menu.");

        if (!$player instanceof PvPCPlayer) {
            return $form;
        }

        $areaInfo = $player->getAreaInfo();


        if ($areaInfo === null) {
            $information = " => The First Position Boundary\n => The Second Position Boundary";
        } else {
            if (!isset($areaInfo->firstPos)) {
                $information = " => The First Position Boundary";
            }

            if (!isset($areaInfo->secondPos)) {
                if (isset($information)) {
                    $information .= "\n => The Second Position Boundary";
                } else {
                    $information = " => The Second Position Boundary";
                }
            }
        }

        if (isset($information)) {
            $form->addLabel(TextFormat::RED . "You still need to set the following information before you can create a PvPArea:" . TextFormat::WHITE . "\n{$information}");
        } else {
            $form->addLabel(TextFormat::GREEN . "You have successfully provided all of the necessary information.");
        }


        return $form;
    }

    /**
     * @param Player $player
     * @param PvPCArea $area
     * @return SimpleForm
     *
     * Sends the form used to delete the area.
     */
    public static function getDeleteForm(Player $player, PvPCArea $area): SimpleForm
    {
        $form = new SimpleForm(function(Player $player, $data, $extraData)
        {
            if($data !== null)
            {
                /** @var PvPCArea $area */
                $area = $extraData["area"];

                switch((int)$data)
                {
                    case 0:
                        PvPCore::getAreaHandler()->deleteArea($area);
                        $player->sendMessage(Utils::getPrefix() . TextFormat::RED . " You have successfully deleted the area.");
                        break;
                    case 1:
                        $form = self::getAreaSelectorMenu($player, Utils::ACTION_DELETE_AREA);
                        $player->sendForm($form);
                        break;
                }
            }
        });

        $form->setTitle("Delete Area");
        $form->setContent("Are you sure you want to delete the PvPArea? If you accept, you can't undo this action." .
            " Select 'Yes' if you want to delete the area, or 'No' if you don't want to delete the area.");
        $form->addButton("Yes", 0, "textures/ui/check.png");
        $form->addButton("No", 0, "textures/ui/cancel.png");

        $form->setExtraData(["area" => $area]);

        return $form;
    }
}