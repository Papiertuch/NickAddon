package de.papiertuch.nickaddon.commands;

import de.papiertuch.bedwars.BedWars;
import de.papiertuch.nickaddon.NickAddon;
import de.papiertuch.nickaddon.utils.NickAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by Leon on 17.06.2019.
 * development with love.
 * © Copyright by Papiertuch
 */

public class Nick implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Player player = (Player) commandSender;
        if (player.hasPermission(NickAddon.getInstance().getNickConfig().getString("command.nick.permission"))) {
            if (NickAddon.getInstance().getNickConfig().getBoolean("lobbyMode.enable")) {
                NickAPI nickAPI = new NickAPI(player);
                boolean bool = nickAPI.getAutoNickState(player.getUniqueId());
                if (bool) {
                    nickAPI.setAutoNick(player.getUniqueId(), false);
                } else {
                    nickAPI.setAutoNick(player.getUniqueId(), true);
                }
            } else if (Bukkit.getPluginManager().getPlugin("BedWars") != null) {
                if (!BedWars.getInstance().getSpectators().contains(player.getUniqueId())) {
                    NickAPI nickAPI = new NickAPI(player);
                    if (args.length == 0) {
                        if (NickAddon.getInstance().getNickPlayers().contains(player.getUniqueId())) {
                            nickAPI.setRandomNick(player.getUniqueId(), false);
                            BedWars.getInstance().getBoard().addPlayerToBoard(player);
                        } else {
                            nickAPI.setRandomNick(player.getUniqueId(), true);
                            BedWars.getInstance().getBoard().addPlayerToBoard(player);
                        }
                    } else if (args[0].equalsIgnoreCase("list")) {
                        if (NickAddon.getInstance().getNickPlayers().size() != 0) {
                            player.sendMessage(NickAddon.getInstance().getNickConfig().getString("prefix") + " §7Hier sind alle genickten Spieler");
                            for (int i = 0; i < NickAddon.getInstance().getNickPlayers().size(); i++) {
                                UUID uuid = NickAddon.getInstance().getNickPlayers().get(i);
                                Player target = Bukkit.getPlayer(uuid);
                                player.sendMessage(NickAddon.getInstance().getNickConfig().getString("prefix") + " " + target.getDisplayName() + " §8» §7" + NickAddon.getInstance().getMySQL().getRealName(uuid));
                            }
                        } else {
                            player.sendMessage(NickAddon.getInstance().getNickConfig().getString("prefix") + " §cEs sind keine Spieler genickt!");
                        }
                    }
                } else {
                    player.sendMessage(NickAddon.getInstance().getNickConfig().getString("prefix") + " §cDu kannst dich nicht als Spectator nicken!");
                }
            } else {
                NickAPI nickAPI = new NickAPI(player);
                if (args.length == 0) {
                    if (NickAddon.getInstance().getNickPlayers().contains(player.getUniqueId())) {
                        nickAPI.setRandomNick(player.getUniqueId(), false);
                        NickAddon.getInstance().updateNameTags(player);
                    } else {
                        nickAPI.setRandomNick(player.getUniqueId(), true);
                        NickAddon.getInstance().updateNameTags(player);
                    }
                } else if (args[0].equalsIgnoreCase("list")) {
                    if (NickAddon.getInstance().getNickPlayers().size() != 0) {
                        player.sendMessage(NickAddon.getInstance().getNickConfig().getString("prefix") + " §7Hier sind alle genickten Spieler");
                        for (int i = 0; i < NickAddon.getInstance().getNickPlayers().size(); i++) {
                            UUID uuid = NickAddon.getInstance().getNickPlayers().get(i);
                            Player target = Bukkit.getPlayer(uuid);
                            player.sendMessage(NickAddon.getInstance().getNickConfig().getString("prefix") + target.getDisplayName() + " §8» §7" + NickAddon.getInstance().getMySQL().getRealName(uuid));
                        }
                    } else {
                        player.sendMessage(NickAddon.getInstance().getNickConfig().getString("prefix") + " §cEs sind keine Spieler genickt!");
                    }
                }
            }
        } else {
            player.sendMessage(NickAddon.getInstance().getNickConfig().getString("message.noPerms"));
        }
        return false;
    }
}
