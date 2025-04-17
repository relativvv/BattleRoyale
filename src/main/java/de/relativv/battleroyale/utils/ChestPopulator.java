package de.relativv.battleroyale.utils;

import java.util.Random;

import de.relativv.battleroyale.main.BattleRoyale;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

public class ChestPopulator extends BlockPopulator {

    public static final int CHANCE_OF_CHEST = 21;
    public static final Material CHEST_MATERIAL = Material.CHEST;

    @Override
    public void populate(World world, Random random, Chunk source) {
        if (random.nextInt(100) < CHANCE_OF_CHEST) {
            ChunkSnapshot snapshot = source.getChunkSnapshot();
            int x = 1 + random.nextInt(14);
            int z = 1 + random.nextInt(14);
            int y = snapshot.getHighestBlockYAt(x, z);

            Block block = source.getBlock(x, y, z);
            if (block.getY() > 5) {
                if(block.getLocation().subtract(0, 1, 0).getBlock().getType() != Material.WATER) {
                    block.setType(CHEST_MATERIAL);
                    BattleRoyale.chests.add(block.getLocation());
                }
            }
        }
    }
}

