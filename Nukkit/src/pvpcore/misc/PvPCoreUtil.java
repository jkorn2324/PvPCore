package pvpcore.misc;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.level.particle.CriticalParticle;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.AnimatePacket;
import cn.nukkit.network.protocol.EntityEventPacket;
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.TextFormat;
import pvpcore.PvPCore;

import java.util.Random;

public class PvPCoreUtil {

    public static int DEFAULT_ATTACK_DELAY = 10;

    public static int COMBO_ATTACK_DELAY = 4;

    public static int MAX_WORLD_HEIGHT = 256;

    public static String positionToString(Position pos, boolean includeLevel) {
        Double x = pos.x, y = pos.y, z = pos.z;
        int realX = x.intValue(), realY = y.intValue(), realZ = z.intValue();
        String level = (includeLevel ? pos.getLevel().getFolderName() : null);
        String result = realX + ":" + realY + ":" + realZ;
        if(level != null){
            result = result + ":" + level;
        }
        return result;
    }

    public static int secondsToTicks(int sec){
        return sec * 20;
    }

    public static Double getMinimumValue(double first, double second){
        return first < second ? first : second;
    }

    public static Double getMaximumValue(double first, double second){
        return first >= second ? first : second;
    }

    public static int doubleToInt(Double d){
        return d.intValue();
    }

    public static void reloadPlayers(){

        if (Server.getInstance().getOnlinePlayers().size() > 0) {
            Task task = new Task() {
                @Override
                public void onRun(int i) {
                    for (Player p : Server.getInstance().getOnlinePlayers().values()) {
                        if (p != null && p.isOnline()) {
                            PvPCore.coreTask.add(p);
                        }
                    }
                }
            };
            Server.getInstance().getScheduler().scheduleDelayedTask(task, 5);
        }
    }

    public static boolean attack(Entity player, Entity attacker, float damage){

        EntityDamageByEntityEvent source = new EntityDamageByEntityEvent(attacker, player, EntityDamageEvent.DamageCause.ENTITY_ATTACK, damage);
        Server.getInstance().getPluginManager().callEvent(source);

        if(player.getAbsorption() > 0) {
            float newHealth = player.getAbsorption() - source.getFinalDamage() > 0 ? source.getFinalDamage() : attacker.getAbsorption();
            player.setAbsorption(player.getAbsorption() - newHealth);
            source.setDamage(-newHealth, EntityDamageEvent.DamageModifier.ABSORPTION);
        }

        if(player instanceof Player) {

            Player p = (Player)player;

            int points = 0;

            int toughness = 0;

            Item[] thearmor = p.getInventory().getArmorContents();

            int v = 0;

            for (int slot = 0; slot < thearmor.length; slot++) {
                Item armor = thearmor[slot];
                points += armor.getArmorPoints();
                v = (int) ((double) v + calculateEnchantmentReduction(armor, source));
                toughness += armor.getToughness();
            }

            float originalDamage = source.getDamage();
            float finalDamage = (float) ((double) (originalDamage * (1.0F - Math.max((float) points / 5.0F, (float) points - originalDamage / (2.0F + (float) toughness / 4.0F)) / 25.0F)) * (1.0D - (double) v * 0.04D));
            source.setDamage(finalDamage - originalDamage, EntityDamageEvent.DamageModifier.ARMOR);
        }

        player.setHealth(player.getHealth() - source.getDamage());
        player.setLastDamageCause(source);
        return true;
    }

    private static double calculateEnchantmentReduction(Item item, EntityDamageEvent source) {
        double result;
        if (!item.hasEnchantments()) {
            result = 0;
        } else {
            double reduction = 0;
            Enchantment[] enchantments = item.getEnchantments();
            for(Enchantment e : enchantments) {
                reduction += (double)e.getDamageProtection(source);
            }
            result = reduction;
        }
        return result;
    }

    public static void doKB(Player damager, Player damaged, float xzKB, float yKB, float originalDamage) {

        if (damager != null && damaged != null) {

            EntityEventPacket pkt = new EntityEventPacket();
            pkt.eid = damaged.getId();
            pkt.event = EntityEventPacket.HURT_ANIMATION;
            Server.broadcastPacket(Server.getInstance().getOnlinePlayers().values(), pkt);

            double movementX = (damaged.x - damager.x) * xzKB;
            double movementZ = (damaged.z - damager.z) * xzKB;

            Vector3 kb = setPlayerKnockback(damaged, (float) movementX, yKB, (float) movementZ, xzKB);

            if (!damager.isCreative()) {
                Item heldItem = damager.getInventory().getItemInHand();
                if (heldItem.isTool()) {
                    if (heldItem.useOn(damaged) && heldItem.getDamage() >= heldItem.getMaxDurability()) {
                        damager.getInventory().setItemInHand(Item.get(Item.AIR));
                    } else {
                        damager.getInventory().setItemInHand(heldItem);
                    }
                }
                damager.getInventory().sendContents(damager);
            }

            Level theLvl = damager.getLevel();

            if (!damager.isOnGround()) {

                AnimatePacket animate = new AnimatePacket();
                animate.action = AnimatePacket.Action.CRITICAL_HIT;
                animate.eid = damaged.getId();
                theLvl.addChunkPacket(damager.getChunkX(), damager.getChunkZ(), animate);
                theLvl.addLevelSoundEvent(damaged, 43);
                NukkitRandom random = new NukkitRandom();
                originalDamage *= 1.5;
                for (int i = 0; i < 5; i++) {
                    CriticalParticle par = new CriticalParticle(new Vector3(damaged.x + (float) random.nextRange(-15, 15) / 10, damaged.y + (float) random.nextRange(0, 20) / 10, damaged.z + (float) random.nextRange(-15, 15) / 10));
                    theLvl.addParticle(par);
                }
            }

            PvPCoreUtil.attack(damaged, damager, originalDamage);
            damaged.setMotion(kb);
            damageArmor(damager, damaged);
        }
    }

    public static void damageArmor(Entity damager, Player damaged) {

        for (int currentSlot = 0; currentSlot < 4; currentSlot++) {
            Item theArmor = damaged.getInventory().getArmorItem(currentSlot);
            if (!theArmor.equals(Item.get(Item.AIR))) {
                if (theArmor.hasEnchantments()) {
                    Enchantment[] enchantments = theArmor.getEnchantments();
                    for (int e = 0; e < enchantments.length; e++) {
                        Enchantment theEnchant = enchantments[e];
                        theEnchant.doPostAttack(damager, damaged);
                    }
                    Enchantment durability = theArmor.getEnchantment(Enchantment.ID_DURABILITY);
                    if (durability != null && durability.getLevel() > 0 && 100 / (durability.getLevel() + 1) <= (new Random()).nextInt(100)) {
                        continue;
                    }
                }
                theArmor.setDamage(theArmor.getDamage() + 1);
                if (theArmor.getDamage() >= theArmor.getMaxDurability()) {
                    damaged.getInventory().setArmorItem(currentSlot, Item.get(Item.AIR));
                } else {
                    damaged.getInventory().setArmorItem(currentSlot, theArmor, true);
                }
            }
        }
    }

    public static String getEnabledString(String pluginName, boolean enabled) {
        String getEnabled = enabled ? "enabled" : "disabled";
        return pluginName + " has been " + getEnabled + "!";
    }

    public static Vector3 setPlayerKnockback(Entity damaged, float x, float y, float z, float base) {

        double f = Math.sqrt((double)(x * x + z * z));

        Vector3 motion = new Vector3(damaged.motionX, damaged.motionY, damaged.motionZ);

        if (f > 0.0D) {
            f = 1.0D / f;
        } else {
            f = 0;
        }
        motion.x /= 2.0D;
        motion.y /= 2.0D;
        motion.z /= 2.0D;
        motion.x += (double)x * f * (double)base;
        motion.y += (double)y;
        motion.z += (double)z * f * (double)base;
        if (motion.y > y) {
            motion.y = y;
        }

        return motion;
    }

    public static boolean isWithinBounds(int val, int minVal, int maxVal) {
        return val >= minVal && val <= maxVal;
    }

    public static boolean isClose(int val, int minVal, int maxVal, int maxDist){
        return val + maxDist >= minVal || val - maxDist <= maxVal;
    }

    public static Position readPosFrom(Object o) {

        Integer x = null, y = null, z = null;

        Position result = null;

        if(o instanceof String){
            String[] split = ((String) o).split(":");
            x = Integer.parseInt(split[0]);
            y = Integer.parseInt(split[1]);
            z = Integer.parseInt(split[2]);
        }

        if(x != null){
            result = new Position(x, y, z);
        }
        return result;
    }

    public static boolean canParse(String argument, boolean isFloat) {

        String abc = "abcdefghijklmnopqrstuvwxyz";

        boolean result = true;

        String chars = abc + abc.toUpperCase() + "!@#$%^&*()_+={[}]|\\:;\"'<,>?/";
        if(!isFloat){
            chars = chars + ".";
        }

        for(int i = 0; i < chars.length(); i++){
            String c = chars.charAt(i) + "";
            if(argument.contains(c)){
                result = false;
                break;
            }
        }
        return result;
    }

    public static float getKnockbackFromObj(Object obj){
        Float kbFl = null;
        Double kbD = null;
        float res = 0;
        if(obj instanceof Float){
            kbFl = (Float)obj;
        } else if (obj instanceof Double){
            kbD = (Double)obj;
        } else if (obj instanceof Integer){
            Integer i = (Integer)obj;
            kbFl = i.floatValue();
        }

        if(kbFl != null){
            res = kbFl;
        } else if (kbD != null){
            res = kbD.floatValue();
        }
        return res;
    }

    public static Boolean getBoolean(String argument) {
        String[][] args = {{"enable", "on", "true"}, {"disable", "off", "false"}};

        Boolean result = null;

        for(int i = 0; i < args.length; i++){
            String[] grp = args[i];
            for(String val : grp){
                if(argument.equalsIgnoreCase(val)){
                    result = i == 0;
                    break;
                }
            }
        }
        return result;
    }

    public static boolean isBoolean(String arg){
        return getBoolean(arg) != null;
    }

    public static boolean isPlayer(CommandSender sender) {
        boolean result = sender instanceof Player;
        if(!result){
            sender.sendMessage(TextFormat.RED + "The console cannot run this command!");
        }
        return result;
    }
}
