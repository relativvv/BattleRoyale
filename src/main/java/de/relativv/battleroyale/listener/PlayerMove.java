package de.relativv.battleroyale.listener;

import de.relativv.battleroyale.main.BattleRoyale;
import de.relativv.battleroyale.utils.GameState;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {

    private BattleRoyale plugin;
    public PlayerMove(BattleRoyale plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if(plugin.state == GameState.WAIT) {
            moveBack(e);
        }
    }


    private void moveBack(PlayerMoveEvent event) {
        Location newLoc = event.getFrom();
        newLoc.setX(newLoc.getBlockX() + 0.5);
        newLoc.setY(newLoc.getBlockY());
        newLoc.setZ(newLoc.getBlockZ() + 0.5);
        event.getPlayer().teleport(newLoc);
    }

}
