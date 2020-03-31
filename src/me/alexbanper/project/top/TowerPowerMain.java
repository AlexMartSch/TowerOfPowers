package me.alexbanper.project.top;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import me.alexbanper.project.top.Commands.createArenaCommand;
import me.alexbanper.project.top.Commands.joinCommand;
import me.alexbanper.project.top.Commands.leaveCommand;
import me.alexbanper.project.top.Commands.startCommand;
import me.alexbanper.project.top.Commands.stopCommand;
import me.alexbanper.project.top.Listeners.GameListener;
import me.alexbanper.project.top.Listeners.ChatListener;
import me.alexbanper.project.top.Managers.CycleManager;
import me.alexbanper.project.top.Managers.ScoreboardManager;
import me.alexbanper.project.top.Managers.TabManager;
import me.alexbanper.project.top.Managers.TeamManager;
import me.confuser.barapi.BarAPI;

public class TowerPowerMain extends JavaPlugin{

	Plugin p;
	public static TowerPowerMain instance;

	@Override
	public void onEnable(){
		instance = this;

		setupConfiguration();
		registerEvents();
		registerCommands();
		setupGame();
	}
	public void registerEvents()
	{
		getServer().getPluginManager().registerEvents(new GameListener(this), this);
		getServer().getPluginManager().registerEvents(new ChatListener(), this);
		getServer().getPluginManager().registerEvents(new TeamManager(), this);
	}
	
	public void registerCommands()
	{
		getCommand("join").setExecutor(new joinCommand());
		getCommand("leave").setExecutor(new leaveCommand());
		
		getCommand("start").setExecutor(new startCommand());
		getCommand("end").setExecutor(new stopCommand());

		getCommand("createarena").setExecutor(new createArenaCommand());
		getCommand("setarenaspawn").setExecutor(new createArenaCommand());
		getCommand("setwall").setExecutor(new createArenaCommand()); 
		getCommand("settower").setExecutor(new createArenaCommand()); 
		getCommand("removearena").setExecutor(new createArenaCommand()); 
		getCommand("setarea").setExecutor(new createArenaCommand());
		getCommand("setcore").setExecutor(new createArenaCommand());
	}
	
	public void setupGame()
	{
		setAllPlayersToSpectator();
		setAllPlayersToSpectator();
		TabManager.getManager().setTabList();
		CycleManager.getManager().setFirstMap();
		ScoreboardManager.getManager().setupScoreBoardForSpectators();
	}
	
	@SuppressWarnings("deprecation")
	public void setAllPlayersToSpectator()
	{
		for (Player online : Bukkit.getOnlinePlayers())
		{
			BarAPI.removeBar(online);
			Location toLobby = new Location(Bukkit.getWorld("Lobby"), 0.434, 65.79427, 0.302, -178, 5);
			online.getPlayer().teleport(toLobby);
			
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TowerPowerMain.instance, new Runnable() {

				@Override
				public void run() {
					online.chat("/join s");
				}
			}, 10L);
		}
	}

	public void setupConfiguration()
	{
		saveDefaultConfig();
		if(!this.getDataFolder().exists())
		{
			this.getDataFolder().mkdir();
			getConfig().options().copyDefaults(true);
		}
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

	@Override
	public void onDisable(){
		saveConfig();
	}
}
