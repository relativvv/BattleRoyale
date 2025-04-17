package de.relativv.battleroyale.listener;

import de.relativv.battleroyale.main.BattleRoyale;
import de.relativv.battleroyale.utils.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerListPing implements Listener {

    private BattleRoyale plugin;
    public ServerListPing(BattleRoyale plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerListPing(ServerListPingEvent e) {
        e.setMaxPlayers(32);

        if(plugin.state == GameState.LOBBY) {
            e.setMotd("§0[§aLobby§0]");
        } else if(plugin.state == GameState.WAIT) {
            e.setMotd("§0[§bWaiting§0]");
        } else if(plugin.state == GameState.INGAME) {
            e.setMotd("§0[§cIngame§0]");
        } else if(plugin.state == GameState.RESTART) {
            e.setMotd("§0[§4Restart§0]");
        }
    }

}
