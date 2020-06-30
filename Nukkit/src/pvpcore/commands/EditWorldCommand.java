package pvpcore.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.utils.TextFormat;
import pvpcore.PvPCore;
import pvpcore.forms.PvPCoreForms;
import pvpcore.utils.Utils;
import pvpcore.worlds.PvPCWorld;

public class EditWorldCommand extends Command
{

    public EditWorldCommand()
    {
        super("editWorld", "Sends the edit world Knockback form to the player.", "Usage: /editWorld", new String[]{"editworld", "editworldkb"});
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

        PvPCWorld world = PvPCore.getWorldHandler().getWorld(((Player) commandSender).getLevel());
        System.out.println(world);

        FormWindow window = PvPCoreForms.getWorldMenu(
                (Player)commandSender,
                world,
                false
        );
        ((Player) commandSender).showFormWindow(window);
        return true;
    }
}
