package me.alexbanper.project.top.Events;

import org.bukkit.entity.Player;

import me.alexbanper.project.top.Logic.GameLogic;
import me.alexbanper.project.top.Managers.GameManager;

public class MatchStart {

	public static MatchStart mse = new MatchStart();
	
	public static MatchStart getMannager()
	{
		return mse;
	}
	
	public void forceStartGame(Player p)
	{
		GameLogic.getManager().CountForStartGame(p, 10);
		return;
	}
	
	public void StartGame(Player p)
	{
		if(GameManager.getManager().isGameOver() == false)
		{
			if(GameManager.getManager().getGameStart())
			{
				GameLogic.getManager().SpawnPlayer(p);
				return;
			}

			if(GameLogic.getManager().checkPlayersToStart() && GameLogic.getManager().canStartGame())
			{
				if(!GameManager.getManager().getIfStartGameCountdown())
				{
					GameLogic.getManager().CountForStartGame(p, 10);
				}
			}else{
				p.sendMessage("§7[§eINFO§7] §c¡Faltan jugadores para iniciar!");
			}
		}
	}
	
}
