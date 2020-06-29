package pvpcore.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.utils.TextFormat;
import pvpcore.forms.PvPCoreForms;
import pvpcore.utils.Utils;

public class PvPCoreCommand extends Command {

    /**
     * The PvPCore Command constructor.
     */
    public PvPCoreCommand()
    {
        super("pvpcore", "Displays the PvPCore Menu to the sender.", "Usage: /pvpcore", new String[]{"pvpcoremenu", "kbmenu", "pvpmenu"});
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
        if(!(commandSender instanceof Player))
        {
            commandSender.sendMessage(Utils.getPrefix() + TextFormat.RED.toString() + " Console can't use this command.");
            return true;
        }

        if(!this.testPermission(commandSender))
        {
            return true;
        }

        FormWindow window = PvPCoreForms.getPvPCoreMenu((Player)commandSender);
        ((Player) commandSender).showFormWindow(window);
        return true;
    }
}
