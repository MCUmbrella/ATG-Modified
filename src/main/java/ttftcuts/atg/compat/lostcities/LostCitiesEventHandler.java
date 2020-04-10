package ttftcuts.atg.compat.lostcities;

import mcjty.lostcities.api.LostCityEvent;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;
import java.util.List;

public class LostCitiesEventHandler {
    private static final List<Biome> BLOCKED_BIOMES = Arrays.asList(
            Biomes.OCEAN,
            Biomes.DEEP_OCEAN,
            Biomes.RIVER,
            Biomes.FROZEN_OCEAN,
            Biomes.FROZEN_RIVER,
            Biomes.BEACH,
            Biomes.COLD_BEACH,
            Biomes.STONE_BEACH
    );

    @SubscribeEvent
    public static void onPreGenCityChunk(LostCityEvent.CharacteristicsEvent event) {
        if(!event.getCharacteristics().isCity) {
            return;
        }

        // TODO: Skip cities because of height

        try{
            BlockPos base = new BlockPos(event.getChunkX() << 4, 200, event.getChunkZ() << 4);
            List<BlockPos> positions = Arrays.asList(
                base.add(15, 0, 0),
                base.add(15, 0, 15),
                base.add(0, 0, 15)
        );
        for(BlockPos pos : positions) {
            Biome biome = event.getWorld().getBiome(pos);
            if (BLOCKED_BIOMES.contains(biome)) {
                event.getCharacteristics().isCity = false;
                return;
            }
        }
        }catch(Throwable e){}
    }
}
