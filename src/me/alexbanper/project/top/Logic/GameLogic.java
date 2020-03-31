package me.alexbanper.project.top.Logic;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import me.alexbanper.project.top.TowerPowerMain;
import me.alexbanper.project.top.Managers.ArenaManager;
import me.alexbanper.project.top.Managers.GameManager;
import me.confuser.barapi.BarAPI;

public class GameLogic {

	public static TowerPowerMain plugin;
	public static GameLogic gl = new GameLogic();
	public GameManager gm = new GameManager();
	public static int counter = 0;

	public static GameLogic getManager(){
		return gl;
	}

	public boolean checkPlayersToStart()
	{

		if((GameManager.getManager().getTeamBlueSize() >= 1) && (GameManager.getManager().getTeamRedSize() >= 1))
		{
			GameManager.getManager().setCanStart(true);
			return true;
		}

		return false;
	}

	public boolean canStartGame()
	{
		if(GameManager.getManager().getCanStart())
			return true;

		return false;
	}

	public void CountForStartGame(Player p, int countdownTime)
	{
		counter = countdownTime;
		GameManager.getManager().setCountForStart(Bukkit.getScheduler().runTaskTimer(TowerPowerMain.instance, new Runnable() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				if(!GameManager.getManager().getGameStart())
				{
					GameManager.getManager().setGameCountdownStart(true);

					if(counter >= 2)
					{
						BarAPI.setMessage("§aLa Partida comienza en §6"+counter+" §asegundos!");
						Bukkit.broadcastMessage("§aLa Partida comienza en §6"+counter+" §asegundos!");
					}

					if(counter == 1)
					{
						Bukkit.broadcastMessage("§aLa Partida comienza en §6"+counter+" §asegundo!");
						BarAPI.setMessage(p, "§aLa Partida comienza en §6"+counter+" §asegundo!");
					}


					if(counter == 0)
					{
						Bukkit.getScheduler().cancelTask(GameManager.getManager().getCountForStart());
						Bukkit.broadcastMessage("§aHa comenzado el juego!");

						SpawnTeams(p);
						startChronometer();

						GameManager.getManager().setGameStart(true);
					}else{
						counter--;
					}
				}
			}
		}, 1L, 20L).getTaskId());
	}

	public void SpawnPlayer(Player player)
	{
		if(GameManager.getManager().ifTeamBlueContains(player.getName()))
		{
			Location spectatorSpawn = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+player.getWorld().getName()+".Teams.Blue.Spawn");
			player.teleport(spectatorSpawn);
			player.setGameMode(GameMode.SURVIVAL);
			player.sendMessage("§a¡Bienvenido al area de juego §9"+player.getName()+"§a!");
			player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_TWINKLE_FAR, 1, 1);
		}
		if(GameManager.getManager().ifTeamRedContains(player.getName()))
		{
			Location spectatorSpawn = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+player.getWorld().getName()+".Teams.Red.Spawn");
			player.teleport(spectatorSpawn);
			player.setGameMode(GameMode.SURVIVAL);
			player.sendMessage("§a¡Bienvenido al area de juego §4"+player.getName()+"§a!");
			player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_TWINKLE_FAR, 1, 1);
		}
	}
	
	
	public void SpawnTeams(Player player)
	{
		Bukkit.getScheduler().runTaskLater(TowerPowerMain.instance, new Runnable() {
			@Override
			public void run() {
				for(Player online : Bukkit.getOnlinePlayers())
				{
					if(GameManager.getManager().ifTeamBlueContains(online.getName()))
					{
						Location spectatorSpawn = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+player.getWorld().getName()+".Teams.Blue.Spawn");
						online.teleport(spectatorSpawn);
						online.setGameMode(GameMode.SURVIVAL);
						online.sendMessage("§a¡Bienvenido al area de juego §9"+online.getName()+"§a!");
						online.playSound(online.getLocation(), Sound.ENTITY_FIREWORK_TWINKLE_FAR, 1, 1);
					}
					if(GameManager.getManager().ifTeamRedContains(online.getName()))
					{
						Location spectatorSpawn = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+player.getWorld().getName()+".Teams.Red.Spawn");
						online.teleport(spectatorSpawn);
						online.setGameMode(GameMode.SURVIVAL);
						online.sendMessage("§a¡Bienvenido al area de juego §4"+online.getName()+"§a!");
						online.playSound(online.getLocation(), Sound.ENTITY_FIREWORK_TWINKLE_FAR, 1, 1);
					}
				}
			}
		}, 3L);
	}


	public int counterX = 0;
	@SuppressWarnings({ "deprecation" })
	public void startChronometer()
	{
		GameManager.getManager().setGameChronometerCount(Bukkit.getScheduler().scheduleAsyncRepeatingTask(TowerPowerMain.instance, new Runnable(){
			
			@Override
			public void run()
			{
				if(counterX > 2)
				{
					ArenaManager.getManager().checkForTowers();
				}

				counterX++;
				for(Player online : Bukkit.getOnlinePlayers())
				{
					BarAPI.setMessage(online, "§bTiempo en Partida: §6"+counterX+ " §bsegundos");
				}
			}
		}, 1L, 25L));
	}


	

	

	
	/*public void PauseGame()
	{

	}

	public void RestartGame()
	{

	}*/




}
