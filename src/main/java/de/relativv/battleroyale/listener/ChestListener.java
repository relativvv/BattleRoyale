package de.relativv.battleroyale.listener;

import de.relativv.battleroyale.main.BattleRoyale;
import de.relativv.battleroyale.utils.ChestPopulator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Random;

public class ChestListener implements Listener {

    private BattleRoyale plugin;
    public ChestListener(BattleRoyale plugin) {
        this.plugin = plugin;
    }

    private HashMap<Location, Inventory> chest = new HashMap<>();

    @EventHandler
    public void onChest(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(e.getClickedBlock() != null) {
                if(e.getClickedBlock().getType() == Material.CHEST) {
                    if(BattleRoyale.chests.contains(e.getClickedBlock().getLocation())) {
                        e.setCancelled(true);
                        p.playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN, 2, 2);
                        if(chest.containsKey(e.getClickedBlock().getLocation())) {
                            p.openInventory(chest.get(e.getClickedBlock().getLocation()));
                        } else {
                            Inventory inv = Bukkit.createInventory(null, InventoryType.CHEST);

                            Random r = new Random();
                            int l = r.nextInt(11)+5;

                            while(l != 0) {
                                l--;
                                Random rnd = new Random();
                                Random rnd2 = new Random();

                                int n2 = rnd.nextInt(27);
                                int n3 = rnd2.nextInt(BattleRoyale.chestItems.size());

                                inv.setItem(n2, BattleRoyale.chestItems.get(n3));
                            }


                            chest.put(e.getClickedBlock().getLocation(), inv);
                            p.openInventory(inv);
                        }
                    }
                }
            }
        }
    }


}
