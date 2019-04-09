<?php
/**
 * Created by PhpStorm.
 * User: jkorn2324
 * Date: 2019-04-08
 * Time: 10:46
 */

declare(strict_types = 1);

namespace jkorn\pvpcore\commands;


class SimpleParameter implements Parameter
{

    private $name;

    private $permission;

    private $optional;

    private $paramType;

    private $setHasExact;

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

    public function setOptional(bool $b) : SimpleParameter {
        $this->optional = $b;
        return $this;
    }

    public function setExactValues(bool $b) : SimpleParameter {
        $this->setHasExact = $b;
        return $this;
    }

    public function hasExactValues() : bool {
        return $this->setHasExact;
    }

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

    private function getExactValues() {
        $str = $this->name;
        if(strpos($this->name, "|")){
            $str = explode("|", $this->name);
        }
        return $str;
    }

    public function getParameterType() : int {
        return $this->paramType;
    }

    public function getName(): string
    {
        return $this->name;
    }

    public function hasPermission(): bool
    {
        return !is_null($this->permission) or $this->permission !== Parameter::NO_PERMISSION;
    }

    public function isOptional() : bool {
        return $this->optional;
    }
}