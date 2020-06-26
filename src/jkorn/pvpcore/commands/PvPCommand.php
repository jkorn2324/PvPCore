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

        $this->setParameters([
            [
                new BaseParameter("help", parent::getPermission(), "See all pvp commands.", true)
            ],
            [
                new BaseParameter("set", parent::getPermission(), "Configures the knockback settings of a level.", true),
                new SimpleParameter(Parameter::NO_PERMISSION, "level", Parameter::PARAMTYPE_STRING),
                $param,
                new SimpleParameter(Parameter::NO_PERMISSION, "value", Parameter::PARAMTYPE_ANY)
            ],
            [
                new BaseParameter("enable", parent::getPermission(), "Enables custom knockback in the specified level.", true),
                new SimpleParameter(Parameter::NO_PERMISSION, "level", Parameter::PARAMTYPE_STRING)
            ],
            [
                new BaseParameter("disable", parent::getPermission(), "Disables custom knockback in the specified level"),
                new SimpleParameter(Parameter::NO_PERMISSION, "level", Parameter::PARAMTYPE_STRING)
            ]
        ]);
    }

    /**
     * @param CommandSender $sender
     * @param string $label
     * @param array $args
     * @return bool|mixed
     */
    public function execute(CommandSender $sender, string $label, array $args)
    {
        if (parent::canExecute($sender, $label, $args))
        {
            $param = $args[0];
            switch ($param)
            {
                case "help":
                    $sender->sendMessage($this->getFullUsage());
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
            }
        }

        return true;
    }

    /**
     * @param CommandSender $sender
     * @param string $level
     * @param bool $enable
     */
    protected function executeCustomKB(CommandSender $sender, string $level, bool $enable): void
    {

        $worldManager = PvPCore::getWorldHandler();

        if ($worldManager->isWorld($level)) {

            $world = PvPCore::getWorldHandler()->getPvPCWorld($level);

            $format = $enable ? TextFormat::GREEN : TextFormat::RED;

            $enabled = $enable ? "enabled" : "disabled";

            $world = $world->setCustomKBEnabled($enable);

            $worldManager->updateWorld($world);

            $msg = $format . "Custom Knockback has been successfully " . $enabled . "!";

        } else {

            $msg = TextFormat::RED . "Level '$level' does not exist!";
        }

        $sender->sendMessage($msg);
    }

    /**
     * @param CommandSender $sender
     * @param $level
     * @param $setting
     * @param $value
     */
    protected function executeSetWorld(CommandSender $sender, $level, $setting, $value)
    {
        $worldManager = PvPCore::getWorldHandler();

        if ($worldManager->isWorld($level)) {

            $hasUpdated = false;
            $updatedVal = "None";

            switch ($setting) {
                case "kb":
                case "knockback":
                    $updatedVal = "knockback";
                    $value = floatval($value);
                    break;
                case "delay":
                case "attackdelay":
                    $updatedVal = "attack-delay";
                    $value = intval($value);
                    break;
            }

            $world = $worldManager->getPvPCWorld($level);

            if ($updatedVal !== "None") {

                if ($updatedVal === "attack-delay" && PvPCore::canParse($value, true)) {
                    $world = $world->setAttackDelayTime($value);
                    $hasUpdated = true;
                }

                if ($updatedVal === "knockback" && PvPCore::canParse($value, false)) {
                    $world = $world->setKB($value);
                    $hasUpdated = true;
                }
            }

            if ($hasUpdated) {
                $worldManager->updateWorld($world);
                $msg = TextFormat::GREEN . "The level '{$level}' has been successfully updated!";
            } else {
                $msg = TextFormat::RED . "The level '{$level}' failed to update!";
            }

        } else {
            $msg = TextFormat::RED . "Level '{$level}' does not exist!";
        }

        if(isset($msg))
        {
            $sender->sendMessage($msg);
        }

    }
}