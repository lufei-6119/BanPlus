package com.banplus;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BanTabCompleter
        implements TabCompleter {

    private final LanguageManager language;

    public BanTabCompleter(
            LanguageManager language
    ) {

        this.language = language;
    }

    @Override
    public List<String> onTabComplete(
            CommandSender sender,
            Command command,
            String alias,
            String[] args
    ) {

        /*
         * /ban
         */
        if (command.getName()
                .equalsIgnoreCase("ban")) {

            // 玩家
            if (args.length == 1) {

                return getPlayers(args[0]);
            }

            // 天数
            if (args.length == 2) {

                return filter(
                        Arrays.asList(
                                "1",
                                "3",
                                "7",
                                "14",
                                "30",
                                "90"
                        ),
                        args[1]
                );
            }

            // 原因
            if (args.length == 3) {

                return filter(
                        language.getList(
                                "tab-reasons"
                        ),
                        args[2]
                );
            }
        }

        /*
         * /unban
         */
        if (command.getName()
                .equalsIgnoreCase("unban")) {

            if (args.length == 1) {

                return getPlayers(args[0]);
            }
        }

        /*
         * /betterban
         */
        if (command.getName()
                .equalsIgnoreCase("betterban")) {

            // ban / unban
            if (args.length == 1) {

                return filter(
                        Arrays.asList(
                                "ban",
                                "unban"
                        ),
                        args[0]
                );
            }

            // ban 玩家
            if (args.length == 2 &&
                    args[0]
                            .equalsIgnoreCase("ban")) {

                return getPlayers(args[1]);
            }

            // ban 玩家 天数
            if (args.length == 3 &&
                    args[0]
                            .equalsIgnoreCase("ban")) {

                return filter(
                        Arrays.asList(
                                "1",
                                "3",
                                "7",
                                "14",
                                "30",
                                "90"
                        ),
                        args[2]
                );
            }

            // ban 玩家 天数 原因
            if (args.length == 4 &&
                    args[0]
                            .equalsIgnoreCase("ban")) {

                return filter(
                        language.getList(
                                "tab-reasons"
                        ),
                        args[3]
                );
            }

            // unban 玩家
            if (args.length == 2 &&
                    args[0]
                            .equalsIgnoreCase("unban")) {

                return getPlayers(args[1]);
            }
        }

        return new ArrayList<>();
    }

    /**
     * 获取在线玩家
     */
    private List<String> getPlayers(
            String input
    ) {

        return Bukkit.getOnlinePlayers()
                .stream()
                .map(Player::getName)
                .filter(name ->
                        name.toLowerCase()
                                .startsWith(
                                        input.toLowerCase()
                                )
                )
                .collect(
                        Collectors.toList()
                );
    }

    /**
     * 过滤 Tab 补全
     */
    private List<String> filter(
            List<String> suggestions,
            String input
    ) {

        return suggestions.stream()
                .filter(value ->
                        value.toLowerCase()
                                .startsWith(
                                        input.toLowerCase()
                                )
                )
                .collect(
                        Collectors.toList()
                );
    }
}