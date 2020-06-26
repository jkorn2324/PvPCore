<?php

declare(strict_types=1);

namespace jkorn\pvpcore;

/**
 * Created by PhpStorm.
 * User: jkorn2324
 * Date: 2019-04-04
 * Time: 16:06
 */

use jkorn\pvpcore\player\PvPCPlayer;
use jkorn\pvpcore\world\PvPCWorld;
use pocketmine\event\Listener;
use pocketmine\event\entity\EntityDamageByEntityEvent;
use pocketmine\event\player\PlayerCreationEvent;
use pocketmine\Player;
use pocketmine\Server;

class PvPCoreListener implements Listener
{
    /** @var PvPCore */
    private $core;
    /** @var Server */
    private $server;

    /**
     * PvPCoreListener constructor.
     * @param PvPCore $core
     */
    public function __construct(PvPCore $core)
    {
        $this->core = $core;
        $this->server = $core->getServer();

        $this->server->getPluginManager()->registerEvents($this, $core);
    }

    /**
     * @param PlayerCreationEvent $event
     *
     * Sets the player class.
     */
    public function setPlayerClass(PlayerCreationEvent $event): void
    {
        $event->setPlayerClass(PvPCPlayer::class);
    }
}