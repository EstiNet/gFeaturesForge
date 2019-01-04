package net.estinet.gFeatures;

import net.estinet.gFeatures.ClioteSky.ClioteSky;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class ListCommand extends CommandBase {
    @Override
    public String getName() {
        return "list";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "Lists online players on EstiNet.";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        gFeatures.playersWaitingForList.add(sender);
        ClioteSky.getInstance().sendAsync(ClioteSky.stringToBytes("playerlist"), "info", "Bungee");
    }
}
