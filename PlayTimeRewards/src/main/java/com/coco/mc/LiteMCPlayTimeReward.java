package com.coco.mc;

import com.coco.mc.command.*;
import com.coco.mc.data.PlayerData;
import com.coco.mc.listener.TabCompleteListener;
import com.coco.mc.placeholders.Placeholders;
import com.coco.mc.util.ConfigManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hakan.core.HCore;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public final class LiteMCPlayTimeReward extends JavaPlugin {

    public static LiteMCPlayTimeReward inst;
    public List<PlayerData> playerData;
    public long uptime;
    public ConfigManager rewards;
    public ConfigManager messages;

    @Override
    public void onEnable() {
        HCore.initialize(this);
        inst = this;
        playerData = new ArrayList<>();
        uptime = System.currentTimeMillis();
        new Placeholders().register();
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();
        if(getConfig().contains("plugin-do-not-edit")){
            playerData = new Gson().fromJson(getConfig().getString("plugin-do-not-edit"), new TypeToken<List<PlayerData>>(){}.getType());
        }
        copyResourceFromJar(this, "rewards.yml");
        copyResourceFromJar(this, "messages.yml");
        rewards = new ConfigManager(this, "rewards", getDataFolder().getAbsolutePath());
        messages = new ConfigManager(this, "messages", getDataFolder().getAbsolutePath());
        HCore.registerCommands(new PlayTImeRewardsCommand(), new StatsCommand(), new PlayTimeCommand(), new RewardRestCommand(), new RewardEditorCommand());
        HCore.registerListeners(new TabCompleteListener());



    }

    @Override
    public void onDisable() {
       getConfig().set("plugin-do-not-edit", new Gson().toJson(playerData));
       saveConfig();
    }

    public static void copyResourceFromJar(JavaPlugin plugin, String resourceName) {
        File pluginFolder = plugin.getDataFolder();
        if (!pluginFolder.exists()) {
            pluginFolder.mkdirs();
        }

        File destFile = new File(pluginFolder, resourceName);

        if (!destFile.exists()) {
            try (InputStream inputStream = plugin.getResource(resourceName);
                 FileOutputStream outputStream = new FileOutputStream(destFile)) {

                if (inputStream != null) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }
                }
            } catch (IOException e) {
                Bukkit.getLogger().warning("Error copying resource " + resourceName + ": " + e.getMessage());
            }
        }
    }
}
