<?php
/**
 * Created by PhpStorm.
 * User: jkorn2324
 * Date: 2019-04-08
 * Time: 10:46
 */

declare(strict_types = 1);

namespace jkorn\pvpcore\commands\parameters;


class BaseParameter implements Parameter
{
    
    private $permission;

    private $name;
    
    private $description;

    /**
     * BaseParameter constructor.
     * @param string $n
     * @param string $basePermission
     * @param string $desc
     * @param bool $hasPerm
     */
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

    /**
     * @return string|null
     */
    public function getPermission() {
        return $this->permission;
    }

    /**
     * @return string
     */
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