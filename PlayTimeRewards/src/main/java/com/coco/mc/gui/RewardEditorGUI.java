package com.coco.mc.gui;
import com.hakan.core.HCore;
import com.hakan.core.ui.inventory.InventoryGui;
import com.hakan.core.ui.inventory.item.ClickableItem;
import com.hakan.core.utils.ColorUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import com.coco.mc.LiteMCPlayTimeReward;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RewardEditorGUI extends InventoryGui {
    FileConfiguration config;

    public RewardEditorGUI() {
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

    public RewardEditorGUI(int page) {
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
        getPagination().setCurrentPage(page);
    }



    @Override
    public void onOpen(@NotNull Player player) {
        int maxPages = 0;
        ConfigurationSection section = config.getConfigurationSection("rewards");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                ItemStack itemStack = convert(section.getString(key + ".item"), player);
                if (itemStack == null) {
                    Bukkit.getLogger().severe("Failed to load item: " + key);
                    continue;
                }
                List<ItemStack> itemRewards = (List<ItemStack>) section.getList(key + ".item-rewards");
                boolean enchanted = section.getBoolean(key + ".enchanted");
                String name = section.getString(key + ".name");
                int slot = section.getInt(key + ".slot");
                List<String> lore = section.getStringList(key + ".lore");
                int page = section.getInt(key + ".page");
                String playTime = "";
                if (section.getInt(key + ".required-play-time.weeks") > 0) {
                    playTime += section.getInt(key + ".required-play-time.weeks") + "w ";
                }
                if (section.getInt(key + ".required-play-time.days") > 0) {
                    playTime += section.getInt(key + ".required-play-time.days") + "d ";
                }
                if (section.getInt(key + ".required-play-time.hours") > 0) {
                    playTime += section.getInt(key + ".required-play-time.hours") + "h ";
                }
                if (section.getInt(key + ".required-play-time.minutes") > 0) {
                    playTime += section.getInt(key + ".required-play-time.minutes") + "m ";
                }
                if (section.getInt(key + ".required-play-time.seconds") > 0) {
                    playTime += section.getInt(key + ".required-play-time.seconds") + "s";
                }
                if(maxPages < page) maxPages = page;
                if(getPagination().getCurrentPage() != page) continue;


                lore = lore.stream().map(s -> PlaceholderAPI.setPlaceholders(player, s)).collect(Collectors.toList());
                String finalPlayTime = playTime;
                lore = lore.stream().map(s -> s.replace("%required-play-time%", finalPlayTime)).collect(Collectors.toList());
                setItem(slot, HCore.itemBuilder(itemStack).lores(true, lore).name(true, name).glow(enchanted).build(), (e) -> {
                    e.setCancelled(true);
                    player.closeInventory();
                    Inventory inventory1 = Bukkit.createInventory(player, 54);
                    if(itemRewards != null){
                        for (ItemStack itemReward : itemRewards) {
                            inventory1.addItem(itemReward);
                        }
                    }
                    player.openInventory(inventory1);
                    HCore.registerEvent(InventoryCloseEvent.class).filter(inventoryCloseEvent -> inventoryCloseEvent.getPlayer().getUniqueId().equals(player.getUniqueId())).limit(1).consume((inventoryCloseEvent -> {
                        if(inventoryCloseEvent.getInventory().getHolder() instanceof Player){
                            Player Holderplayer = (Player) inventoryCloseEvent.getInventory().getHolder();
                            if(!Holderplayer.getUniqueId().equals(player.getUniqueId())){
                                return;
                            }
                        }
                        List<ItemStack> itemStacks = new ArrayList<>();
                        for (ItemStack content : inventoryCloseEvent.getInventory().getContents()) {
                            if(content != null && content.getType() != Material.AIR){
                                itemStacks.add(content);
                            }
                        }
                        config.set("rewards." + key + ".item-rewards", itemStacks);
                        LiteMCPlayTimeReward.inst.rewards.save();
                    }));
                });


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
                new RewardEditorGUI(getPagination().getCurrentPage() + 1).open(player);
            });
        }else{
            // Add the back item, it's middle page
            setItem(backSlot, HCore.itemBuilder(backItem).name(true, backName).build(), e -> {
                new RewardEditorGUI(getPagination().getCurrentPage() - 1).open(player);
            });
        }
        if((getPagination().getCurrentPage()) == maxPages){
            // Add the back item, it's the last page
            setItem(backSlot, HCore.itemBuilder(backItem).name(true, backName).build(), e -> {
                new RewardEditorGUI(getPagination().getCurrentPage() - 1).open(player);
            });
        }else{
            // Add the forward item, it's the middle page
            setItem(nextSlot, HCore.itemBuilder(nextItem).name(true, nextName).build(), e -> {
                new RewardEditorGUI(getPagination().getCurrentPage() + 1).open(player);
            });
        }
        boolean fillEmpty = config.getBoolean("fill-empty.enabled");
        if (fillEmpty) {
            while (toInventory().firstEmpty() != -1) {
                setItem(toInventory().firstEmpty(),
                        HCore.itemBuilder(convert(config.getString("fill-empty.item"), player)).name(ColorUtil.colored(config.getString("fill-empty.name"))).build());
            }
        }
    }

    public ItemStack convert(String str, Player player) {
        String[] parts = str.split(":");
        if (parts.length == 2) {
            if (parts[0].equalsIgnoreCase("player_head") && parts[1].equalsIgnoreCase("{player}")) {
                return HCore.skullBuilderByPlayer(player.getName()).build();
            }
            if (parts[0].equalsIgnoreCase("player_head")) {
                return HCore.skullBuilder(parts[1]).build();
            }
            return HCore.itemBuilder(Material.valueOf(parts[0].toUpperCase())).durability(Short.parseShort(parts[1])).build();
        }
        return null;
    }
}