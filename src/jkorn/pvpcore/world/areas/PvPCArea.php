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

    private $firstPos;

    private $secondPos;

    private $name;

    private $knockback;

    private $attackDelay;

    private $enabled;

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

    public function setPosition(Position $pos, bool $pos2) : self {
        if($pos2 === true)
            $this->secondPos = $pos;
        else $this->firstPos = $pos;
        return $this;
    }

    public function setEnabled(bool $result) : self {
        $this->enabled = $result;
        return $this;
    }

    public function setAttackDel(int $del) : self {
        $this->attackDelay = $del;
        return $this;
    }

    public function setKB(float $kb) : self {
        $this->knockback = $kb;
        return $this;
    }

    public function canUseAreaKB() : bool {
        return $this->enabled === true;
    }

    public function getAttackDelay() : int {
        return $this->attackDelay;
    }

    public function getKnockback() : float {
        return $this->knockback;
    }

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

    public function getName() : string {
        return $this->name;
    }

    public function getLevel() : Level {
        return Server::getInstance()->getLevelByName($this->level);
    }

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

    private function posToArr(Position $pos) : array {
        return ["x" => $pos->x, "y" => $pos->y, "z" => $pos->z];
    }

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

    public function equals($object) : bool{

        $result = false;

        if ($object instanceof PvPCArea)

            $result = $object->getName() === $this->name;

        return $result;
    }


}