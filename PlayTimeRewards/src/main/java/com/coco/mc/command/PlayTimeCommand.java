package com.coco.mc.command;

import com.hakan.core.command.executors.basecommand.BaseCommand;
import com.hakan.core.command.executors.subcommand.SubCommand;
import com.hakan.core.utils.ColorUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import com.coco.mc.LiteMCPlayTimeReward;
import com.coco.mc.util.PermUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@BaseCommand(
        name = "litemcplaytime",
        aliases = "playtime"
)
public class PlayTimeCommand {

    @SubCommand()
    public void onStats(CommandSender sender, String[] args){
        if(sender instanceof Player player){
            if(args.length == 0) {
                if (PermUtil.hasPerm(player, "playtime.use")) {
                    List<String> strings = LiteMCPlayTimeReward.inst.messages.getList("play-time");
                    for (String string : strings) {
                        player.sendMessage(ColorUtil.colored(PlaceholderAPI.setPlaceholders(player, string)).replace("{player}", "Your"));
                    }
                }
            } else if (args.length == 1 && PermUtil.hasPerm(player, "playtime.use.others")) {
                String playerName = args[0];
                Player bukkitPlayer = Bukkit.getPlayer(playerName);
                if(bukkitPlayer == null){
                    sender.sendMessage(ColorUtil.colored(LiteMCPlayTimeReward.inst.messages.getString("player-not-found")));
                    return;
                }
                List<String> strings = LiteMCPlayTimeReward.inst.messages.getList("play-time");
                for (String string : strings) {
                    if(player.getUniqueId() == bukkitPlayer.getUniqueId()){
                        player.sendMessage(ColorUtil.colored(PlaceholderAPI.setPlaceholders(bukkitPlayer, string)).replace("{player}", "Your"));
                    }else{
                        player.sendMessage(ColorUtil.colored(PlaceholderAPI.setPlaceholders(bukkitPlayer, string)).replace("{player}", bukkitPlayer.getName()));
                    }
                 }
            }
        }else{
            sender.sendMessage(ColorUtil.colored(LiteMCPlayTimeReward.inst.messages.getString("console")));
        }
    }
}
