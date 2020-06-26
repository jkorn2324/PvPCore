<?php

declare(strict_types=1);

namespace jkorn\pvpcore\world;

use jkorn\pvpcore\utils\IKBObject;
use jkorn\pvpcore\utils\PvPCKnockback;
use jkorn\pvpcore\utils\IExportedValue;
use jkorn\pvpcore\utils\Utils;
use pocketmine\Player;
use pocketmine\Server;
use pocketmine\level\Level;

/**
 * Created by PhpStorm.
 * User: jkorn2324
 * Date: 2019-04-05
 * Time: 11:17
 */

class PvPCWorld implements IKBObject
{

    /** @var Level|null */
    private $level;

    /** @var bool */
    private $customKb;

    /** @var PvPCKnockback */
    private $knockbackInfo;

    /**
     * PvPCWorld constructor.
     * @param string $lvl
     * @param bool $kb
     * @param PvPCKnockback $knockback
     */
    public function __construct(string $lvl, bool $kb, PvPCKnockback $knockback)
    {
        $this->customKb = $kb;
        $this->level = Server::getInstance()->getLevelByName($lvl);
        $this->knockbackInfo = $knockback;
    }

    /**
     * @return Level|null
     *
     * Gets the level in the world.
     */
    public function getLevel(): ?Level
    {
        return $this->level;
    }

    /**
     * @return bool
     *
     * Determines if the KB is enabled or not.
     */
    public function isCustomKBEnabled(): bool
    {
        return $this->customKb;
    }

    /**
     * @param bool $b
     *
     * Sets the custom kb.
     */
    public function setCustomKBEnabled(bool $b): void
    {
        $this->customKb = $b;
    }

    /**
     * @return PvPCKnockback
     *
     * Gets
     */
    public function getKnockback(): PvPCKnockback
    {
        return $this->knockbackInfo;
    }

    /**
     * @return array
     *
     * Converts the world to an array.
     */
    public function toArray(): array
    {
        return [
            "level" => $this->level !== null ? $this->level->getName() : null,
            "customKB" => $this->customKb,
            "kb-info" => $this->getKnockback()->toArray()
        ];
    }

    /**
     * @param $object
     * @return bool
     *
     * Determines if the objects are equivalent.
     */
    public function equals($object): bool
    {
        if($object instanceof PvPCWorld)
        {
            $level = $object->getLevel();
            if($level instanceof Level && $this->level instanceof Level)
            {
                return Utils::areLevelsEqual($this->level, $level);
            }

           return $this->knockbackInfo->equals($object->getKnockback());
        }

        return false;
    }

    /**
     * @param Player $player1
     * @param Player $player2
     * @return bool
     *
     * Determines if the players can use the custom knockback.
     */
    public function canUseKnocback(Player $player1, Player $player2): bool
    {
        if(!$this->level instanceof Level || !$this->customKb)
        {
            return false;
        }

        $level = $player1->getLevel();
        return Utils::areLevelsEqual($level, $player2->getLevel())
            && Utils::areLevelsEqual($level, $this->level);
    }
}