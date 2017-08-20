package ttftcuts.atg.biome;

import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;

public class BiomeSnowyGravelBeach extends Biome
{
    public BiomeSnowyGravelBeach()
    {
        super(new BiomeProperties("Snowy Gravel Beach")
                .setBaseHeight(0.0f)
                .setHeightVariation(0.025f)
                .setTemperature(0.05f)
                .setRainfall(0.4f)
                .setSnowEnabled()
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
