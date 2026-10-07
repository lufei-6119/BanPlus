package com.banplus;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.List;

public class BanListener implements Listener {

    private final BanManager banManager;
    private final LanguageManager language;

    public BanListener(
            BanManager banManager,
            LanguageManager language
    ) {

        this.banManager = banManager;
        this.language = language;
    }

    @EventHandler
    public void onPlayerLogin(
            PlayerLoginEvent event
    ) {

        if (!banManager.isBanned(
                event.getPlayer().getUniqueId()
        )) {
            return;
        }

        BanManager.BanData data =
                banManager.getBanData(
                        event.getPlayer()
                                .getUniqueId()
                );

        if (data == null) {
            return;
        }

        String reason =
                data.getReason();

        String endTime =
                banManager.formatDate(
                        data.getEndTime()
                );

        List<String> lines =
                language.getList(
                        "login-ban",
                        "{reason}",
                        reason,
                        "{time}",
                        endTime
                );

        String message =
                String.join(
                        "\n",
                        lines
                );

        event.disallow(
                PlayerLoginEvent.Result.KICK_BANNED,
                message
        );
    }
}