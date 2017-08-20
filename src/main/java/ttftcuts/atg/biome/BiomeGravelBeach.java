package ttftcuts.atg.biome;

import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;

public class BiomeGravelBeach extends Biome
{
    public BiomeGravelBeach()
    {
        super(new BiomeProperties("Gravel Beach")
                .setBaseHeight(0.0f)
                .setHeightVariation(0.025f)
                .setTemperature(0.25f)
                .setRainfall(0.4f)
        );

        this.spawnableCreatureList.clear();
        this.topBlock = Blocks.GRAVEL.getDefaultState();
        this.fillerBlock = Blocks.GRAVEL.getDefaultState();
        this.decorator.treesPerChunk = -999;
        this.decorator.deadBushPerChunk = 0;
        this.decorator.reedsPerChunk = 0;
        this.decorator.cactiPerChunk = 0;
    }
}
