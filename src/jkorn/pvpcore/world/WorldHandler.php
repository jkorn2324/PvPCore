<?php

declare(strict_types=1);

namespace jkorn\pvpcore\world;

use jkorn\pvpcore\PvPCore;
use pocketmine\level\Level;
use pocketmine\utils\Config;

/**
 * Created by PhpStorm.
 * User: jkorn2324
 * Date: 2019-04-05
 * Time: 11:33
 */

class WorldHandler
{

    /** @var PvPCWorld[]|array */
    private $worlds;

    /** @var string */
    private $path;

    /**
     * WorldHandler constructor.
     * @param PvPCore $core
     */
    public function __construct(PvPCore $core)
    {
        $this->worlds = [];
        $this->path = $core->getDataFolder() . "arenas.json";

        $this->initArenas();
    }

    /**
     * Initialize the arenas.
     */
    private function initArenas(): void
    {
        if(!file_exists($this->path))
        {
            $file = fopen($this->path, "w");
            fclose($file);
            return;
        }

        $contents = json_decode(file_get_contents($this->path), true);
        foreach($contents as $arenaName => $data)
        {
            // todo
        }
    }

    /**
     * @param string|Level $level
     * @return PvPCWorld|null
     *
     * Gets the pvp world from the level (name or instance).
     */
    public function getPvPCWorld($level)
    {
        /* $result = null;
        $worlds = $this->getAllWorlds();
        if(count($worlds) > 0){
            if(array_key_exists($level, $worlds) and is_array($worlds[$level])){
                $map = $worlds[$level];
                $customKb = null;
                $attackDelay = null;
                $knockback = null;

                if(array_key_exists("customKb", $map) and is_bool($map["customKb"])){
                    $customKb = $map["customKb"];
                }
                if(array_key_exists("attack-delay", $map) and is_int($map["attack-delay"])){
                    $attackDelay = $map["attack-delay"];
                }

                if(array_key_exists("knockback", $map) and is_float($map["knockback"])){
                    $knockback = $map["knockback"];
                }

                if(is_bool($customKb) and is_int($attackDelay) and is_float($knockback)){
                    $result = new PvPCWorld($level, $customKb, $attackDelay, $knockback);
                }
            }
        }
        return $result; */
        return null;
    }

    /**
     * @param string|Level $level
     * @return bool
     *
     * Determines if the level is registered to the pvp worlds list.
     */
    public function isWorld($level) : bool
    {
        // return !is_null($this->getWorld($level));
        return false;
    }

    /**
     * @param PvPCWorld $world
     *
     * Adds the world to the worlds list & saves it to the config.
     */
    public function addWorld(PvPCWorld $world): void
    {
        /* $map = $world->toMap();
        $name = $world->getLevel()->getName();
        $worlds = $this->config->get("worlds");
        if(is_array($worlds) and !key_exists($name, $worlds)){
            $worlds[$name] = $map;
            $this->config->set("worlds", $worlds);
            $this->config->save();
        } */
    }

    /**
     * @param PvPCWorld $world
     *
     * Updates the world to the world handler.
     */
    public function updateWorld(PvPCWorld $world): void
    {
        /* $map = $world->toMap();
        $name = $world->getLevel()->getName();
        $worlds = $this->config->get("worlds");
        if(is_array($worlds) and key_exists($name, $worlds)){
            $worlds[$name] = $map;
            $this->config->set("worlds", $worlds);
            $this->config->save();
        } */
    }
}