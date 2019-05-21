<?php
/**
 * Created by PhpStorm.
 * User: jkorn2324
 * Date: 2019-05-19
 * Time: 22:16
 */

namespace jkorn\pvpcore\commands;


use jkorn\pvpcore\commands\parameters\BaseParameter;
use jkorn\pvpcore\commands\parameters\Parameter;
use jkorn\pvpcore\commands\parameters\SimpleParameter;
use jkorn\pvpcore\PvPCore;
use pocketmine\command\CommandSender;
use pocketmine\Player;
use pocketmine\utils\TextFormat;

class AreaCommand extends BaseCommand
{

    public function __construct()
    {
        parent::__construct("pvparea", "The base area for PvPCore command.", "/pvparea help");

        $parameters = [
            0 => [
                new BaseParameter("help", $this->getPermission(), "Lists all the pvparea commands.")
            ],
            1 => [
                new BaseParameter("create", $this->getPermission(), "Creates a new pvparea at the current position."),
                new SimpleParameter(Parameter::NO_PERMISSION, "area-name", Parameter::PARAMTYPE_STRING)
            ],
            2 => [
                new BaseParameter("delete", $this->getPermission(), "Deletes a current PvPArea with the given name."),
                new SimpleParameter(Parameter::NO_PERMISSION, "area-name", Parameter::PARAMTYPE_STRING)
            ],
            3 => [
                new BaseParameter("list", $this->getPermission(), "Lists all the current PvPAreas.")
            ],
            4 => [
                new BaseParameter("pos1", $this->getPermission(), "Sets the first bound of a PvPArea."),
                new SimpleParameter(Parameter::NO_PERMISSION, "area-name", Parameter::PARAMTYPE_STRING)
            ],
            5 => [
                new BaseParameter("pos2", $this->getPermission(), "Sets the second bound of a PvPArea."),
                new SimpleParameter(Parameter::NO_PERMISSION, "area-name", Parameter::PARAMTYPE_STRING)
            ],
            6 => [
                new BaseParameter("enable", $this->getPermission(), "Enables pvp for a PvPArea."),
                new SimpleParameter(Parameter::NO_PERMISSION, "area-name", Parameter::PARAMTYPE_STRING)
            ],
            7 => [
                new BaseParameter("disable", $this->getPermission(), "Disables pvp for a PvPArea."),
                new SimpleParameter(Parameter::NO_PERMISSION, "area-name", Parameter::PARAMTYPE_STRING)
            ],
            8 => [
                new BaseParameter("kb", $this->getPermission(), "Sets the knockback of the specified PvPArea."),
                new SimpleParameter(Parameter::NO_PERMISSION, "area-name", Parameter::PARAMTYPE_STRING),
                new SimpleParameter(Parameter::NO_PERMISSION, "kb", Parameter::PARAMTYPE_FLOAT)
            ],
            9 => [
                new BaseParameter("attdel", $this->getPermission(), "Sets the attack delay of the specified PvPArea."),
                new SimpleParameter(Parameter::NO_PERMISSION, "area-name", Parameter::PARAMTYPE_STRING),
                new SimpleParameter(Parameter::NO_PERMISSION, "attack-delay", Parameter::PARAMTYPE_INTEGER)
            ]
        ];

        $this->setParameters($parameters);

    }

    public function execute(CommandSender $sender, string $label, array $args) {

        $msg = null;

        if($this->canExecute($sender, $label, $args)) {

            $name = strval($args[0]);

            switch($name) {
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
                    $msg = PvPCore::getAreaHandler()->listAreas();
                    break;
                case "pos1":
                    $this->setPosition($sender, strval($args[1]), false);
                    break;
                case "pos2":
                    $this->setPosition($sender, strval($args[1]), true);
                    break;
                case "enable":
                    $this->enableArea($sender, strval($args[1]));
                    break;
                case "disable":
                    $this->enableArea($sender, strval($args[1]), false);
                    break;
                case "kb":
                    $this->setKB($sender, strval($args[1]), floatval($args[2]));
                    break;
                case "attdel":
                    $this->setAttackDelay($sender, strval($args[1]), intval($args[2]));
                    break;
            }
        }

        if(!is_null($msg)) $sender->sendMessage($msg);

        return true;
    }

    private function createPvPArea(CommandSender $sender, string $name) : void {

        $msg = null;

        if($sender instanceof Player) {

            PvPCore::getAreaHandler()->createArea($sender->getPlayer(), $name);
            $msg = TextFormat::GREEN . "Successfully created a new PvPArea called '$name.'";

        } else $msg = TextFormat::RED . "Console can't use this command.";

        if(!is_null($msg)) $sender->sendMessage($msg);
    }

    private function deletePvPArea(CommandSender $sender, string $name) : void {

        $msg = null;

        if(PvPCore::getAreaHandler()->doesAreaExist($name)) {
            PvPCore::getAreaHandler()->deleteArea($name);
            $msg = TextFormat::GREEN . "Successfully deleted PvPArea '$name'!";
        } else $msg = TextFormat::RED . "PvPArea called '$name' does not exist.";

        if(!is_null($msg)) $sender->sendMessage($msg);

    }

    private function setPosition(CommandSender $sender, string $name, bool $pos2) : void {

        $msg = null;

        if($sender instanceof Player) {
            if(PvPCore::getAreaHandler()->doesAreaExist($name)) {
                $area = PvPCore::getAreaHandler()->getArea($name);
                $area = $area->setPosition($sender->asPosition(), $pos2);
                PvPCore::getAreaHandler()->updateArea($name, $area);
                $msg = TextFormat::GREEN . "The PvPArea called '$name' has been successfully updated.";
            } else $msg = TextFormat::RED . "PvPArea called '$name' does not exist.";
        } else $msg = TextFormat::RED . "Console can't use this command.";

        if(!is_null($msg)) $sender->sendMessage($msg);
    }

    private function enableArea(CommandSender $sender, string $name, bool $enable = true) : void {

        $msg = null;

        if(PvPCore::getAreaHandler()->doesAreaExist($name)) {
            $area = PvPCore::getAreaHandler()->getArea($name);
            $area = $area->setEnabled($enable);
            $enableVal = ($enable === true) ? "enabled" : "disabled";
            $format = ($enable === true) ? TextFormat::GREEN : TextFormat::RED;
            $msg = $format . "The PvPArea '$name' has been $enableVal!";
            PvPCore::getAreaHandler()->updateArea($name, $area);
        } else $msg = TextFormat::RED . "PvPArea called '$name' does not exist.";

        if(!is_null($msg)) $sender->sendMessage($msg);

    }

    private function setAttackDelay(CommandSender $sender, string $name, int $attackDel) : void {

        $msg = null;

        if(PvPCore::getAreaHandler()->doesAreaExist($name)) {

            $area = PvPCore::getAreaHandler()->getArea($name);
            $area = $area->setAttackDel(abs($attackDel));
            PvPCore::getAreaHandler()->updateArea($name, $area);
            $msg = TextFormat::GREEN . "The PvPArea called '$name' has been successfully updated.";
        } else $msg = TextFormat::RED . "PvPArea called '$name' does not exist.";

        if(!is_null($msg)) $sender->sendMessage($msg);
    }

    private function setKB(CommandSender $sender, string $name, float $kb) : void {

        $msg = null;

        if(PvPCore::getAreaHandler()->doesAreaExist($name)) {

            $area = PvPCore::getAreaHandler()->getArea($name);
            $area = $area->setKB(abs($kb));
            PvPCore::getAreaHandler()->updateArea($name, $area);
            $msg = TextFormat::GREEN . "The PvPArea called '$name' has been successfully updated.";
        } else $msg = TextFormat::RED . "PvPArea called '$name' does not exist.";

        if(!is_null($msg)) $sender->sendMessage($msg);
    }
}