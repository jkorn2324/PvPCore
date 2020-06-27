<?php
/**
 * Created by PhpStorm.
 * User: jkorn2324
 * Date: 2020-06-26
 * Time: 18:57
 */

namespace jkorn\pvpcore\commands;


use jkorn\pvpcore\player\PvPCPlayer;
use jkorn\pvpcore\utils\Utils;
use pocketmine\command\Command;
use pocketmine\command\CommandSender;
use pocketmine\command\utils\CommandException;
use pocketmine\Player;
use pocketmine\utils\TextFormat;

class PvPAreaCommand extends Command
{

    public function __construct()
    {
        parent::__construct("pvparea", "Sets the pvparea information in the player.", "Usage: /pvparea <pos1:pos2>", ["pvpareapos"]);
        parent::setPermission("pvpcore.permission.edit");
    }

    /**
     * @param CommandSender $sender
     * @param string $commandLabel
     * @param string[] $args
     *
     * @return mixed
     * @throws CommandException
     */
    public function execute(CommandSender $sender, string $commandLabel, array $args)
    {
        if (!$this->testPermission($sender)) {
            return true;
        }

        if (!$sender instanceof PvPCPlayer) {
            if ($sender instanceof Player) {
                $message = TextFormat::RED . "Internal Plugin Error. Please leave and join back to use this command.";
            } else {
                $message = TextFormat::RED . "Console can't use this command.";
            }
            $sender->sendMessage(Utils::getPrefix() . " " . $message);
            return true;
        }

        if (count($args) <= 0 || (($firstArg = $args[0]) !== "pos1" && $firstArg !== "pos2")) {
            $sender->sendMessage(Utils::getPrefix() . " " . TextFormat::RED . $this->getUsage());
            return true;
        }


        if ($firstArg === "pos1") {
            $sender->setFirstPos();
        } else {
            $sender->setSecondPos();
        }

        return true;
    }
}