package de.relativv.battleroyale.listener;

import de.relativv.battleroyale.main.BattleRoyale;
import de.relativv.battleroyale.utils.GameState;
import de.relativv.battleroyale.utils.Team;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChat implements Listener {

    private BattleRoyale plugin;
    public AsyncPlayerChat(BattleRoyale plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        if(plugin.state == GameState.LOBBY || plugin.state == GameState.RESTART) {

            if(PlayerInteract.renaming.contains(p)) {
                if (e.getMessage().length() >= 4) {
                    e.setCancelled(true);

                    if (!plugin.gameUtils.nameGiven(e.getMessage().substring(0, 4))) {
                        Team team = Team.getTeamByPlayerInside(p);
                        team.setName(e.getMessage().substring(0, 4));

                        for (Player inTeam : team.getTeamList()) {
                            inTeam.sendMessage(plugin.prefix + "§7Dein Team heisst jetzt§8: §a" + e.getMessage().substring(0, 4));
                        }

                        team.updateScoreboard();
                        PlayerInteract.renaming.remove(p);
                    } else {
                        p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
                        p.sendMessage(plugin.prefix + "§cDieser Teamname ist bereits vergeben!");
                    }
                }
            }
            e.setFormat(p.getDisplayName() + "§8: §7" + e.getMessage());


        } else if(plugin.state == GameState.INGAME || plugin.state == GameState.WAIT) {
            if (plugin.spectator.contains(e.getPlayer())) {
                e.setCancelled(true);
                for (Player spec : plugin.spectator) {
                    spec.sendMessage("§8[§7Spec§8] §r" + p.getDisplayName() + "§8: §7" + e.getMessage());
                }
            } else {
                e.setFormat(p.getDisplayName() + "§8: §7" + e.getMessage());
            }
        }
    }
}
