package de.papiertuch.nickaddon.utils;

import de.papiertuch.nickaddon.NickAddon;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.haoshoku.nick.NickPlugin;
import xyz.haoshoku.nick.api.NickAPI;

import java.util.Random;
import java.util.UUID;

/**
 * Created by Leon on 17.06.2019.
 * development with love.
 * © Copyright by Papiertuch
 */

public class NickAddonAPI {

    public void setAutoNick(Player player, boolean value) {
        if (value) {
            player.sendMessage(NickAddon.getInstance().getNickConfig().getString("message.autoNickEnable"));
            NickAddon.getInstance().getMySQL().setNick(player.getUniqueId(), true);
            setRandomNickName(player);
        } else {
            player.sendMessage(NickAddon.getInstance().getNickConfig().getString("message.autoNickDisable"));
            NickAddon.getInstance().getMySQL().setNick(player.getUniqueId(), false);
        }
    }

    public void setNick(Player player, Boolean value) {
        if (value) {
            String nick = (NickAddon.getInstance().getNickConfig().getBoolean("autoNick.randomName") ? getRandomNickName() : getNickName(player));
            NickAPI.setSkin(player, nick);
            NickAPI.setUniqueId(player, nick);
            NickAPI.nick(player, nick);
            NickAPI.setGameProfileName(player, nick);
            NickAPI.refreshPlayer(player);
            player.sendMessage(NickAddon.getInstance().getNickConfig().getString("message.nick")
                    .replace("%nick%", nick));
        } else {
            NickAPI.resetNick(player);
            NickAPI.resetSkin(player);
            NickAPI.resetUniqueId(player);
            NickAPI.refreshPlayer(player);
            player.sendMessage(NickAddon.getInstance().getNickConfig().getString("message.unNick"));
        }
    }

    public boolean getAutoNickState(Player player) {
        return NickAddon.getInstance().getMySQL().getState(player.getUniqueId());
    }

    public String getRandomNickName() {
        Random r = new Random();
        String name = NickAddon.getInstance().getNickConfig().getConfiguration().getStringList("nicks").get(r.nextInt(NickAddon.getInstance().getNickConfig().getConfiguration().getStringList("nicks").size()));
        return name;
    }

    private void setRandomNickName(Player player) {
        Random r = new Random();
        String name = NickAddon.getInstance().getNickConfig().getConfiguration().getStringList("nicks").get(r.nextInt(NickAddon.getInstance().getNickConfig().getConfiguration().getStringList("nicks").size()));
        NickAddon.getInstance().getMySQL().setName(player.getUniqueId(), name);
    }

    public void disableNick(Player player) {
        NickAddon.getInstance().getMySQL().setNick(player.getUniqueId(), false);
    }

    private String getNickName(Player player) {
        return NickAddon.getInstance().getMySQL().getName(player.getUniqueId());
    }

    public void setRandomNick(Player player, Boolean value) {
        if (value) {
            Random r = new Random();
            String nick = NickAddon.getInstance().getNickConfig().getConfiguration().getStringList("nicks").get(r.nextInt(NickAddon.getInstance().getNickConfig().getConfiguration().getStringList("nicks").size()));
            NickAPI.setSkin(player, nick);
            NickAPI.setUniqueId(player, nick);
            NickAPI.nick(player, nick);
            NickAPI.setGameProfileName(player, nick);
            NickAPI.refreshPlayer(player);
            player.sendMessage(NickAddon.getInstance().getNickConfig().getString("message.nick")
                    .replace("%nick%", nick));
        } else {
            NickAPI.resetNick(player);
            NickAPI.resetSkin(player);
            NickAPI.resetUniqueId(player);
            NickAPI.refreshPlayer(player);
            player.sendMessage(NickAddon.getInstance().getNickConfig().getString("message.unNick"));
        }
    }

}
