<?php

declare(strict_types=1);

namespace jkorn\pvpcore\commands;


use jkorn\pvpcore\forms\PvPCoreForms;
use jkorn\pvpcore\utils\Utils;
use pocketmine\command\Command;
use pocketmine\command\CommandSender;
use pocketmine\Player;
use pocketmine\utils\TextFormat;

class PvPCoreCommand extends Command
{

    public function __construct()
    {
        parent::__construct("pvpcoremenu", "Displays the PvPCore Menu to the sender.", "Usage: /pvpcoremenu", ["kbmenu", "pvpmenu"]);
        parent::setPermission("pvpcore.permission.menu");
    }

    /**
     * @param CommandSender $sender
     * @param string $commandLabel
     * @param string[] $args
     *
     * @return mixed
     *
     * Executes the command.
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

        $form = PvPCoreForms::getPvPCoreMenu($sender);
        $sender->sendForm($form);
        return true;
    }
}