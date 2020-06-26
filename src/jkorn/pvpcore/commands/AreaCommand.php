<?php
/**
 * Created by PhpStorm.
 * User: jkorn2324
 * Date: 2019-05-19
 * Time: 22:16
 */

declare(strict_types=1);

namespace jkorn\pvpcore\commands;


use jkorn\pvpcore\commands\parameters\BaseParameter;
use jkorn\pvpcore\commands\parameters\Parameter;
use jkorn\pvpcore\commands\parameters\SimpleParameter;
use jkorn\pvpcore\player\PvPCPlayer;
use jkorn\pvpcore\PvPCore;
use jkorn\pvpcore\utils\Utils;
use jkorn\pvpcore\world\areas\PvPCArea;
use pocketmine\command\CommandSender;
use pocketmine\utils\TextFormat;

class AreaCommand extends BaseCommand
{

    public function __construct()
    {
        parent::__construct("pvparea", "The base area for PvPCore command.", "/pvparea help");

        // TODO: Edit PvPArea Command.

        $this->setParameters([
            [
                new BaseParameter("help", $this->getPermission(), "Lists all the pvparea commands.")
            ],
            [
                new BaseParameter("create", $this->getPermission(), "Creates a new pvparea at the current position."),
                new SimpleParameter(Parameter::NO_PERMISSION, "area-name", Parameter::PARAMTYPE_STRING)
            ],
            [
                new BaseParameter("delete", $this->getPermission(), "Deletes a current PvPArea with the given name."),
                new SimpleParameter(Parameter::NO_PERMISSION, "area-name", Parameter::PARAMTYPE_STRING)
            ],
            [
                new BaseParameter("list", $this->getPermission(), "Lists all the current PvPAreas.")
            ],
            [
                new BaseParameter("pos1", $this->getPermission(), "Sets the first position-bound of the PvPArea.")
            ],
            [
                new BaseParameter("pos2", $this->getPermission(), "Sets the second position-bound of a PvPArea."),
            ],
            [
                new BaseParameter("enable", $this->getPermission(), "Enables pvp for a PvPArea."),
                new SimpleParameter(Parameter::NO_PERMISSION, "area-name", Parameter::PARAMTYPE_STRING)
            ],
            [
                new BaseParameter("disable", $this->getPermission(), "Disables pvp for a PvPArea."),
                new SimpleParameter(Parameter::NO_PERMISSION, "area-name", Parameter::PARAMTYPE_STRING)
            ],
            [
                new BaseParameter("x-kb", $this->getPermission(), "Sets the x-knockback of the specified PvPArea."),
                new SimpleParameter(Parameter::NO_PERMISSION, "area-name", Parameter::PARAMTYPE_STRING),
                new SimpleParameter(Parameter::NO_PERMISSION, "kb", Parameter::PARAMTYPE_FLOAT)
            ],
            [
                new BaseParameter("y-kb", $this->getPermission(), "Sets the y-knockback of the specified PvPArea."),
                new SimpleParameter(Parameter::NO_PERMISSION, "area-name", Parameter::PARAMTYPE_STRING),
                new SimpleParameter(Parameter::NO_PERMISSION, "kb", Parameter::PARAMTYPE_FLOAT)
            ],
            [
                new BaseParameter("attdel", $this->getPermission(), "Sets the attack delay of the specified PvPArea."),
                new SimpleParameter(Parameter::NO_PERMISSION, "area-name", Parameter::PARAMTYPE_STRING),
                new SimpleParameter(Parameter::NO_PERMISSION, "attack-delay", Parameter::PARAMTYPE_INTEGER)
            ]
        ]);
    }

    /**
     * @param CommandSender $sender
     * @param string $label
     * @param array $args
     * @return bool|mixed
     *
     * Executes the command based.
     */
    public function execute(CommandSender $sender, string $label, array $args)
    {

        if($this->canExecute($sender, $label, $args))
        {
            $name = strval($args[0]);

            switch($name)
            {
                case "help":
                    $msg = $this->getFullUsage();
                    break;
                case "create":
                    $this->createPvPArea($sender, strval($args[1]));
                    break;
                case "delete":
                    $this->deletePvPArea($sender, strval($args[1]));
                    break;
                case "list":
                    // $msg = PvPCore::getAreaHandler()->listAreas();
                    break;
                case "pos1":
                    $this->setPosition($sender, false);
                    break;
                case "pos2":
                    $this->setPosition($sender, true);
                    break;
                case "enable":
                    $this->enableArea($sender, strval($args[1]));
                    break;
                case "disable":
                    $this->enableArea($sender, strval($args[1]), false);
                    break;
                case "x-kb":
                    $this->setKB($sender, strval($args[1]), floatval($args[2]), true);
                    break;
                case "y-kb":
                    $this->setKB($sender, strval($args[1]), floatval($args[2]), false);
                    break;
                case "attdel":
                    $this->setAttackDelay($sender, strval($args[1]), intval($args[2]));
                    break;
            }
        }

        if(isset($msg))
        {
            $sender->sendMessage($msg);
        }

        return true;
    }

    /**
     * @param CommandSender $sender
     * @param string $name
     *
     * Creates a PvP Area.
     */
    private function createPvPArea(CommandSender $sender, string $name) : void {

        if($sender instanceof PvPCPlayer)
        {
            $sender->createArea($name);
            $msg = TextFormat::GREEN . "Successfully created a new PvPArea called '{$name}.'";
        }
        else
        {
            $msg = TextFormat::RED . "Console can't use this command.";
        }

        $sender->sendMessage($msg);
    }

    /**
     * @param CommandSender $sender
     * @param string $name
     *
     * Deletes a PvP Area.
     */
    private function deletePvPArea(CommandSender $sender, string $name) : void {

        if(PvPCore::getAreaHandler()->deleteArea($name))
        {
            $msg = TextFormat::GREEN . "Successfully deleted PvPArea '{$name}'!";
        }
        else
        {
            $msg = TextFormat::RED . "PvPArea called '{$name}' does not exist.";
        }

        $sender->sendMessage($msg);
    }

    /**
     * @param CommandSender $sender
     * @param bool $pos2
     *
     * Sets the position.
     */
    private function setPosition(CommandSender $sender, bool $pos2) : void {

        if($sender instanceof PvPCPlayer)
        {
            if($pos2)
            {
                $sender->setSecondPos();
                return;
            }

            $sender->setFirstPos();
        }
        else
        {
            $msg = TextFormat::RED . "Console can't use this command.";
        }

        if(isset($msg))
        {
            $sender->sendMessage($msg);
        }
    }

    /**
     * @param CommandSender $sender
     * @param string $name
     * @param bool $enable
     *
     * Enables the pvp area.
     */
    private function enableArea(CommandSender $sender, string $name, bool $enable = true) : void {

        $area = PvPCore::getAreaHandler()->getArea($name);
        if($area instanceof PvPCArea)
        {
            $area->setKBEnabled($enable);
            $enableVal = $enable ? "enabled" : "disabled";
            $format = $enable ? TextFormat::GREEN : TextFormat::RED;
            $msg = $format . "The PvPArea '{$name}' has been {$enableVal}!";
        }
        else
        {
            $msg = TextFormat::RED . "PvPArea called '{$name}' does not exist.";
        }

        $sender->sendMessage($msg);
    }

    /**
     * @param CommandSender $sender
     * @param string $name
     * @param int $attackDel
     *
     * Attack delay.
     */
    private function setAttackDelay(CommandSender $sender, string $name, int $attackDel) : void {

        $area = PvPCore::getAreaHandler()->getArea($name);

        if($area instanceof PvPCArea)
        {
            $area->getKnockback()->update(Utils::SPEED_KB, abs($attackDel));
            $msg = TextFormat::GREEN . "The PvPArea called '$name' has been successfully updated.";
        }
        else
        {
            $msg = TextFormat::RED . "PvPArea called '$name' does not exist.";
        }

        $sender->sendMessage($msg);
    }

    /**
     * @param CommandSender $sender
     * @param string $name
     * @param float $kb
     * @param bool $xKB
     *
     * Sets the knockback of a particular pvp area.
     */
    private function setKB(CommandSender $sender, string $name, float $kb, bool $xKB) : void
    {
        $area = PvPCore::getAreaHandler()->getArea($name);
        if($area instanceof PvPCArea)
        {
            $update = $xKB ? Utils::X_KB : Utils::Y_KB;
            $area->getKnockback()->update($update, abs($kb));
            $msg = TextFormat::GREEN . "The PvPArea called '{$name}' has been successfully updated.";
        }
        else
        {
            $msg = TextFormat::RED . "PvPArea called '{$name}' does not exist.";
        }

        $sender->sendMessage($msg);
    }
}