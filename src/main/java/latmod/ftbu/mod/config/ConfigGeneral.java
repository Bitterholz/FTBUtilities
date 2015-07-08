package latmod.ftbu.mod.config;

import java.io.File;

import latmod.ftbu.core.LatCoreMC;
import latmod.ftbu.core.event.FTBUReadmeEvent;
import latmod.ftbu.core.util.LatCore;

import com.google.gson.annotations.Expose;

public class ConfigGeneral
{
	private static File saveFile;
	
	@Expose public Boolean allowCreativeInteractSecure;
	@Expose public String commandFTBU;
	@Expose public String commandAdmin;
	@Expose public Float restartTimer;
	@Expose public Boolean safeSpawn;
	@Expose public Boolean spawnPVP;
	@Expose public Boolean enableDedicatedOnSP;
	@Expose public Integer maxClaims;
	
	public static void load()
	{
		saveFile = new File(LatCoreMC.latmodFolder, "ftbu/general.txt");
		FTBUConfig.general = LatCore.fromJsonFromFile(saveFile, ConfigGeneral.class);
		if(FTBUConfig.general == null) FTBUConfig.general = new ConfigGeneral();
		FTBUConfig.general.loadDefaults();
		save();
	}
	
	public void loadDefaults()
	{
		if(allowCreativeInteractSecure == null) allowCreativeInteractSecure = true;
		if(commandFTBU == null) commandFTBU = "ftbu";
		if(commandAdmin == null) commandAdmin = "admin";
		if(restartTimer == null) restartTimer = 0F;
		if(safeSpawn == null) safeSpawn = false;
		if(spawnPVP == null) spawnPVP = true;
		if(enableDedicatedOnSP == null) enableDedicatedOnSP = false;
		if(maxClaims == null) maxClaims = 16;
	}
	
	public static void save()
	{
		if(FTBUConfig.general == null) load();
		if(!LatCore.toJsonFile(saveFile, FTBUConfig.general))
			LatCoreMC.logger.warn(saveFile.getName() + " failed to save!");
	}

	public static void saveReadme(FTBUReadmeEvent e)
	{
		FTBUReadmeEvent.ReadmeFile.Category general = e.file.get("latmod/ftbu/general.txt");
		general.add("allowCreativeInteractSecure", "If set to true, creative players will be able to access protected chests / chunks.", true);
		general.add("commandFTBU", "Command name for ftbu command.", "ftbu");
		general.add("commandAdmin", "Command name for ftbu command.", "admin");
		general.add("restartTimer", "Server will automatically shut down after X hours. 0 - Disabled, 0.5 - 30 minutes, 1 - 1 Hour, 24 - 1 Day, 168 - 1 Week, 720 - 1 Month, etc.", 0);
		general.add("safeSpawn", "If set to true, explosions and hostile mobs in spawn area will be disabled.", false);
		general.add("spawnPVP", "If set to false, players won't be able to attack each other in spawn area.", true);
		general.add("enableDedicatedOnSP", "Enables server-only features on singleplayer / LAN worlds.", false);
	}
}