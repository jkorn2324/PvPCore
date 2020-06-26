<?php
/**
 * Created by PhpStorm.
 * User: jkorn2324
 * Date: 2019-05-19
 * Time: 20:18
 */

declare(strict_types=1);

namespace jkorn\pvpcore\world\areas;


use jkorn\pvpcore\utils\IKBObject;
use jkorn\pvpcore\utils\PvPCKnockback;
use jkorn\pvpcore\utils\Utils;
use pocketmine\level\Level;
use pocketmine\math\Vector3;
use pocketmine\Player;
use pocketmine\Server;


class PvPCArea implements IKBObject
{

    /** @var Vector3 */
    private $firstPos, $secondPos;
    /** @var PvPCKnockback */
    private $knockback;

    /** @var string */
    private $name;

    /** @var bool */
    private $enabled;

    /** @var Level|null */
    private $level;

    /**
     * PvPCArea constructor.
     * @param string $name
     * @param string|Level|null $level
     * @param Vector3 $firstPos
     * @param Vector3 $secondPos
     * @param PvPCKnockback $knockback
     * @param bool $enabled
     */
    public function __construct(string $name, $level, Vector3 $firstPos, Vector3 $secondPos, PvPCKnockback $knockback, bool $enabled = true)
    {
        $this->name = $name;
        $this->firstPos = $firstPos;
        $this->secondPos = $secondPos;
        $this->level = $level instanceof Level ? $level : is_string($level) ? Server::getInstance()->getLevelByName($level) : null;
        $this->knockback = $knockback;
        $this->enabled = $enabled;

    }

    /**
     * @param Vector3 $pos
     * @param bool $pos2
     *
     * Updates the position.
     */
    public function setPosition(Vector3 $pos, bool $pos2): void
    {

        $position = &($pos2 ? $this->secondPos : $this->firstPos);
        $position = $pos;
    }

    /**
     * @param bool $result
     *
     * Sets the enable.
     */
    public function setKBEnabled(bool $result): void
    {
        $this->enabled = $result;
    }

    /**
     * @return bool
     *
     * Determines if the kb is enabled or not.
     */
    public function isKBEnabled(): bool
    {
        return $this->enabled;
    }

    /**
     * @return PvPCKnockback
     *
     * Gets the knockback values for the area.
     */
    public function getKnockback(): PvPCKnockback
    {
        return $this->knockback;
    }

    /**
     * @return string
     *
     * Gets the name of the Area.
     */
    public function getName(): string
    {
        return $this->name;
    }

    /**
     * @return Level|null
     */
    public function getLevel(): ?Level
    {
        return $this->level;
    }

    /**
     * @param Vector3 $pos
     * @return bool
     *
     * Determines if the position is within the area.
     */
    private function isWithinArea(Vector3 $pos) : bool
    {
        $maxX = Utils::getMaxValue((float)$this->firstPos->x, (float)$this->secondPos->x);
        $minX = Utils::getMinValue((float)$this->firstPos->x, (float)$this->secondPos->x);
        $maxY = Utils::getMaxValue((float)$this->firstPos->y, (float)$this->secondPos->y);
        $minY = Utils::getMinValue((float)$this->firstPos->y, (float)$this->secondPos->y);
        $maxZ = Utils::getMaxValue((float)$this->firstPos->z, (float)$this->secondPos->z);
        $minZ = Utils::getMinValue((float)$this->firstPos->z, (float)$this->secondPos->z);

        return $pos->x >= $minX && $pos->x <= $maxX
            && $pos->y >= $minY && $pos->y <= $maxY
            && $pos->z >= $minZ && $pos->z <= $maxZ;
    }

    /**
     * @return array
     *
     * Converts the area to an array.
     */
    public function toArray() : array
    {
        return [
            "enabled" => $this->enabled,
            "first-pos" => Utils::vec3ToArr($this->firstPos),
            "second-pos" => Utils::vec3ToArr($this->secondPos),
            "knockback" => $this->knockback->toArray(),
            "level" => $this->level !== null ? $this->level->getName() : null
        ];
    }

    /**
     * @param $object
     * @return bool
     *
     * Determines if the pvp area is equal to another.
     */
    public function equals($object) : bool
    {

        if ($object instanceof PvPCArea)
        {
            return $object->getName() === $this->name
                && $object->isKBEnabled() === $this->enabled;
        }

        return false;
    }


    /**
     * @param Player $player1
     * @param Player $player2
     * @return bool
     *
     * Determines if the player can use the custom knockback.
     */
    public function canUseKnocback(Player $player1, Player $player2): bool
    {
        if(!$this->level instanceof Level || !$this->enabled)
        {
            return false;
        }

        $level = $player1->getLevel();
        if(!Utils::areLevelsEqual($level, $player2->getLevel())
        || !Utils::areLevelsEqual($level, $this->level))
        {
            return false;
        }

        return $this->isWithinArea($player1->asVector3())
            && $this->isWithinArea($player2->asVector3());
    }

    /**
     * @param string $name
     * @param $data
     * @return PvPCArea|null
     *
     * Decodes the area based on the name and the data.
     */
    public static function decode(string $name, $data): ?PvPCArea
    {
        if(isset($data["enabled"], $data["first-pos"], $data["second-pos"], $data["knockback"], $data["level"]))
        {
            $server = Server::getInstance();

            $level = $data["level"];
            if($level !== null)
            {
                $server->loadLevel($level);
            }

            $enabled = (bool)$data["enabled"];
            $firstPos = Utils::arrToVec3($data["first-pos"]);
            $secondPos = Utils::arrToVec3($data["second-pos"]);
            $knockback = PvPCKnockback::decode($data["knockback"]);

            if($firstPos !== null && $secondPos !== null && $knockback !== null)
            {
                return new PvPCArea(
                    $name,
                    $level,
                    $firstPos,
                    $secondPos,
                    $knockback,
                    $enabled
                );
            }
        }

        return null;
    }

    /**
     * @param string $name
     * @param $data
     * @return PvPCArea|null
     *
     * Decodes the PvPCArea based on the data from the old information.
     */
    public static function decodeLegacy(string $name, $data): ?PvPCArea
    {
        if(isset($data["level"], $data["kb"], $data["attack-delay"], $data["enabled"], $data["first-pos"], $data["second-pos"]))
        {
            $server = Server::getInstance();

            $level = $data["level"];
            if($level !== null)
            {
                $server->loadLevel($level);
            }

            $enabled = (bool)$data["enabled"];
            $firstPos = Utils::arrToVec3($data["first-pos"]);
            $secondPos = Utils::arrToVec3($data["second-pos"]);

            if($firstPos !== null && $secondPos !== null)
            {
                return new PvPCArea(
                    $name,
                    $level,
                    $firstPos,
                    $secondPos,
                    new PvPCKnockback((float)$data["kb"], (float)$data["kb"], (int)$data["attack-delay"]),
                    $enabled
                );
            }
        }

        return null;
    }
}