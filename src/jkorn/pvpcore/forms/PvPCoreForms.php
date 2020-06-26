<?php

declare(strict_types=1);

namespace jkorn\pvpcore\forms;


use jkorn\pvpcore\forms\types\CustomForm;
use jkorn\pvpcore\forms\types\SimpleForm;
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
        $form = new SimpleForm(function(Player $player, $data, $extraData)
        {
            if($data !== null)
            {
                switch((int)$data)
                {
                    case 0:
                        $form = self::getWorldsMenu($player);
                        break;
                    case 1:
                        $form = self::getAreasMenu($player);
                        break;
                }

                if(isset($form) && $form instanceof AbstractForm)
                {
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
        $form = new SimpleForm(function(Player $player, $data, $extraData)
        {
            if($data !== null)
            {
                switch((int)$data)
                {
                    case 0:
                    case 1:
                        $form = self::getWorldSelectorMenu($player, boolval($data));
                        break;
                    case 2:
                        $form = self::getPvPCoreMenu($player);
                        break;
                }

                if(isset($form) && $form instanceof AbstractForm)
                {
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
        $form = new SimpleForm(function(Player $player, $data, $extraData)
        {
            if($data !== null)
            {
                switch((int)$data)
                {
                    case 0:
                        $form = self::getAreaSelectorMenu($player, Utils::ACTION_EDIT_AREA);
                        break;
                    case 1:
                        $form = self::getAreaSelectorMenu($player, Utils::ACTION_VIEW_AREA);
                        break;
                    case 2:
                        $form = self::getAreaSelectorMenu($player, Utils::ACTION_DELETE_AREA);
                        break;
                    case 3:
                        // TODO: Create area form.
                        break;
                    case 5:
                        $form = self::getPvPCoreMenu($player);
                        break;
                }

                if(isset($form) && $form instanceof AbstractForm)
                {
                    $player->sendForm($form);
                }
            }
        });

        $form->setTitle("Areas Configuration");
        $form->setContent("The areas configuration menu.");

        $form->addButton("Edit Area Knockback Settings", 0, "textures/ui/debug_glyph_color.png");
        $form->addButton("View Area Knockback Settings", 0, "textures/ui/magnifyingGlass.png");
        $form->addButton("Create New Area", 0, "textures/ui/imagetaggedcornergreen.png");
        $form->addButton("Delete Existing Area", 0, "textures/ui/realms_red_x.png");
        $form->addButton("Go Back", 0, "textures/ui/");

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
        $form = new SimpleForm(function(Player $player, $data, $extraData)
        {
            if($data !== null)
            {
                /** @var PvPCWorld[] $worlds */
                $worlds = $extraData["worlds"];

                if(count($worlds) <= 0)
                {
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
        if(count($worlds) <= 0)
        {
            $form->addButton("None", 0, "textures/ui/redX1.png");
            $form->setExtraData(["worlds" => [], "view"]);
            return $form;
        }

        foreach($worlds as $world)
        {
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
        $form = new SimpleForm(function(Player $player, $data, $extraData)
        {
            if($data !== null)
            {
                /** @var PvPCArea[] $areas */
                $areas = $extraData["areas"];

                if(count($areas) <= 0)
                {
                    return;
                }

                $type = (int)$extraData["type"];
                $area = $areas[(int)$data];

                switch($type)
                {
                    case Utils::ACTION_EDIT_AREA:
                        // TODO: Edit the area.
                        break;
                    case Utils::ACTION_VIEW_AREA:
                        // TODO: View the area.
                        break;
                    case Utils::ACTION_DELETE_AREA:
                        // TODO: Delete the area.
                        break;
                }
            }
        });

        $form->setTitle("Select Area");

        $type %= 3;
        switch($type)
        {
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

        if(isset($description))
        {
            $form->setContent($description);
        }

        /** @var PvPCArea[] $areas */
        $areas = array_values(PvPCore::getAreaHandler()->getAreas());
        if(count($areas) <= 0)
        {
            $form->addButton("None", 0, "textures/ui/redX1.png");
            $form->setExtraData(["areas" => [], "type" => $type]);
            return $form;
        }

        $form->setExtraData(["areas" => $areas, "type" => $type]);

        foreach($areas as $area)
        {
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
        $form = new CustomForm(function(Player $player, $data, $extraData)
        {
            $view = (bool)$extraData["view"];
            if($view)
            {
                $form = self::getWorldSelectorMenu($player, true);
                $player->sendForm($form);
                return;
            }

            if($data !== null)
            {
                /** @var PvPCWorld $world */
                $world = $extraData["world"];
                $knockback = $world->getKnockback();

                $kbEnabled = (bool)$data[2];
                if($world->isKBEnabled() !== $kbEnabled)
                {
                    $world->setKBEnabled($kbEnabled);
                }

                $xzKB = floatval($data[3]);
                if($knockback->getXZKb() !== $xzKB)
                {
                    $knockback->update(Utils::X_KB, $xzKB);
                }

                $yKB = floatval($data[4]);
                if($knockback->getYKb() !== $yKB)
                {
                    $knockback->update(Utils::Y_KB, $yKB);
                }

                $speedKB = intval($data[5]);
                if($knockback->getSpeed() !== $speedKB)
                {
                    $knockback->update(Utils::SPEED_KB, $speedKB);
                }

                $player->sendMessage(Utils::getPrefix() . TextFormat::GREEN . " The kb has been successfully updated.");
            }
        });

        $title = $view ? "World Information" : "Edit World Configuration";
        $form->setTitle($title);

        $desc = $view ? "Displays the Knockback information of the world." : "Edit the knockback configuration of the world.";
        $form->addLabel($desc);

        if($view)
        {
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
        $form->addInput(TextFormat::WHITE . "Vertical (Y) Knockback [Default = 0.4]: ", "Default = 0.4", strval($knockbackData->getYKb()));
        $form->addInput(TextFormat::WHITE . "Attack Delay [Default = 10]: ", "Default = 10", strval($knockbackData->getSpeed()));
        $form->setExtraData(["view" => false, "world" => $world]);
        return $form;
    }
}