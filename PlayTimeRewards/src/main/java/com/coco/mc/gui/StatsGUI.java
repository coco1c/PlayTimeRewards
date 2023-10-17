package com.coco.mc.gui;

import com.hakan.core.HCore;
import com.hakan.core.ui.inventory.InventoryGui;
import com.hakan.core.utils.ColorUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import com.coco.mc.LiteMCPlayTimeReward;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class StatsGUI extends InventoryGui {

    FileConfiguration config;
    Player ePlayer;
    public StatsGUI(Player player) {
        super("id",
                ColorUtil.colored(LiteMCPlayTimeReward.inst.getConfig().getString("stats-gui.title"))
                        .replace("{player}", player.getName()), LiteMCPlayTimeReward.inst.getConfig().getInt("stats-gui.size"), InventoryType.CHEST);
        config = LiteMCPlayTimeReward.inst.getConfig();
        ePlayer = player;
    }



    @Override
    public void onOpen(@NotNull Player player) {
        if(player.getUniqueId().equals(ePlayer.getUniqueId())){
            ePlayer = null;
        }
        ConfigurationSection section = config.getConfigurationSection("stats-gui.items");
        if(section != null){
            for (String key : section.getKeys(false)) {
                ItemStack itemStack;
                if(ePlayer == null) {
                    itemStack = convert(section.getString(key + ".item"), player);
                }else{
                    itemStack = convert(section.getString(key + ".item"), ePlayer);
                }
                if(itemStack == null){
                    Bukkit.getLogger().severe("Failed to load item: " + key);
                    continue;
                }
                boolean enchanted = section.getBoolean(key + ".enchanted");
                String name = section.getString(key + ".name");
                int slot = section.getInt(key + ".slot");
                List<String> lore = section.getStringList(key + ".lore");
                if(ePlayer == null){
                    lore = lore.stream().map(s -> PlaceholderAPI.setPlaceholders(player, s)).collect(Collectors.toList());
                }else{
                    lore = lore.stream().map(s -> PlaceholderAPI.setPlaceholders(ePlayer, s)).collect(Collectors.toList());
                }
                if(enchanted){
                    setItem(slot, HCore.itemBuilder(itemStack).lores(true, lore).name(true, name)
                            .glow(true).build());
                }else{
                    setItem(slot, HCore.itemBuilder(itemStack).lores(true, lore).name(true, name).build());
                }
            }
        }
        boolean fillEmpty = config.getBoolean("stats-gui.fill-empty.enabled");
        if(fillEmpty){
            while (toInventory().firstEmpty() != -1){
                setItem(toInventory().firstEmpty(),
                        HCore.itemBuilder(convert(config.getString("stats-gui.fill-empty.item"), player)).name(ColorUtil.colored(config.getString("stats-gui.fill-empty.name"))).build());
            }
        }
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
