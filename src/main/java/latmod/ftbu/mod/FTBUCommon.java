package latmod.ftbu.mod;

import java.util.UUID;

import latmod.ftbu.core.*;
import latmod.ftbu.core.api.IFTBUReloadable;
import latmod.ftbu.core.event.FTBUReadmeEvent;
import latmod.ftbu.core.net.*;
import latmod.ftbu.core.tile.TileLM;
import latmod.ftbu.core.world.*;
import latmod.ftbu.mod.config.FTBUConfig;
import latmod.ftbu.mod.player.*;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityEvent;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;

public class FTBUCommon implements IFTBUReloadable // FTBUClient
{
	public void preInit()
	{
	}
	
	public void postInit()
	{
	}
	
	public void onReloaded(Side s, ICommandSender sender) throws Exception
	{
		if(s.isServer())
		{
			float prevRRTimer = FTBUConfig.general.restartTimer.floatValue();
			
			FTBUConfig.instance.load();
			if(FTBUConfig.general.isDedi())
			{
				for(EntityPlayerMP ep : LatCoreMC.getAllOnlinePlayers())
					IServerConfig.Registry.updateConfig(ep, null);
				
				if(prevRRTimer != FTBUConfig.general.restartTimer.floatValue())
					FTBUTickHandler.serverStarted();
			}
		}
	}
	
	public void onReadmeEvent(FTBUReadmeEvent e)
	{
	}
	
	public boolean isShiftDown() { return false; }
	public boolean isCtrlDown() { return false; }
	public boolean isTabDown() { return false; }
	public boolean inGameHasFocus() { return true; }
	
	public EntityPlayer getClientPlayer()
	{ return null; }
	
	public EntityPlayer getClientPlayer(UUID id)
	{ return null; }
	
	public World getClientWorld()
	{ return null; }
	
	public LMWorld<?> getClientWorldLM()
	{ return null; }
	
	public double getReachDist(EntityPlayer ep)
	{
		if(ep != null && ep instanceof EntityPlayerMP)
			return ((EntityPlayerMP)ep).theItemInWorldManager.getBlockReachDistance();
		return 0D;
	}
	
	public void spawnDust(World w, double x, double y, double z, int col) { }
	public boolean openClientGui(EntityPlayer ep, String mod, int id, NBTTagCompound data) { return false; }
	public <M extends MessageLM<?>> void handleClientMessage(IClientMessageLM<M> m, MessageContext ctx) { }
	public void readTileData(TileLM t, S35PacketUpdateTileEntity p) { }
	
	public void chunkChanged(EntityEvent.EnteringChunk e)
	{
		if(e.entity instanceof EntityPlayerMP)
		{
			EntityPlayerMP ep = (EntityPlayerMP)e.entity;
			LMPlayerServer p = LMWorldServer.inst.getPlayer(ep);
			if(p == null) return;
			
			if(p.lastPos == null) p.lastPos = new EntityPos(ep);
			
			else if(!p.lastPos.equalsPos(ep))
			{
				if(Claims.isOutsideWorldBorderD(ep.dimension, ep.posX, ep.posZ))
				{
					ep.motionX = ep.motionY = ep.motionZ = 0D;
					IChatComponent warning = new ChatComponentTranslation("ftbu:chunktype." + ChunkType.WORLD_BORDER.lang + ".warning");
					warning.getChatStyle().setColor(EnumChatFormatting.RED);
					LatCoreMC.notifyPlayer(ep, new Notification("world_border", warning, 3000));
					
					if(Claims.isOutsideWorldBorderD(p.lastPos.dim, p.lastPos.x, p.lastPos.z))
					{
						LatCoreMC.printChat(ep, "Teleporting to spawn!");
						FTBUEventHandler.instance.teleportToSpawn(ep);
					}
					else LMDimUtils.teleportPlayer(ep, p.lastPos);
					ep.worldObj.playSoundAtEntity(ep, "random.fizz", 1F, 1F);
				}
				
				p.lastPos.set(ep);
			}
			
			int currentChunkType = ChunkType.getChunkTypeI(ep.dimension, e.newChunkX, e.newChunkZ, p);
			
			if(p.lastChunkType == -99 || p.lastChunkType != currentChunkType)
			{
				p.lastChunkType = currentChunkType;
				
				ChunkType type = ChunkType.getChunkTypeFromI(currentChunkType, p);
				IChatComponent msg = null;
				
				if(type.isClaimed())
					msg = new ChatComponentText("" + LMWorldServer.inst.getPlayer(currentChunkType));
				else
					msg = new ChatComponentTranslation("ftbu:chunktype." + type.lang);
				
				//msg.getChatStyle().setColor(type.chatColor);
				
				Notification n = new Notification("chunk_changed", msg, 3000);
				n.setColor(type.areaColor);
				
				LatCoreMC.notifyPlayer(ep, n);
				LMNetHelper.sendTo(ep, new MessageAreaUpdate(e.newChunkX, e.newChunkZ, ep.dimension, currentChunkType));
			}
		}
	}
}