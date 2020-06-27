<?php

declare(strict_types=1);

namespace jkorn\pvpcore\forms;


use pocketmine\form\Form as IForm;
use pocketmine\form\FormValidationException;
use pocketmine\Player;

class AbstractForm implements IForm
{
    /** @var array */
    protected $data = [];
    /** @var callable */
    private $callable;

    /** @var array */
    private $extraData = [];

    public function __construct(?callable  $callable)
    {
        $this->callable = $callable;
    }

    /**
     * @return callable|null
     *
     * Gets the callable function used for forms.
     */
    public function getCallable() : ?callable
    {
        return $this->callable;
    }

    /**
     * @param callable|null $callable
     *
     * Sets the callable function used for forms.
     */
    public function setCallable(?callable $callable)
    {
        $this->callable = $callable;
    }

    /**
     * @param array $data
     *
     * Sets the extra data of the form.
     */
    public function setExtraData(array $data): void
    {
        $this->extraData = $data;
    }

    /**
     * @param $data
     *
     * Processes the data.
     */
    public function processData(&$data) : void {}

    /**
     * Handles a form response from a player.
     *
     * @param Player $player
     * @param mixed $data
     *
     * @throws FormValidationException if the data could not be processed
     */
    public function handleResponse(Player $player, $data): void
    {
        $this->processData($data);
        $callable = $this->getCallable();

        if($callable !== null) {
            $callable($player, $data, $this->extraData);
        }
    }

    /**
     * Specify data which should be serialized to JSON
     * @link https://php.net/manual/en/jsonserializable.jsonserialize.php
     * @return mixed data which can be serialized by <b>json_encode</b>,
     * which is a value of any type other than a resource.
     * @since 5.4.0
     */
    public function jsonSerialize()
    {
        return $this->data;
    }
}