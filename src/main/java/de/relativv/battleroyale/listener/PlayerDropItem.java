package de.relativv.battleroyale.listener;

import de.relativv.battleroyale.main.BattleRoyale;
import de.relativv.battleroyale.utils.GameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItem implements Listener {

    private BattleRoyale plugin;
    public PlayerDropItem(BattleRoyale plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();

        if(plugin.state == GameState.LOBBY || plugin.state == GameState.WAIT || plugin.state == GameState.RESTART) {
            if (!plugin.build.contains(p)) {
                e.setCancelled(true);
            }
        } else if(plugin.state == GameState.INGAME) {
            if(p.getWorld() != Bukkit.getWorld("world")) {
                e.setCancelled(true);
            }
        }
    }
}
