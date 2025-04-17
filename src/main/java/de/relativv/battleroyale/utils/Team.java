package de.relativv.battleroyale.utils;

import de.relativv.battleroyale.main.BattleRoyale;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.HashMap;

public class Team {

    private String name;
    private ArrayList<Player> teamList;

    public static HashMap<Player, Team> invites = new HashMap<Player, Team>();

    public Team(String name, ArrayList<Player> teamList) {
        this.name = name;
        this.teamList = teamList;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Player> getTeamList() {
        return teamList;
    }



    public static Team getTeamAliveByName(String name) {
        for(Team teams : BattleRoyale.getInstance().teamsAlive) {
            if(teams.getName().equals(name)) {
                return teams;
            }
        }
        Bukkit.broadcastMessage(BattleRoyale.getInstance().prefix + "§cNullpointerexception §8- §4Ein Team ist Null!");
        return null;
    }



    public static Team getTeamAliveByPlayerInside(Player p) {
        for(Team teams : BattleRoyale.getInstance().teamsAlive) {
            if(teams.getTeamList().contains(p)) {
                return teams;
            }
        }
        Bukkit.broadcastMessage(BattleRoyale.getInstance().prefix + "§cNullpointerexception §8- §4Ein Team ist Null!");
        return null;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setScoreboard() {
        Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = sb.getObjective("aaa");
        if(obj == null) {
            obj = sb.registerNewObjective("aaa", "bbb");
        }


        org.bukkit.scoreboard.Team t = getTeamm(sb, name, "§8[§a" + name + "§8] §9", "§9");


        for(Player pl : getTeamList()) {
            t.addPlayer(pl);
            pl.setDisplayName("§8[§a" + name + "§8] §9" + pl.getName());
            pl.setPlayerListName("§8[§a" + name + "§8] §9" + pl.getName());
            pl.setCustomName("§8[§a" + name + "§8] §9" + pl.getName());
            pl.setScoreboard(sb);
        }

    }






    public void updateScoreboard() {
        if (!getTeamList().isEmpty()) {
            Scoreboard sb = getTeamList().get(0).getScoreboard();

            Objective obj = sb.getObjective("aaa");
            if (obj == null) {
                obj = sb.registerNewObjective("aaa", "bbb");
            }


            org.bukkit.scoreboard.Team t = getTeamm(sb, name, "§8[§a" + name + "§8] §9", "§9");

            for (Player pl : getTeamList()) {
                t.addPlayer(pl);
                pl.setDisplayName("§8[§a" + name + "§8] §9" + pl.getName());
                pl.setPlayerListName("§8[§a" + name + "§8] §9" + pl.getName());
                pl.setCustomName("§8[§a" + name + "§8] §9" + pl.getName());
                pl.setScoreboard(sb);
            }

        } else {
            BattleRoyale.getInstance().teams.remove(this);
            BattleRoyale.getInstance().teamsAlive.remove(this);
        }
    }




    public static org.bukkit.scoreboard.Team getTeamm(Scoreboard sb, String t, String prefix, String suffix) {
        org.bukkit.scoreboard.Team team = sb.getTeam(t);
        if(team == null) {
            team = sb.registerNewTeam(t);
        }
        team.setPrefix(prefix);
        team.setSuffix(suffix);
        return sb.getTeam(t);
    }




    public static Team getTeamByName(String name) {
        for(Team teams : BattleRoyale.getInstance().teams) {
            if(teams.getName().equals(name)) {
                return teams;
            }
        }
        return null;
    }



    public static Team getTeamByPlayerInside(Player p) {
        for(Team teams : BattleRoyale.getInstance().teams) {
            if(teams.getTeamList().contains(p)) {
                return teams;
            }
        }
        return null;
    }
}
