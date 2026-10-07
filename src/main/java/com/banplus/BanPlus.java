package com.banplus;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class BanPlus extends JavaPlugin {

    private BanManager banManager;
    private LanguageManager languageManager;

    @Override
    public void onEnable() {

        /*
         * 创建默认 config.yml
         */
        saveDefaultConfig();

        /*
         * 创建语言管理器
         */
        languageManager =
                new LanguageManager(this);

        /*
         * 创建 Ban 管理器
         */
        banManager =
                new BanManager(this);

        /*
         * 创建命令处理器
         */
        BanCommand banCommand =
                new BanCommand(
                        this,
                        banManager,
                        languageManager
                );

        /*
         * 创建 Tab 补全
         */
        BanTabCompleter tabCompleter =
                new BanTabCompleter(
                        languageManager
                );

        /*
         * 注册命令
         */
        getCommand("ban")
                .setExecutor(banCommand);

        getCommand("ban")
                .setTabCompleter(
                        tabCompleter
                );

        getCommand("unban")
                .setExecutor(banCommand);

        getCommand("unban")
                .setTabCompleter(
                        tabCompleter
                );

        getCommand("betterban")
                .setExecutor(banCommand);

        getCommand("betterban")
                .setTabCompleter(
                        tabCompleter
                );

        /*
         * 注册事件
         */
        Bukkit.getPluginManager()
                .registerEvents(
                        new BanListener(
                                banManager,
                                languageManager
                        ),
                        this
                );

        /*
         * 每分钟检查一次过期 Ban
         */
        Bukkit.getScheduler()
                .runTaskTimer(
                        this,
                        () ->
                                banManager
                                        .checkExpiredBans(),
                        20L * 60L,
                        20L * 60L
                );

        getLogger().info(
                languageManager.get(
                        "plugin-enabled"
                )
        );
    }

    @Override
    public void onDisable() {

        if (languageManager != null) {

            getLogger().info(
                    languageManager.get(
                            "plugin-disabled"
                    )
            );

        } else {

            getLogger().info(
                    "Better Ban disabled!"
            );
        }
    }

    public BanManager getBanManager() {
        return banManager;
    }

    public LanguageManager getLanguageManager() {
        return languageManager;
    }
}