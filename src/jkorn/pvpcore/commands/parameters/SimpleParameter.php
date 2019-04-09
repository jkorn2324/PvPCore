<?php
/**
 * Created by PhpStorm.
 * User: jkorn2324
 * Date: 2019-04-08
 * Time: 10:46
 */

declare(strict_types = 1);

namespace jkorn\pvpcore\commands\parameters;


class SimpleParameter implements Parameter
{

    private $name;

    private $permission;

    private $optional;

    private $paramType;

    private $setHasExact;

    /**
     * SimpleParameter constructor.
     * @param string $permission
     * @param string $theName
     * @param int $type
     */
    public function __construct(string $permission, string $theName, int $type)
    {
        $this->name = $theName;
        $this->optional = false;
        $this->paramType = $type;
        $this->setHasExact = false;
        if($permission === Parameter::NO_PERMISSION or is_null($permission)){
            $permission = null;
        } else {
            $this->permission = "$permission.$theName";
        }
    }

    /**
     * @param bool $b
     * @return SimpleParameter
     */
    public function setOptional(bool $b) : SimpleParameter {
        $this->optional = $b;
        return $this;
    }

    /**
     * @param bool $b
     * @return SimpleParameter
     */
    public function setExactValues(bool $b) : SimpleParameter {
        $this->setHasExact = $b;
        return $this;
    }

    /**
     * @return bool
     */
    public function hasExactValues() : bool {
        return $this->setHasExact;
    }

    /**
     * @param string $str
     * @return bool
     */
    public function isExactValue(string $str) : bool {
        $val = $this->getExactValues();
        $result = false;
        if(is_array($val)){
            foreach($val as $key){
                if(is_string($key) and $str === $key){
                    $result = true;
                    break;
                }
            }
        } else {
            if(is_string($val)) $result = $str === $val;
        }
        return $result;
    }

    /**
     * @return array|string
     */
    private function getExactValues() {
        $str = $this->name;
        if(strpos($this->name, "|")){
            $str = explode("|", $this->name);
        }
        return $str;
    }

    /**
     * @return int
     */
    public function getParameterType() : int {
        return $this->paramType;
    }

    /**
     * @return string
     */
    public function getName(): string
    {
        return $this->name;
    }

    /**
     * @return bool
     */
    public function hasPermission(): bool
    {
        return !is_null($this->permission) or $this->permission !== Parameter::NO_PERMISSION;
    }

    /**
     * @return bool
     */
    public function isOptional() : bool {
        return $this->optional;
    }
}