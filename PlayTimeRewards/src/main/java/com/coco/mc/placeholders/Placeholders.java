package com.coco.mc.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import com.coco.mc.LiteMCPlayTimeReward;
import com.coco.mc.data.PlayerData;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;

public class Placeholders extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "playtime";
    }

    @Override
    public @NotNull String getAuthor() {
        return "coco";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        PlayerData playerData = PlayerData.get(player.getUniqueId());
        List<PlayerData> playerDatas = LiteMCPlayTimeReward.inst.playerData.stream()
                .sorted(Comparator.comparingLong(PlayerData::getPlayTime).reversed())
                .toList();
        long timeSpentMillis = playerData.getPlayTime();
        Duration duration = Duration.ofMillis(timeSpentMillis);

        // Placeholder replacements
        String formattedPlayTime = "";
        String playTimeInSeconds = String.valueOf(duration.toSeconds());
        String playTimeInMinutes = String.valueOf(duration.toMinutes());
        String playTimeInHours = String.valueOf(duration.toHours());
        String playTimeInDays = String.valueOf(duration.toDays());
        String playTimeInWeeks = String.valueOf(duration.toDays() / 7);
        String timesJoined = String.valueOf(playerData.getTimesJoined());

        // Calculate time components
        long days = duration.toDays();
        long hours = duration.toHoursPart();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();
        long week = duration.toDays() / 7;

        if (days > 0) {
            formattedPlayTime += days + " Day" + (days > 1 ? "s" : "");
        } else if (hours > 0) {
            formattedPlayTime += hours + " Hour" + (hours > 1 ? "s" : "");
        } else if (minutes > 0) {
            formattedPlayTime += minutes + " Minute" + (minutes > 1 ? "s" : "");
        } else if (seconds > 0) {
            formattedPlayTime += seconds + " Second" + (seconds > 1 ? "s" : "");
        }

        String formated = "";
        if (days > 0) {
            formated += days + "d ";
        }
        if (hours > 0) {
            formated += hours + "h ";
        }
        if (minutes > 0) {
            formated += minutes + "m ";
        }
        if (seconds > 0) {
            formated += seconds + "s";
        }



        if (params.equalsIgnoreCase("player")) {
            return playerData.getName();
        }
        if (params.equalsIgnoreCase("time")) {
           return formated;
        }
        if (params.equalsIgnoreCase("time_small")) {
           return formattedPlayTime;
        }
        if(params.equalsIgnoreCase("time_seconds")){
            return playTimeInSeconds;
        }
        if(params.equalsIgnoreCase("time_minutes")){
            return playTimeInMinutes;
        }
        if(params.equalsIgnoreCase("time_hours")){
            return playTimeInHours;
        }
        if(params.equalsIgnoreCase("time_days")){
            return playTimeInDays;
        }
        if(params.equalsIgnoreCase("time_weeks")){
            return playTimeInWeeks;
        }
        if(params.equalsIgnoreCase("times_joined")){
            return timesJoined;
        }
        if ((params.equalsIgnoreCase("serveruptime"))){
            Duration duration1 = Duration.ofMillis(System.currentTimeMillis() - LiteMCPlayTimeReward.inst.uptime);
            return String.format("%02d:%02d:%02d", duration1.toHoursPart(), duration1.toMinutesPart(), duration1.toSecondsPart());
        }
        if(params.equalsIgnoreCase("top_1_name")){
            try {
                return playerDatas.get(0).getName();
            }catch (Exception e){
                return "None";
            }
        }
        if(params.equalsIgnoreCase("top_2_name")){
            try {
                return playerDatas.get(1).getName();
            }catch (Exception e){
                return "None";
            }
        }
        if(params.equalsIgnoreCase("top_3_name")){
            try {
                return playerDatas.get(2).getName();
            }catch (Exception e){
                return "None";
            }
        }
        if(params.equalsIgnoreCase("top_4_name")){
            try {
                return playerDatas.get(3).getName();
            }catch (Exception e){
                return "None";
            }
        }
        if(params.equalsIgnoreCase("top_5_name")){
            try {
                return playerDatas.get(4).getName();
            }catch (Exception e){
                return "None";
            }
        }
        if(params.equalsIgnoreCase("top_6_name")){
            try {
                return playerDatas.get(5).getName();
            }catch (Exception e){
                return "None";
            }
        }
        if(params.equalsIgnoreCase("top_7_name")){
            try {
                return playerDatas.get(6).getName();
            }catch (Exception e){
                return "None";
            }
        }
        if(params.equalsIgnoreCase("top_8_name")){
            try {
                return playerDatas.get(7).getName();
            }catch (Exception e){
                return "None";
            }
        }
        if(params.equalsIgnoreCase("top_9_name")){
            try {
                return playerDatas.get(8).getName();
            }catch (Exception e){
                return "None";
            }
        }
        if(params.equalsIgnoreCase("top_10_name")){
            try {
                return playerDatas.get(9).getName();
            }catch (Exception e){
                return "None";
            }
        }

        if(params.equalsIgnoreCase("top_1_time")){
            try {
                return playerDatas.get(0).getName();
            }catch (Exception e){
                return "00:00:00";
            }
        }
        if(params.equalsIgnoreCase("top_2_time")){
            try {
                return playerDatas.get(1).getName();
            }catch (Exception e){
                return "00:00:00";
            }
        }
        if(params.equalsIgnoreCase("top_3_time")){
            try {
                return playerDatas.get(2).getName();
            }catch (Exception e){
                return "00:00:00";
            }
        }
        if(params.equalsIgnoreCase("top_4_time")){
            try {
                return playerDatas.get(3).getName();
            }catch (Exception e){
                return "00:00:00";
            }
        }
        if(params.equalsIgnoreCase("top_5_time")){
            try {
                return playerDatas.get(4).getName();
            }catch (Exception e){
                return "00:00:00";
            }
        }

        if(params.equalsIgnoreCase("top_6_time")){
            try {
                return playerDatas.get(5).getName();
            }catch (Exception e){
                return "00:00:00";
            }
        }

        if(params.equalsIgnoreCase("top_7_time")){
            try {
                return playerDatas.get(6).getName();
            }catch (Exception e){
                return "00:00:00";
            }
        }
        if(params.equalsIgnoreCase("top_8_time")){
            try {
                return playerDatas.get(7).getName();
            }catch (Exception e){
                return "00:00:00";
            }
        }
        if(params.equalsIgnoreCase("top_9_time")){
            try {
                return playerDatas.get(8).getName();
            }catch (Exception e){
                return "00:00:00";
            }
        }
        if(params.equalsIgnoreCase("top_10_time")){
            try {
                return playerDatas.get(9).getName();
            }catch (Exception e){
                return "00:00:00";
            }
        }

        if(params.equalsIgnoreCase("position")){
            for (int i = 0; i < playerDatas.size(); i++) {
                if(playerDatas.get(i).getUUID().equals(playerData.getUUID())){
                    return String.valueOf(i + 1);
                }
            }
        }




        return null;
    }

    public String convertToFormat(long time){
        Duration duration = Duration.ofMillis(System.currentTimeMillis() - time);
        return String.format("%02d:%02d:%02d", duration.toHoursPart(), duration.toMinutesPart(), duration.toSecondsPart());
    }

}
