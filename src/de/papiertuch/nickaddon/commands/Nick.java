package de.papiertuch.nickaddon.commands;

import de.papiertuch.bedwars.BedWars;
import de.papiertuch.nickaddon.NickAddon;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.haoshoku.nick.api.NickAPI;

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
        if (!player.hasPermission(NickAddon.getInstance().getNickConfig().getString("command.nick.permission"))) {
            player.sendMessage(NickAddon.getInstance().getNickConfig().getString("message.noPerms"));
            return true;
        }
        if (NickAddon.getInstance().getNickConfig().getBoolean("lobbyMode.enable")) {
            NickAddon.getInstance().getApi().setAutoNick(player, !NickAddon.getInstance().getApi().getAutoNickState(player));
            return true;
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            if (!NickAPI.getNickedPlayers().isEmpty()) {
                player.sendMessage(NickAddon.getInstance().getNickConfig().getString("prefix") + " §7Hier sind alle genickten Spieler");
                for (UUID uuid : NickAPI.getNickedPlayers().keySet()) {
                    Player target = Bukkit.getPlayer(uuid);
                    player.sendMessage(NickAddon.getInstance().getNickConfig().getString("prefix") + " " + target.getDisplayName() + " §8» §7" + NickAPI.getOriginalName(target));
                }
            } else {
                player.sendMessage(NickAddon.getInstance().getNickConfig().getString("prefix") + " §cEs sind keine Spieler genickt!");
            }
            return true;
        }
        if (Bukkit.getPluginManager().getPlugin("BedWars") == null) {
            NickAddon.getInstance().getApi().setRandomNick(player, !NickAPI.isNicked(player));
            NickAddon.getInstance().updateNameTags(player);
        } else {
            if (BedWars.getInstance().getSpectators().contains(player.getUniqueId())) {
                player.sendMessage(NickAddon.getInstance().getNickConfig().getString("prefix") + " §cDu kannst dich nicht als Spectator nicken!");
                return true;
            }
            NickAddon.getInstance().getApi().setRandomNick(player, !NickAPI.isNicked(player));
            BedWars.getInstance().getBoard().addPlayerToBoard(player);
        }
        return false;
    }
}
