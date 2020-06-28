<?php

declare(strict_types=1);

namespace jkorn\pvpcore;

/**
 * Created by PhpStorm.
 * User: jkorn2324
 * Date: 2019-04-04
 * Time: 16:01
 */

use jkorn\pvpcore\commands\CreateAreaCommand;
use jkorn\pvpcore\commands\EditWorldCommand;
use jkorn\pvpcore\commands\PvPAreaCommand;
use jkorn\pvpcore\commands\PvPCoreCommand;
use jkorn\pvpcore\utils\Utils;
use jkorn\pvpcore\world\areas\AreaHandler;
use pocketmine\plugin\PluginBase;
use jkorn\pvpcore\world\WorldHandler;


class PvPCore extends PluginBase
{

    // TODO FIX THESE BUGS:
    // - Loading all levels.
    // - Attack Speed not working.

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

        // Loads all of the levels.
        Utils::loadLevels($this);

        self::$worldHandler = new WorldHandler($this);
        self::$areaHandler = new AreaHandler($this);

        new PvPCoreListener($this);
        $this->deleteConfig();
    }

    /**
     * Called when the PvPCore plugin is disabled.
     */
    public function onDisable()
    {
        if (self::$worldHandler instanceof WorldHandler) {
            self::$worldHandler->save();
        }

        if(self::$areaHandler instanceof AreaHandler) {
            self::$areaHandler->save();
        }
    }

    /**
     * @return PvPCore
     *
     * The static instance of the plugin.
     */
    public static function getInstance(): PvPCore
    {
        return self::$instance;
    }

    /**
     * @return WorldHandler
     *
     * The world handler.
     */
    public static function getWorldHandler(): WorldHandler
    {
        return self::$worldHandler;
    }

    /**
     * @return AreaHandler
     *
     * The area manager.
     */
    public static function getAreaHandler(): AreaHandler
    {
        return self::$areaHandler;
    }

    /**
     * Deletes the old config from the plugin folder
     * if it exists.
     */
    private function deleteConfig(): void
    {
        $configFile = $this->getDataFolder() . "config.yml";
        if (file_exists($configFile)) {
            unlink($configFile);
        }
    }

    /**
     * Initializes all of the commands.
     */
    private function initCommands()
    {
        Utils::registerCommand(new PvPCoreCommand());
        Utils::registerCommand(new EditWorldCommand());
        Utils::registerCommand(new CreateAreaCommand());
        Utils::registerCommand(new PvPAreaCommand());
    }
}