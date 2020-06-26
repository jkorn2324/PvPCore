<?php
/**
 * Created by PhpStorm.
 * User: jkorn2324
 * Date: 2020-06-11
 * Time: 22:35
 */

declare(strict_types=1);

namespace jkorn\pvpcore\utils;


use pocketmine\command\Command;
use pocketmine\level\Level;
use pocketmine\math\Vector3;
use pocketmine\Player;
use pocketmine\Server;

class Utils
{

    const X_KB = "kb-x";
    const Y_KB = "kb-y";
    const SPEED_KB = "kb-speed";

    /**
     * @param Vector3 $vec3
     * @return array
     *
     * Converts a vector3 to an array.
     */
    public static function vec3ToArr(Vector3 $vec3): array
    {
        return [
            "x" => $vec3->x,
            "y" => $vec3->y,
            "z" => $vec3->z
        ];
    }


    /**
     * @param array $info
     * @return Vector3|null
     *
     * Parses an array to a vector3.
     */
    public static function arrToVec3(array $info): ?Vector3
    {
        if(isset($info["x"], $info["y"], $info["z"]))
        {
            return new Vector3(
                $info["x"],
                $info["y"],
                $info["z"]
            );
        }
        return null;
    }

    /**
     * @param Command $command
     *
     * Registers the command.
     */
    public static function registerCommand(Command $command): void
    {
        Server::getInstance()->getCommandMap()->register($command->getName(), $command);
    }

    /**
     * @param float ...$values
     * @return float|null
     *
     * Gets the max value out of a list of values.
     */
    public static function getMaxValue(float...$values)
    {
        $maxValue = null;

        foreach($values as $value)
        {
            if($maxValue === null)
            {
                $maxValue = $value;
                continue;
            }

            if($value > $maxValue)
            {
                $maxValue = $value;
            }
        }

        return $maxValue;
    }

    /**
     * @param float ...$values
     * @return float|null
     *
     * Gets the minimum value out of a list of values.
     */
    public static function getMinValue(float...$values)
    {
        $minValue = null;

        foreach($values as $value)
        {
            if($minValue === null)
            {
                $minValue = $value;
                continue;
            }

            if($value < $minValue)
            {
                $minValue = $value;
            }
        }

        return $minValue;
    }

    /**
     * @param Level $level1
     * @param Level $level2
     * @return bool
     *
     * Determines if the levels are equivalent.
     */
    public static function areLevelsEqual(Level $level1, Level $level2)
    {
        return $level1->getId() === $level2->getId();
    }
}