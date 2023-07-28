package net.skypieamc.worldgen.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.noise.NoiseConfig;
import net.skypieamc.worldgen.utils.Noise;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(NoiseChunkGenerator.class)
public abstract class WorldGenMixin {
    @Shadow
    public abstract int getSeaLevel();

    Noise noise = new Noise(new Random());
    Noise noise2 = new Noise(new Random());

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void buildSurface(ChunkRegion region, StructureAccessor structures, NoiseConfig noiseConfig, Chunk chunk) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                this.buildColumn(x + chunk.getPos().getStartX(), z + chunk.getPos().getStartZ(), region, chunk);
            }

        }
    }

    private void buildColumn(int x, int z, ChunkRegion region, Chunk chunk) {
        double scale = noise2.sample(x * 0.001, z * 0.001);
        double height = noise.sample(x * 0.04, z * 0.04) * 10 * scale;

        height += 64;
        height += noise.sample(x * 0.005, z * 0.005) * 70 * scale;
        for (int y = chunk.getBottomY(); y < chunk.getTopY(); y++) {
            if (y < height) {
                chunk.setBlockState(new BlockPos(x, y, z), Blocks.GRASS_BLOCK.getDefaultState(), false);
                if (y < height - 1) {
                    chunk.setBlockState(new BlockPos(x, y, z), Blocks.DIRT.getDefaultState(), false);
                }
                if (y < height - 5) {
                    chunk.setBlockState(new BlockPos(x, y, z), Blocks.STONE.getDefaultState(), false);
                }
            } else {
                if (y < this.getSeaLevel()) {
                    if (y < height - 1) {
                        chunk.setBlockState(new BlockPos(x, y, z), Blocks.SAND.getDefaultState(), false);
                    }
                    chunk.setBlockState(new BlockPos(x, y, z), Blocks.WATER.getDefaultState(), false);
                } else {
                    chunk.setBlockState(new BlockPos(x, y, z), Blocks.AIR.getDefaultState(), false);
                }
            }
        }
    }
}
