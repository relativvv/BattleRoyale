package de.relativv.battleroyale.utils;

import de.relativv.battleroyale.main.BattleRoyale;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class GameUtils {

    private BattleRoyale plugin;
    public GameUtils(BattleRoyale plugin) {
        this.plugin = plugin;
    }



    public void createNewTeam(Player owner) {
        Team team = new Team(owner.getName().substring(0, 4), new ArrayList<>());
        team.getTeamList().add(owner);

        plugin.teams.add(team);
    }


    public boolean nameGiven(String newName) {
        for(Team teams : BattleRoyale.getInstance().teams) {
            if(teams.getName().equalsIgnoreCase(newName)) {
                return true;
            }
        }
        return false;
    }



    public void playerLeaveTeam(Player toLeave) {
        Team team = Team.getTeamByPlayerInside(toLeave);

        if((team.getTeamList().size()-1) == 0) {
            team.getTeamList().remove(toLeave);
            plugin.teams.remove(team);
        } else {
            team.getTeamList().remove(toLeave);
        }

        createNewTeam(toLeave);
    }



    public void playerJoinTeam(Player teamOwner, Player toJoin) {
        Team teamAl = Team.getTeamByPlayerInside(teamOwner);
        Team teamJoiningAll = Team.getTeamByPlayerInside(toJoin);

        if((teamJoiningAll.getTeamList().size()-1) == 0) {
            teamJoiningAll.getTeamList().remove(toJoin);
            plugin.teams.remove(teamJoiningAll);
        } else {
            teamJoiningAll.getTeamList().remove(toJoin);
        }

        teamAl.getTeamList().add(toJoin);
        for(Player all : teamAl.getTeamList()) {
            all.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }
        teamAl.setScoreboard();
        teamAl.updateScoreboard();
    }



    public void playerDeathInTeam(Player toRemove) {
        Team team = Team.getTeamAliveByPlayerInside(toRemove);
        if((team.getTeamList().size()-1) == 0) {
            team.getTeamList().remove(toRemove);
            plugin.teamsAlive.remove(team);
        } else {
            team.getTeamList().remove(toRemove);
        }
    }




    public static boolean twoPlayersInOneTeam(Player p1, Player p2) {
        for(Team teams : BattleRoyale.getInstance().teams) {
            if(teams.getTeamList().contains(p1) && teams.getTeamList().contains(p2)) {
                return true;
            }
        }
        return false;
    }
}
