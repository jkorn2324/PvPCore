<?php

declare(strict_types=1);

namespace jkorn\pvpcore\world;

use jkorn\pvpcore\PvPCore;
use pocketmine\level\Level;
use pocketmine\utils\Config;
use pocketmine\Server;

/**
 * Created by PhpStorm.
 * User: jkorn2324
 * Date: 2019-04-05
 * Time: 11:33
 */

class WorldHandler
{

    /* @var Config */
    private $config;

    /**
     * WorldHandler constructor.
     * @param PvPCore $core
     */
    public function __construct(PvPCore $core)
    {
        $this->config = $core->getConfig();
        /*$allWorlds = [];

        $levels = Server::getInstance()->getLevels();

        foreach($levels as $level){
            $name = $level->getName();
            $defaultAttackDelay = 10;
            $customKb = false;
            $knockback = 0.4;
            $world = new PvPCWorld($name, $customKb, $defaultAttackDelay, $knockback);
            array_push($allWorlds, $world);
        }

        $path = PvPCore::getInstance()->getDataFolder() . "/config.yml";
        $this->config = new Config($path, Config::YAML, array());

        $worldsKey = "worlds";

        if(!$this->config->exists("$worldsKey")) $this->config->set("$worldsKey", array());

        $worlds = $this->config->get("$worldsKey");

        $edited = false;

        if(is_array($worlds)){

            $size = count($allWorlds);

            for($i = 0; $i < $size; $i++){
                $world = $allWorlds[$i];
                if($world instanceof PvPCWorld) {
                    $theName = $world->getLevel()->getName();
                    if(!array_key_exists($theName, $worlds)) {
                        $worlds[$theName] = $world->toMap();
                        $edited = true;
                    }
                }
            }
        }

        if($edited === true)
            $this->config->set("$worldsKey", $worlds);


        $this->config->save();*/
    }

    /**
     * @param Level $level
     * @return PvPCWorld
     */
    public function getWorldFromLevel(Level $level) : PvPCWorld {
        return $this->getWorld($level->getName());
    }

    /**
     * @param string $level
     * @return PvPCWorld|null
     */
    public function getWorld(string $level) {
        $result = null;
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
        return $result;
    }

    /**
     * @param string $level
     * @return bool
     */
    public function isWorld(string $level) : bool {
        return !is_null($this->getWorld($level));
    }

    /**
     * @param PvPCWorld $world
     */
    public function addWorld(PvPCWorld $world) : void {
        $map = $world->toMap();
        $name = $world->getLevel()->getName();
        $worlds = $this->config->get("worlds");
        if(is_array($worlds) and !key_exists($name, $worlds)){
            $worlds[$name] = $map;
            $this->config->set("worlds", $worlds);
            $this->config->save();
        }
    }

    /**
     * @return array
     */
    private function getAllWorlds() : array {
        $worlds = array();

        if($this->config->exists("worlds") and is_array($this->config->get("worlds")))
            $worlds = $this->config->get("worlds");

        return $worlds;
    }

    /**
     * @param PvPCWorld $world
     */
    public function updateWorld(PvPCWorld $world) : void {
        $map = $world->toMap();
        $name = $world->getLevel()->getName();
        $worlds = $this->config->get("worlds");
        if(is_array($worlds) and key_exists($name, $worlds)){
            $worlds[$name] = $map;
            $this->config->set("worlds", $worlds);
            $this->config->save();
        }
    }
}