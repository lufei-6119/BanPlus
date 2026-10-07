package com.banplus;

import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

public class LanguageManager {

    private final BanPlus plugin;

    public LanguageManager(BanPlus plugin) {
        this.plugin = plugin;
    }

    /**
     * 获取当前语言
     */
    public String getLanguage() {
        return plugin.getConfig().getString("language", "zh_CN");
    }

    /**
     * 获取语言文本
     */
    public String get(String path) {
        String language = getLanguage();

        String key = "messages." + language + "." + path;

        String message = plugin.getConfig().getString(key);

        /*
         * 如果当前语言不存在，则回退到中文
         */
        if (message == null) {
            message = plugin.getConfig().getString(
                    "messages.zh_CN." + path,
                    path
            );
        }

        return color(message);
    }

    /**
     * 获取文本并替换变量
     *
     * 例如：
     * {player}
     * {days}
     * {reason}
     */
    public String get(String path, String... replacements) {

        String message = get(path);

        for (int i = 0; i + 1 < replacements.length; i += 2) {

            String key = replacements[i];
            String value = replacements[i + 1];

            message = message.replace(
                    key,
                    value
            );
        }

        return message;
    }

    /**
     * 获取多行文本
     */
    public List<String> getList(String path) {

        String language = getLanguage();

        List<String> messages =
                plugin.getConfig().getStringList(
                        "messages." +
                                language +
                                "." +
                                path
                );

        if (messages.isEmpty()) {

            messages =
                    plugin.getConfig().getStringList(
                            "messages.zh_CN." +
                                    path
                    );
        }

        return messages.stream()
                .map(this::color)
                .collect(Collectors.toList());
    }

    /**
     * 替换变量
     */
    public List<String> getList(
            String path,
            String... replacements
    ) {

        List<String> messages = getList(path);

        for (int i = 0; i + 1 < replacements.length; i += 2) {

            String key = replacements[i];
            String value = replacements[i + 1];

            messages = messages.stream()
                    .map(message ->
                            message.replace(key, value))
                    .collect(Collectors.toList());
        }

        return messages;
    }

    /**
     * Minecraft 颜色代码
     */
    private String color(String text) {

        if (text == null) {
            return "";
        }

        return ChatColor.translateAlternateColorCodes(
                '&',
                text
        );
    }
}