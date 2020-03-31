package me.alexbanper.project.top.Events;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import de.domedd.developerapi.messagebuilder.TitleBuilder;
import me.alexbanper.project.top.TowerPowerMain;
import me.alexbanper.project.top.Logic.GameLogic;
import me.alexbanper.project.top.Managers.CycleManager;
import me.alexbanper.project.top.Managers.GameManager;
import me.confuser.barapi.BarAPI;

public class MatchStop {

	public static MatchStop ms = new MatchStop();
	public static MatchStop getManager() {
		return ms;
	}
	
	@SuppressWarnings("deprecation")
	public void StopGame()
	{
		if(GameManager.getManager().getGameStart())
		{
			if(GameManager.getManager().ifCoresIsActive())
			{
				if(GameManager.getManager().isGameOver() == true)
					return;
				
				GameManager.getManager().setGameOver(true);
				int TotalChronometer = GameLogic.getManager().counterX;
				GameManager.getManager().cancelChronometer();
				Bukkit.broadcastMessage("The game ended with " + GameManager.getManager().getWinner() + " like winners!");
				Bukkit.broadcastMessage("Time of game: " + TotalChronometer + " seconds!");
				
				TitleBuilder tb = new TitleBuilder(GameManager.getManager().getWinner()+" wins!", "¡Thanks for playing!")
						.setTimings(3, 75, 3, 4, 64, 4);
				
				BarAPI.setMessage(GameManager.getManager().getWinner()+" wins!");

				for(Player online : Bukkit.getOnlinePlayers())
				{
					online.playSound(online.getLocation(), Sound.ENTITY_WITHER_DEATH, 1, 1);

					tb.send(online);

					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TowerPowerMain.instance, new Runnable() {

						@Override
						public void run() {
							BarAPI.removeBar(online);
						}
					}, 50L);

					
					if(GameManager.getManager().ifTeamRedContains(online.getName()) || GameManager.getManager().ifTeamBlueContains(online.getName()) )
					{
						online.getPlayer().chat("/join s");
					}

					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TowerPowerMain.instance, new Runnable() {

						@Override
						public void run() {
							GameManager.getManager().stopGameBooleans();
						}
					}, 60L);
				}
				CycleManager.getManager().checkingForMapsCycle();
				
			}
		}
	}
	
}
