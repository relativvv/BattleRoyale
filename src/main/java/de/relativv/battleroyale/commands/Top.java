package de.relativv.battleroyale.commands;

import de.relativv.battleroyale.main.BattleRoyale;
import de.relativv.battleroyale.utils.GameState;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Top implements CommandExecutor {

    private BattleRoyale plugin;
    public Top(BattleRoyale plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;

            if(plugin.state == GameState.INGAME) {
                Location toTp = new Location(p.getWorld(), p.getLocation().getX(), p.getWorld().getHighestBlockYAt(p.getLocation().getBlockX(), p.getLocation().getBlockZ()), p.getLocation().getZ());
                p.teleport(toTp.add(0, 1, 0));
                p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                p.sendMessage(plugin.prefix + "§7Du bist nun wieder an der Oberfläche!");
            } else {
                p.sendMessage(plugin.prefix + "§cDas Spiel hat noch nicht begonnen!");
                p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 2, 2);
            }
        }
        return true;
    }
}
