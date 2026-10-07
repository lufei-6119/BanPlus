package com.banplus;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BanCommand implements CommandExecutor {

    private final BanPlus plugin;
    private final BanManager banManager;
    private final LanguageManager language;

    public BanCommand(
            BanPlus plugin,
            BanManager banManager,
            LanguageManager language
    ) {

        this.plugin = plugin;
        this.banManager = banManager;
        this.language = language;
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        /*
         * /ban
         */
        if (command.getName().equalsIgnoreCase("ban")) {

            if (!sender.hasPermission("betterban.ban")) {
                sender.sendMessage(
                        language.get("no-permission")
                );
                return true;
            }

            if (args.length < 3) {
                sender.sendMessage(
                        language.get("ban-usage")
                );
                return true;
            }

            return executeBan(sender, args);
        }

        /*
         * /unban
         */
        if (command.getName().equalsIgnoreCase("unban")) {

            if (!sender.hasPermission("betterban.unban")) {
                sender.sendMessage(
                        language.get("no-permission")
                );
                return true;
            }

            if (args.length != 1) {
                sender.sendMessage(
                        language.get("unban-usage")
                );
                return true;
            }

            return executeUnban(
                    sender,
                    args[0]
            );
        }

        /*
         * /betterban
         */
        if (command.getName().equalsIgnoreCase("betterban")) {

            if (args.length == 0) {
                sendHelp(sender);
                return true;
            }

            /*
             * /betterban ban
             */
            if (args[0].equalsIgnoreCase("ban")) {

                if (!sender.hasPermission("betterban.ban")) {
                    sender.sendMessage(
                            language.get("no-permission")
                    );
                    return true;
                }

                if (args.length < 4) {
                    sender.sendMessage(
                            language.get(
                                    "betterban-ban-usage"
                            )
                    );
                    return true;
                }

                String[] banArgs =
                        new String[args.length - 1];

                System.arraycopy(
                        args,
                        1,
                        banArgs,
                        0,
                        banArgs.length
                );

                return executeBan(
                        sender,
                        banArgs
                );
            }

            /*
             * /betterban unban
             */
            if (args[0].equalsIgnoreCase("unban")) {

                if (!sender.hasPermission("betterban.unban")) {
                    sender.sendMessage(
                            language.get("no-permission")
                    );
                    return true;
                }

                if (args.length != 2) {
                    sender.sendMessage(
                            language.get(
                                    "betterban-unban-usage"
                            )
                    );
                    return true;
                }

                return executeUnban(
                        sender,
                        args[1]
                );
            }

            sendHelp(sender);
            return true;
        }

        return false;
    }

    /**
     * 执行 Ban
     */
    private boolean executeBan(
            CommandSender sender,
            String[] args
    ) {

        String playerName = args[0];

        int days;

        try {

            days = Integer.parseInt(args[1]);

        } catch (NumberFormatException e) {

            sender.sendMessage(
                    language.get("invalid-days")
            );

            return true;
        }

        if (days <= 0) {

            sender.sendMessage(
                    language.get(
                            "days-must-positive"
                    )
            );

            return true;
        }

        /*
         * 获取原因
         */
        StringBuilder reasonBuilder =
                new StringBuilder();

        for (int i = 2; i < args.length; i++) {

            if (i > 2) {
                reasonBuilder.append(" ");
            }

            reasonBuilder.append(args[i]);
        }

        String reason =
                reasonBuilder.toString();

        /*
         * 获取玩家
         */
        Player target =
                Bukkit.getPlayerExact(playerName);

        UUID uuid;

        if (target != null) {

            uuid = target.getUniqueId();

        } else {

            OfflinePlayer offlinePlayer =
                    Bukkit.getOfflinePlayer(
                            playerName
                    );

            if (!offlinePlayer.hasPlayedBefore()) {

                sender.sendMessage(
                        language.get(
                                "player-not-found",
                                "{player}",
                                playerName
                        )
                );

                return true;
            }

            uuid =
                    offlinePlayer.getUniqueId();
        }

        /*
         * 禁止封禁自己
         */
        if (sender instanceof Player player) {

            if (player.getUniqueId()
                    .equals(uuid)) {

                sender.sendMessage(
                        language.get(
                                "cannot-ban-self"
                        )
                );

                return true;
            }
        }

        /*
         * 禁止封禁 OP
         */
        if (target != null && target.isOp()) {

            sender.sendMessage(
                    language.get(
                            "cannot-ban-op"
                    )
            );

            return true;
        }

        /*
         * 计算时间
         */
        long duration =
                days *
                        24L *
                        60L *
                        60L *
                        1000L;

        banManager.banPlayer(
                uuid,
                duration,
                reason
        );

        long endTime =
                System.currentTimeMillis()
                        + duration;

        String endDate =
                banManager.formatDate(
                        endTime
                );

        /*
         * 成功提示
         */
        sender.sendMessage(
                language.get(
                        "ban-success",
                        "{player}",
                        playerName
                )
        );

        sender.sendMessage(
                language.get(
                        "ban-duration",
                        "{days}",
                        String.valueOf(days)
                )
        );

        sender.sendMessage(
                language.get(
                        "ban-reason",
                        "{reason}",
                        reason
                )
        );

        sender.sendMessage(
                language.get(
                        "ban-end-time",
                        "{time}",
                        endDate
                )
        );

        /*
         * 踢出在线玩家
         */
        if (target != null) {

            String kickMessage =
                    String.join(
                            "\n",
                            language.getList(
                                    "kick-ban",
                                    "{reason}",
                                    reason,
                                    "{days}",
                                    String.valueOf(days),
                                    "{time}",
                                    endDate
                            )
                    );

            target.kickPlayer(
                    kickMessage
            );
        }

        /*
         * 全服广播
         */
        Bukkit.broadcastMessage(
                language.get(
                        "ban-broadcast",
                        "{player}",
                        playerName,
                        "{days}",
                        String.valueOf(days)
                )
        );

        return true;
    }

    /**
     * 执行 Unban
     */
    private boolean executeUnban(
            CommandSender sender,
            String playerName
    ) {

        OfflinePlayer player =
                Bukkit.getOfflinePlayer(
                        playerName
                );

        if (!player.hasPlayedBefore()) {

            sender.sendMessage(
                    language.get(
                            "player-not-found",
                            "{player}",
                            playerName
                    )
            );

            return true;
        }

        UUID uuid =
                player.getUniqueId();

        if (!banManager.isBanned(uuid)) {

            sender.sendMessage(
                    language.get(
                            "not-banned",
                            "{player}",
                            playerName
                    )
            );

            return true;
        }

        banManager.unbanPlayer(uuid);

        sender.sendMessage(
                language.get(
                        "unban-success",
                        "{player}",
                        playerName
                )
        );

        Bukkit.broadcastMessage(
                language.get(
                        "unban-broadcast",
                        "{player}",
                        playerName
                )
        );

        return true;
    }

    /**
     * 帮助
     */
    private void sendHelp(
            CommandSender sender
    ) {

        for (String line :
                language.getList("help")) {

            sender.sendMessage(line);
        }
    }
}