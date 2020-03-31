package me.alexbanper.project.top.Managers;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.alexbanper.project.top.TowerPowerMain;
import me.alexbanper.project.top.Logic.GameLogic;
import me.confuser.barapi.BarAPI;

public class CycleManager {

	public static CycleManager cm = new CycleManager();
	List<String> list = getCycleList();
	String currentMap = null;
	
	public static CycleManager getManager(){
		return cm;
	}
	
	public void checkingForMapsCycle()
	{
		GameManager.getManager().setGameOver(true);
		if(CycleManager.getManager().getNextMap() == "end")
		{
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TowerPowerMain.instance, new Runnable() {

				@Override
				public void run() {
					Bukkit.broadcastMessage("§c§l¡Teletransportando jugadores al Lobby!");
					Location toLobby = new Location(Bukkit.getWorld("Lobby"), 0.434, 65.79427, 0.302, -178, 5);
					for(Player online : Bukkit.getOnlinePlayers())
					{
						online.getPlayer().teleport(toLobby);

						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TowerPowerMain.instance, new Runnable() {

							@Override
							public void run() {
								online.getPlayer().setGameMode(GameMode.SPECTATOR);
							}
						}, 5L);

						online.getPlayer().setGameMode(GameMode.SPECTATOR);
					}
					WorldManager.getManager().resetMaps();
				}
			}, 160L);
		}else{			
			CycleManager.getManager().cycleCounter();
		}
	}
	
	@SuppressWarnings("deprecation")
	public void cycleCounter()
	{
		GameManager.getManager().setTaskIDforCycle(Bukkit.getScheduler().runTaskTimer(TowerPowerMain.instance, new Runnable() {
			int countcycle = 23;
			@Override
			public void run() {
				
				if(countcycle > 1)
				{
					BarAPI.setMessage("§3Cambiando a §b"+CycleManager.getManager().getNextMap()+" §3en §a"+countcycle+" §3segundos");
				}
				
				if(countcycle < 11 && countcycle > 1)
					Bukkit.broadcastMessage("§3Cambiando a §b"+CycleManager.getManager().getNextMap()+" §3en §a"+countcycle+" §3segundos");
				
				if(countcycle == 1)
				{
					BarAPI.setMessage("§3Cambiando a §b"+CycleManager.getManager().getNextMap()+" §3en §a"+countcycle+" §3segundo");
					Bukkit.broadcastMessage("§3Cambiando a §b"+CycleManager.getManager().getNextMap()+" §3en §a"+countcycle+" §3segundo");
				}
					
				
				if(countcycle == 0)
				{
					Bukkit.getScheduler().cancelTask(GameManager.getManager().getTaskCycle());
					GameManager.getManager().setGameOver(true);
					doCycle(CycleManager.getManager().getNextMap());
					GameManager.getManager().removeBossBar();
					
					GameManager.getManager().stopGameBooleans();
					GameLogic.getManager().counterX = 0;
				}else{
					countcycle--;
				}
			}
		}, 1L, 20L).getTaskId());	
	}
	
	public void doCycle(String nextMap)
	{
		if(GameManager.getManager().isGameOver() == true)
		{
			Location nextMapSpawn = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+CycleManager.getManager().getNextMap()+".Teams.Spectator.Spawn");
			
			for(Player online : Bukkit.getOnlinePlayers())
			{
				online.teleport(nextMapSpawn);
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TowerPowerMain.instance, new Runnable() {

					@Override
					public void run() {
						online.getPlayer().setGameMode(GameMode.SPECTATOR);
					}
				}, 7L);
			}
			CycleManager.getManager().setCurrentMap(nextMap);
			GameManager.getManager().setGameOver(false);
			Bukkit.broadcastMessage("Welcome to "+CycleManager.getManager().getCurrentMap());
			return;
		}	
	}

	@SuppressWarnings("unchecked")
	public List<String> getCycleList(){
		return (List<String>) TowerPowerMain.instance.getConfig().getList("Arenas.MapCycling");
	}
	
	public int checkListSize()
	{
		return list.size();
	}
	
	public String getFirstMap()
	{
		String firstMap = list.get(0);
		return firstMap;
	}
	
	public String getMap(int value)
	{
		return list.get(value);
	}
	
	
	public String getCurrentMap()
	{
		return currentMap;
	}

	public void setCurrentMap(String newValue)
	{
		currentMap = newValue;
	}
	
	public String getNextMap()
	{
		int nextMapInt = list.indexOf(currentMap)+1;
		if(nextMapInt < checkListSize())
		{
			String nextMap = list.get(nextMapInt);
			return nextMap;
		}
		return "end";
	}
	
	public void setFirstMap()
	{
		currentMap = getFirstMap();
	}
	
	public boolean checkForMapExists(String mapName)
	{
		if(list.contains(mapName))
			return true;
		return false;
	}
	
	public boolean checkForMapExists(int value)
	{
		if(list.get(value) != null)
			return true;
		return false;
	}
	
	public boolean isLastMap(Player player)
	{
		if(player.getWorld().getName() == getCurrentMap())
			return true;
		return false;
	}
	
	
}
