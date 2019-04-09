<?php
/**
 * Created by PhpStorm.
 * User: jkorn2324
 * Date: 2019-04-08
 * Time: 11:46
 */

namespace jkorn\pvpcore\commands;


use pocketmine\command\Command;
use pocketmine\Server;

class CommandRegistry
{

    private static $commands = array();

    public static function init() : void {

        self::registerCommand("pvp", new PvPCommand());

        foreach(array_keys(self::$commands) as $key){
            if(is_string($key)){
                $command = self::$commands[$key];
                Server::getInstance()->getCommandMap()->register($key, $command);
            }
        }

    }

    public static function registerCommand(string $name, Command $cmd){
        $key = "$name";
        if(!array_key_exists($key, self::$commands)){
            self::$commands[$key] = $cmd;
        }
    }
}