package net.estinet.gFeatures;

import net.estinet.gFeatures.ClioteSky.ClioteSky;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Mod(modid = gFeatures.MODID, name = gFeatures.NAME, version = gFeatures.VERSION, serverSideOnly = true, acceptableRemoteVersions = "*")
public class gFeatures {

    public static List<ICommandSender> playersWaitingForList = new ArrayList<>();

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
        ClioteSky.addHook(new DisplayMessageClioteHook("displaymessage"));
        ClioteSky.addHook(new InfoPlayerListClioteHook("info playerlist"));
        logger.info("Initialized gFeatures.");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // later
        logger.info("Starting gFeatures " + VERSION + "...");
        logger.info("Connecting to ClioteSky...");
        ClioteSky.initClioteSky();
        MinecraftForge.EVENT_BUS.register(new gFeatures());
        updatePlayerList();
        logger.info("Started gFeatures.");
    }

    @Mod.EventHandler
    public static void init(FMLServerStartingEvent event) {
        event.registerServerCommand(new ListCommand());
        event.registerServerCommand(new HubCommand());
    }

    // EstiChat port
    @SubscribeEvent
    public void chat(ServerChatEvent event) {
        ClioteSky.getInstance().sendAsync(ClioteSky.stringToBytes(event.getPlayer().getName() + " <" + event.getPlayer().getDisplayName() + "> " + event.getComponent().getUnformattedText()), "chat", "Bungee");
        estiChatLastSent = event.getComponent().getUnformattedText();
    }

    @SubscribeEvent
    public void join(PlayerEvent.PlayerLoggedInEvent event) {
        ClioteSky.getInstance().sendAsync(ClioteSky.stringToBytes(event.player.getName() + " §6[§3Join§6] §r" + event.player.getDisplayNameString()), "chat", "Bungee");
        updatePlayerList();
    }

    @SubscribeEvent
    public void leave(PlayerEvent.PlayerLoggedOutEvent event) {
        ClioteSky.getInstance().sendAsync(ClioteSky.stringToBytes(event.player.getName() + " §6[§3Leave§6] §r" + event.player.getDisplayNameString()), "chat", "Bungee");
        updatePlayerList();
    }

    public static void updatePlayerList() {
        StringBuilder cliMsg = new StringBuilder();
        for (EntityPlayer p : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
            cliMsg.append(p.getName()).append("§");
        }
        ClioteSky.getInstance().sendAsync(ClioteSky.stringToBytes("update " + cliMsg.substring(0, cliMsg.length() - 1)), "fakeplayer", "Bungee"); // update player list
    }
}
