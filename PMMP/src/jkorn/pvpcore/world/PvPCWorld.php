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
    /** @var string */
    private $localizedLevel;

    /**
     * PvPCWorld constructor.
     * @param string $lvl
     * @param bool $kb
     * @param PvPCKnockback $knockback
     */
    public function __construct(string $lvl, bool $kb, PvPCKnockback $knockback)
    {
        $this->customKb = $kb;
        $this->localizedLevel = $lvl;
        $this->level = Server::getInstance()->getLevelByName($lvl);
        $this->knockbackInfo = $knockback;
    }

    /**
     * @return string
     *
     * Gets the localized name of the level.
     */
    public function getLocalizedLevel(): string
    {
        return $this->localizedLevel;
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
     * @param Level $level
     *
     * Called to reload the level information.
     */
    public function setLevel(Level $level): void
    {
        $this->localizedLevel = $level->getName();
        $this->level = $level;
    }


    /**
     * @return bool
     *
     * Determines if the KB is enabled or not.
     */
    public function isKBEnabled(): bool
    {
        return $this->customKb;
    }

    /**
     * @param bool $b
     *
     * Sets the custom kb.
     */
    public function setKBEnabled(bool $b): void
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
            "kbEnabled" => $this->customKb,
            "kbInfo" => $this->getKnockback()->toArray()
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
        if ($object instanceof PvPCWorld) {
            $level = $object->getLevel();
            if ($level instanceof Level && $this->level instanceof Level) {
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
        if (!$this->level instanceof Level || !$this->customKb) {
            return false;
        }

        return Utils::areLevelsEqual($player1->getLevel(), $this->level)
            && Utils::areLevelsEqual($player2->getLevel(), $this->level);
    }

    /**
     * @param string $worldName
     * @param array $data
     * @return PvPCWorld|null
     *
     * Decodes the data & turns it into a PvPCWorld object according to the new format.
     */
    public static function decode(string $worldName, array $data): ?PvPCWorld
    {
        if (isset($data["kbEnabled"], $data["kbInfo"])) {
            $kbEnabled = (bool)$data["kbEnabled"];
            $knockback = PvPCKnockback::decode($data["kbInfo"]) ?? new PvPCKnockback();
            return new PvPCWorld(
                $worldName,
                $kbEnabled,
                $knockback
            );
        }

        return null;
    }

    /**
     * @param string $worldName
     * @param $data
     * @return PvPCWorld|null
     *
     * Decodes the data and turns it into a PvPCWorld object based on the old format.
     */
    public static function decodeLegacy(string $worldName, $data): ?PvPCWorld
    {
        $customKB = true;
        $attackDelay = 10;
        $knockback = 0.4;

        if (isset($data["customKb"])) {
            $customKB = $data["customKb"];
        }

        if (isset($data["attack-delay"])) {
            $attackDelay = (int)$data["attack-delay"];
        }

        if (isset($data["knockback"])) {
            $knockback = (float)$data["knockback"];
        }

        return new PvPCWorld(
            $worldName,
            $customKB,
            new PvPCKnockback($knockback, $knockback, $attackDelay)
        );
    }
}