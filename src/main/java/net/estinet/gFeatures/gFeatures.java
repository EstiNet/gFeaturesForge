package net.estinet.gFeatures;

import net.estinet.gFeatures.ClioteSky.ClioteSky;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = gFeatures.MODID, name = gFeatures.NAME, version = gFeatures.VERSION, serverSideOnly = true, acceptableRemoteVersions = "*")
public class gFeatures {
    static final String MODID = "gfeatures";
    static final String NAME = "gFeatures";
    static final String VERSION = "1.0.0";
    public static boolean DEBUG = false;

    public static Logger logger;

    public static Logger getLogger() {
        return logger;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        ClioteSky.initClioteSky();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // some example code
        logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }
}
