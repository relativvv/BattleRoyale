package de.relativv.battleroyale.listener;

import de.relativv.battleroyale.main.BattleRoyale;
import de.relativv.battleroyale.utils.*;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {

    private BattleRoyale plugin;
    public PlayerQuit(BattleRoyale plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        if(plugin.state == GameState.LOBBY) {
            e.setQuitMessage("§c<§4<§c< §r" + p.getDisplayName());
            for(Player all : Bukkit.getOnlinePlayers()) {
                if (all != p) {
                    all.playSound(all.getLocation(), Sound.ENTITY_CHICKEN_EGG, 0.5F, 0.5F);
                }
            }

            plugin.alive.remove(p);
            plugin.build.remove(p);

            Team team = Team.getTeamByPlayerInside(p);

            if((team.getTeamList().size()-1) == 0) {
                team.getTeamList().remove(p);
                plugin.teams.remove(team);
            } else {
                team.updateScoreboard();
                team.getTeamList().remove(p);
            }



        } else if(plugin.state == GameState.WAIT) {
            playerDeath(p);
            e.setQuitMessage(plugin.prefix + "§7Der Spieler §r" + p.getDisplayName() + " §7ist gestorben!");
            win();
            BattleRoyale.getInstance().statsAPI.addStat(p, StatType.Deaths, 1);

        } else if(plugin.state == GameState.INGAME) {
            e.setQuitMessage(null);
            playerDeath(p);

            if(EntityDamage.lastDMG.containsKey(p)) {
                Player k = EntityDamage.lastDMG.get(p);
                plugin.statsAPI.addStat(k, StatType.Kills, 1);

                Bukkit.broadcastMessage(plugin.prefix + "§7Der Spieler §r" + p.getDisplayName() + " §7wurde von §r" + k.getDisplayName() + " §7getötet!");

                k.sendMessage(" ");
                k.sendMessage(CreditAPI.prefix + "§7Du hast §r" + p.getDisplayName() + " §7getötet!");
                k.sendMessage(CreditAPI.prefix + "§8[§a+§8] §9100 Credits");
                k.sendMessage(" ");
                plugin.creditAPI.addCredits(k, 100);

                EntityDamage.lastDMG.remove(p);

            } else {
                e.setQuitMessage(plugin.prefix + "§7Der Spieler §r" + p.getDisplayName() + " §7ist gestorben!");
            }

            win();
            BattleRoyale.getInstance().statsAPI.addStat(p, StatType.Deaths, 1);

        } else if(plugin.state == GameState.RESTART) {
            e.setQuitMessage(null);
        }
    }







    private void playerDeath(Player d) {
        plugin.gameUtils.playerDeathInTeam(d);

        for(Player all : Bukkit.getOnlinePlayers()) {
            all.playSound(d.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);
        }

        if(FileManager.locCfg.isSet("lobby.x")) {
            double x = FileManager.locCfg.getDouble("lobby.x");
            double y = FileManager.locCfg.getDouble("lobby.y");
            double z = FileManager.locCfg.getDouble("lobby.z");

            double yaw = FileManager.locCfg.getDouble("lobby.yaw");
            double pitch = FileManager.locCfg.getDouble("lobby.pitch");

            String worldname = FileManager.locCfg.getString("lobby.worldname");
            Location loc = new Location(Bukkit.getWorld(worldname), x, y, z);
            loc.setYaw((float) yaw);
            loc.setPitch((float) pitch);

            d.teleport(loc);
            d.playSound(d.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);

        } else {
            Bukkit.broadcastMessage(plugin.prefix + "§cLobby wurde noch nicht gesetzt!");
        }
    }






    private boolean askForWin() {
        return plugin.teamsAlive.size() == 1;
    }



    private void win() {
        if(askForWin()) {
            Team team = Team.getTeamByPlayerInside(plugin.teamsAlive.get(0).getTeamList().get(0));

            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(plugin.prefix + "§e§lDer Gewinner steht fest!");
            Bukkit.broadcastMessage(plugin.prefix + "  §6Gewonnen hat§8: §aTeam §8- §a" + team.getName());
            for(Player winner : team.getTeamList()) {
                Bukkit.broadcastMessage(plugin.prefix + "    §7- §a" + winner.getName());
            }
            Bukkit.broadcastMessage("");

            for(Player winner : team.getTeamList()) {
                winner.sendMessage(" ");
                winner.sendMessage(CreditAPI.prefix + "§7Du hast gewonnen!");
                winner.sendMessage(CreditAPI.prefix + "§8[§a+§8] §9600 Credits");
                winner.sendMessage(" ");
                plugin.creditAPI.addCredits(winner, 600);
                BattleRoyale.getInstance().statsAPI.addStat(winner, StatType.Won, 1);
            }

            for(Player all : Bukkit.getOnlinePlayers()) {
                if(!plugin.spectator.contains(all)) {
                    all.sendMessage(" ");
                    all.sendMessage(CreditAPI.prefix + "§8[§a+§8] §955 Credits für die Teilnahme");
                    all.sendMessage(" ");
                    plugin.creditAPI.addCredits(all, 55);
                }
                ScoreAPI.setScoreboard(all);
                ScoreAPI.updateScoreboard(all);
            }

            plugin.countdowns.restartCountdown();


            if(FileManager.locCfg.isSet("lobby.x")) {
                double x = FileManager.locCfg.getDouble("lobby.x");
                double y = FileManager.locCfg.getDouble("lobby.y");
                double z = FileManager.locCfg.getDouble("lobby.z");

                double yaw = FileManager.locCfg.getDouble("lobby.yaw");
                double pitch = FileManager.locCfg.getDouble("lobby.pitch");

                String worldname = FileManager.locCfg.getString("lobby.worldname");
                Location loc = new Location(Bukkit.getWorld(worldname), x, y, z);
                loc.setYaw((float) yaw);
                loc.setPitch((float) pitch);

                for(Player all : Bukkit.getOnlinePlayers()) {
                    all.teleport(loc);
                    all.getInventory().clear();
                    all.getInventory().setArmorContents(null);
                    all.setGameMode(GameMode.ADVENTURE);
                    all.setMaxHealth(20D);
                    all.setHealth(20D);
                    all.setFoodLevel(20);
                    all.setSaturation(20);
                    all.setFireTicks(0);
                    all.setAllowFlight(false);
                    all.setFlying(false);
                    all.playSound(all.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1, 1);
                    all.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(""));
                }

            } else {
                Bukkit.broadcastMessage(plugin.prefix + "§cLobby wurde noch nicht gesetzt!");
            }

        }
    }

}
