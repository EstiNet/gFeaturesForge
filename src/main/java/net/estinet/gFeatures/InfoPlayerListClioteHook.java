package net.estinet.gFeatures;

import net.estinet.gFeatures.ClioteSky.ClioteHook;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentString;

import java.nio.charset.StandardCharsets;

public class InfoPlayerListClioteHook extends ClioteHook {
    public InfoPlayerListClioteHook(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public void run(byte[] data, String sender) {
        String output = "";
        String rec = new String(data, StandardCharsets.UTF_8);
        int num = 0;
        for (String server : rec.split("§")) {
            for (String p : rec.split("€")) {
                num++;
            }
        }
        if (num == 0) {
            output = "§6--------§3There are no players on right now.§6--------" + output;
        }
        // TODO

        for (ICommandSender s : gFeatures.playersWaitingForList) {
            s.sendMessage(new TextComponentString(output));
        }
    }
}
