package de.relativv.battleroyale.listener;

import de.relativv.battleroyale.main.BattleRoyale;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodLevelChange implements Listener {

    private BattleRoyale plugin;
    public FoodLevelChange(BattleRoyale plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }

}
