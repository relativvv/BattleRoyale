package de.relativv.battleroyale.utils;

import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQL {

    public static Connection con;

    public MySQL() {
    }

    public static boolean isConnected() {
        return con != null;
    }

    public void connect(String hostname, String username, String database, String password, String port) {
        if (!isConnected()) {
            try {
                con = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port + "/" + database, username, password);
                Bukkit.getConsoleSender().sendMessage("§3MySQL Status§8: §aConnected");
            } catch (SQLException var7) {
                var7.printStackTrace();
            }
        }

    }

    public void disconnect() {
        if (isConnected()) {
            try {
                con.close();
                Bukkit.getConsoleSender().sendMessage("§cConnection to the Database aborted!");
            } catch (SQLException var2) {
                var2.printStackTrace();
            }
        }

    }

    public void update(String qry) {
        try {
            PreparedStatement ps = con.prepareStatement(qry);
            ps.executeUpdate();
        } catch (SQLException var3) {
            var3.printStackTrace();
        }

    }

    public static Connection getCon() {
        return con;
    }
}

