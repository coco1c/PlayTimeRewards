package com.coco.mc.util;

import com.hakan.core.utils.ColorUtil;
import com.coco.mc.LiteMCPlayTimeReward;
import org.bukkit.command.CommandSender;

public class PermUtil {

    public static boolean hasPerm(CommandSender sender, String perm){
        if(sender.hasPermission(perm)){
            return true;
        }

        sender.sendMessage(ColorUtil.colored(LiteMCPlayTimeReward.inst.messages.getString("no-permission")));
        return false;
    }
}
