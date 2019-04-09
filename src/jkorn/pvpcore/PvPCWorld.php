<?php

declare(strict_types=1);

namespace jkorn\pvpcore;

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

    public function __construct(string $lvl, bool $kb, int $delayTime, float $kbSize)
    {
        $this->customKb = $kb;
        $this->attackDelayTime = $delayTime;
        $this->level = $lvl;
        $this->knockback = $kbSize;
    }

    public function getLevel() : Level {
        return Server::getInstance()->getLevelByName($this->level);
    }

    public function hasCustomKB() : bool {
        return $this->customKb;
    }

    public function getAttackDelayTime() : int {
        return $this->attackDelayTime;
    }

    public function getKnockBack() : float {
        return $this->knockback;
    }

    public function setHasCustomKB(bool $b) : PvPCWorld {
        $this->customKb = $b;
        return $this;
    }

    public function setAttackDelayTime(int $i) : PvPCWorld {
        $this->attackDelayTime = $i;
        return $this;
    }

    public function setKB(float $f) : PvPCWorld {
        $this->knockback = $f;
        return $this;
    }

    public function toMap() : array {
        $result = [
            "customKb" => $this->customKb,
            "attack-delay" => $this->attackDelayTime,
            "knockback" => $this->knockback
        ];
        return $result;
    }
}