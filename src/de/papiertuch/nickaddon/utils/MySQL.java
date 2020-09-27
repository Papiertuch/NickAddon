package de.papiertuch.nickaddon.utils;

import de.papiertuch.nickaddon.NickAddon;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Leon on 17.06.2019.
 * development with love.
 * © Copyright by Papiertuch
 */

public class MySQL {

    private Connection connection;

    public void connect() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + NickAddon.getInstance().getNickConfig().getString("mysql.host") + ":3306/" + NickAddon.getInstance().getNickConfig().getString("mysql.dataBase") + "?autoReconnect=true", NickAddon.getInstance().getNickConfig().getString("mysql.user"), NickAddon.getInstance().getNickConfig().getString("mysql.password"));
            Bukkit.getServer().getConsoleSender().sendMessage("§8[§cNick§8] §aA connection to MySQL was successful");
        } catch (Exception e) {
            Bukkit.getServer().getConsoleSender().sendMessage("§8[§cNick§8] §cThe connection to the MySQL server failed...");
        }
    }

    public void createTable() {
        update("CREATE TABLE IF NOT EXISTS nick (UUID VARCHAR(100), NAME VARCHAR(100), STATE BOOL, NICK VARCHAR(100))");
    }

    public void updateName(Player player) {
        try {
            PreparedStatement preparedStatement =
                    getConnection().prepareStatement("UPDATE nick SET NAME = ? WHERE UUID = ?");
            preparedStatement.setString(2, player.getUniqueId().toString());
            preparedStatement.setString(1, player.getName());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void setName(UUID uuid, String name) {
        try {
            PreparedStatement preparedStatement =
                    getConnection().prepareStatement("UPDATE nick SET NICK = ? WHERE UUID = ?");
            preparedStatement.setString(2, uuid.toString());
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void setNick(UUID uuid, boolean value) {
        try {
            PreparedStatement preparedStatement =
                    getConnection().prepareStatement("UPDATE nick SET STATE = ? WHERE UUID = ?");
            preparedStatement.setString(2, uuid.toString());
            preparedStatement.setBoolean(1, value);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private boolean isExistPlayer(UUID uuid) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM nick WHERE UUID = ?");
            preparedStatement.setString(1, uuid.toString());
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                if (rs.getString("UUID") != null) {
                    return true;
                }
                return false;
            }
            rs.close();
            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public void createPlayer(Player player) {
        if (!isExistPlayer(player.getUniqueId())) {
            Random r = new Random();
            String name = NickAddon.getInstance().getNickConfig().getConfiguration().getStringList("nicks").get(r.nextInt(NickAddon.getInstance().getNickConfig().getConfiguration().getStringList("nicks").size()));
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement("INSERT INTO nick (UUID, NAME, STATE, NICK) VALUES (?, ?, ?, ?)");
                preparedStatement.setString(1, player.getUniqueId().toString());
                preparedStatement.setString(2, player.getName());
                preparedStatement.setBoolean(3, false);
                preparedStatement.setString(4, name);
                preparedStatement.executeUpdate();
                preparedStatement.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public String getRealName(UUID uuid) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM nick WHERE UUID = ?");
            preparedStatement.setString(1, uuid.toString());
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getString("NAME");
            }
            rs.close();
            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public String getName(UUID uuid) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM nick WHERE UUID = ?");
            preparedStatement.setString(1, uuid.toString());
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getString("NICK");
            }
            rs.close();
            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public Boolean getState(UUID uuid) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM nick WHERE UUID = ?");
            preparedStatement.setString(1, uuid.toString());
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("STATE");
            }
            rs.close();
            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }


    public void disconnect() {
        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return connection != null;
    }

    public Connection getConnection() {
        return connection;
    }

    public void update(String qry) {
        try {
            PreparedStatement ps = connection.prepareStatement(qry);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
