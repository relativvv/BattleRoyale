package de.relativv.battleroyale.commands;

import de.relativv.battleroyale.main.BattleRoyale;
import de.relativv.battleroyale.utils.FileManager;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetLocation implements CommandExecutor {

    private BattleRoyale plugin;
    public SetLocation(BattleRoyale plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;

            if(p.hasPermission("battleRoyale.setlocation")) {


                if(args.length == 1) {

                    double x = p.getLocation().getX();
                    double y = p.getLocation().getY();
                    double z = p.getLocation().getZ();

                    double yaw = p.getLocation().getYaw();
                    double pitch = p.getLocation().getPitch();

                    String worldname = p.getWorld().getName();


                    if(args[0].equalsIgnoreCase("lobby")) {
                        p.sendMessage(plugin.prefix + "§aPosition gesetzt§8: §aLOBBY");
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 2, 2);

                        FileManager.locCfg.set("lobby.x", x);
                        FileManager.locCfg.set("lobby.y", y);
                        FileManager.locCfg.set("lobby.z", z);

                        FileManager.locCfg.set("lobby.yaw", yaw);
                        FileManager.locCfg.set("lobby.pitch", pitch);

                        FileManager.locCfg.set("lobby.worldname", worldname);

                        FileManager.saveLocations();

                    } else {
                        p.sendMessage(plugin.prefix + "§c/setlocation <Lobby>");
                        p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 2, 2);
                    }

                } else {
                    p.sendMessage(plugin.prefix + "§c/setlocation <Lobby>");
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
