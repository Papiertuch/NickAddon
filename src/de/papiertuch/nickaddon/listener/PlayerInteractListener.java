package de.papiertuch.nickaddon.listener;

import de.papiertuch.nickaddon.NickAddon;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by Leon on 17.06.2019.
 * development with love.
 * © Copyright by Papiertuch
 */

public class PlayerInteractListener implements Listener {


    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getPlayer() == null) return;
        if (event.getPlayer().getItemInHand() == null) return;
        if (event.getPlayer().getItemInHand().getItemMeta() == null) return;
        if (event.getPlayer().getItemInHand().getItemMeta().getDisplayName() == null) return;
        if (event.getAction() == null) return;
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            if (player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(NickAddon.getInstance().getNickConfig().getString("item.nick.name"))) {
                player.performCommand("nick");
                Sound sound = Sound.valueOf(NickAddon.getInstance().getNickConfig().getString("interact.sound"));
                if (sound != null) {
                    player.playSound(player.getLocation(), sound, 10F, 10F);
                }
            }
        }
    }
}
