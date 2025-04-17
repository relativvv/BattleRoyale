package de.relativv.battleroyale.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileManager {

    public static File locationFile = new File("plugins/BattleRoyale", "locations.yml");
    public static FileConfiguration locCfg = YamlConfiguration.loadConfiguration(locationFile);


    public static void saveLocations() {
        try {
            locCfg.save(locationFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
