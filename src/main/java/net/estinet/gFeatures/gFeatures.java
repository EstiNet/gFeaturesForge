package net.estinet.gFeatures;

import net.estinet.gFeatures.ClioteSky.ClioteSky;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = gFeatures.MODID, name = gFeatures.NAME, version = gFeatures.VERSION, serverSideOnly = true, acceptableRemoteVersions = "*")
public class gFeatures {

    @Config(modid = gFeatures.MODID, name = gFeatures.MODID + "_map")
    public static class gFeaturesConfig {
        @Config.Comment("ClioteSky Config")
        public static String clioteSkyName = "";
        public static String clioteSkyCategory = "";
        public static String clioteSkyAddress = "";
        public static String clioteSkyPort = "";
        public static boolean clioteSkyCheckTLS = false;
    }

    static final String MODID = "gfeatures";
    static final String NAME = "gFeatures";
    static final String VERSION = "1.0.0f";
    public static boolean DEBUG = false;
    public static String estiChatLastSent = "";

    public static Logger logger;

    public static Logger getLogger() {
        return logger;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        logger.info("Initializing gFeatures " + VERSION + "...");

        ClioteSky.addHook(new ConsoleClioteHook("consolechat"));
        logger.info("Initialized gFeatures.");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // later
        logger.info("Starting gFeatures " + VERSION + "...");
        logger.info("Connecting to ClioteSky...");
        ClioteSky.initClioteSky();
        MinecraftForge.EVENT_BUS.register(new gFeatures());
        logger.info("Started gFeatures.");
    }

    // EstiChat port
    @SubscribeEvent
    public void chat(ServerChatEvent event) {
        ClioteSky.getInstance().sendAsync(ClioteSky.stringToBytes(event.getPlayer().getName() + " <" + event.getPlayer().getDisplayName() + "> " + event.getMessage()), "chat", "Bungee");
        estiChatLastSent = event.getMessage();
    }

    @SubscribeEvent
    public void join(PlayerEvent.PlayerLoggedInEvent event) {
        ClioteSky.getInstance().sendAsync(ClioteSky.stringToBytes(event.player.getName() + " §6[§3Join§6] §r" + event.player.getDisplayNameString()), "chat", "Bungee");
    }

    @SubscribeEvent
    public void leave(PlayerEvent.PlayerLoggedOutEvent event) {
        ClioteSky.getInstance().sendAsync(ClioteSky.stringToBytes(event.player.getName() + " §6[§3Leave§6] §r" + event.player.getDisplayNameString()), "chat", "Bungee");
    }
}
