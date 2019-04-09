<?php
/**
 * Created by PhpStorm.
 * User: jkorn2324
 * Date: 2019-04-08
 * Time: 10:46
 */

declare(strict_types = 1);

namespace jkorn\pvpcore\commands\parameters;


interface Parameter
{
    public const PARAMTYPE_STRING = 0;
    public const PARAMTYPE_INTEGER = 1;
    public const PARAMTYPE_TARGET = 2;
    public const PARAMTYPE_BOOLEAN = 3;
    public const PARAMTYPE_FLOAT = 4;
    public const PARAMTYPE_ANY = 5;

    public const NO_PERMISSION = "none";

    /**
     * @return string
     */
    function getName() : string;

    /**
     * @return bool
     */
    function hasPermission() : bool;

}