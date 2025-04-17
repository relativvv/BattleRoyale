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
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerDeath implements Listener {

    private BattleRoyale plugin;
    public PlayerDeath(BattleRoyale plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();

        e.setDeathMessage(null);

        if(plugin.state == GameState.INGAME) {
            playerDeath(p);

            if(askForWin()) {
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


                            p.setGameMode(GameMode.ADVENTURE);
                            p.setMaxHealth(20D);
                            p.setHealth(20D);
                            p.setFoodLevel(20);
                            p.setSaturation(20);
                            p.setFireTicks(0);
                            p.setAllowFlight(false);
                            p.setFlying(false);

                            p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1, 1);
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(""));

                        } else {
                            Bukkit.broadcastMessage(plugin.prefix + "§cLobby wurde noch nicht gesetzt!");
                        }
                    }
                }.runTaskLater(BattleRoyale.getInstance(), 3L);
            }












            plugin.statsAPI.addStat(p, StatType.Deaths, 1);

            if(p.getKiller() != null) {
                Player k = p.getKiller();
                plugin.statsAPI.addStat(k, StatType.Kills, 1);

                Bukkit.broadcastMessage(plugin.prefix + "§7Der Spieler §r" + p.getDisplayName() + " §7wurde von §r" + k.getDisplayName() + " §7getötet!");
                k.sendMessage(" ");
                k.sendMessage(CreditAPI.prefix + "§7Du hast §r" + p.getDisplayName() + " §7getötet!");
                k.sendMessage(CreditAPI.prefix + "§8[§a+§8] §9100 Credits");
                k.sendMessage(" ");
                plugin.creditAPI.addCredits(k, 100);


            } else {
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
                    Bukkit.broadcastMessage(plugin.prefix + "§7Der Spieler §r" + p.getDisplayName() + " §7ist gestorben!");
                }
            }

            win();
        }
    }




    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        if(!askForWin()) {
            if (plugin.state == GameState.INGAME) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (FileManager.locCfg.isSet("lobby.x")) {
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

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                p.setGameMode(GameMode.SPECTATOR);
                                p.sendMessage(plugin.prefix + "§aDu bist jetzt ein Spectator!");
                            }
                        }.runTaskLater(BattleRoyale.getInstance(), 10L);

                    }
                }.runTaskLater(BattleRoyale.getInstance(), 5L);
            }


        } else {
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
                        p.getInventory().clear();
                        p.getInventory().setArmorContents(null);
                        p.setGameMode(GameMode.ADVENTURE);
                        p.setMaxHealth(20D);
                        p.setHealth(20D);
                        p.setFoodLevel(20);
                        p.setSaturation(20);
                        p.setFireTicks(0);
                        p.setAllowFlight(false);
                        p.setFlying(false);
                        p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1, 1);
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(""));

                    } else {
                        Bukkit.broadcastMessage(plugin.prefix + "§cLobby wurde noch nicht gesetzt!");
                    }
                }
            }.runTaskLater(BattleRoyale.getInstance(), 3L);
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
                plugin.statsAPI.addStat(winner, StatType.Won, 1);
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
                    all.setGameMode(GameMode.ADVENTURE);
                    all.getInventory().clear();
                    all.getInventory().setArmorContents(null);
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





    private void playerDeath(Player d) {
        plugin.gameUtils.playerDeathInTeam(d);

        for(Player all : Bukkit.getOnlinePlayers()) {
            all.playSound(d.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);
        }

    }
}
