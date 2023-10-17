package com.coco.mc.command;


import com.hakan.core.command.executors.basecommand.BaseCommand;
import com.hakan.core.command.executors.subcommand.SubCommand;
import com.hakan.core.utils.ColorUtil;
import com.coco.mc.LiteMCPlayTimeReward;
import com.coco.mc.gui.StatsGUI;
import com.coco.mc.util.PermUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@BaseCommand(
        name = "stats"
)
public class StatsCommand {

    @SubCommand()
    public void onStats(CommandSender sender, String[] args){
        if(sender instanceof Player player){
            if(args.length == 0) {
                if (PermUtil.hasPerm(player, "playtime.stats")) {
                    new StatsGUI(player).open(player);
                }
            } else if (args.length == 1 && PermUtil.hasPerm(player, "playtime.stats.others")) {
                String playerName = args[0];
                Player bukkitPlayer = Bukkit.getPlayer(playerName);
                if(bukkitPlayer == null){
                    sender.sendMessage(ColorUtil.colored(LiteMCPlayTimeReward.inst.messages.getString("player-not-found")));
                    return;
                }
                new StatsGUI(bukkitPlayer).open(player);
            }
        }else{
            sender.sendMessage(ColorUtil.colored(LiteMCPlayTimeReward.inst.messages.getString("console")));
        }
    }

    @SubCommand(
            args = "reload"
    )
    public void reload(CommandSender sender, String[] args){
        if(PermUtil.hasPerm(sender, "playtime.reload")){
            LiteMCPlayTimeReward.inst.reloadConfig();
            LiteMCPlayTimeReward.inst.rewards.reload();
            LiteMCPlayTimeReward.inst.messages.reload();
            sender.sendMessage(ColorUtil.colored(LiteMCPlayTimeReward.inst.messages.getString("reload")));
        }
    }


}
