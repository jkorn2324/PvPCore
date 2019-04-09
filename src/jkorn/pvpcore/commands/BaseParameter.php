<?php
/**
 * Created by PhpStorm.
 * User: jkorn2324
 * Date: 2019-04-08
 * Time: 10:46
 */

declare(strict_types = 1);

namespace jkorn\pvpcore\commands;


class BaseParameter implements Parameter
{
    
    private $permission;

    private $name;
    
    private $description;

    public function __construct(string $n, string $basePermission, string $desc, bool $hasPerm = true)
    {
        $this->name = $n;
        $this->description = $desc;
        if($hasPerm and $basePermission !== Parameter::NO_PERMISSION){
            $this->permission = "$basePermission." . $this->getName();
        } else {
            $this->permission = null;
        }
    }

    /**
     * @return bool
     */
    public function hasPermission() : bool
    {
        return $this->permission != null;
    }

    public function getPermission() {
        return $this->permission;
    }

    public function getDescription() : string {
        return $this->description;
    }

    /**
     * @return string
     */
    public function getName(): string
    {
        return $this->name;
    }
}