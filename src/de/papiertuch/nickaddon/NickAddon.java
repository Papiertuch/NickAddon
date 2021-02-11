package de.papiertuch.nickaddon;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.lib.player.permission.PermissionGroup;
import de.papiertuch.nickaddon.commands.Nick;
import de.papiertuch.nickaddon.listener.AsyncPlayerChatListener;
import de.papiertuch.nickaddon.listener.PlayerInteractListener;
import de.papiertuch.nickaddon.listener.PlayerJoinListener;
import de.papiertuch.nickaddon.utils.MySQL;
import de.papiertuch.nickaddon.utils.NickAddonAPI;
import de.papiertuch.nickaddon.utils.NickConfig;
import de.papiertuch.nickaddon.utils.TabListGroup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;
import xyz.haoshoku.nick.api.NickAPI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leon on 17.06.2019.
 * development with love.
 * © Copyright by Papiertuch
 */

public class NickAddon extends JavaPlugin {

    private static NickAddon instance;
    private NickConfig nickConfig;
    private NickAddonAPI api;
    private MySQL mySQL;
    private List<TabListGroup> tabListGroups;

    @Override
    public void onEnable() {
        instance = this;
        nickConfig = new NickConfig();
        mySQL = new MySQL();
        api = new NickAddonAPI();

        tabListGroups = new ArrayList<>();

        nickConfig.loadConfig();
        mySQL.connect();
        if (mySQL.isConnected()) {
            mySQL.createTable();
        }


        if (nickConfig.getBoolean("lobbyMode.enable")) {
            getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        }
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new AsyncPlayerChatListener(), this);

        getCommand("nick").setExecutor(new Nick());

        if (nickConfig.getBoolean("tabList.useCloudNetV2")) {
            for (String string : CloudAPI.getInstance().getPermissionPool().getGroups().keySet()) {
                Bukkit.broadcastMessage(string);
                PermissionGroup permissionGroup = CloudAPI.getInstance().getPermissionGroup(string);
                tabListGroups.add(new TabListGroup(string,
                        permissionGroup.getPrefix(),
                        permissionGroup.getSuffix(),
                        permissionGroup.getDisplay(),
                        permissionGroup.getTagId(),
                        ""));
            }
        } else {
            for (String tabList : getNickConfig().getConfiguration().getStringList("tabList")) {
                tabListGroups.add(
                        new TabListGroup(tabList,
                                nickConfig.getString(tabList + ".prefix"),
                                nickConfig.getString(tabList + ".suffix"),
                                nickConfig.getString(tabList + ".display"),
                                nickConfig.getInt(tabList + ".tagId"),
                                nickConfig.getString(tabList + ".permission")));
            }
        }
        NickAPI.getConfig().setGameProfileChanges(true);
    }

    public void updateNameTags(Player player) {
        TabListGroup playerPermissionGroup = getTabListGroup(player);

        initScoreboard(player);
        for (Player all : player.getServer().getOnlinePlayers()) {
            initScoreboard(all);
            if (playerPermissionGroup != null)
                addTeamEntry(player, all, playerPermissionGroup);

            TabListGroup targetPermissionGroup = getTabListGroup(all);

            if (targetPermissionGroup != null)
                addTeamEntry(all, player, targetPermissionGroup);
        }
    }

    public MySQL getMySQL() {
        return mySQL;
    }

    public static NickAddon getInstance() {
        return instance;
    }

    public NickConfig getNickConfig() {
        return nickConfig;
    }

    public TabListGroup getDefaultGroup() {
        if (nickConfig.getBoolean("tabList.useCloudNetV2")) {
            for (TabListGroup tabListGroup : tabListGroups) {
                if (tabListGroup.getName().equalsIgnoreCase(CloudAPI.getInstance().getPermissionPool().getDefaultGroup().getName())) {
                    return tabListGroup;
                }
            }
        }
        return tabListGroups.get(tabListGroups.size() - 1);
    }

    public TabListGroup getTabListGroup(Player player) {
        if (!nickConfig.getBoolean("lobbyMode.enable") && !NickAPI.isNicked(player)) {
            for (TabListGroup tabListGroup : tabListGroups) {
                if (nickConfig.getBoolean("tabList.useCloudNetV2")) {
                    PermissionGroup permissionGroup = CloudAPI.getInstance().getOnlinePlayer(player.getUniqueId()).getPermissionEntity().getHighestPermissionGroup(CloudAPI.getInstance().getPermissionPool());
                    if (permissionGroup.getName().equalsIgnoreCase(tabListGroup.getName())) {
                        return tabListGroup;
                    }
                } else if (player.hasPermission(tabListGroup.getPermission())) {
                    return tabListGroup;
                }
            }
        }
        return getDefaultGroup();
    }

    private void addTeamEntry(Player target, Player all, TabListGroup permissionGroup) {
        Team team = all.getScoreboard().getTeam(permissionGroup.getTagId() + permissionGroup.getName());

        if (team == null)
            team = all.getScoreboard().registerNewTeam(permissionGroup.getTagId() + permissionGroup.getName());

        team.setPrefix(ChatColor.translateAlternateColorCodes('&', permissionGroup.getPrefix()));
        team.setSuffix(ChatColor.translateAlternateColorCodes('&', permissionGroup.getSuffix()));
        team.addEntry(target.getName());
        target.setDisplayName(ChatColor.translateAlternateColorCodes('&', permissionGroup.getDisplay()) + target.getName());
    }

    private void initScoreboard(Player all) {
        if (all.getScoreboard() == null)
            all.setScoreboard(all.getServer().getScoreboardManager().getNewScoreboard());
    }

    @Override
    public void onDisable() {
        if (mySQL.isConnected()) {
            mySQL.disconnect();
        }
    }

    public NickAddonAPI getApi() {
        return api;
    }
}
