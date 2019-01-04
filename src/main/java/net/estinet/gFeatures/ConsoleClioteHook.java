package net.estinet.gFeatures;

import net.estinet.gFeatures.ClioteSky.ClioteHook;
import net.estinet.gFeatures.ClioteSky.ClioteSky;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.List;

public class ConsoleClioteHook extends ClioteHook {
    public ConsoleClioteHook(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public void run(byte[] data, String sender) {
        List<String> args = ClioteSky.parseBytesToStringList(data);
        assert args != null;
        String server = args.get(0);
        args.remove(0);
        if (args.size() == 0) return;
        if (server.equals(ClioteSky.name)) return;

        switch (args.get(0)) {
            case "[Join]":
                args.set(0, "§6[§3Join§6]§r");
                break;
            case "[Leave]":
                args.set(0, "§6[§3Leave§6]§r");
                break;
            case "[Switch]":
                args.set(0, "§6[§Switch§6]§r");
                break;
        }

        StringBuilder msg = new StringBuilder();
        for (String arg : args) msg.append(arg).append(" ");
        msg = new StringBuilder(msg.substring(0, msg.length() - 1));
        gFeatures.getLogger().info("[" + server + "] " + msg);

        for (EntityPlayer p : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
            p.sendMessage(new TextComponentString("[" + server + "] " + msg));
        }
    }
}
