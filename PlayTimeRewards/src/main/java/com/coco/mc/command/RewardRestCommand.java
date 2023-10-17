package com.coco.mc.command;

import com.hakan.core.command.executors.basecommand.BaseCommand;
import com.hakan.core.command.executors.subcommand.SubCommand;
import com.hakan.core.utils.ColorUtil;
import com.coco.mc.LiteMCPlayTimeReward;
import com.coco.mc.data.PlayerData;
import com.coco.mc.util.PermUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@BaseCommand(
        name = "litemcplaytimerewardreset",
        aliases = "rewardreset"
)

public class RewardRestCommand {

    @SubCommand(
            permissionMessage = ""
    )
    public void onRewardReset(CommandSender sender, String[] args){
        if(args.length == 0) {
            if (PermUtil.hasPerm(sender, "playtime.rewards.reset")) {
                for (PlayerData playerDatum : LiteMCPlayTimeReward.inst.playerData) {
                    playerDatum.getRewards().clear();
                }
                sender.sendMessage(ColorUtil.colored("&aDone!"));
            }
        } else if (args.length == 1) {
            String playerName = args[0];
            Player bukkitPlayer = Bukkit.getPlayer(playerName);
            if(bukkitPlayer == null){
                sender.sendMessage(ColorUtil.colored(LiteMCPlayTimeReward.inst.messages.getString("player-not-found")));
                return;
            }
           PlayerData.get(bukkitPlayer.getUniqueId()).getRewards().clear();
           sender.sendMessage(ColorUtil.colored("&aDone!"));
        }
    }
}
