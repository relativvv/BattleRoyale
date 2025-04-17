package de.relativv.battleroyale.commands;

import de.relativv.battleroyale.main.BattleRoyale;
import de.relativv.battleroyale.utils.StatType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Stats implements CommandExecutor {

    private BattleRoyale plugin;
    public Stats(BattleRoyale plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;

            if(args.length == 0) {

                p.sendMessage("§7------------- §8[ §bStats §9- §3" + p.getName() + " §8] §7-------------");
                p.sendMessage(" ");
                p.sendMessage("  §aKills§8: §e" + plugin.statsAPI.getStat(p, StatType.Kills));
                p.sendMessage("  §aTode§8: §e" + plugin.statsAPI.getStat(p, StatType.Deaths));
                p.sendMessage("  §aGespielt§8: §e" + plugin.statsAPI.getStat(p, StatType.Played));
                p.sendMessage("  §aGewonnen§8: §e" + plugin.statsAPI.getStat(p, StatType.Won));
                p.sendMessage(" ");
                p.sendMessage("§7------------- §8[ §bStats §9- §3" + p.getName() + " §8] §7-------------");

            } else if(args.length == 1) {
                String name = args[0];
                OfflinePlayer target = Bukkit.getOfflinePlayer(name);

                p.sendMessage("§7------------- §8[ §bStats §9- §3" + target.getName() + " §8] §7-------------");
                p.sendMessage(" ");
                p.sendMessage("  §aKills§8: §e" + plugin.statsAPI.getStat(target, StatType.Kills));
                p.sendMessage("  §aTode§8: §e" + plugin.statsAPI.getStat(target, StatType.Deaths));
                p.sendMessage("  §aGespielt§8: §e" + plugin.statsAPI.getStat(target, StatType.Played));
                p.sendMessage("  §aGewonnen§8: §e" + plugin.statsAPI.getStat(target, StatType.Won));
                p.sendMessage(" ");
                p.sendMessage("§7------------- §8[ §bStats §9- §3" + target.getName() + " §8] §7-------------");
            }

        }
        return true;
    }
}
