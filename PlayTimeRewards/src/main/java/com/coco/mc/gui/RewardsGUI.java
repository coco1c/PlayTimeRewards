package com.coco.mc.gui;

import com.bencodez.votingplugin.VotingPluginMain;
import com.hakan.core.HCore;
import com.hakan.core.ui.inventory.InventoryGui;
import com.hakan.core.ui.inventory.item.ClickableItem;
import com.hakan.core.utils.ColorUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import com.coco.mc.LiteMCPlayTimeReward;
import com.coco.mc.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class RewardsGUI extends InventoryGui {
    FileConfiguration config;
    public RewardsGUI() {
        super("id",
                ColorUtil.colored(LiteMCPlayTimeReward.inst.rewards.getString("title")), LiteMCPlayTimeReward.inst.rewards.getInt("size"), InventoryType.CHEST);
        config = LiteMCPlayTimeReward.inst.rewards.getYml();
        List<ClickableItem> clickableItems = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            ClickableItem item = new ClickableItem(HCore.itemBuilder(Material.AIR).build(), InventoryClickEvent::getAction);
            clickableItems.add(item);
        }
        getPagination().setSlots(0, 2);
        getPagination().setItems(clickableItems);
        getPagination().setCurrentPage(1);
    }

    public RewardsGUI(int page) {
        super("id",
                ColorUtil.colored(LiteMCPlayTimeReward.inst.rewards.getString("title")), LiteMCPlayTimeReward.inst.rewards.getInt("size"), InventoryType.CHEST);
        config = LiteMCPlayTimeReward.inst.rewards.getYml();

        List<ClickableItem> clickableItems = new ArrayList<>();
        for (int i = 0; i < page * 20; i++) {
            ClickableItem item = new ClickableItem(HCore.itemBuilder(Material.AIR).build(), InventoryClickEvent::getAction);
            clickableItems.add(item);
        }
        getPagination().setSlots(0, 1);
        getPagination().setItems(clickableItems);
        getPagination().setCurrentPage(page);
    }

    @Override
    public void onOpen(@NotNull Player player) {
        ConfigurationSection section = config.getConfigurationSection("rewards");
        int maxPages = 0;
        if(section != null){
            for (String key : section.getKeys(false)) {
                ItemStack itemStack = convert(section.getString(key + ".item"), player);
                if(itemStack == null){
                    Bukkit.getLogger().severe("Failed to load item: " + key);
                    continue;
                }
                List<ItemStack> itemRewards = (List<ItemStack>) section.getList(key + ".item-rewards");
                boolean enchanted = section.getBoolean(key + ".enchanted");
                String name = section.getString(key + ".name");
                int slot = section.getInt(key + ".slot");
                int page = section.getInt(key + ".page");
                List<String> lore = section.getStringList(key + ".lore");
                String playTime = "";
                if(section.getInt(key + ".required-play-time.weeks") > 0){
                    playTime += section.getInt(key + ".required-play-time.weeks") + "w ";
                }
                if(section.getInt(key + ".required-play-time.days") > 0){
                    playTime += section.getInt(key + ".required-play-time.days") + "d ";
                }
                if(section.getInt(key + ".required-play-time.hours") > 0){
                    playTime += section.getInt(key + ".required-play-time.hours") + "h ";
                }
                if(section.getInt(key + ".required-play-time.minutes") > 0){
                    playTime += section.getInt(key + ".required-play-time.minutes") + "m ";
                }
                if(section.getInt(key + ".required-play-time.seconds") > 0){
                    playTime += section.getInt(key + ".required-play-time.seconds") + "s";
                }
                if(maxPages < page) maxPages = page;
                if(getPagination().getCurrentPage() != page) continue;

                lore = lore.stream().map(s -> PlaceholderAPI.setPlaceholders(player, s)).collect(Collectors.toList());
                String finalPlayTime = playTime;
                lore = lore.stream().map(s -> s.replace("%required-play-time%", finalPlayTime)).collect(Collectors.toList());
                if(PlayerData.get(player.getUniqueId()).getRewards().contains(key)){
                    itemStack = convert(section.getString(key + ".reward-claimed-item.item"), player);
                    enchanted = section.getBoolean(key + ".reward-claimed-item.enchanted");
                    name = section.getString(key + ".reward-claimed-item.name");
                    lore = section.getStringList(key + ".reward-claimed-item.lore");
                    lore = lore.stream().map(s -> PlaceholderAPI.setPlaceholders(player, s)).collect(Collectors.toList());
                    lore = lore.stream().map(s -> s.replace("%required-play-time%", finalPlayTime)).collect(Collectors.toList());
                    setItem(slot, HCore.itemBuilder(itemStack).lores(true, lore).name(true, name).glow(enchanted).build(), (e) -> {
                        e.setCancelled(true);
                        if(hasRequired(player, "rewards." + key)){
                            if(PlayerData.get(player.getUniqueId()).getRewards().contains(key)){
                                player.sendMessage(ColorUtil.colored(LiteMCPlayTimeReward.inst.messages.getString("already-rewarded")));
                                return;
                            }
                            List<String> commands = section.getStringList(key + ".commands");
                            commands.forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command
                                    .replace("{player}", player.getName())));
                            PlayerData.get(player.getUniqueId()).getRewards().add(key);
                            player.closeInventory();

                            if(section.getString(key + ".reward-sound") != null && !Objects.requireNonNull(section.getString(key + ".reward-sound")).isEmpty()) {
                                player.playSound(player, Sound.valueOf(section.getString(key + ".reward-sound").toUpperCase()), 1, 1);
                            }
                        }else{
                            player.sendMessage(ColorUtil.colored(LiteMCPlayTimeReward.inst.messages.getString("not-enough-play-time")));
                        }
                    });

                }else{
                    setItem(slot, HCore.itemBuilder(itemStack).lores(true, lore).name(true, name).glow(enchanted).build(), (e) -> {
                        e.setCancelled(true);
                        if(hasRequired(player, "rewards." + key)){
                            if(PlayerData.get(player.getUniqueId()).getRewards().contains(key)){
                                player.sendMessage(ColorUtil.colored(LiteMCPlayTimeReward.inst.messages.getString("already-rewarded")));
                                return;
                            }
                           if(itemRewards != null){
                               for (ItemStack itemReward : itemRewards) {
                                 HashMap<Integer, ItemStack> itemStackHashMap = player.getInventory().addItem(itemReward);
                                   for (ItemStack value : itemStackHashMap.values()) {
                                       player.getWorld().dropItem(player.getLocation(), value);
                                   }
                               }
                           }
                            List<String> commands = section.getStringList(key + ".commands");
                            commands.forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command
                                    .replace("{player}", player.getName())));
                            PlayerData.get(player.getUniqueId()).getRewards().add(key);
                            player.closeInventory();
                            int takeLevel = section.getInt(key + ".reward-take-level");
                            player.setLevel(player.getLevel() - takeLevel);

                            if(section.getString(key + ".reward-sound") != null && !Objects.requireNonNull(section.getString(key + ".reward-sound")).isEmpty()) {
                                player.playSound(player, Sound.valueOf(section.getString(key + ".reward-sound").toUpperCase()), 1, 1);
                            }
                        }else{
                            player.sendMessage(ColorUtil.colored(LiteMCPlayTimeReward.inst.messages.getString("not-enough-play-time")));
                        }
                    });
                }

            }
        }
            // TODO add the back and forward button to the GUI
            ItemStack backItem = convert(config.getString("navigators.back.item"), player);
            int backSlot = config.getInt("navigators.back.slot");
            String backName = config.getString("navigators.back.name");
            ItemStack nextItem = convert(config.getString("navigators.forward.item"), player);
            int nextSlot = config.getInt("navigators.forward.slot");
            String nextName = config.getString("navigators.forward.name");
            if((getPagination().getCurrentPage() - 1) == 0){
                // Add the forward item, it's the first page
                setItem(nextSlot, HCore.itemBuilder(nextItem).name(true, nextName).build(), e -> {
                   new RewardsGUI(getPagination().getCurrentPage() + 1).open(player);
                });
            }else{
                // Add the back item, it's middle page
                setItem(backSlot, HCore.itemBuilder(backItem).name(true, backName).build(), e -> {
                    new RewardsGUI(getPagination().getCurrentPage() - 1).open(player);
                });
            }
            if((getPagination().getCurrentPage()) == maxPages){
                // Add the back item, it's the last page
                setItem(backSlot, HCore.itemBuilder(backItem).name(true, backName).build(), e -> {
                    new RewardsGUI(getPagination().getCurrentPage() - 1).open(player);
                });
            }else{
                // Add the forward item, it's the middle page
                setItem(nextSlot, HCore.itemBuilder(nextItem).name(true, nextName).build(), e -> {
                    new RewardsGUI(getPagination().getCurrentPage() + 1).open(player);
                });
            }

        boolean fillEmpty = config.getBoolean("fill-empty.enabled");
        if(fillEmpty){
            while (toInventory().firstEmpty() != -1){
                setItem(toInventory().firstEmpty(),
                        HCore.itemBuilder(convert(config.getString("fill-empty.item"), player)).name(ColorUtil.colored(config.getString("fill-empty.name"))).build());
            }
        }
    }


    public boolean hasRequired(Player player, String path){
        PlayerData playerData = PlayerData.get(player.getUniqueId());
        int requiredWeeks = config.getInt(path + ".required-play-time.weeks");
        int requiredDays = config.getInt(path + ".required-play-time.days");
        int requiredHours = config.getInt(path + ".required-play-time.hours");
        int requiredMinutes = config.getInt(path + ".required-play-time.minutes");
        int requiredSeconds = config.getInt(path + ".required-play-time.seconds");
        int requiredVote = config.getInt(path + ".required-vote");
        int requiredLevel = config.getInt(path + ".required-level");
        boolean weeks = false;
        boolean days = false;
        boolean hours = false;
        boolean min = false;
        boolean seconds = false;
        if(requiredWeeks == 0){
            weeks = true;
        }else if(requiredWeeks > 0 && playerData.getWeeks() >= requiredWeeks){
            weeks = true;
        }
        if(requiredDays == 0){
            days = true;
        }else if (requiredDays > 0 && playerData.getDays() >= requiredDays){
            days = true;
        }
        if(requiredHours == 0){
            hours = true;
        }else if(requiredHours > 0 && playerData.getHours() >= requiredHours){
            hours = true;
        }

        if(requiredMinutes == 0){
            min = true;
        }else if(requiredMinutes > 0 && playerData.getMinutes() >= requiredMinutes){
            min = true;
        }

        if(requiredSeconds == 0){
            seconds = true;
        }else if(requiredSeconds > 0 && playerData.getSeconds() >= requiredSeconds){
            seconds = true;
        }

        boolean vote = false;

        if(requiredVote == 0){
            vote = true;
        }else if(requiredVote > 0 && VotingPluginMain.plugin.getVotingPluginUserManager().getVotingPluginUser(player).getPoints() >= requiredVote){
            vote = true;
        }

        boolean level = false;
        if(requiredLevel == 0){
            level = true;
        }else if(requiredLevel > 0 && player.getExpToLevel() >= requiredLevel){
            level = true;
        }




        return weeks && days && hours && min && seconds && vote && level;
    }

    public ItemStack convert(String str, Player player){
        String[] parts = str.split(":");
        if(parts.length == 2){
            if(parts[0].equalsIgnoreCase("player_head") && parts[1].equalsIgnoreCase("{player}")){
                return HCore.skullBuilderByPlayer(player.getName()).build();
            }
            if(parts[0].equalsIgnoreCase("player_head")){
                return HCore.skullBuilder(parts[1]).build();
            }
            return HCore.itemBuilder(Material.valueOf(parts[0].toUpperCase())).durability(Short.parseShort(parts[1])).build();
        }
        return null;
    }
}

