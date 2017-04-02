package ttftcuts.atg.tweaks;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ttftcuts.atg.ATG;
import ttftcuts.atg.util.CoordCache;
import ttftcuts.atg.util.CoordPair;
import ttftcuts.atg.util.GeneralUtil;

import java.lang.reflect.*;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class GrassColours {
    private static final String[] GRASS_COLOR = {"a", "field_180291_a", "GRASS_COLOR"};
    private static final String[] COLOR_RESOLVER = {"ais$a", "net.minecraft.world.biome.BiomeColorHelper$ColorResolver"};
    private static final String[] GET_COLOR_AT_POS = {"a", "func_180283_a", "getColorAtPos"};

    public static void init() {
        ATG.logger.info("ATTEMPTING TO COMMIT GREAT EVIL:");
        try {
            doImmenseEvil();
        } catch(Throwable e) {
            e.printStackTrace();
        }
        MinecraftForge.EVENT_BUS.register(new Listener());
    }

    public static void doImmenseEvil() throws Exception {
        // de-finalise the grass colour field
        Field grass_color = ReflectionHelper.findField(BiomeColorHelper.class, GRASS_COLOR);
        grass_color.setAccessible(true);

        Field modifiers = Field.class.getDeclaredField("modifiers");

        AccessController.doPrivileged((PrivilegedAction) () -> {
            modifiers.setAccessible(true);
            return null;
        });

        modifiers.setInt(grass_color, grass_color.getModifiers() & ~Modifier.FINAL);

        // get the interface
        Class colorResolver = ReflectionHelper.getClass(Minecraft.class.getClassLoader(), COLOR_RESOLVER);

        // get what the field was so it can be wrapped
        Object wrappedResolver = grass_color.get(null);

        // get the version of the method used by the object to be wrapped - avoids exceptions for calling an abstract method
        Class wrappedResolverClass = wrappedResolver.getClass();
        Method wrappedGetColorAtPos = ReflectionHelper.findMethod(wrappedResolverClass, null, GET_COLOR_AT_POS, Biome.class, BlockPos.class);

        // build a proxy
        Method getColorAtPos = ReflectionHelper.findMethod(colorResolver, null, GET_COLOR_AT_POS, Biome.class, BlockPos.class);
        Object proxy = Proxy.newProxyInstance(colorResolver.getClassLoader(), new Class[] { colorResolver }, new GrassHandler(getColorAtPos, wrappedResolver, wrappedGetColorAtPos) );

        // set the field
        grass_color.set(null, proxy);
    }

    private static class GrassHandler implements InvocationHandler {
        public final Method abstractGetColorAtPos;
        public final Object wrappedResolver;
        public final Method wrappedGetColorAtPos;

        public GrassHandler(Method abstractGetColorAtPos, Object wrappedResolver, Method wrappedGetColorAtPos) {
            this.abstractGetColorAtPos = abstractGetColorAtPos;
            this.abstractGetColorAtPos.setAccessible(true);
            this.wrappedResolver = wrappedResolver;
            this.wrappedGetColorAtPos = wrappedGetColorAtPos;

            ATG.logger.info(abstractGetColorAtPos);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.equals(this.abstractGetColorAtPos)) {
                Biome biome = (Biome)args[0];
                BlockPos pos = (BlockPos)args[1];

                World world = Minecraft.getMinecraft().world;

                if (world != null && GeneralUtil.isWorldATG(world)) {
                    // probe the current world biomes to eliminate most cases of it being the wrong one (I hope)
                    Biome probe = world.getBiome(pos);
                    if (probe == biome) {
                        return getGrassColour(world, biome, pos);
                    }
                }

                if (wrappedResolver != null) {
                    return wrappedGetColorAtPos.invoke(this.wrappedResolver, biome, pos);
                }

                return 0xFF00FF;
            }
            return null;
        }
    }

    public static class GrassCacheEntry extends CoordPair {
        public Biome biome = null;

        public GrassCacheEntry(int x, int z) {
            super(x, z);
        }
    }

    public static Map<Integer, CoordCache<GrassCacheEntry>> grassCaches = new HashMap<>();

    public static int getGrassColour(World world, Biome biome, BlockPos pos) {
        CoordCache<GrassCacheEntry> grassCache;
        int dim = world.provider.getDimension();
        if (grassCaches.containsKey(dim)) {
            grassCache = grassCaches.get(dim);
        } else {
            grassCache = new CoordCache<>(256);
            grassCaches.put(dim, grassCache);
        }

        int rad = 5;
        int divisor = (rad*2 + 1);
        divisor *= divisor;

        int r = 0;
        int g = 0;
        int b = 0;

        Map<Biome, Integer> biomeColours = new HashMap<>();
        Biome ib;
        int col,x,z;

        for (BlockPos.MutableBlockPos ipos : BlockPos.getAllInBoxMutable(pos.add(-rad, 0, -rad), pos.add(rad, 0, rad)))
        {
            x = ipos.getX();
            z = ipos.getZ();
            GrassCacheEntry entry = grassCache.get(x,z);
            if (entry == null) {
                entry = new GrassCacheEntry(x,z);
                grassCache.put(x,z,entry);
            }
            if (entry.biome != null) {
                ib = entry.biome;
            } else {
                ib = world.getBiome(ipos);
                entry.biome = ib;
            }

            if (biomeColours.containsKey(ib)) {
                col = biomeColours.get(ib);
            } else {
                col = ib.getGrassColorAtPos(pos);
                biomeColours.put(ib, col);
            }

            r += (col & 0xFF0000) >> 16;
            g += (col & 0x00FF00) >> 8;
            b += (col & 0x0000FF);
        }

        return (r / divisor & 255) << 16 | (g / divisor & 255) << 8 | b / divisor & 255;
    }

    @SideOnly(Side.CLIENT)
    public static class Listener {

        @SubscribeEvent
        public void login(PlayerEvent.PlayerLoggedInEvent event) {
            clearCache(event);
        }

        @SubscribeEvent
        public void logout(PlayerEvent.PlayerLoggedOutEvent event) {
            clearCache(event);
        }

        @SubscribeEvent
        public void changedim(PlayerEvent.PlayerChangedDimensionEvent event) {
            clearCache(event);
        }

        @SubscribeEvent
        public void respawn(PlayerEvent.PlayerRespawnEvent event) {
            clearCache(event);
        }

        public void clearCache(PlayerEvent event) {
            if (event.player.world.isRemote && event.player == Minecraft.getMinecraft().player) {
                ATG.logger.info("Clearing grass colour biome cache");
                //grassCache.clear();
            }
        }
    }
}
