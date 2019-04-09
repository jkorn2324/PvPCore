<?php

declare(strict_types=1);

namespace jkorn\pvpcore\world;

use pocketmine\Server;
use pocketmine\level\Level;

/**
 * Created by PhpStorm.
 * User: jkorn2324
 * Date: 2019-04-05
 * Time: 11:17
 */

class PvPCWorld
{
    private $attackDelayTime;

    private $level;

    private $customKb;

    private $knockback;

    /**
     * PvPCWorld constructor.
     * @param string $lvl
     * @param bool $kb
     * @param int $delayTime
     * @param float $kbSize
     */
    public function __construct(string $lvl, bool $kb, int $delayTime, float $kbSize)
    {
        $this->customKb = $kb;
        $this->attackDelayTime = $delayTime;
        $this->level = $lvl;
        $this->knockback = $kbSize;
    }

    /**
     * @return Level
     */
    public function getLevel() : Level {
        return Server::getInstance()->getLevelByName($this->level);
    }

    /**
     * @return bool
     */
    public function hasCustomKB() : bool {
        return $this->customKb;
    }

    /**
     * @return int
     */
    public function getAttackDelayTime() : int {
        return $this->attackDelayTime;
    }

    /**
     * @return float
     */
    public function getKnockBack() : float {
        return $this->knockback;
    }

    /**
     * @param bool $b
     * @return PvPCWorld
     */
    public function setHasCustomKB(bool $b) : PvPCWorld {
        $this->customKb = $b;
        return $this;
    }

    /**
     * @param int $i
     * @return PvPCWorld
     */
    public function setAttackDelayTime(int $i) : PvPCWorld {
        $this->attackDelayTime = $i;
        return $this;
    }

    /**
     * @param float $f
     * @return PvPCWorld
     */
    public function setKB(float $f) : PvPCWorld {
        $this->knockback = $f;
        return $this;
    }

    /**
     * @return array
     */
    public function toMap() : array {
        $result = [
            "customKb" => $this->customKb,
            "attack-delay" => $this->attackDelayTime,
            "knockback" => $this->knockback
        ];
        return $result;
    }
}