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

    /**
     * @param EntityDamageByEntityEvent $event
     * @priority LOW
     * @ignoreCancelled FALSE
     */
    public function onEntityDamage(EntityDamageByEntityEvent $event) : void {

        /* $damager = $event->getDamager(); $damaged = $event->getEntity();

        if($damaged instanceof Player and $damager instanceof Player){

            $damager = $damager->getPlayer();

            $damaged = $damaged->getPlayer();

            $lvl = $damaged->getLevel();
            $world = PvPCore::getWorldHandler()->getPvPCWorld($lvl);

            if($world !== null and $world instanceof PvPCWorld){

                $useCustomKB = $world->hasCustomKB();
                $time = $world->getAttackDelayTime();
                $knockback = $world->getKnockBack();

                if(PvPCore::getAreaHandler()->isInSameAreas($damaged, $damager)) {
                    $closestArea = PvPCore::getAreaHandler()->getClosestAreaTo($damaged->getPlayer());
                    if($closestArea->canUseAreaKB()) {
                        $knockback = $closestArea->getKnockback();
                        $time = $closestArea->getAttackDelay();
                        $useCustomKB = $closestArea->canUseAreaKB();
                    }
                }

                if($useCustomKB === true) {
                    $event->setKnockBack($knockback);
                    $event->setAttackCooldown($time);
                }
            }
        } */
    }
}