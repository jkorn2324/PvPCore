<?php
/**
 * Created by PhpStorm.
 * User: jkorn2324
 * Date: 2020-06-26
 * Time: 17:58
 */

declare(strict_types=1);

namespace jkorn\pvpcore\commands;


use jkorn\pvpcore\forms\PvPCoreForms;
use jkorn\pvpcore\utils\Utils;
use pocketmine\command\Command;
use pocketmine\command\CommandSender;
use pocketmine\command\utils\CommandException;
use pocketmine\Player;
use pocketmine\utils\TextFormat;

class CreateAreaCommand extends Command
{

    public function __construct()
    {
        parent::__construct("createArea", "Command used to create the PvPArea.", "Usage: /createArea", ["createarea", "areacreate", "createpvparea"]);
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

        $form = PvPCoreForms::getCreateAreaForm($sender);
        $sender->sendForm($form);
        return true;
    }
}