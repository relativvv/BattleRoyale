package de.relativv.battleroyale.listener;

import de.relativv.battleroyale.main.BattleRoyale;
import de.relativv.battleroyale.utils.GameState;
import de.relativv.battleroyale.utils.GameUtils;
import de.relativv.battleroyale.utils.Team;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;

public class PlayerInteract implements Listener {

    private BattleRoyale plugin;
    public PlayerInteract(BattleRoyale plugin) {
        this.plugin = plugin;
    }

    public static ArrayList<Player> renaming = new ArrayList<>();


     @EventHandler
     public void onInteract(PlayerInteractEvent e) {
         Player p = e.getPlayer();

         if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
             if(plugin.state == GameState.LOBBY) {
                 if(e.getItem() != null) {
                     if(e.getItem().getType() == Material.OAK_SIGN) {
                         e.setCancelled(true);
                         p.updateInventory();
                         p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 2, 2);

                         if(!renaming.contains(p)) {
                             renaming.add(p);
                             p.sendMessage(plugin.prefix + "§7Schreibe in den Chat, wie dein Team heissen soll!");
                         } else {
                             renaming.remove(p);
                             p.sendMessage(plugin.prefix + "§7Du hast den Umbennungsmodus verlassen.");
                         }



                     } else if(e.getItem().getType() == Material.BARRIER) {
                        e.setCancelled(true);
                        p.updateInventory();

                         Team teams = Team.getTeamByPlayerInside(p);
                         if(teams.getTeamList().size() >= 2) {
                             for (Player inTeam : teams.getTeamList()) {
                                 inTeam.playSound(inTeam.getLocation(), Sound.ENTITY_BLAZE_HURT, 2, 2);
                                 inTeam.sendMessage(plugin.prefix + "§b" + p.getName() + " §chat das Team verlassen.");
                             }
                         } else {
                             p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
                             p.sendMessage(plugin.prefix + "§cDu kannst dein Team nicht verlassen!");
                         }

                         plugin.gameUtils.playerLeaveTeam(p);
                         teams.updateScoreboard();

                         Team t = Team.getTeamByPlayerInside(p);
                         t.setScoreboard();
                         t.updateScoreboard();

                     }
                 }
             }




         } else if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
             if(e.getClickedBlock() != null) {
                 if (plugin.state != GameState.INGAME) {
                     e.setCancelled(true);
                 }
                 if (plugin.spectator.contains(p)) {
                     e.setCancelled(true);
                 }
             }
         }
     }
}
