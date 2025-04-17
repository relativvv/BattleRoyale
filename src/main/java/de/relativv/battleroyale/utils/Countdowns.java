package de.relativv.battleroyale.utils;

import de.relativv.battleroyale.listener.PlayerInteract;
import de.relativv.battleroyale.main.BattleRoyale;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class Countdowns {

    public static int lobbyCD;
    public static int waitCD;
    public static int ingameCD;
    public static int restartCD;


    public void lobbyCountdown() {

        lobbyCD = BattleRoyale.getInstance().lobbyTime;

        BattleRoyale.getInstance().lobbySched = Bukkit.getScheduler().scheduleSyncRepeatingTask(BattleRoyale.getInstance(), new Runnable() {
            @Override
            public void run() {
                if (BattleRoyale.getInstance().state == GameState.LOBBY) {
                    if (BattleRoyale.getInstance().teams.size() >= 2) {

                        float exp = ((float) lobbyCD / BattleRoyale.getInstance().lobbyTime);
                        if (exp >= 1F) {
                            exp = 1F;
                        }
                        if (exp <= 0F) {
                            exp = 0F;
                        }

                        if (BattleRoyale.getInstance().state == GameState.LOBBY) {
                            for (Player all : Bukkit.getOnlinePlayers()) {
                                all.setExp(exp);
                                if (lobbyCD > 0) {
                                    all.setLevel(lobbyCD);
                                } else {
                                    all.setLevel(0);
                                    all.setExp(0F);
                                }
                            }
                        }


                        if (lobbyCD == 60 || lobbyCD == 50 || lobbyCD == 40 || lobbyCD == 30 || lobbyCD == 20 || lobbyCD == 10 || lobbyCD == 9 || lobbyCD == 8 || lobbyCD == 7 || lobbyCD == 6
                                || lobbyCD == 5 || lobbyCD == 4 || lobbyCD == 3 || lobbyCD == 2 || lobbyCD == 1) {

                            Bukkit.broadcastMessage(BattleRoyale.getInstance().prefix + " §7Das Spiel beginnt in §a" + lobbyCD + " §7Sekunden.");
                            for (Player all : Bukkit.getOnlinePlayers()) {
                                all.playSound(all.getLocation(), Sound.UI_BUTTON_CLICK, 2, 2);
                            }


                        } else if (lobbyCD == 0) {
                            Bukkit.broadcastMessage(BattleRoyale.getInstance().prefix + "§aDie Spieler werden teleportiert..");
                            BattleRoyale.getInstance().state = GameState.WAIT;
                            BattleRoyale.getInstance().teamsAlive.addAll(BattleRoyale.getInstance().teams);

                            Bukkit.getWorld("world").setTime(1000);
                            Bukkit.getWorld("world").setThundering(false);
                            Bukkit.getWorld("world").setStorm(false);
                            Bukkit.getWorld("world").setAutoSave(false);
                            Bukkit.getWorld("world").setDifficulty(Difficulty.NORMAL);

                            BattleRoyale.getInstance().border.setSize(20);
                            BattleRoyale.getInstance().border.setSize(BattleRoyale.getInstance().alive.size() * 400, 11);


                            for(Team teams : BattleRoyale.getInstance().teamsAlive) {
                                int x = new Random().nextInt(220)+Bukkit.getWorld("world").getSpawnLocation().getBlockX();
                                int z = new Random().nextInt(220)+Bukkit.getWorld("world").getSpawnLocation().getBlockX();
                                int y = Bukkit.getWorld("world").getHighestBlockYAt(x, z);

                                Location loc = new Location(Bukkit.getWorld("world"), x, y, z);
                                loc.subtract(0, 1, 0).getBlock().setType(Material.BEDROCK);

                                for(Player pl : teams.getTeamList()) {
                                    pl.teleport(loc);
                                    pl.playSound(pl.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.5F, 0.5F);
                                }
                            }



                            for (Player all : BattleRoyale.getInstance().alive) {
                                all.getInventory().clear();
                                all.getInventory().setArmorContents(null);
                                all.setFireTicks(0);
                                all.setFoodLevel(20);
                                all.setSaturation(10);
                                all.setMaxHealth(20D);
                                all.setHealth(20D);
                                all.setLevel(0);
                                all.setExp(0F);
                                all.setFlying(false);
                                all.setAllowFlight(false);
                                all.setGameMode(GameMode.SURVIVAL);
                                BattleRoyale.getInstance().statsAPI.addStat(all, StatType.Played, 1);

                                PlayerInteract.renaming.remove(all);
                                BattleRoyale.getInstance().build.remove(all);
                            }

                            waitCountdown();
                            Bukkit.getScheduler().cancelTask(BattleRoyale.getInstance().lobbySched);
                        }

                        lobbyCD--;

                    } else {
                        lobbyCD = BattleRoyale.getInstance().lobbyTime;
                        for (Player all : Bukkit.getOnlinePlayers()) {
                            all.setLevel(BattleRoyale.getInstance().lobbyTime);
                            all.setExp(1F);
                        }
                    }
                } else {
                    Bukkit.getScheduler().cancelTask(BattleRoyale.getInstance().lobbySched);
                }
            }
        }, 0, 20L);

    }











    public void waitCountdown() {
        waitCD = BattleRoyale.getInstance().waitTime;

        BattleRoyale.getInstance().waitSched = Bukkit.getScheduler().scheduleSyncRepeatingTask(BattleRoyale.getInstance(), new Runnable() {
            @Override
            public void run() {
                if (BattleRoyale.getInstance().state == GameState.WAIT) {
                    if (BattleRoyale.getInstance().teamsAlive.size() >= 2) {

                        if(waitCD == 15 || waitCD == 14 || waitCD == 13 || waitCD == 12 || waitCD == 11 || waitCD == 10 || waitCD == 9 || waitCD == 8 || waitCD == 7
                                || waitCD == 6 || waitCD == 5 || waitCD == 4 || waitCD == 3 || waitCD == 2 || waitCD == 1) {

                            for(Player all : Bukkit.getOnlinePlayers()) {
                                all.playSound(all.getLocation(), Sound.UI_BUTTON_CLICK, 2, 2);
                            }

                            Bukkit.broadcastMessage(BattleRoyale.getInstance().prefix + "§7Die Runde beginnt in §a" + waitCD + " §7Sekunden!");




                        } else if(waitCD == 0) {
                            for(Player all : Bukkit.getOnlinePlayers()) {
                                all.playSound(all.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            }

                            Bukkit.broadcastMessage(BattleRoyale.getInstance().prefix + "§aDie Runde beginnt jetzt! Viel Glück!");
                            BattleRoyale.getInstance().state = GameState.INGAME;
                            ingameCountdown();
                            Bukkit.getScheduler().cancelTask(BattleRoyale.getInstance().waitSched);
                        }

                        waitCD--;


                    } else {
                        BattleRoyale.getInstance().state = GameState.RESTART;

                        Bukkit.broadcastMessage("");
                        Bukkit.broadcastMessage(BattleRoyale.getInstance().prefix + "§e§lDer Gewinner steht fest!");
                        Bukkit.broadcastMessage(BattleRoyale.getInstance().prefix + "  §6Gewonnen hat§8: §c§oNiemand");
                        Bukkit.broadcastMessage(BattleRoyale.getInstance().prefix + "    §7- §c§oNiemand");
                        Bukkit.broadcastMessage("");

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
                                all.setMaxHealth(20D);
                                all.setHealth(20D);
                                all.setFoodLevel(20);
                                all.setSaturation(20);
                                all.setFireTicks(0);
                                all.setAllowFlight(false);
                                all.setFlying(false);
                                all.playSound(all.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1, 1);
                                if(!BattleRoyale.getInstance().spectator.contains(all)) {
                                    all.sendMessage(" ");
                                    all.sendMessage(CreditAPI.prefix + "§8[§a+§8] §955 für die Teilnahme");
                                    all.sendMessage(" ");
                                    BattleRoyale.getInstance().creditAPI.addCredits(all, 55);
                                }
                            }

                            restartCountdown();
                            Bukkit.getScheduler().cancelTask(BattleRoyale.getInstance().waitSched);

                        } else {
                            Bukkit.broadcastMessage(BattleRoyale.getInstance().prefix + "§cLobby wurde noch nicht gesetzt!");
                        }
                    }



                } else {
                    Bukkit.getScheduler().cancelTask(BattleRoyale.getInstance().waitSched);
                }
            }
        }, 0L, 20L);
    }











    public void ingameCountdown() {
        ingameCD = BattleRoyale.getInstance().ingameTime;

        Bukkit.getWorld("world").setDifficulty(Difficulty.NORMAL);

        BattleRoyale.getInstance().border.setSize(40, 720);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player all : BattleRoyale.getInstance().alive) {
                    if (BattleRoyale.getInstance().state == GameState.INGAME) {
                        int border = (int) all.getWorld().getWorldBorder().getSize();
                        all.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§4Zonen-Größe§8: §b" + border));
                    } else {
                        all.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(""));
                        cancel();
                    }
                }
            }
        }.runTaskTimer(BattleRoyale.getInstance(), 0L, 20L);







        BattleRoyale.getInstance().ingameSched = Bukkit.getScheduler().scheduleSyncRepeatingTask(BattleRoyale.getInstance(), new Runnable() {
            @Override
            public void run() {
                if (BattleRoyale.getInstance().state == GameState.INGAME) {
                    if(BattleRoyale.getInstance().teamsAlive.size() >= 2) {

                        if(ingameCD == 1200 || ingameCD == 900 || ingameCD == 600 || ingameCD == 300 || ingameCD == 180 || ingameCD == 60 || ingameCD == 30 || ingameCD == 15
                                || ingameCD == 10 || ingameCD == 9 || ingameCD == 8 || ingameCD == 7 || ingameCD == 6 || ingameCD == 5 || ingameCD == 4 || ingameCD == 3
                                || ingameCD == 2 || ingameCD == 1) {

                            for(Player all : Bukkit.getOnlinePlayers()) {
                                all.playSound(all.getLocation(), Sound.UI_BUTTON_CLICK, 2, 2);
                            }
                            Bukkit.broadcastMessage(BattleRoyale.getInstance().prefix + "§7Das Spiel endet in " + toMinutes(ingameCD) + "§7!");



                        } else if(ingameCD == 0) {
                            Bukkit.broadcastMessage(BattleRoyale.getInstance().prefix + "§cDer Server startet neu!");
                            BattleRoyale.getInstance().state = GameState.RESTART;

                            Bukkit.broadcastMessage("");
                            Bukkit.broadcastMessage(BattleRoyale.getInstance().prefix + "§e§lDer Gewinner steht fest!");
                            Bukkit.broadcastMessage(BattleRoyale.getInstance().prefix + "  §6Gewonnen hat§8: §c§oNiemand");
                            Bukkit.broadcastMessage(BattleRoyale.getInstance().prefix + "    §7- §c§oNiemand");
                            Bukkit.broadcastMessage("");

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
                                    all.setMaxHealth(20D);
                                    all.setHealth(20D);
                                    all.setFoodLevel(20);
                                    all.setSaturation(20);
                                    all.setFireTicks(0);
                                    all.setAllowFlight(false);
                                    all.setFlying(false);
                                    all.playSound(all.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1, 1);

                                    ScoreAPI.setScoreboard(all);
                                    ScoreAPI.updateScoreboard(all);

                                    if(!BattleRoyale.getInstance().spectator.contains(all)) {
                                        all.sendMessage(" ");
                                        all.sendMessage(CreditAPI.prefix + "§8[§a+§8] §955 für die Teilnahme");
                                        all.sendMessage(" ");
                                        BattleRoyale.getInstance().creditAPI.addCredits(all, 55);
                                    }
                                }


                                restartCountdown();
                                Bukkit.getScheduler().cancelTask(BattleRoyale.getInstance().ingameSched);

                            } else {
                                Bukkit.broadcastMessage(BattleRoyale.getInstance().prefix + "§cLobby wurde noch nicht gesetzt!");
                            }
                        }



                        ingameCD--;


                    }
//                    else {
//                        Bukkit.broadcastMessage("");
//                        Bukkit.broadcastMessage(BattleRoyale.getInstance().prefix + "§e§lDer Gewinner steht fest!");
//                        Bukkit.broadcastMessage(BattleRoyale.getInstance().prefix + "  §6Gewonnen hat§8: §c§oNiemand");
//                        Bukkit.broadcastMessage(BattleRoyale.getInstance().prefix + "    §7- §c§oNiemand");
//                        Bukkit.broadcastMessage("");
//
//                        if(FileManager.locCfg.isSet("lobby.x")) {
//                            double x = FileManager.locCfg.getDouble("lobby.x");
//                            double y = FileManager.locCfg.getDouble("lobby.y");
//                            double z = FileManager.locCfg.getDouble("lobby.z");
//
//                            double yaw = FileManager.locCfg.getDouble("lobby.yaw");
//                            double pitch = FileManager.locCfg.getDouble("lobby.pitch");
//
//                            String worldname = FileManager.locCfg.getString("lobby.worldname");
//                            Location loc = new Location(Bukkit.getWorld(worldname), x, y, z);
//                            loc.setYaw((float) yaw);
//                            loc.setPitch((float) pitch);
//
//                            for(Player all : Bukkit.getOnlinePlayers()) {
//                                all.teleport(loc);
//                                all.setGameMode(GameMode.ADVENTURE);
//                                all.setMaxHealth(20D);
//                                all.setHealth(20D);
//                                all.setFoodLevel(20);
//                                all.setSaturation(20);
//                                all.setFireTicks(0);
//                                all.setAllowFlight(false);
//                                all.setFlying(false);
//                                all.playSound(all.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1, 1);
//                                if(!BattleRoyale.getInstance().spectator.contains(all)) {
//                                    all.sendMessage(" ");
//                                    all.sendMessage(CreditAPI.prefix + "§8[§a+§8] §955 für die Teilnahme");
//                                    all.sendMessage(" ");
//                                    BattleRoyale.getInstance().creditAPI.addCredits(all, 55);
//                                }
//                            }
//
//                            BattleRoyale.getInstance().state = GameState.RESTART;
//                            restartCountdown();
//
//                        } else {
//                            Bukkit.broadcastMessage(BattleRoyale.getInstance().prefix + "§cLobby wurde noch nicht gesetzt!");
//                        }
//                    }

                } else {
                    Bukkit.getScheduler().cancelTask(BattleRoyale.getInstance().ingameSched);
                }
            }
        }, 0L, 20L);
    }















    public void restartCountdown() {
        BattleRoyale.getInstance().state = GameState.RESTART;
        restartCD = BattleRoyale.getInstance().restartTime;

        for(Player all : Bukkit.getOnlinePlayers()) {
            all.getInventory().clear();
            all.getInventory().setArmorContents(null);
        }

        BattleRoyale.getInstance().restartSched = Bukkit.getScheduler().scheduleSyncRepeatingTask(BattleRoyale.getInstance(), new Runnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage(BattleRoyale.getInstance().prefix + "§cDer Server startet in §b" + restartCD + " §cSekunden neu!");

                if(restartCD == 0) {
                    Bukkit.broadcastMessage(BattleRoyale.getInstance().prefix + "§cDer Server startet jetzt neu!");
                    Bukkit.getServer().shutdown();
                }

                restartCD--;
            }
        }, 0L, 20L);
    }






    private String toMinutes(int seconds) {
        String minutes = "";
        int min = seconds / 60;
        int rest = seconds % 60;

        if(rest > 0) {
            minutes = "§a" + min + " §7Minuten und §a" + rest + " §7Sekunden";
        } else {
            minutes = "§a" + min + " §7Minuten";
        }

        return minutes;
    }

}
