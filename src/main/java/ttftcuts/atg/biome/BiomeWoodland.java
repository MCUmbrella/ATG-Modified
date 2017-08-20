package ttftcuts.atg.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import ttftcuts.atg.ATGBiomes;

import java.util.Random;

public class BiomeWoodland extends Biome {
    public BiomeWoodland() {
        super(new Biome.BiomeProperties("Woodland")
                .setBaseHeight(0.105f)
                .setHeightVariation(0.15f)
                .setTemperature(0.73f)
                .setRainfall(0.67f)
        );

        this.decorator.treesPerChunk = 5;
        this.decorator.grassPerChunk = 4;
        this.decorator.flowersPerChunk = 3;
    }

    @Override
    public WorldGenAbstractTree getRandomTreeFeature(Random rand)
    {
        return rand.nextInt(4) == 0 ? ATGBiomes.Features.OAK_SHRUB : Biome.TREE_FEATURE;
    }
}
