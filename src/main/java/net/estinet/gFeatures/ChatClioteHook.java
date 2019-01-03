package net.estinet.gFeatures;

import net.estinet.gFeatures.ClioteSky.ClioteHook;

public class ChatClioteHook extends ClioteHook {
    public ChatClioteHook(String identifier, String gFeatureName) {
        this.identifier = identifier;
        this.gFeatureName = gFeatureName;
    }
    @Override
    public void run(byte[] data, String sender) {

    }
}
