<?php
/**
 * Created by PhpStorm.
 * User: jkorn2324
 * Date: 2019-05-19
 * Time: 21:57
 */

declare(strict_types=1);

namespace jkorn\pvpcore\world\areas;


use jkorn\pvpcore\PvPCore;
use jkorn\pvpcore\utils\PvPCKnockback;
use jkorn\pvpcore\utils\Utils;
use pocketmine\math\Vector3;
use pocketmine\Player;
use pocketmine\Server;
use pocketmine\utils\TextFormat;
use stdClass;

class AreaHandler
{

    /** @var PvPCArea[] */
    private $areas;
    /** @var string */
    private $areaFile;

    /** @var Server */
    private $server;
    /** @var PvPCore */
    private $core;

    public function __construct(PvPCore $core)
    {
        $this->areas = [];

        $this->core = $core;
        $this->server = $core->getServer();

        $this->initAreas($core->getDataFolder());
    }

    /**
     * @param string $dataFolder
     *
     * Initializes the areas and stores them to the array.
     */
    private function initAreas(string $dataFolder): void
    {
        $this->areaFile = $dataFolder . "areas.json";

        if(!file_exists($this->areaFile))
        {
            $file = fopen($this->areaFile, "w");
            fclose($file);

            $this->decodeAreas($dataFolder . "config.yml", false);
            return;
        }

        $this->decodeAreas($this->areaFile);
    }

    /**
     * @param string $file
     * @param bool $json
     *
     * Decodes the areas accordingly, used for initialization only.
     */
    private function decodeAreas(string $file, bool $json = true): void
    {
        if($json)
        {
            $contents = json_decode(file_get_contents($file), true);
            if(!is_array($contents))
            {
                return;
            }

            foreach($contents as $areaName => $data)
            {
                $area = PvPCArea::decode($areaName, $data);
                if($area instanceof PvPCArea)
                {
                    $this->areas[$area->getName()] = $area;
                }
            }
        }
        else
        {
            if(!file_exists($file))
            {
                return;
            }

            $contents = yaml_parse_file($file);
            if(!isset($contents["areas"]) || !is_array($contents["areas"]))
            {
                return;
            }

            $contents = $contents["areas"];
            foreach($contents as $name => $data)
            {
                $area = PvPCArea::decodeLegacy($name, $data);
                if($area instanceof PvPCArea)
                {
                    $this->areas[$area->getName()] = $area;
                }
            }
        }
    }

    /**
     * Saves the PvPAreas to the json file.
     */
    public function save(): void
    {
        $areas = [];
        foreach($this->areas as $area)
        {
            $areas[$area->getName()] = $area->toArray();
        }

        file_put_contents($this->areaFile, json_encode($areas));
    }

    /**
     * @param stdClass|null $info
     * @param string $name
     * @param Player $player
     * @return bool - Returns true if the area is successfully created.
     *
     * Create an area based on the information and its name.
     */
    public function createArea(?stdClass $info, string $name, Player $player): bool
    {
        // Checks if area exists.
        if(isset($this->areas[$name]))
        {
            $player->sendMessage(Utils::getPrefix() . TextFormat::RED . " The area already exists!");
            return false;
        }

        if($info instanceof stdClass && isset($info->firstPos, $info->secondPos))
        {
            /** @var Vector3 $firstPos */
            $firstPos = $info->firstPos;
            /** @var Vector3 $secondPos */
            $secondPos = $info->secondPos;

            $area = new PvPCArea(
                $name,
                $player->getLevel(),
                $firstPos,
                $secondPos,
                new PvPCKnockback()
            );

            $this->areas[$area->getName()] = $area;
            return true;
        }

        $player->sendMessage(Utils::getPrefix() . TextFormat::RED . " Failed to create the area.");
        return false;
    }

    /**
     * @param PvPCArea &$area - Address to the area object.
     * @return bool - Returns true if area has been deleted.
     *
     * Deletes the area based on the name.
     */
    public function deleteArea(PvPCArea &$area): bool
    {
        if(isset($this->areas[$area->getName()]))
        {
            unset($this->areas[$area->getName()]);
            $area = null;
            return true;
        }

        return false;
    }

    /**
     * @param string $name
     * @return PvPCArea|null
     *
     * Gets a area based on the name.
     */
    public function getArea($name)
    {
        if(isset($this->areas[$name]))
        {
            return $this->areas[$name];
        }

        return null;
    }

    /**
     * @param Player $player
     * @param Player $attacker
     * @return PvPCArea|null
     *
     * Gets the area from the player's location.
     */
    public function getAreaKnockback(Player $player, Player $attacker): ?PvPCArea
    {
        foreach($this->areas as $area)
        {
            if($area->canUseKnocback($player, $attacker))
            {
                return $area;
            }
        }

        return null;
    }

    /**
     * @return array|PvPCArea[]
     *
     * Gets the areas.
     */
    public function getAreas()
    {
        return $this->areas;
    }
}