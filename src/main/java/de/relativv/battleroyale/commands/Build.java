package de.relativv.battleroyale.commands;

import de.relativv.battleroyale.main.BattleRoyale;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Build implements CommandExecutor {

    private BattleRoyale plugin;
    public Build(BattleRoyale plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            if(p.hasPermission("system.build")) {
                if (args.length == 0) {
                    if (plugin.build.contains(p)) {
                        plugin.build.remove(p);
                        p.sendMessage(plugin.prefix + "§7Dein Baumodus wurde §cdeaktiviert");
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                    } else {
                        plugin.build.add(p);
                        p.sendMessage(plugin.prefix + "§7Dein Baumodus wurde §aaktiviert");
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                    }



                } else if(args.length == 1) {
                    String name = args[0];
                    if (Bukkit.getOfflinePlayer(name).isOnline()) {
                        Player target = Bukkit.getPlayer(name);

                        if (plugin.build.contains(target)) {
                            plugin.build.remove(target);
                            target.sendMessage(plugin.prefix + "§7Dein Baumodus wurde §cdeaktiviert");
                            sender.sendMessage(plugin.prefix + "§7Der Baumodus von §a" + target.getName() + " §7wurde §cdeaktiviert");
                            target.playSound(target.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                        } else {
                            plugin.build.add(target);
                            target.sendMessage(plugin.prefix + "§7Dein Baumodus wurde §aaktiviert");
                            sender.sendMessage(plugin.prefix + "§7Der Baumodus von §a" + target.getName() + " §7wurde §aaktiviert");
                            target.playSound(target.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                        }
                    }
                }
            } else {
                p.sendMessage(plugin.noPerm);
                p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 2, 2);
            }
        }
        return true;
    }
}
