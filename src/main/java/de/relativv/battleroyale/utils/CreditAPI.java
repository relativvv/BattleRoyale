package de.relativv.battleroyale.utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreditAPI {

    public CreditAPI() {
        Bukkit.getConsoleSender().sendMessage(prefix + "§3CreditSystem§8: §aLoaded");
    }

    public static String prefix = "§8▌ §eCredits §8» §r";


    public void createTables() {
        try {
            MySQL.getCon().prepareStatement("CREATE TABLE IF NOT EXISTS CreditSystem (ID int(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY, UUID VARCHAR(61), PlayerName VARCHAR(16), Credits int(15))").executeUpdate();
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }


    public boolean userExists(OfflinePlayer p) {
        try {
            PreparedStatement ps = MySQL.getCon().prepareStatement("SELECT Credits from CreditSystem WHERE UUID = ?");
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
                PreparedStatement ps = MySQL.getCon().prepareStatement("INSERT INTO CreditSystem (UUID, PlayerName, Credits) VALUES (?, ?, ?)");
                ps.setString(1, p.getUniqueId().toString());
                ps.setString(2, p.getName());
                ps.setInt(3, 100);
                ps.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }



    public int getCredits(OfflinePlayer p) {
            try {
                PreparedStatement ps = MySQL.getCon().prepareStatement("SELECT Credits FROM CreditSystem WHERE UUID = ?");
                ps.setString(1, p.getUniqueId().toString());
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    return rs.getInt("Credits");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return -1;
        }



    public void addCredits(OfflinePlayer p, int amount) {
        if(userExists(p)) {
            int current = getCredits(p);
            int newAmount = current + amount;
            try {
                PreparedStatement ps = MySQL.getCon().prepareStatement("UPDATE CreditSystem SET Credits = ? WHERE UUID = ?");
                ps.setInt(1, newAmount);
                ps.setString(2, p.getUniqueId().toString());
                ps.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            createUser(p);
            addCredits(p, amount);
        }
    }



    public void setCredits(OfflinePlayer p, int amount) {
        if(userExists(p)) {
            try {
                PreparedStatement ps = MySQL.getCon().prepareStatement("UPDATE CreditSystem SET Credits = ? WHERE UUID = ?");
                ps.setInt(1, amount);
                ps.setString(2, p.getUniqueId().toString());
                ps.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            createUser(p);
            setCredits(p, amount);
        }
    }



    public void removeCredits(OfflinePlayer p, int amount) {
        int current = getCredits(p);
        int newAmount = current-amount;
        if(userExists(p)) {
            try {
                PreparedStatement ps = MySQL.getCon().prepareStatement("UPDATE CreditSystem SET Credits = ? WHERE UUID = ?");
                ps.setInt(1, newAmount);
                ps.setString(2, p.getUniqueId().toString());
                ps.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            createUser(p);
            removeCredits(p, amount);
        }
    }


}
