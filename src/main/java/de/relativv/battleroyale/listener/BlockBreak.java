package de.relativv.battleroyale.listener;

import de.relativv.battleroyale.main.BattleRoyale;
import de.relativv.battleroyale.utils.GameState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreak implements Listener {

    private BattleRoyale plugin;
    public BlockBreak(BattleRoyale plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();

        if(plugin.state == GameState.LOBBY || plugin.state == GameState.WAIT || plugin.state == GameState.RESTART) {
            if(!plugin.build.contains(p)) {
                e.setCancelled(true);
            }
        } else if(plugin.state == GameState.INGAME) {
            if(p.getWorld() == Bukkit.getWorld("world")) {

                    if(plugin.blocksPlaced.contains(e.getBlock().getLocation())) {
                        e.setCancelled(false);
                        plugin.blocksPlaced.remove(e.getBlock().getLocation());

                    } else if(!plugin.blocksPlaced.contains(e.getBlock().getLocation())) {
                        if (e.getBlock().getType() != Material.OAK_LEAVES && e.getBlock().getType() != Material.BIRCH_LEAVES && e.getBlock().getType() != Material.SPRUCE_LEAVES
                                && e.getBlock().getType() != Material.ACACIA_LEAVES && e.getBlock().getType() != Material.JUNGLE_LEAVES && e.getBlock().getType() != Material.DARK_OAK_LEAVES
                                && e.getBlock().getType() != Material.WHEAT_SEEDS && e.getBlock().getType() != Material.MELON_SEEDS && e.getBlock().getType() != Material.PUMPKIN_SEEDS
                                && e.getBlock().getType() != Material.CARROT && e.getBlock().getType() != Material.CARROTS && e.getBlock().getType() != Material.POTATO && e.getBlock().getType() != Material.POTATOES
                                && e.getBlock().getType() != Material.WHEAT && e.getBlock().getType() != Material.MELON && e.getBlock().getType() != Material.GRASS && e.getBlock().getType() != Material.TALL_GRASS
                                && e.getBlock().getType() != Material.GRAVEL && e.getBlock().getType() != Material.SUGAR_CANE && e.getBlock().getType() != Material.BOOKSHELF && e.getBlock().getType() != Material.LILY_PAD) {

                            e.setCancelled(true);
                    }
                }
            } else {
                e.setCancelled(true);
            }
        }
    }
}
