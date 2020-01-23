<?php

declare(strict_types=1);

namespace jkorn\pvpcore;

/**
 * Created by PhpStorm.
 * User: jkorn2324
 * Date: 2019-04-04
 * Time: 16:06
 */

use jkorn\pvpcore\world\PvPCWorld;
use pocketmine\event\Listener;
use pocketmine\event\entity\EntityDamageByEntityEvent;
use pocketmine\Player;

class PvPCoreListener implements Listener
{
    private $theCore;

    /**
     * PvPCoreListener constructor.
     * @param PvPCore $core
     */
    public function __construct(PvPCore $core) {
        $this->theCore = $core;
    }

    /**
     * @param EntityDamageByEntityEvent $event
     * @priority LOW
     * @ignoreCancelled FALSE
     */
    public function onEntityDamage(EntityDamageByEntityEvent $event) : void {

        $damager = $event->getDamager(); $damaged = $event->getEntity();

        if($damaged instanceof Player and $damager instanceof Player){

            $damager = $damager->getPlayer();

            $damaged = $damaged->getPlayer();

            $lvl = $damaged->getLevel();
            $world = PvPCore::getWorldHandler()->getWorldFromLevel($lvl);

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
        }
    }
}