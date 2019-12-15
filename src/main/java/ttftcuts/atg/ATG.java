package ttftcuts.atg;

import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ttftcuts.atg.compat.ModCompat;
import ttftcuts.atg.compat.lostcities.LostCitiesEventHandler;
import ttftcuts.atg.configuration.ConfigHandler;
import ttftcuts.atg.generator.GlobalRegistry;

@Mod(modid = ATG.MODID, version = ATG.VERSION, acceptedMinecraftVersions = "[1.12,1.13)", acceptableRemoteVersions = "*", dependencies = "required-before:lostcities")
public class ATG
{
    public static final String MODID = "atg";
    public static final String VERSION = "2";

    public static final Logger logger = LogManager.getLogger(MODID);

    public static WorldTypeATG worldType;

    public static GlobalRegistry globalRegistry = new GlobalRegistry();;
    public static ModCompat modCompat = new ModCompat();
    public static ConfigHandler config;

    @Mod.Instance(MODID)
    public static ATG instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        config = new ConfigHandler(event.getSuggestedConfigurationFile());
        worldType = new WorldTypeATG("atg");

        MinecraftForge.EVENT_BUS.register(LostCitiesEventHandler.class);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }

    @Mod.EventHandler
    public void loadComplete(FMLLoadCompleteEvent event) {
    }

    @Mod.EventHandler
    public void handleIMC(FMLInterModComms.IMCEvent event) {
        modCompat.processIMC(event.getMessages());
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
    }

    @Mod.EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
    }
}
