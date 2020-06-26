<?php

declare(strict_types=1);

namespace jkorn\pvpcore;

/**
 * Created by PhpStorm.
 * User: jkorn2324
 * Date: 2019-04-04
 * Time: 16:01
 */

use jkorn\pvpcore\commands\AreaCommand;
use jkorn\pvpcore\commands\PvPCommand;
use jkorn\pvpcore\utils\Utils;
use jkorn\pvpcore\world\areas\AreaHandler;
use pocketmine\plugin\PluginBase;
use jkorn\pvpcore\world\WorldHandler;


class PvPCore extends PluginBase
{

    /** @var PvPCore */
    private static $instance;

    /** @var WorldHandler */
    private static $worldHandler;
    /** @var AreaHandler */
    private static $areaHandler;

    public function onEnable()
    {
        self::$instance = $this;

        $this->initCommands();
        $this->initConfig();

        // Loads all of the levels.
        Utils::loadLevels($this);

        if(!self::$worldHandler instanceof WorldHandler)
        {
            self::$worldHandler = new WorldHandler($this);
        }

        if(!self::$areaHandler instanceof AreaHandler)
        {
            self::$areaHandler = new AreaHandler($this);
        }

        new PvPCoreListener($this);
    }

    /**
     * Called when the PvPCore plugin is disabled.
     */
    public function onDisable()
    {
        if(self::$worldHandler instanceof WorldHandler)
        {
            self::$worldHandler->save();
        }
    }

    /**
     * @return PvPCore
     *
     * The static instance of the plugin.
     */
    public static function getInstance() : PvPCore
    {
        return self::$instance;
    }

    /**
     * @return WorldHandler
     *
     * The world handler.
     */
    public static function getWorldHandler() : WorldHandler
    {
        return self::$worldHandler;
    }

    /**
     * @return AreaHandler
     *
     * The area manager.
     */
    public static function getAreaHandler() : AreaHandler
    {
        return self::$areaHandler;
    }

    /**
     * Initializes the config.
     */
    private function initConfig() : void
    {
        /* $levels = $this->getServer()->getLevels();

        $allWorlds = [];

        foreach($levels as $level) {
            $name = $level->getName();
            $defaultAttackDelay = 10;
            $customKb = false;
            $knockback = 0.4;
            $world = new PvPCWorld($name, $customKb, $defaultAttackDelay, $knockback);
            array_push($allWorlds, $world);
        }

        $file = $this->getDataFolder() . "/config.yml";
        $config = new Config($file, Config::YAML, []);

        $worldsKey = "worlds";
        $areasKey = "areas";

        if(!$config->exists($worldsKey)) {
            $config->set($worldsKey, []);
            $config->save();
        }

        if(!$config->exists($areasKey)) {
            $config->set($areasKey, []);
            $config->save();
        }

        $worlds = $config->get($worldsKey);

        $edited = false;

        foreach($allWorlds as $world) {
            if($world instanceof PvPCWorld) {
                $name = $world->getLevel()->getName();
                if(!array_key_exists($name, $worlds)) {
                    $worlds[$name] = $world->toMap();
                    $edited = true;
                }
            }
        }

        if($edited === true) {
            $config->set("$worldsKey", $worlds);
            $config->save();
        } */
    }

    /**
     * @param string $s
     * @param bool $isInteger
     * @return bool
     */
    public static function canParse($s, bool $isInteger): bool
    {

        if(is_string($s)) {

            $canParse = is_numeric($s);

        } else {
            if($isInteger){
                $canParse = is_int($s);
            } else {
                $canParse = is_float($s);
            }
        }

        return $canParse;
    }

    /**
     * Initializes all of the commands.
     */
    private function initCommands()
    {
        Utils::registerCommand(new PvPCommand());
        Utils::registerCommand(new AreaCommand());
    }
}