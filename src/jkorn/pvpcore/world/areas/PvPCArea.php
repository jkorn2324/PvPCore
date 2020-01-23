<?php
/**
 * Created by PhpStorm.
 * User: jkorn2324
 * Date: 2019-05-19
 * Time: 20:18
 */

declare(strict_types=1);

namespace jkorn\pvpcore\world\areas;


use pocketmine\level\Level;
use pocketmine\level\Position;
use pocketmine\Player;
use pocketmine\Server;

class PvPCArea
{

    /** @var Position */
    private $firstPos;

    /** @var Position */
    private $secondPos;

    /** @var string */
    private $name;

    /** @var float */
    private $knockback;

    /** @var int */
    private $attackDelay;

    /** @var bool */
    private $enabled;

    /** @var string */
    private $level;

    public function __construct(string $name, Level $level, Position $firstPos, Position $secondPos, float $kb = 0.4, int $attackDelay = 10, bool $enabled = true)
    {
        $this->name = $name;
        $this->firstPos = $firstPos;
        $this->secondPos = $secondPos;
        $this->level = $level->getName();
        $this->knockback = $kb;
        $this->attackDelay = $attackDelay;
        $this->enabled = $enabled;

    }

    /**
     * @param Position $pos
     * @param bool $pos2
     * @return PvPCArea
     *
     * Sets the position.
     */
    public function setPosition(Position $pos, bool $pos2) : self {

        if($pos2) {
            $this->secondPos = $pos;
        } else {
            $this->firstPos = $pos;
        }
        return $this;
    }

    /**
     * @param bool $result
     * @return PvPCArea
     *
     * Sets the enable.
     */
    public function setEnabled(bool $result) : self {
        $this->enabled = $result;
        return $this;
    }

    /**
     * @param int $del
     * @return PvPCArea
     *
     * Set the attack delay.
     */
    public function setAttackDel(int $del) : self {
        $this->attackDelay = $del;
        return $this;
    }

    /**
     * @param float $kb
     * @return PvPCArea
     */
    public function setKB(float $kb) : self {
        $this->knockback = $kb;
        return $this;
    }

    /**
     * @return bool
     */
    public function canUseAreaKB() : bool {
        return $this->enabled;
    }

    /**
     * @return int
     */
    public function getAttackDelay() : int {
        return $this->attackDelay;
    }

    /**
     * @return float
     */
    public function getKnockback() : float {
        return $this->knockback;
    }

    /**
     * @param Player $player
     * @return bool
     *
     * Is the within the bounds of the area.
     */
    public function isWithinBounds(Player $player) : bool {

        $position = $player->asPosition();

        $result = false;

        if($player->getLevel() === $this->getLevel()) {

            $vals = $this->getVals();

            $resultX = $this->within(intval($position->x), $vals["min-x"], $vals["max-x"]);

            $resultY = $this->within(intval($position->y), $vals["min-y"], $vals["max-y"]);

            $resultZ = $this->within(intval($position->z), $vals["min-z"], $vals["max-z"]);

            $result = $resultX and $resultY and $resultZ;

        }

        return $result;
    }

    /**
     * @return string
     *
     * Sets the name.
     */
    public function getName() : string {
        return $this->name;
    }

    /**
     * @return Level|null
     */
    public function getLevel() : Level {
        return Server::getInstance()->getLevelByName($this->level);
    }

    /**
     * @param int $val
     * @param int $minVal
     * @param int $maxVal
     * @return bool
     */
    private function within(int $val, int $minVal, int $maxVal) : bool {
        return $val >= $minVal and $val <= $maxVal;
    }

    /**
     * @return array|int[]
     */
    private function getVals() : array {

        $first_x = intval($this->firstPos->x);

        $sec_x = intval($this->secondPos->x);

        $first_y = intval($this->firstPos->y);

        $sec_y = intval($this->secondPos->y);

        $first_z = intval($this->firstPos->z);

        $sec_z = intval($this->secondPos->z);

        $maxX = ($first_x > $sec_x) ? $first_x : $sec_x;

        $minX = ($first_x <= $sec_x) ? $first_x : $sec_x;

        $maxY = ($first_y > $sec_y) ? $first_y : $sec_y;

        $minY = ($first_y <= $sec_y) ? $first_y : $sec_y;

        $maxZ = ($first_z > $sec_z) ? $first_z : $sec_z;

        $minZ = ($first_z <= $sec_z) ? $first_z : $sec_z;

        return ["min-x" => $minX, "max-x" => $maxX, "min-y" => $minY, "max-y" => $maxY, "min-z" => $minZ, "max-z" => $maxZ];
    }

    /**
     * @param Position $pos
     * @return array
     *
     * Changes the position to an array object.
     */
    private function posToArr(Position $pos) : array {
        return [
            "x" => $pos->x,
            "y" => $pos->y,
            "z" => $pos->z
        ];
    }

    /**
     * @return array
     */
    public function toMap() : array {

        $arr = [
            "kb" => $this->knockback,
            "attack-delay" => $this->attackDelay,
            "enabled" => $this->enabled,
            "first-pos" => $this->posToArr($this->firstPos),
            "second-pos" => $this->posToArr($this->secondPos),
            "level" => $this->level
        ];

        return $arr;
    }

    /**
     * @param $object
     * @return bool
     *
     * Determines if the pvp area is equal to another.
     */
    public function equals($object) : bool{

        $result = false;

        if ($object instanceof PvPCArea)

            $result = $object->getName() === $this->name;

        return $result;
    }


}