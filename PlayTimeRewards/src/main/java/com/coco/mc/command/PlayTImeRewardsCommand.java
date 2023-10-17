package com.coco.mc.command;


import com.hakan.core.command.executors.basecommand.BaseCommand;
import com.hakan.core.command.executors.subcommand.SubCommand;
import com.hakan.core.utils.ColorUtil;
import com.coco.mc.LiteMCPlayTimeReward;
import com.coco.mc.gui.RewardsGUI;
import com.coco.mc.util.PermUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@BaseCommand(
        name = "playtimerewards",
        aliases = "rewards"
)
public class PlayTImeRewardsCommand {

    @SubCommand()
    public void onPlayTimeRewards(CommandSender sender, String[] args){
        if(sender instanceof Player player){
            if(args.length == 0) {
                if (PermUtil.hasPerm(player, "playtime.rewards")) {
                    new RewardsGUI().open(player);
                }
            }
        }else{
            sender.sendMessage(ColorUtil.colored(LiteMCPlayTimeReward.inst.messages.getString("console")));
        }
    }
}
