<?php
/**
 * Created by PhpStorm.
 * User: jkorn2324
 * Date: 2020-06-11
 * Time: 23:11
 */

declare(strict_types=1);

namespace jkorn\pvpcore\player;

use jkorn\pvpcore\PvPCore;
use jkorn\pvpcore\world\areas\PvPCArea;
use pocketmine\Player;
use pocketmine\utils\TextFormat;
use stdClass;

class PvPCPlayer extends Player
{

    /** @var stdClass|null - The pvp area info. */
    private $pvpAreaInfo = null;

    /**
     * Sets the first position of the area information.
     */
    public function setFirstPos(): void
    {
        if($this->pvpAreaInfo === null)
        {
            $this->pvpAreaInfo = new stdClass();
        }

        $this->pvpAreaInfo->firstPos = $this->asVector3();

        $this->sendMessage(TextFormat::GREEN . "Successfully set the first position of the PvPArea.");
    }

    /**
     * Sets the second position of the area information.
     */
    public function setSecondPos(): void
    {
        if($this->pvpAreaInfo === null)
        {
            $this->pvpAreaInfo = new stdClass();
        }

        $this->pvpAreaInfo->secondPos = $this->asVector3();

        $this->sendMessage(TextFormat::GREEN . "Successfully set the second position of the PvPArea.");
    }

    /**
     * @param string $name
     *
     * Creates the area based on the name.
     */
    public function createArea(string $name): void
    {
        if(PvPCore::getAreaHandler()->createArea($this->pvpAreaInfo, $this->getLevel(), $name))
        {
            $this->sendMessage(TextFormat::GREEN . "Successfully created a new PvPArea.");

            $this->pvpAreaInfo = null;
            return;
        }

        $this->sendMessage(TextFormat::RED . "Failed to create the PvPArea.");
    }
}
