package de.relativv.battleroyale.commands;

import de.relativv.battleroyale.main.BattleRoyale;
import de.relativv.battleroyale.utils.Countdowns;
import de.relativv.battleroyale.utils.GameState;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Start implements CommandExecutor {

    private BattleRoyale plugin;
    public Start(BattleRoyale plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;

            if(p.hasPermission("battleRoyale.start")) {

                if(plugin.state == GameState.LOBBY) {
                    if(Countdowns.lobbyCD >= 13) {

                        Countdowns.lobbyCD = 12;
                        p.sendMessage(plugin.prefix + "§aDer Countdown wurde verkürzt!");
                        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5F, 0.5F);

                    } else {
                        p.sendMessage(plugin.prefix + "§cDas Spiel startet bereits!");
                        p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 2, 2);
                    }

                } else {
                    p.sendMessage(plugin.prefix + "§cDas Spiel ist bereits gestartet!");
                    p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 2, 2);
                }

            } else {
                p.sendMessage(plugin.noPerm);
                p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 2, 2);
            }
        }
        return true;
    }
}
