<?php
/**
 * Created by PhpStorm.
 * User: jkorn2324
 * Date: 2020-06-26
 * Time: 17:50
 */

declare(strict_types=1);

namespace jkorn\pvpcore\commands;


use jkorn\pvpcore\forms\PvPCoreForms;
use jkorn\pvpcore\PvPCore;
use jkorn\pvpcore\utils\Utils;
use pocketmine\command\Command;
use pocketmine\command\CommandSender;
use pocketmine\command\utils\CommandException;
use pocketmine\Player;
use pocketmine\utils\TextFormat;

class EditWorldCommand extends Command
{

    public function __construct()
    {
        parent::__construct("editWorld", "Sends the edit world Knockback form to the player.", "Usage: /editWorld", ["editworld", "editworldkb"]);
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
        if(!$sender instanceof Player)
        {
            $sender->sendMessage(Utils::getPrefix() . TextFormat::RED . " Console can't use this command.");
            return true;
        }

        if(!$this->testPermission($sender))
        {
            return true;
        }

        $form = PvPCoreForms::getWorldMenu(
            $sender,
            PvPCore::getWorldHandler()->getWorld($sender->getLevel()),
            false
        );

        $sender->sendForm($form);
        return true;
    }
}