package com.coco.mc.listener;

import com.hakan.core.HCore;
import com.coco.mc.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.TabCompleteEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TabCompleteListener implements Listener {

    @EventHandler
    public void onTabComplete(TabCompleteEvent event){
        if(event.getBuffer().contains("stats") || event.getBuffer().contains("playtime") || event.getBuffer().contains("rewardreset") || event.getBuffer().contains("litemcplaytimerewardreset")){
            if(event.getSender() instanceof Player){
                    List<String> strings = new ArrayList<>();
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        strings.add(onlinePlayer.getName());
                    }
                    event.setCompletions(strings);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        HCore.syncScheduler().every(1L, TimeUnit.MILLISECONDS).run((runnable) -> {
            if(event.getPlayer() == null || !event.getPlayer().isOnline()){
                runnable.cancel();
            }
            PlayerData.get(event.getPlayer().getUniqueId()).addPlayTime(50);
        });
    }
}
