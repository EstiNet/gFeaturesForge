package net.estinet.gFeatures;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class HubCommand extends CommandBase {
    @Override
    public String getName() {
        return "hub";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "Connect directly to estinet.net to play other gamemodes!";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        sender.sendMessage(new TextComponentString("Server switching is currently not supported. Connect directly to estinet.net to play other gamemodes!"));
    }
}
