<?php
/**
 * Created by PhpStorm.
 * User: jkorn2324
 * Date: 2019-04-08
 * Time: 14:03
 */

namespace jkorn\pvpcore\commands;


use jkorn\pvpcore\commands\parameters\Parameter;
use jkorn\pvpcore\commands\parameters\SimpleParameter;
use jkorn\pvpcore\commands\parameters\BaseParameter;
use jkorn\pvpcore\PvPCore;
use pocketmine\command\CommandSender;
use pocketmine\utils\TextFormat;

class PvPCommand extends BaseCommand
{

    /**
     * PvPCommand constructor.
     */
    public function __construct()
    {
        parent::__construct("pvp", "The base command of PvPCore.", "/pvp help");

        $param = new SimpleParameter(Parameter::NO_PERMISSION, "knockback|attackdelay", Parameter::PARAMTYPE_STRING);
        $param = $param->setExactValues(true);

        $params = array(
            0 => array(
                new BaseParameter("help", parent::getPermission(), "See all pvp commands.", true)
            ),

            1 => array(
                new BaseParameter("set", parent::getPermission(), "Configures the knockback settings of a level.", true),
                new SimpleParameter(Parameter::NO_PERMISSION, "level", Parameter::PARAMTYPE_STRING),
                $param,
                new SimpleParameter(Parameter::NO_PERMISSION, "value", Parameter::PARAMTYPE_ANY)
            ),
            2 => array(
                new BaseParameter("enable", parent::getPermission(), "Enables custom knockback in the specified level.", true),
                new SimpleParameter(Parameter::NO_PERMISSION, "level", Parameter::PARAMTYPE_STRING)
            ),
            3 => array(
                new BaseParameter("disable", parent::getPermission(), "Disables custom knockback in the specified level"),
                new SimpleParameter(Parameter::NO_PERMISSION, "level", Parameter::PARAMTYPE_STRING)
            )
        );
        $this->setParameters($params);
    }

    /**
     * @param CommandSender $sender
     * @param string $label
     * @param array $args
     * @return bool|mixed
     */
    public function execute(CommandSender $sender, string $label, array $args)
    {
        $msg = null;

        if (parent::canExecute($sender, $label, $args)) {
            $param = $args[0];
            switch ($param) {
                case "help":
                    $msg = $this->getFullUsage();
                    break;
                case "set":
                    $this->executeSetWorld($sender, $args[1], $args[2], $args[3]);
                    break;
                case "enable":
                    $level = $args[1];
                    $this->executeCustomKB($sender, $level, true);
                    break;
                case "disable":
                    $level = $args[1];
                    $this->executeCustomKB($sender, $level, false);
                    break;
                default:
            }
        }
        if (!is_null($msg)) $sender->sendMessage($msg);
        return true;
    }

    /**
     * @param CommandSender $sender
     * @param string $level
     * @param bool $enable
     */
    protected function executeCustomKB(CommandSender $sender, string $level, bool $enable): void
    {
        $msg = null;

        if (PvPCore::getWorldHandler()->isWorld($level)) {

            $world = PvPCore::getWorldHandler()->getWorld($level);

            $str = ($enable ? TextFormat::GREEN . "enabled" . TextFormat::RESET : TextFormat::RED . "disabled" . TextFormat::RESET);

            $world = $world->setHasCustomKB($enable);

            PvPCore::getWorldHandler()->updateWorld($world);

            $msg = TextFormat::GRAY . "Custom Knockback has been successfully " . $str . TextFormat::GRAY . "!";

        } else $msg = TextFormat::RED . "Level '$level' does not exist!";

        if (!is_null($msg)) $sender->sendMessage($msg);
    }

    /**
     * @param CommandSender $sender
     * @param $level
     * @param $setting
     * @param $value
     */
    protected function executeSetWorld(CommandSender $sender, $level, $setting, $value)
    {
        $msg = null;

        if (PvPCore::getWorldHandler()->isWorld($level)) {

            $hasUpdated = false;
            $updatedVal = "None";

            switch ($setting) {
                case "knockback":
                    $updatedVal = "knockback";
                    $value = floatval($value);
                    break;
                case "attackdelay":
                    $updatedVal = "attack-delay";
                    $value = intval($value);
                    break;
                default:
            }

            $world = PvPCore::getWorldHandler()->getWorld($level);

            if ($updatedVal !== "None") {
                if ($updatedVal === "attack-delay" and is_int($value)) {
                    $world = $world->setAttackDelayTime($value);
                    $hasUpdated = true;
                }
                if ($updatedVal === "knockback" and is_float($value)) {
                    $world = $world->setKB($value);
                    $hasUpdated = true;
                }
            }

            if ($hasUpdated) {
                PvPCore::getWorldHandler()->updateWorld($world);
                $msg = TextFormat::GREEN . $level . TextFormat::GRAY . " has been successfully updated!";
            } else {
                $msg = TextFormat::RED . $level . TextFormat::GRAY . " failed to update!";
            }

        } else $msg = TextFormat::RED . "Level '$level' does not exist!";

        if(!is_null($msg)) $sender->sendMessage($msg);
    }
}