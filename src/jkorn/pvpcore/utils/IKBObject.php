<?php
/**
 * Created by PhpStorm.
 * User: jkorn2324
 * Date: 2020-06-13
 * Time: 18:57
 */

declare(strict_types=1);

namespace jkorn\pvpcore\utils;


use pocketmine\Player;

interface IKBObject extends IExportedValue
{

    /**
     * @param Player $player1
     * @param Player $player2
     * @return bool
     *
     * Determines if the player can use the custom knockback.
     */
    function canUseKnocback(Player $player1, Player $player2): bool;

}