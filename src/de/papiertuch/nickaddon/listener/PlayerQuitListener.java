package de.papiertuch.nickaddon.listener;

import de.papiertuch.nickaddon.NickAddon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Leon on 03.07.2019.
 * development with love.
 * © Copyright by Papiertuch
 */

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (NickAddon.getInstance().getNickPlayers().contains(player.getUniqueId())) {
            NickAddon.getInstance().getNickPlayers().remove(player.getUniqueId());
        }
    }

}
