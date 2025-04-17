package de.relativv.battleroyale.listener;

import de.relativv.battleroyale.main.BattleRoyale;
import de.relativv.battleroyale.utils.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class WorldEvents implements Listener {

    private BattleRoyale plugin;
    public WorldEvents(BattleRoyale plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        if(e.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL || e.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(plugin.prefix + "§cDu kannst andere Dimensionen nicht betreten!");
        }
    }


    @EventHandler
    public void onSpawn(CreatureSpawnEvent e) {
        if(plugin.state == GameState.LOBBY || plugin.state == GameState.RESTART) {
            if(e.getSpawnReason() != CreatureSpawnEvent.SpawnReason.CUSTOM) {
                e.setCancelled(true);
            }
        }
    }

}
