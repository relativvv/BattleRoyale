package de.relativv.battleroyale.utils;

import de.relativv.battleroyale.main.BattleRoyale;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatsAPI {

    public StatsAPI() {
        Bukkit.getConsoleSender().sendMessage("§3StatsSystem§8: §aLoaded");
    }



    public void createTables() {
        BattleRoyale.getInstance().sql.update("CREATE TABLE IF NOT EXISTS Stats (ID int(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY, UUID VARCHAR(61), Kills VARCHAR(16), Deaths int(15), Played int(15), Won int(15))");
    }


    public boolean userExists(OfflinePlayer p) {
        try {
            PreparedStatement ps = MySQL.getCon().prepareStatement("SELECT UUID from Stats WHERE UUID = ?");
            ps.setString(1, p.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }


    public void createUser(OfflinePlayer p) {
        if(!userExists(p)) {
            try {
                PreparedStatement ps = MySQL.getCon().prepareStatement("INSERT INTO Stats (UUID, Kills, Deaths, Played, Won) VALUES (?, ?, ?, ?, ?)");
                ps.setString(1, p.getUniqueId().toString());
                ps.setInt(2, 0);
                ps.setInt(3, 0);
                ps.setInt(4, 0);
                ps.setInt(5, 0);
                ps.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }



    public int getStat(OfflinePlayer p, StatType stat) {
        try {
            PreparedStatement ps = MySQL.getCon().prepareStatement("SELECT " + stat.toString() + " FROM Stats WHERE UUID = ?");
            ps.setString(1, p.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getInt(stat.toString());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }



    public void addStat(OfflinePlayer p, StatType stat, int amount) {
        if(userExists(p)) {
            int current = getStat(p, stat);
            int newAmount = current + amount;
            try {
                PreparedStatement ps = MySQL.getCon().prepareStatement("UPDATE Stats SET " + stat.toString() + " = ? WHERE UUID = ?");
                ps.setInt(1, newAmount);
                ps.setString(2, p.getUniqueId().toString());
                ps.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            createUser(p);
            addStat(p, stat, amount);
        }
    }



    public void setStat(OfflinePlayer p, StatType stat, int amount) {
        if(userExists(p)) {
            try {
                PreparedStatement ps = MySQL.getCon().prepareStatement("UPDATE Stats SET " + stat.toString() + " = ? WHERE UUID = ?");
                ps.setInt(1, amount);
                ps.setString(2, p.getUniqueId().toString());
                ps.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            createUser(p);
            setStat(p, stat, amount);
        }
    }



    public void removestat(OfflinePlayer p, StatType stat, int amount) {
        int current = getStat(p, stat);
        int newAmount = current-amount;
        if(userExists(p)) {
            try {
                PreparedStatement ps = MySQL.getCon().prepareStatement("UPDATE Stats SET " + stat.toString() + " = ? WHERE UUID = ?");
                ps.setInt(1, newAmount);
                ps.setString(2, p.getUniqueId().toString());
                ps.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            createUser(p);
            removestat(p, stat, amount);
        }
    }

}
