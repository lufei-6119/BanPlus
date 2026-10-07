package com.banplus;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BanManager {

    private final BanPlus plugin;
    private final File banFile;
    private final YamlConfiguration bans;

    private final Map<UUID, BanData> banCache = new HashMap<>();

    public BanManager(BanPlus plugin) {
        this.plugin = plugin;

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        this.banFile = new File(plugin.getDataFolder(), "bans.yml");
        this.bans = YamlConfiguration.loadConfiguration(banFile);

        loadBans();
    }

    /**
     * 加载所有封禁数据
     */
    private void loadBans() {
        ConfigurationSection section = bans.getConfigurationSection("bans");

        if (section == null) {
            return;
        }

        for (String uuidString : section.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(uuidString);

                long startTime = section.getLong(uuidString + ".start-time");
                long endTime = section.getLong(uuidString + ".end-time");
                String reason = section.getString(uuidString + ".reason", "未提供原因");

                BanData data = new BanData(uuid, startTime, endTime, reason);

                // 已经过期的 Ban 直接删除
                if (System.currentTimeMillis() >= endTime) {
                    bans.set("bans." + uuidString, null);
                } else {
                    banCache.put(uuid, data);
                }

            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("发现无效的 UUID: " + uuidString);
            }
        }

        saveFile();
    }

    /**
     * 封禁玩家
     */
    public void banPlayer(UUID uuid, long duration, String reason) {

        long startTime = System.currentTimeMillis();
        long endTime = startTime + duration;

        BanData data = new BanData(
                uuid,
                startTime,
                endTime,
                reason
        );

        banCache.put(uuid, data);

        String path = "bans." + uuid.toString();

        bans.set(path + ".start-time", startTime);
        bans.set(path + ".end-time", endTime);
        bans.set(path + ".reason", reason);

        saveFile();
    }

    /**
     * 解封玩家
     */
    public boolean unbanPlayer(UUID uuid) {

        if (!banCache.containsKey(uuid)) {
            return false;
        }

        banCache.remove(uuid);

        bans.set("bans." + uuid.toString(), null);

        saveFile();

        return true;
    }

    /**
     * 判断玩家是否被封禁
     */
    public boolean isBanned(UUID uuid) {

        BanData data = banCache.get(uuid);

        if (data == null) {
            return false;
        }

        // 到期自动解封
        if (System.currentTimeMillis() >= data.getEndTime()) {
            unbanPlayer(uuid);
            return false;
        }

        return true;
    }

    /**
     * 获取封禁数据
     */
    public BanData getBanData(UUID uuid) {

        if (!isBanned(uuid)) {
            return null;
        }

        return banCache.get(uuid);
    }

    /**
     * 定期检查所有 Ban 是否到期
     */
    public void checkExpiredBans() {

        for (UUID uuid : new HashMap<>(banCache).keySet()) {

            if (!isBanned(uuid)) {
                continue;
            }

            BanData data = banCache.get(uuid);

            if (System.currentTimeMillis() >= data.getEndTime()) {
                unbanPlayer(uuid);
            }
        }
    }

    /**
     * 保存 bans.yml
     */
    private void saveFile() {

        try {
            bans.save(banFile);
        } catch (IOException e) {
            plugin.getLogger().severe("无法保存 bans.yml！");
            e.printStackTrace();
        }
    }

    /**
     * 获取玩家名字
     */
    public String getPlayerName(UUID uuid) {

        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);

        if (player.getName() != null) {
            return player.getName();
        }

        return uuid.toString();
    }

    /**
     * 格式化时间
     */
    public String formatDate(long timestamp) {

        SimpleDateFormat format =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return format.format(new Date(timestamp));
    }

    /**
     * Ban 数据
     */
    public static class BanData {

        private final UUID uuid;
        private final long startTime;
        private final long endTime;
        private final String reason;

        public BanData(
                UUID uuid,
                long startTime,
                long endTime,
                String reason
        ) {
            this.uuid = uuid;
            this.startTime = startTime;
            this.endTime = endTime;
            this.reason = reason;
        }

        public UUID getUuid() {
            return uuid;
        }

        public long getStartTime() {
            return startTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public String getReason() {
            return reason;
        }
    }
}