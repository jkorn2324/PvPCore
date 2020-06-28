package pvpcore;

import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerCreationEvent;
import pvpcore.player.PvPCorePlayer;

public class PvPCListener implements Listener {

    /** The core accessor. */
    private PvPCore core;
    /** The server accessor */
    private Server server;

    /**
     * Constructor for the PvPCore Listener.
     * @param core
     */
    public PvPCListener(PvPCore core)
    {
        this.core = core;
        this.server = core.getServer();

        this.server.getPluginManager().registerEvents(this, core);
    }

    /**
     * Called when the player class is created.
     * @param event - The player creation event object.
     */
    @EventHandler
    public void onPlayerCreation(PlayerCreationEvent event)
    {
        event.setPlayerClass(PvPCorePlayer.class);
    }

    /* @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Task task = new Task() {
            @Override
            public void onRun(int i) {
                PvPCore.coreTask.add(event.getPlayer());
            }
        };
        Server.getInstance().getScheduler().scheduleDelayedTask(task, 5);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Task task = new Task() {
            @Override
            public void onRun(int i) {
                PvPCore.coreTask.remove(event.getPlayer());
            }
        };
        Server.getInstance().getScheduler().scheduleDelayedTask(task, 3);
    }

    @EventHandler
    public void playerDamaged(EntityDamageByEntityEvent event) {

        if(event.getDamager() instanceof Player && event.getEntity() instanceof Player) {

            Player damager = (Player)event.getDamager();
            Player damaged = (Player)event.getEntity();

            boolean canDamage = damager.canSee(damaged) && damager.getAdventureSettings().get(AdventureSettings.Type.ATTACK_PLAYERS);

            if(canDamage) {
                boolean canDamagev2 = true;
                if (!damager.equals(damaged)) {
                    if(event instanceof EntityDamageByChildEntityEvent){
                        Entity child = ((EntityDamageByChildEntityEvent)event).getChild();
                        if(child instanceof EntityPotion){
                            canDamagev2 = false;
                        }
                    }
                    if(canDamagev2) {

                        Level playerLevel = damaged.getLevel();

                        String levelName = playerLevel.getFolderName();

                        PvPCWorld currentWorld = PvPCore.worldHandler.get(levelName);

                        if (currentWorld != null) {

                            float xzKnockback;

                            boolean isKBOn = currentWorld.isCustomKBOn();

                            int attackDelay = PvPCoreUtil.DEFAULT_ATTACK_DELAY;

                            float yKnockback = -1f;

                            if(isKBOn){

                                event.setCancelled(true);

                                attackDelay = currentWorld.getAttackDelay();
                                xzKnockback = currentWorld.getEventKnockback();
                                yKnockback = currentWorld.getYKnockback();

                                if(PvPCore.areaHandler.arePlayersInSameArea(damager, damaged)){
                                    PvPCArea area = PvPCore.areaHandler.getAreaFrom(damaged, damager);
                                    if(area != null && area.canUseKB()){
                                        attackDelay = area.getAttackDelay();
                                        xzKnockback = area.getXZKB();
                                        yKnockback = area.getYKB();
                                    }
                                }

                                if(PvPCore.coreTask.canHit(damaged)){
                                    PvPCore.coreTask.setNoDamage(damaged, attackDelay);
                                    PvPCoreUtil.doKB(damager, damaged, xzKnockback, yKnockback, event.getOriginalDamage());
                                }

                            } else {

                                xzKnockback = event.getKnockBack();

                                boolean execute = false;

                                if(PvPCore.areaHandler.arePlayersInSameArea(damager, damaged)){
                                    PvPCArea area = PvPCore.areaHandler.getAreaFrom(damaged, damager);
                                    if(area != null && area.canUseKB()){
                                        attackDelay = area.getAttackDelay();
                                        xzKnockback = area.getXZKB();
                                        yKnockback = area.getYKB();
                                        execute = true;
                                    }
                                }

                                if(execute && yKnockback != -1f){

                                    event.setCancelled(true);

                                    if(PvPCore.coreTask.canHit(damaged)){
                                        PvPCore.coreTask.setNoDamage(damaged, attackDelay);
                                        PvPCoreUtil.doKB(damager, damaged, xzKnockback, yKnockback, event.getOriginalDamage());
                                    }

                                } else {
                                    event.setKnockBack(xzKnockback);
                                }
                            }
                        } else {
                            coreInstance.getLogger().alert("The world does not exist!");
                        }
                    } else {
                        event.setCancelled();
                    }
                } else {
                    event.setCancelled();
                }
            } else {
                event.setCancelled();
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player p = event.getEntity();
        if(PvPCore.coreTask.get(p) != null){
            PvPPlayer player = PvPCore.coreTask.get(p);
            player.setNoDamageTicks(0);
        }
    } */
}
