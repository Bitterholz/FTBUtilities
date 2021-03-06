package com.feed_the_beast.ftbu.ranks;

import com.feed_the_beast.ftbl.api.IRankConfig;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbu.api.IRank;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Created by LatvianModder on 27.09.2016.
 */
public class DefaultPlayerRank extends AbstractDefaultRank
{
    public static final DefaultPlayerRank INSTANCE = new DefaultPlayerRank();

    @Override
    public String getName()
    {
        return "player";
    }

    @Override
    public IRank getParent()
    {
        return EmptyRank.INSTANCE;
    }

    @Override
    public Event.Result hasPermission(String permission)
    {
        return Event.Result.DEFAULT;
    }

    @Override
    public String getDisplayName()
    {
        return "Player";
    }

    @Override
    public TextFormatting getColor()
    {
        return TextFormatting.WHITE;
    }

    @Override
    public String getPrefix()
    {
        return "";
    }

    @Override
    IConfigValue createValue(IRankConfig config)
    {
        return config.getDefValue().copy();
    }
}