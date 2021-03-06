package com.feed_the_beast.ftbu.ranks;

import com.feed_the_beast.ftbu.api_impl.FTBUtilitiesAPI_Impl;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by LatvianModder on 21.02.2016.
 */
public class CmdOverride implements ICommand
{
    public final ICommand parent;
    public final String permissionNode;

    public CmdOverride(ICommand c, String pn)
    {
        parent = c;
        permissionNode = pn;
    }

    @Override
    public String getCommandName()
    {
        return parent.getCommandName();
    }

    @Override
    public String getCommandUsage(ICommandSender sender)
    {
        return parent.getCommandName();
    }

    @Override
    public List<String> getCommandAliases()
    {
        return parent.getCommandAliases();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        parent.execute(server, sender, args);
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        if(sender instanceof EntityPlayerMP)
        {
            Event.Result result = FTBUtilitiesAPI_Impl.INSTANCE.getRank(((EntityPlayerMP) sender).getGameProfile()).hasPermission(permissionNode);
            return result == Event.Result.DEFAULT ? parent.checkPermission(server, sender) : (result == Event.Result.ALLOW);
        }

        return parent.checkPermission(server, sender);
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        return parent.getTabCompletionOptions(server, sender, args, pos);
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index)
    {
        return parent.isUsernameIndex(args, index);
    }

    @Override
    public int compareTo(ICommand o)
    {
        return getCommandName().compareToIgnoreCase(o.getCommandName());
    }
}
