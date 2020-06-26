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
use pocketmine\entity\Attribute;
use pocketmine\entity\Entity;
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

    /**
     * @param Entity $attacker
     * @param float $damage
     * @param float $x
     * @param float $z
     * @param float $base
     *
     * Gives the player knockback values.
     */
    public function knockBack(Entity $attacker, float $damage, float $x, float $z, float $base = 0.4): void
    {
        $xzKB = $base; $yKb = $base;
        // TODO: Get player's pvp area and get knockback.

        $f = sqrt($x * $x + $z * $z);
        if($f <= 0)
        {
            return;
        }
        if(mt_rand() / mt_getrandmax() > $this->getAttributeMap()->getAttribute(Attribute::KNOCKBACK_RESISTANCE)->getValue())
        {
            $f = 1 / $f;

            $motion = clone $this->motion;

            $motion->x /= 2;
            $motion->y /= 2;
            $motion->z /= 2;
            $motion->x += $x * $f * $xzKB;
            $motion->y += $yKb;
            $motion->z += $z * $f * $xzKB;

            if($motion->y > $yKb){
                $motion->y = $yKb;
            }

            $this->setMotion($motion);
        }
    }
}
