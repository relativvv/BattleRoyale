package de.relativv.battleroyale.listener;

import de.relativv.battleroyale.main.BattleRoyale;
import de.relativv.battleroyale.utils.GameState;
import de.relativv.battleroyale.utils.GameUtils;
import de.relativv.battleroyale.utils.Team;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class EntityDamage implements Listener {

    private BattleRoyale plugin;
    public EntityDamage(BattleRoyale plugin) {
        this.plugin = plugin;
    }

    public static HashMap<Player, Player> lastDMG = new HashMap<>();

    @EventHandler
    public void onDMG(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {

            Player d = (Player) e.getDamager();
            Player p = (Player) e.getEntity();


            if (plugin.state == GameState.LOBBY) {
                e.setCancelled(true);
                if (d.getItemInHand().getType() == Material.DIAMOND_SWORD) {
                    e.setCancelled(true);


                    if (Team.invites.containsKey(d) && Team.invites.containsValue(Team.getTeamByPlayerInside(p))) {
                        plugin.gameUtils.playerJoinTeam(p, d);

                        Team.invites.remove(d);
                        Team.invites.remove(p);

                        String joined = BattleRoyale.getInstance().prefix + "§a" + d.getName() + " §7ist deinem Team beigetreten.";
                        String joinedP = BattleRoyale.getInstance().prefix + "§7Du bist dem Team §a" + Team.getTeamByPlayerInside(p).getName() + " §7beigetreten.";

                        for (Player teamPl : Team.getTeamByPlayerInside(p).getTeamList()) {
                            teamPl.sendMessage(joined);
                            teamPl.playSound(teamPl.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 0.5F, 0.5F);
                        }

                        p.sendMessage(joinedP);
                        p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 0.5F, 0.5F);


                    } else {
                        if (!plugin.gameUtils.twoPlayersInOneTeam(d, p)) {
                            if (Team.getTeamByPlayerInside(d).getTeamList().size() < 2) {


                                Team.invites.remove(p);
                                Team.invites.put(p, Team.getTeamByPlayerInside(d));

                                String requestMsg = BattleRoyale.getInstance().prefix + "§7Dein Team hat §a" + p.getName() + " §7eine Teameinladung gesendet!";
                                String receivedMsg = BattleRoyale.getInstance().prefix + "§7Du hast eine Teameinladung von §a" + Team.getTeamByPlayerInside(d).getName() + " §7erhalten.";

                                for (Player teamPl : Team.getTeamByPlayerInside(p).getTeamList()) {
                                    teamPl.sendMessage(requestMsg);
                                    teamPl.playSound(teamPl.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 0.5F, 0.5F);
                                }

                                p.sendMessage(receivedMsg);
                                p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 0.5F, 0.5F);


                            } else {
                                d.sendMessage(BattleRoyale.getInstance().prefix + "§cDein Team ist voll!");
                                d.playSound(d.getLocation(), Sound.ITEM_SHIELD_BREAK, 2, 2);

                            }

                        } else {
                            for (Player teamPl : Team.getTeamByPlayerInside(d).getTeamList()) {
                                teamPl.sendMessage(BattleRoyale.getInstance().prefix + "§cDieser Spieler ist bereits in deinem Team!");
                                teamPl.playSound(teamPl.getLocation(), Sound.ITEM_SHIELD_BREAK, 2, 2);
                            }
                        }
                    }
                }


            } else if (plugin.state == GameState.INGAME) {
                if (GameUtils.twoPlayersInOneTeam(d, p)) {
                    e.setDamage(0D);
                    return;
                }



                if (!GameUtils.twoPlayersInOneTeam(d, p)) {
                    lastDMG.remove(p);
                    lastDMG.put(p, d);


                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            lastDMG.remove(d);
                            lastDMG.remove(p);
                        }
                    }.runTaskLater(plugin, 20 * 15);
                }
            }




        } else if (e.getDamager() instanceof Arrow && e.getEntity() instanceof Player) {
            Arrow a = (Arrow) e.getDamager();
            if(a.getShooter() instanceof Player) {
                Player d = (Player) a.getShooter();
                Player p = (Player) e.getEntity();

                if (plugin.state == GameState.INGAME) {
                    if (GameUtils.twoPlayersInOneTeam(d, p)) {
                        e.setDamage(0D);
                        return;
                    }

                    if (!GameUtils.twoPlayersInOneTeam(d, p)) {
                        lastDMG.remove(p);
                        lastDMG.put(p, d);


                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                lastDMG.remove(d);
                                lastDMG.remove(p);
                            }
                        }.runTaskLater(plugin, 20 * 15);
                    }
                }
            }
        }
    }





    @EventHandler
    public void onDMG(EntityDamageEvent e) {
        if(plugin.state == GameState.LOBBY || plugin.state == GameState.WAIT || plugin.state == GameState.RESTART) {
            e.setCancelled(true);
        }
    }

}
