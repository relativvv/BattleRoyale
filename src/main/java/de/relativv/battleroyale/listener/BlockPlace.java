package de.relativv.battleroyale.listener;

import de.relativv.battleroyale.main.BattleRoyale;
import de.relativv.battleroyale.utils.GameState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlace implements Listener {

    private BattleRoyale plugin;
    public BlockPlace(BattleRoyale plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();

        if(plugin.state == GameState.LOBBY || plugin.state == GameState.WAIT || plugin.state == GameState.RESTART) {
            if(!plugin.build.contains(p)) {
                e.setCancelled(true);
            }
        } else if(plugin.state == GameState.INGAME) {
            if(p.getWorld() == Bukkit.getWorld("world")) {
                plugin.blocksPlaced.add(e.getBlockPlaced().getLocation());
                if(e.getBlockPlaced().getType() == Material.TNT) {
                    e.getBlockPlaced().setType(Material.AIR);
                    TNTPrimed tnt = e.getBlockPlaced().getWorld().spawn(e.getBlock().getLocation(), TNTPrimed.class);
                    tnt.setFuseTicks(40);
                }
            } else {
                e.setCancelled(true);
            }
        }
    }
}
