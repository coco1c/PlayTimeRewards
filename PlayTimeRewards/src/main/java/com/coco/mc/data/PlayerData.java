package com.coco.mc.data;

import com.coco.mc.LiteMCPlayTimeReward;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerData {

    private String name;
    private long joinTime;
    private int timesJoined;
    private long playTime;
    private UUID UUID;

    List<String> rewards;

    public PlayerData(String name, long joinTime, int timesJoined, UUID UUID) {
        this.name = name;
        this.joinTime = joinTime;
        this.timesJoined = timesJoined;
        this.UUID = UUID;
        this.playTime = 0;
        rewards = new ArrayList<>();
    }

    public List<String> getRewards() {
        return rewards;
    }

    public String getName() {
        return name;
    }

    public java.util.UUID getUUID() {
        return UUID;
    }

    public int getTimesJoined() {
        return timesJoined;
    }

    public long getJoinTime() {
        return joinTime;
    }

    public void setTimesJoined(int timesJoined) {
        this.timesJoined = timesJoined;
    }

    public int getWeeks(){
        Duration duration = Duration.ofMillis(playTime);
        return (int) (duration.toDays() / 7);
    }

    public int getDays(){
        Duration duration = Duration.ofMillis(playTime);
        return (int) (duration.toDays());
    }

    public int getHours(){
        Duration duration = Duration.ofMillis(playTime);
        return (int) (duration.toHours());
    }

    public int getMinutes(){
        Duration duration = Duration.ofMillis(playTime);
        return (int) (duration.toMinutes());
    }

    public int getSeconds(){
        Duration duration = Duration.ofMillis(playTime);
        return (int) (duration.toSeconds());
    }

    public long getPlayTime() {
        return playTime;
    }

    public void addPlayTime(long playTime){
        this.playTime += playTime;
    }

    public static @NotNull PlayerData get(UUID UUID){
        for (PlayerData playerDatum : LiteMCPlayTimeReward.inst.playerData) {
            if(playerDatum.getUUID().equals(UUID)){
                return playerDatum;
            }
        }

        PlayerData playerData = new PlayerData(Bukkit.getOfflinePlayer(UUID).getName(), System.currentTimeMillis(), 1, UUID);
        LiteMCPlayTimeReward.inst.playerData.add(playerData);
        return playerData;
    }
}
