package net.estinet.gFeatures;

import net.estinet.gFeatures.ClioteSky.ClioteHook;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.nio.charset.StandardCharsets;

public class DisplayMessageClioteHook extends ClioteHook {
    public DisplayMessageClioteHook(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public void run(byte[] data, String sender) {
        String disp = new String(data, StandardCharsets.UTF_8);

        gFeatures.getLogger().info(disp);

        for (EntityPlayer p : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
            p.sendMessage(new TextComponentString(disp));
        }
    }
}
