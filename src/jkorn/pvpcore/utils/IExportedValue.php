<?php
/**
 * Created by PhpStorm.
 * User: jkorn2324
 * Date: 2020-06-13
 * Time: 18:11
 */

declare(strict_types=1);

namespace jkorn\pvpcore\utils;


interface IExportedValue
{

    /**
     * @return array
     *
     * Exports the value to an array.
     */
    function toArray(): array;

    /**
     * @param $object
     * @return bool
     *
     * Determines whether the objects are equivalent.
     */
    function equals($object): bool;

}