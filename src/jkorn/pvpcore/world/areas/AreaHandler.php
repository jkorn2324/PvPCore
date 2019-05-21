<?php
/**
 * Created by PhpStorm.
 * User: jkorn2324
 * Date: 2019-05-19
 * Time: 21:57
 */

namespace jkorn\pvpcore\world\areas;


use jkorn\pvpcore\PvPCore;
use pocketmine\level\Position;
use pocketmine\math\Vector3;
use pocketmine\Player;
use pocketmine\Server;
use pocketmine\utils\Config;

class AreaHandler
{

    /* @var Config */
    private $config;

    public function __construct(PvPCore $core) {
        $this->config = $core->getConfig();
    }

    public function createArea(Player $player, string $name) : void {

        $firstPos = $player->asPosition()->add(16, 0, 16);

        $firstPos = $firstPos->setComponents($firstPos->x, 0, $firstPos->z);

        $secPos = $player->asPosition()->add(-16, 0, -16);

        $secPos = $secPos->setComponents($secPos->x, 256, $secPos->z);

        $firstPos = new Position($firstPos->x, $firstPos->y, $firstPos->z, $player->level);

        $secPos = new Position($secPos->x, $secPos->y, $secPos->z, $player->level);

        $area = new PvPCArea($name, $player->getLevel(), $firstPos, $secPos);

        $areas = $this->config->get("areas");

        if(!array_key_exists($name, $areas)) {
            $areas[$name] = $area->toMap();
            $this->config->set("areas", $areas);
            $this->config->save();
        }
    }

    public function deleteArea(string $name) : void {

        $areas = $this->config->get("areas");

        if(array_key_exists($name, $areas)) {
            unset($areas[$name]);
            $this->config->set("areas", $areas);
            $this->config->save();
        }
    }

    public function doesAreaExist(string $name) : bool {
        return !is_null($this->getArea($name));
    }

    /**
     * @param string $name
     * @return PvPCArea|null
     */
    public function getArea(string $name) {

        $areas = $this->config->get("areas");

        $result = null;

        if(array_key_exists($name, $areas)) {

            $area = $areas[$name];

            $level = null;
            $firstPos = null;
            $secPos = null;
            $kb = 0.4;
            $attackDel = 10;
            $enabled = true;

            if(array_key_exists("level", $area))
                $level = Server::getInstance()->getLevelByName($area["level"]);

            if(array_key_exists("kb", $area))
                $kb = floatval($area["kb"]);

            if(array_key_exists("attack-delay", $area))
                $attackDel = intval($area["attack-delay"]);

            if(array_key_exists("enabled", $area))
                $enabled = boolval($area["enabled"]);

            if(array_key_exists("first-pos", $area)) {
                $vec3 = $this->parseVec3($area["first-pos"]);
                if(!is_null($vec3) and !is_null($level)) $firstPos = new Position($vec3->x, $vec3->y, $vec3->z, $level);
            }

            if(array_key_exists("second-pos", $area)) {
                $vec3 = $this->parseVec3($area["second-pos"]);
                if(!is_null($vec3) and !is_null($level)) $secPos = new Position($vec3->x, $vec3->y, $vec3->z, $level);
            }

            $found = !is_null($secPos) and !is_null($firstPos) and !is_null($level);

            if($found === true)
                $result = new PvPCArea($name, $level, $firstPos, $secPos, $kb, $attackDel, $enabled);
        }

        return $result;
    }

    public function updateArea(string $name, PvPCArea $area) : void {

        $areas = $this->config->get("areas");

        if(array_key_exists($name, $areas)) {
            $areas[$name] = $area->toMap();
            $this->config->set("areas", $areas);
            $this->config->save();
        }
    }

    /**
     * @return array|PvPCArea[]
     */
    public function getAreas() : array {

        $areas = $this->config->get("areas");

        $keys = array_keys($areas);

        $result = [];

        foreach($keys as $key) {

            $area = $this->getArea($key);

            if(!is_null($area))
                $result[] = $area;
        }

        return $result;
    }

    /**
     * @param Player $player
     * @return PvPCArea|null
     */
    public function getClosestAreaTo(Player $player) {

        $areas = $this->getAreas();

        $result = null;

        foreach($areas as $area) {
            if($area->isWithinBounds($player)) {
                $result = $area;
                break;
            }
        }

        return $result;
    }

    public function listAreas() : string {

        $areas = $this->getAreas();

        $len = count($areas) - 1;

        $count = 0;

        $result = "List of PvPAreas: {Areas}";

        $replaced = "None";

        if($len + 1 > 0) {

            $replaced = "";

            foreach($areas as $area) {

                $comma = ($count === $len) ? "" : ", ";

                $level = $area->getLevel()->getName();

                $replaced .= $area->getName() . " ($level)" . $comma;

                $count++;
            }
        }

        return str_replace("{Areas}", $replaced, $result);
    }

    public function isInArea(Player $player) : bool {
        return !is_null($this->getClosestAreaTo($player));
    }

    public function isInSameAreas(Player $a, Player $b) : bool {

        $result = false;

        if($this->isInArea($a) and $this->isInArea($b)) {

            $bArea = $this->getClosestAreaTo($b);

            $aArea = $this->getClosestAreaTo($a);

            if(!is_null($bArea) and !is_null($aArea))
                $result = $bArea->equals($aArea);

        }

        return $result;
    }

    /**
     * @param $val
     * @return Vector3|null
     */
    private function parseVec3($val) {

        $result = null;

        if(is_array($val)) {

            $x = -1;
            $y = -1;
            $z = -1;

            if(array_key_exists("x", $val))
                $x = floatval($val["x"]);

            if(array_key_exists("y", $val))
                $y = floatval($val["y"]);

            if(array_key_exists("z", $val))
                $z = floatval($val["z"]);


            $canParse = $x !== -1 and $y !== -1 and $z !== -1;

            if($canParse === true)
                $result = new Vector3($x, $y, $z);
        }

        return $result;
    }
}