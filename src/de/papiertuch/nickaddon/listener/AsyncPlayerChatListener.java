package de.papiertuch.nickaddon.listener;

import de.papiertuch.nickaddon.NickAddon;
import de.papiertuch.nickaddon.utils.TabListGroup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Created by Leon on 20.06.2019.
 * development with love.
 * © Copyright by Papiertuch
 */

public class AsyncPlayerChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

        if (NickAddon.getInstance().getNickConfig().getBoolean("lobbyMode.enable") && !NickAddon.getInstance().getNickConfig().getBoolean("lobbyMode.chatEnable")) {
            return;
        }
        if (Bukkit.getPluginManager().getPlugin("BedWars") == null) {
            Player player = event.getPlayer();
            TabListGroup tabListGroup = NickAddon.getInstance().getTabListGroup(player);
            if (player.hasPermission("chat.color")) {
                event.setFormat(ChatColor.translateAlternateColorCodes('&', NickAddon.getInstance().getNickConfig().getString("chat.format")
                        .replace("%player%", player.getName())
                        .replace("%display%", tabListGroup.getDisplay())
                        .replace("%prefix%", tabListGroup.getPrefix())
                        .replace("%suffix%", tabListGroup.getSuffix())
                        .replace("%group%", tabListGroup.getName())
                        .replace("%message%", event.getMessage())).replace("%", "%%"));
                return;
            }
            event.setFormat(NickAddon.getInstance().getNickConfig().getString("chat.format")
                    .replace("%player%", player.getName())
                    .replace("%display%", tabListGroup.getDisplay())
                    .replace("%prefix%", tabListGroup.getPrefix())
                    .replace("%suffix%", tabListGroup.getSuffix())
                    .replace("%group%", tabListGroup.getName())
                    .replace("%message%", event.getMessage()).replace("%", "%%"));
        }
    }
}
