package me.beastman3226.BusinessCore.ce;

import me.beastman3226.BusinessCore.util.MessageUtility;
import org.bukkit.command.CommandSender;

/**Sends the normal help message
 *
 * @author beastman3226
 */
public class BusinessHelp {

    public static void execute(CommandSender sender, String[] subtractArg) {
        sender.sendMessage(MessageUtility.B_HELP);
    }

}
