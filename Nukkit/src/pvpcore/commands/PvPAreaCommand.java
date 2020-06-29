package pvpcore.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import pvpcore.player.PvPCorePlayer;
import pvpcore.utils.Utils;

public class PvPAreaCommand extends Command {

    public PvPAreaCommand()
    {
        super("pvparea", "Sets the pvparea information in the player", "Usage: /pvparea <pos1:pos2>", new String[]{"pvpareapos"});
        super.setPermission("pvpcore.permission.edit");
    }

    /**
     * Executes the command.
     * @param commandSender - The command sender.
     * @param s - The command label.
     * @param strings - The arguments for the commands.
     * @return true if command executes successfully, false otherwise.
     */
    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings)
    {
        if(!this.testPermission(commandSender))
        {
            return true;
        }

        if(!(commandSender instanceof PvPCorePlayer))
        {
            String message;
            if(commandSender instanceof Player)
            {
                message = TextFormat.RED.toString() + "Internal Plugin Error. Please leave and join back to use this command.";
            } else {
                message = TextFormat.RED.toString() + "Console can't use this command.";
            }
            commandSender.sendMessage(Utils.getPrefix() + " " + message);
            return true;
        }

        String firstArg;
        if(strings.length <= 0 || (!(firstArg = strings[0]).equals("pos1") && !firstArg.equals("pos2")))
        {
            commandSender.sendMessage(Utils.getPrefix() + " " + TextFormat.RED.toString() + this.getUsage());
            return true;
        }

        if(firstArg.equals("pos1"))
        {
            ((PvPCorePlayer) commandSender).setFirstPos();
        } else {
            ((PvPCorePlayer) commandSender).setSecondPos();
        }

        return true;
    }
}
