package me.alexbanper.project.top.Managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import me.alexbanper.project.top.TowerPowerMain;


public class ScoreboardManager {

	public static ScoreboardManager sman = new ScoreboardManager();

	public static ScoreboardManager getManager()
	{
		return sman;
	}

	org.bukkit.scoreboard.ScoreboardManager m = Bukkit.getScoreboardManager();
	Scoreboard b = m.getNewScoreboard();
	Scoreboard spc = m.getNewScoreboard();

	public void setupScoreboardGame()
	{
		Objective o = b.registerNewObjective("GameObjs", "dummy");
		o.setDisplaySlot(DisplaySlot.SIDEBAR);

		Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(TowerPowerMain.instance, new Runnable() {
			@Override
			public void run() {
					o.setDisplayName("§b§lObjetivos");

					Score blueTeam = o.getScore("§9§lAzul");
					blueTeam.setScore(9);

					Score Tower1b = o.getScore("§l⚪ §eBlue Tower 1"); //⚫⚫⚫ ✔✔ 
					Tower1b.setScore(8);
					Score Tower2b = o.getScore("§l⚪ §eBlue Tower 2"); 
					Tower2b.setScore(7);
					Score blueCore = o.getScore("§l◇ §eBlue Core"); 
					blueCore.setScore(6);

					Score spacer = o.getScore(" ");
					spacer.setScore(5);

					Score redTeam = o.getScore("§4§lRojo");
					redTeam.setScore(4);
					Score Tower1r = o.getScore("§l⚪ §eRed Tower 1");  
					Tower1r.setScore(3);
					Score Tower2r = o.getScore("§l⚪ §eRed Tower 2");
					Tower2r.setScore(2);
					Score redCore = o.getScore("§l◇ §eRed Core"); //◆◆◆
					redCore.setScore(1);
			}
		}, 20L, 20L);

		for(Player online : Bukkit.getOnlinePlayers())
		{
			if(GameManager.getManager().bothTeamsContains(online.getName()))
			online.setScoreboard(b);
		}
	}

	public void setupScoreBoardForSpectators()
	{
		Objective o = spc.registerNewObjective("SpectObj", "dummy");
		o.setDisplaySlot(DisplaySlot.SIDEBAR);
		o.setDisplayName("§d§lWaiting Players...");

		Score infoLine = o.getScore("Usa §b/join §fpara unirte");
		infoLine.setScore(5);

		Score currentMap = o.getScore("§e§lMapa Actual: §6"+CycleManager.getManager().getCurrentMap());
		currentMap.setScore(4);

		Score nextMap = o.getScore("§e§lNext Map: §6"+CycleManager.getManager().getNextMap());
		nextMap.setScore(3);
		
		for(Player online : Bukkit.getOnlinePlayers())
		{
			online.setScoreboard(spc);
		}
	}

}
