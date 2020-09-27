package de.papiertuch.nickaddon.utils;

import de.papiertuch.nickaddon.NickAddon;
import net.haoshoku.nick.NickPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Random;
import java.util.UUID;

/**
 * Created by Leon on 17.06.2019.
 * development with love.
 * © Copyright by Papiertuch
 */

public class NickAPI {

    private net.haoshoku.nick.api.NickAPI nickAPI;

    public NickAPI(Player player) {
        this.nickAPI = NickPlugin.getPlugin().getAPI();
        this.nickAPI.setTabCompleteStatus(0);
        this.nickAPI.setSkinChangingForPlayer(true);
    }

    public void setAutoNick(UUID uuid, boolean value) {
        Player player = Bukkit.getPlayer(uuid);
        if (value) {
            player.sendMessage(NickAddon.getInstance().getNickConfig().getString("message.autoNickEnable"));
            NickAddon.getInstance().getMySQL().setNick(uuid, true);
            setRandomNickName(uuid);
        } else {
            player.sendMessage(NickAddon.getInstance().getNickConfig().getString("message.autoNickDisable"));
            NickAddon.getInstance().getMySQL().setNick(uuid, false);
        }
    }

    public void setNick(UUID uuid, Boolean value) {
        Player player = Bukkit.getPlayer(uuid);
        if (value) {
            String nick;
            if (NickAddon.getInstance().getNickConfig().getBoolean("autoNick.randomName")) {
                nick = getRandomNickName();
            } else {
                nick = getNickName(uuid);
            }
            nickAPI.nick(player, nick);
            nickAPI.setSkin(player, nick);
            nickAPI.refreshPlayer(player);
            nickAPI.setGameProfileName(player, nick);
            player.sendMessage(NickAddon.getInstance().getNickConfig().getString("message.nick")
                    .replace("%nick%", nick));
            if (!NickAddon.getInstance().getNickPlayers().contains(uuid)) {
                NickAddon.getInstance().getNickPlayers().add(uuid);
            }
        } else {
            nickAPI.unnick(player);
            nickAPI.refreshPlayer(player);
            nickAPI.resetGameProfileName(player);
            player.sendMessage(NickAddon.getInstance().getNickConfig().getString("message.unNick"));
            NickAddon.getInstance().getNickPlayers().remove(uuid);
        }
    }

    public boolean getAutoNickState(UUID uuid) {
        return NickAddon.getInstance().getMySQL().getState(uuid);
    }

    public String getRandomNickName() {
        Random r = new Random();
        String name = NickAddon.getInstance().getNickConfig().getConfiguration().getStringList("nicks").get(r.nextInt(NickAddon.getInstance().getNickConfig().getConfiguration().getStringList("nicks").size()));
        return name;
    }

    private void setRandomNickName(UUID uuid) {
        Random r = new Random();
        String name = NickAddon.getInstance().getNickConfig().getConfiguration().getStringList("nicks").get(r.nextInt(NickAddon.getInstance().getNickConfig().getConfiguration().getStringList("nicks").size()));
        NickAddon.getInstance().getMySQL().setName(uuid, name);
    }

    public void disableNick(UUID uuid) {
        NickAddon.getInstance().getMySQL().setNick(uuid, false);
    }

    private String getNickName(UUID uuid) {
        return NickAddon.getInstance().getMySQL().getName(uuid);
    }

    public void setRandomNick(UUID uuid, Boolean value) {
        Player player = Bukkit.getPlayer(uuid);
        if (value) {
            Random r = new Random();
            String nick = NickAddon.getInstance().getNickConfig().getConfiguration().getStringList("nicks").get(r.nextInt(NickAddon.getInstance().getNickConfig().getConfiguration().getStringList("nicks").size()));
            nickAPI.nick(player, nick);
            nickAPI.setSkin(player, nick);
            nickAPI.setGameProfileName(player, nick);
            nickAPI.refreshPlayer(player);
            player.sendMessage(NickAddon.getInstance().getNickConfig().getString("message.nick")
                    .replace("%nick%", nick));
            if (!NickAddon.getInstance().getNickPlayers().contains(uuid)) {
                NickAddon.getInstance().getNickPlayers().add(uuid);
            }
        } else {
            nickAPI.unnick(player);
            nickAPI.refreshPlayer(player);
            nickAPI.resetGameProfileName(player);
            player.sendMessage(NickAddon.getInstance().getNickConfig().getString("message.unNick"));
            NickAddon.getInstance().getNickPlayers().remove(uuid);
        }
    }

}
