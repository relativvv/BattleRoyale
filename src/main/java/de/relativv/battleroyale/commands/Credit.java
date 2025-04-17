package de.relativv.battleroyale.commands;

import de.relativv.battleroyale.main.BattleRoyale;
import de.relativv.battleroyale.utils.CreditAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Credit implements CommandExecutor {

    private BattleRoyale plugin;
    public Credit(BattleRoyale plugin) {
        this.plugin = plugin;
    }

    // /credits add <Player> <amount>
    // /credits set <Player> <amount>
    // /credits remove <Player> <amount>
    // /credits <Player>


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 0) {
            if(sender instanceof Player) {
                Player p = (Player) sender;

                if(plugin.creditAPI.userExists(p)) {
                    p.sendMessage(CreditAPI.prefix + "§7Du hast §a" + plugin.creditAPI.getCredits(p) + " §7Credits.");
                } else {
                    plugin.creditAPI.createUser(p);
                    p.sendMessage(CreditAPI.prefix + "§7Du hast §a" + plugin.creditAPI.getCredits(p) + " §7Credits.");
                }
            }



        } else if(args.length == 1) {
            String name = args[0];
            if(Bukkit.getOfflinePlayer(name).isOnline()) {
                Player target = Bukkit.getPlayer(name);

                sender.sendMessage(CreditAPI.prefix + "§7Der Spieler §a" + target.getName() + " §7hat §a" + plugin.creditAPI.getCredits(target) + " §7Credits.");

            } else {
                sender.sendMessage(CreditAPI.prefix + "§cDieser Spieler ist nicht online!");
            }



        } else if(args.length == 3) {
            if(sender.hasPermission("system.credits")) {
                String name = args[1];
                OfflinePlayer target = Bukkit.getOfflinePlayer(name);
                if (isInteger(args[2])) {
                    int amount = Integer.parseInt(args[2]);
                    if (amount >= 0) {


                        if (args[0].equalsIgnoreCase("add")) {
                            plugin.creditAPI.addCredits(target, amount);
                            sender.sendMessage(CreditAPI.prefix + "§8[§a+§8] §a" + amount + " §7Credits zu §a" + target.getName() + " §7Vermögen hinzugefügt." + " §7(§eNeuer Kontostand§8: §a" + plugin.creditAPI.getCredits(target) + "§7)");
                            if (target.isOnline()) {
                                if (amount == 1) {
                                    Bukkit.getPlayer(name).sendMessage(CreditAPI.prefix + "§a" + amount + " §7Credit wurden deinem Konto gutgeschrieben.");
                                } else {
                                    Bukkit.getPlayer(name).sendMessage(CreditAPI.prefix + "§a" + amount + " §7Credits wurden deinem Konto gutgeschrieben.");
                                }
                            }

                        } else if (args[0].equalsIgnoreCase("set")) {
                            plugin.creditAPI.setCredits(target, amount);
                            sender.sendMessage(CreditAPI.prefix + "§a" + target.getName() + " §7hat jetzt §a" + amount + " §7Credits." + " §7(§eNeuer Kontostand§8: §a" + plugin.creditAPI.getCredits(target) + "§7)");
                            if (target.isOnline()) {
                                Bukkit.getPlayer(name).sendMessage(CreditAPI.prefix + "§7Du hast jetzt §a" + amount + " §7Credits.");
                            }

                        } else if (args[0].equalsIgnoreCase("remove")) {
                            int curr = plugin.creditAPI.getCredits(target);
                            int newAm = curr - amount;
                            if (newAm >= 0) {
                                plugin.creditAPI.removeCredits(target, amount);
                                sender.sendMessage(CreditAPI.prefix + "§8[§4-§8] §7Entfernt §a" + amount + " §7Credits von §a" + target.getName() + " §7Kontostand entfernt." + " §7(§eNeuer Kontostand§8: §a" + plugin.creditAPI.getCredits(target) + "§7)");
                                if (target.isOnline()) {
                                    if (amount == 1) {
                                        Bukkit.getPlayer(name).sendMessage(CreditAPI.prefix + "§a" + amount + " §7Credit wurde von deinem Konto abgehoben.");
                                    } else {
                                        Bukkit.getPlayer(name).sendMessage(CreditAPI.prefix + "§a" + amount + " §7Credits wurden von deinem Konto abgehoben.");
                                    }
                                }
                            } else {
                                sender.sendMessage(CreditAPI.prefix + "§cDu kannst keinen negativen Kontostand setzen.");
                            }
                        }

                    } else {
                        sender.sendMessage(CreditAPI.prefix + "§cDu musst 0 oder mehr Credits angeben.");
                    }
                } else {
                    sender.sendMessage(CreditAPI.prefix + "§cDu musst eine Zahl angeben.");
                }
            } else {
                sender.sendMessage(CreditAPI.prefix + "§cDu hast keine Berechtigung dazu!");
            }


        } else {
            sender.sendMessage(CreditAPI.prefix + "§cSyntax§8: §7/credits <set | add | remove> <Player> <Amount>");
            return true;
        }

        return true;
    }





    private boolean isInteger(String i) {
        try {
            Integer.parseInt(i);
            return true;
        } catch(NumberFormatException ex) {
            return false;
        }
    }
}
