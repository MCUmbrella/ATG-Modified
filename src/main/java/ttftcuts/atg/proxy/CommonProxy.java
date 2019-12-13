package ttftcuts.atg.proxy;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ttftcuts.atg.ATG;
//import ttftcuts.atg.ATGBiomes;
//import ttftcuts.atg.generator.biome.VillageBlocks;

@Mod.EventBusSubscriber
public class CommonProxy {
    @SubscribeEvent
    public static void onBiomeRegistry(RegistryEvent.Register<Biome> event) {
        //ATGBiomes.init(event.getRegistry());
    }

    public void preInit(FMLPreInitializationEvent event) {
        ATG.modCompat.preInit();
    }

    public void init(FMLInitializationEvent event) {
        //MinecraftForge.TERRAIN_GEN_BUS.register(new VillageBlocks());
        ATG.modCompat.init();
    }

    public void postInit(FMLPostInitializationEvent event) {
        ATG.modCompat.postInit();
    }

    public void loadComplete(FMLLoadCompleteEvent event) {
        ATG.modCompat.processIMC(FMLInterModComms.fetchRuntimeMessages(this));
        ATG.modCompat.registerBuiltInModules();
    }

    public void serverStarting(FMLServerStartingEvent event) {

    }

    public void serverStopped(FMLServerStoppedEvent event) {

    }
}
