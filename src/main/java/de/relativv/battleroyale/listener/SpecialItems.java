package de.relativv.battleroyale.listener;

import de.relativv.battleroyale.main.BattleRoyale;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class SpecialItems implements Listener {

    private BattleRoyale plugin;
    public SpecialItems(BattleRoyale plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSpecial(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
            if (e.getItem() != null) {
                if (e.getItem().getType() == Material.SNOWBALL) {
                    if (e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eBrücke bauen")) {
                        e.setCancelled(true);
                        p.getItemInHand().setAmount(p.getItemInHand().getAmount()-1);
                        p.updateInventory();
                        Snowball ball = p.launchProjectile(Snowball.class);
                        getBlockBehindPlayer(ball).getBlock().setType(Material.OAK_PLANKS);
                        p.playSound(p.getLocation(), Sound.ENTITY_SNOWBALL_THROW, 1, 1);
                    }
                } else if (e.getItem().getType() == Material.FEATHER) {
                    if (e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6Fallschirm")) {
                        e.setCancelled(true);
                        p.getItemInHand().setAmount(p.getItemInHand().getAmount()-1);
                        p.updateInventory();

                        Chicken chicken = p.getWorld().spawn(p.getLocation(), Chicken.class);
                        chicken.setPassenger(p);

                        p.playSound(p.getLocation(), Sound.ENTITY_WITHER_HURT, 1, 0.5F);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onExplosionBows(ProjectileHitEvent e) {
        if(e.getEntity() instanceof Arrow) {
            Arrow a = (Arrow) e.getEntity();
            if(a.getShooter() instanceof Player) {
                Player p = (Player) a.getShooter();
                if(p.getItemInHand().getType() == Material.BOW) {
                    if(p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("§cExplosionsbogen")) {
                        TNTPrimed tnt = a.getWorld().spawn(a.getLocation(), TNTPrimed.class);
                        tnt.setFuseTicks(3);

                    }
                }
            }
        }
    }

    public Location getBlockBehindPlayer(Entity ent) {
        Vector inverseDirectionVec = ent.getLocation().getDirection().normalize().multiply(-1);
        return ent.getLocation().add(inverseDirectionVec);
    }

}
