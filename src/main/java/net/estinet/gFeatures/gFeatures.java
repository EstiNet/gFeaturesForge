package net.estinet.gFeatures;

import net.estinet.gFeatures.ClioteSky.ClioteSky;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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
        @Config.Comment("Other Config")
        public static boolean isUsingProxy = true;
        public static boolean rcOnJoin = false;
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
        logger.info("Initialized gFeatures.");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // later
        logger.info("Starting gFeatures " + VERSION + "...");
        logger.info("Connecting to ClioteSky...");

        if (!gFeaturesConfig.isUsingProxy) {
            ClioteSky.addHook(new ConsoleClioteHook("consolechat"));
            ClioteSky.addHook(new DisplayMessageClioteHook("displaymessage"));
            ClioteSky.addHook(new InfoPlayerListClioteHook("info playerlist"));
        }

        ClioteSky.initClioteSky();
        MinecraftForge.EVENT_BUS.register(new gFeatures());
        logger.info("Started gFeatures.");
    }

    @Mod.EventHandler
    public static void starting(FMLServerStartingEvent event) {
        // TODO permissions event.registerServerCommand(new ListCommand());
        // TODO event.registerServerCommand(new HubCommand());
    }

    @Mod.EventHandler
    public static void started(FMLServerStartedEvent event) {
        if (event.getSide().isServer()) updatePlayerList();
    }

    // EstiChat port
    @SubscribeEvent
    public void chat(ServerChatEvent event) {
        ClioteSky.getInstance().sendAsync(ClioteSky.stringToBytes(event.getPlayer().getName() + " " + event.getComponent().getUnformattedText()), "chat", "Bungee");
        estiChatLastSent = event.getComponent().getUnformattedText();
    }

    @SubscribeEvent
    public void join(PlayerEvent.PlayerLoggedInEvent event) {
        ClioteSky.getInstance().sendAsync(ClioteSky.stringToBytes(event.player.getName() + " §6[§3Join§6] §r" + event.player.getDisplayNameString()), "chat", "Bungee");
        if (!gFeaturesConfig.isUsingProxy) updatePlayerList();
        if (gFeaturesConfig.rcOnJoin) {
            EntityPlayer p = event.player;
            NBTTagCompound entityData = p.getEntityData();
            if (!entityData.getBoolean("gFeatures.firstJoin")) {
                entityData.setBoolean("gFeatures.firstJoin", true);
                FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager().executeCommand(p, "rtp");
            }
        }
    }

    @SubscribeEvent
    public void leave(PlayerEvent.PlayerLoggedOutEvent event) {
        ClioteSky.getInstance().sendAsync(ClioteSky.stringToBytes(event.player.getName() + " §6[§3Leave§6] §r" + event.player.getDisplayNameString()), "chat", "Bungee");
        if (!gFeaturesConfig.isUsingProxy) updatePlayerList(event.player.getName());
    }

    @SideOnly(Side.SERVER)
    public static void updatePlayerList(String... omit) {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        StringBuilder cliMsg = new StringBuilder();
        for (String p : server.getOnlinePlayerNames()) {
            boolean skip = false;
            for (String o : omit) {
                if (o.equals(p)) {
                    skip = true;
                    break;
                }
            }
            if (skip) continue;
            cliMsg.append(p).append("§");
        }
        if (cliMsg.length() == 0) cliMsg.append("d"); // fix for substring crash on empty
        ClioteSky.getInstance().sendAsync(ClioteSky.stringToBytes("update " + cliMsg.substring(0, cliMsg.length() - 1)), "fakeplayer", "Bungee"); // update player list
    }
}
