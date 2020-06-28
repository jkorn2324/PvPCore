<?php
/**
 * Created by PhpStorm.
 * User: jkorn2324
 * Date: 2020-06-13
 * Time: 18:02
 */

declare(strict_types=1);

namespace jkorn\pvpcore\utils;


class PvPCKnockback implements IExportedValue
{

    /** @var float */
    private $xKB, $yKB;

    /** @var int */
    private $speed;

    public function __construct(float $x = 0.4, float $y = 0.4, int $speed = 10)
    {
        $this->xKB = $x;
        $this->yKB = $y;

        $this->speed = $speed;
    }

    /**
     * @param string $key
     * @param $value
     *
     * Updates the knockback value based on the key.
     */
    public function update(string $key, $value): void
    {
        switch ($key)
        {
            case Utils::X_KB:
                $this->xKB = (float)$value;
                break;
            case Utils::Y_KB:
                $this->yKB = (float)$value;
                break;
            case Utils::SPEED_KB:
                $this->speed = (int)$value;
                break;
        }
    }

    /**
     * @return float
     *
     * Gets the horizontal kb value.
     */
    public function getXZKb(): float
    {
        return $this->xKB;
    }

    /**
     * @return float
     *
     * Gets the vertical kb value.
     */
    public function getYKb(): float
    {
        return $this->yKB;
    }

    /**
     * @return int
     *
     * Gets the speed value.
     */
    public function getSpeed(): int
    {
        return $this->speed;
    }

    /**
     * @return array
     *
     * Exports the value to an array.
     */
    public function toArray(): array
    {
        return [
            Utils::X_KB => $this->xKB,
            Utils::Y_KB => $this->yKB,
            Utils::SPEED_KB => $this->speed
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
        if($object instanceof PvPCKnockback)
        {
            return $object->xKB === $this->xKB
                && $object->yKB === $this->yKB
                && $object->speed === $this->speed;
        }

        return false;
    }

    /**
     * @param $data
     * @return PvPCKnockback|null
     *
     * Decodes the knockback values.
     */
    public static function decode($data): ?PvPCKnockback
    {
        if(isset($data[$x = Utils::X_KB], $data[$y = Utils::Y_KB], $data[$speed = Utils::SPEED_KB]))
        {
            return new PvPCKnockback(
                (float)$data[$x],
                (float)$data[$y],
                (int)$data[$speed]
            );
        }

        return null;
    }
}