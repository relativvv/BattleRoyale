package de.relativv.battleroyale.listener;

import de.relativv.battleroyale.main.BattleRoyale;
import de.relativv.battleroyale.utils.*;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerJoin implements Listener {

    private BattleRoyale plugin;
    public PlayerJoin(BattleRoyale plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if(plugin.state == GameState.LOBBY) {

            readyLobbyPlayer(p);

            e.setJoinMessage("§a>§2>§a> §r" + p.getDisplayName());
            for(Player all : Bukkit.getOnlinePlayers()) {
                if (all != p) {
                    all.playSound(all.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2, 2);
                }
            }



        } else if(plugin.state == GameState.WAIT || plugin.state == GameState.INGAME) {
            readySpectator(p);
            e.setJoinMessage(null);


        } else if(plugin.state == GameState.RESTART) {
            e.setJoinMessage(null);
        }
    }


    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        Player p = (Player) e.getPlayer();

        if(plugin.state == GameState.LOBBY) {
            if(p.hasPermission("hideplayer.bypass") || p.isOp() || p.hasPermission("system.owner")
                    || p.hasPermission("system.admin")
                    || p.hasPermission("system.dev")
                    || p.hasPermission("system.mod")
                    || p.hasPermission("system.builder")
                    || p.hasPermission("system.sup")
                    || p.hasPermission("system.yt")) {

                if(Bukkit.getOfflinePlayers().length >= Bukkit.getMaxPlayers()) {
                    for(Player all : Bukkit.getOnlinePlayers()) {
                        if(!(all.isOp() || all.hasPermission("system.owner")
                                || all.hasPermission("system.admin")
                                || all.hasPermission("system.dev")
                                || all.hasPermission("system.mod")
                                || all.hasPermission("system.builder")
                                || all.hasPermission("system.sup")
                                || all.hasPermission("system.yt"))) {

                            all.kickPlayer(plugin.systemPrefix + "§cUm einem Teammitglied/Youtuber/Premium-Spieler Platz zu schaffen, wurdest du aus der Runde geworfen!");
                            e.allow();
                            return;
                        }
                    }
                    e.disallow(PlayerLoginEvent.Result.KICK_OTHER, plugin.systemPrefix + "§cEs wurde kein Spieler zum kicken gefunden.");
                }
            } else {
                e.disallow(PlayerLoginEvent.Result.KICK_OTHER, plugin.systemPrefix + "§cDer Server ist voll!");
            }



        } else if(plugin.state == GameState.WAIT || plugin.state == GameState.INGAME) {
            if(Bukkit.getOfflinePlayers().length >= Bukkit.getMaxPlayers()) {
                e.allow();
            }

        } else if(plugin.state == GameState.RESTART) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "§4Die Runde ist bereits beendet!");
        }
    }










    private void readyLobbyPlayer(Player p) {
        new BukkitRunnable() {
            @Override
            public void run() {
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

                    p.teleport(loc);
                    p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);

                } else {
                    Bukkit.broadcastMessage(plugin.prefix + "§cLobby wurde noch nicht gesetzt!");
                }




                p.setGameMode(GameMode.ADVENTURE);
                p.setMaxHealth(20D);
                p.setHealth(20D);
                p.setFoodLevel(20);
                p.setSaturation(20);
                p.setFireTicks(0);
                p.setAllowFlight(false);
                p.setFlying(false);
                p.setLevel(Countdowns.lobbyCD);

                p.getInventory().setItem(0, new ItemBuilder(Material.DIAMOND_SWORD, 1)
                        .setDisPlayname("§bSpieler einladen")
                        .build());
                p.getInventory().setItem(4, new ItemBuilder(Material.OAK_SIGN, 1)
                        .setDisPlayname("§9Team umbennen")
                        .build());
                p.getInventory().setItem(8, new ItemBuilder(Material.BARRIER, 1)
                        .setDisPlayname("§cTeam verlassen")
                        .build());
            }
        }.runTaskLater(BattleRoyale.getInstance(), 2L);



        plugin.alive.add(p);
        plugin.gameUtils.createNewTeam(p);
        Team team = Team.getTeamByPlayerInside(p);
        for(Player all : team.getTeamList()) {
            all.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }
        team.setScoreboard();
        team.updateScoreboard();


        float exp = ((float) Countdowns.lobbyCD / BattleRoyale.getInstance().lobbyTime);
        if (exp >= 1F) {
            exp = 1F;
        }
        if (exp <= 0F) {
            exp = 0F;
        }

        p.setExp(exp);
        p.setSneaking(false);
        p.setSprinting(false);
    }




    private void readySpectator(Player p) {
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

            p.teleport(loc);
            p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_HIT, 1, 1);

        } else {
            Bukkit.broadcastMessage(plugin.prefix + "§cLobby wurde noch nicht gesetzt!");
        }

        p.setGameMode(GameMode.SPECTATOR);
        p.sendMessage(plugin.prefix + "§aDu bist jetzt ein Spectator!");
    }
}
