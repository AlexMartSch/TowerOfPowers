package me.alexbanper.project.top.Managers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import me.alexbanper.project.top.TowerPowerMain;
import me.alexbanper.project.top.Events.MatchStart;
import me.confuser.barapi.BarAPI;

public class GameManager {

	public static TowerPowerMain plugin;
	public static GameManager am = new GameManager();
	private String currentMap = CycleManager.getManager().getCurrentMap();

	/*
	 * SETUP TEAMS
	 */
	private HashMap<String, String> TeamBlue = new HashMap<String, String>();
	private HashMap<String, String> TeamRed = new HashMap<String, String>();
	private HashMap<String, String> TeamSpectator = new HashMap<String, String>();
	
	Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
	Team team = null;

	/*
	 * SETUP WINNER
	 */
	private boolean redWin = false;
	private boolean blueWin = false;
	private String playerBreakTower = null;

	/*
	 * GAME BOOLEANS
	 */
	private boolean gameStart = false;
	private boolean canStart = false;
	public boolean alreadyAnnounceTowerFalls = false;
	private boolean coreActiveBlue = false;
	private boolean coreActiveRed = false;
	private boolean gameOver = false;

	/*
	 * COUNTDOWNS AND TIMERS
	 */
	private boolean gameCountdownStart = false;
	public int gameCountdown = 0;
	private boolean gameChronometerStart = false;
	private int gameChronometerCount = 0;
	private int TaskCycle = 0;

	/*
	 * SETUP MAIN COUNTERS
	 */
	private int CountForStart = 0;
	public int MainTaskID = 0;

	/*
	 * SETUP TOWERS
	 */
	private boolean Tower1b = true;
	private boolean Tower2b = true;
	private boolean Tower1r = true;
	private boolean Tower2r = true;
	private Location middleTower1b;
	private Location middleTower2b;
	private Location middleTower1r;
	private Location middleTower2r;
	private Location middleCoreBlue;
	private Location middleCoreRed;

	public static GameManager getManager(){
		return am;
	}

	public boolean ifCoresIsActive()
	{
		if(coreActiveBlue || coreActiveRed)
			return true;
		return false;
	}

	@SuppressWarnings("deprecation")
	public void removeBossBar()
	{
		for(Player online : Bukkit.getOnlinePlayers())
		{
			BarAPI.removeBar(online);
		}
	}
	
	public boolean bothTeamsContains(String value)
	{
		if(ifTeamRedContains(value) && ifTeamBlueContains(value))
			return true;
		return false;
	}
	
	public void stopGameBooleans()
	{
		setAllTowerTo(true);
		CountForStart = 0;
		MainTaskID = 0;
		gameCountdown = 0;
		gameChronometerCount = 0;
		gameCountdownStart = false;
		gameChronometerStart = false;
		gameStart = false;
		canStart = false;
		alreadyAnnounceTowerFalls = false;
		coreActiveBlue = false;
		coreActiveRed = false;
		gameOver = false;
		redWin = false;
		blueWin = false;
		playerBreakTower = null;
	}

	private void setAllTowerTo(boolean value)
	{
		Tower1b = value;
		Tower2b = value;
		Tower1r = value;
		Tower2r = value;
	}

	public void calculateMiddle(String arenaName, String Tower, Location min, Location max)
	{
		if(Tower == "coreblue")
		{
			middleCoreBlue = min.clone().add(max.clone()).multiply(0.5);
			middleCoreBlue.setY(middleCoreBlue.getY() + 8);
			TowerPowerMain.instance.getConfig().set("Arenas."+arenaName+".Teams.Blue.Core.Middle", middleCoreBlue);
			return;
		}

		if(Tower == "corered")
		{
			middleCoreRed = min.clone().add(max.clone()).multiply(0.5);
			middleCoreRed.setY(middleCoreRed.getY() + 8);
			TowerPowerMain.instance.getConfig().set("Arenas."+arenaName+".Teams.Red.Core.Middle", middleCoreRed);
			return;
		}

		if(Tower == "1b")
		{
			middleTower1b = min.clone().add(max.clone()).multiply(0.5);
			middleTower1b.setY(middleTower1b.getY() + 10);
			TowerPowerMain.instance.getConfig().set("Arenas."+arenaName+".Teams.Blue.Tower.1.Middle", middleTower1b);
			return;
		}
		if(Tower == "2b")
		{
			middleTower2b = min.clone().add(max.clone()).multiply(0.5);
			middleTower2b.setY(middleTower2b.getY() + 10);
			TowerPowerMain.instance.getConfig().set("Arenas."+arenaName+".Teams.Blue.Tower.2.Middle", middleTower2b);
			return;
		}
		if(Tower == "1r")
		{
			middleTower1r = min.clone().add(max.clone()).multiply(0.5);
			middleTower1r.setY(middleTower1r.getY() + 10);
			TowerPowerMain.instance.getConfig().set("Arenas."+arenaName+".Teams.Red.Tower.1.Middle", middleTower1r);
			return;
		}
		if(Tower == "2r")
		{
			middleTower2r = min.clone().add(max.clone()).multiply(0.5);
			middleTower2r.setY(middleTower2r.getY() + 10);
			TowerPowerMain.instance.getConfig().set("Arenas."+arenaName+".Teams.Red.Tower.2.Middle", middleTower2r);
			return;
		}
	}

	public Location getMiddleTower1b()
	{
		return middleTower1b;

	}

	public Location getMiddleTower2b()
	{
		return middleTower2b;

	}

	public Location getMiddleTower1r()
	{
		return middleTower1r;

	}

	public Location getMiddleTower2r()
	{
		return middleTower2r;

	}

	public Object TeamSpectatorGetKey(String key)
	{
		return TeamSpectator.get(key);
	}

	public Set<String> TeamSpectatorKeySet()
	{
		return TeamSpectator.keySet();
	}

	public Set<String> TeamBlueKeySet()
	{
		return TeamBlue.keySet();
	}

	public Set<String> TeamRedKeySet()
	{
		return TeamRed.keySet();
	}

	public boolean ifTeamSpectatorContains(String key)
	{
		if(TeamSpectator.containsKey(key))
			return true;
		return false;
	}

	public boolean ifTeamBlueContains(String Key)
	{
		if(TeamBlue.containsKey(Key))
			return true;
		return false;
	}

	public boolean ifTeamRedContains(String Key)
	{
		if(TeamRed.containsKey(Key))
			return true;
		return false;
	}

	public int getTeamSpectatorSize()
	{
		return TeamSpectator.size();
	}

	public int getTeamBlueSize()
	{
		return TeamBlue.size();
	}

	public int getTeamRedSize()
	{
		return TeamRed.size();
	}

	public void setCoreActiveBlue(boolean value)
	{
		coreActiveBlue = value;
	}

	public boolean getCoreActiveBlue()
	{
		return coreActiveBlue;
	}

	public void setCoreActiveRed(boolean value)
	{
		coreActiveRed = value;
	}

	public boolean getCoreActiveRed()
	{
		return coreActiveRed;
	}

	public void setPlayerBreakTower(String value)
	{
		playerBreakTower = value;
	}

	public String getPlayerBreakTower()
	{
		return playerBreakTower;
	}

	public int getMainTaskID()
	{
		return MainTaskID;
	}

	public void setTower1b(boolean value)
	{
		Tower1b=value;
	}
	public void setTower2b(boolean value)
	{
		Tower2b=value;
	}
	public void setTower1r(boolean value)
	{
		Tower1r=value;
	}
	public void setTower2r(boolean value)
	{
		Tower2r=value;
	}

	public boolean getTower1b()
	{
		return Tower1b;
	}
	public boolean getTower2b()
	{
		return Tower2b;
	}
	public boolean getTower1r()
	{
		return Tower1r;
	}
	public boolean getTower2r()
	{
		return Tower2r;
	}

	public void setCanStart(boolean value)
	{
		canStart = value;
	}

	public boolean getCanStart()
	{
		return canStart;
	}

	public boolean isInTeam(String PlayerName, String teamName)
	{
		if(teamName == "b" || teamName == "blue")
		{
			if(TeamBlue.containsKey(PlayerName))
				return true;
		}

		if(teamName == "r" || teamName == "red")
		{
			if(TeamRed.containsKey(PlayerName))
				return true;
		}
		if(teamName == "s" || teamName == "spectator")
		{
			if(TeamSpectator.containsKey(PlayerName))
				return true;
		}
		return false;
	}

	public boolean isInGameTeam(String PlayerName)
	{
		if(TeamBlue.containsKey(PlayerName) || TeamRed.containsKey(PlayerName))
			return true;

		if(TeamSpectator.containsKey(PlayerName))
			return false;

		return false;
	}

	@SuppressWarnings("deprecation")
	public void changeTeam(Player p, String playername, String teamname)
	{
		
		if(board.getTeam(p.getName()) == null)
		{
			team = board.registerNewTeam(p.getName());
		}else{
			team = board.getTeam(p.getName());
		}
		
		
		if(teamname == "s")
		{
			removeMemberOfBlueTeam(playername);
			removeMemberOfRedTeam(playername);
			addMemberToSpectatorTeam(playername);
			team.setPrefix("§b");
			team.addPlayer(p);
			return;
		}
		if(teamname == "b")
		{
			if(TeamBlue.size() == 16)
			{
				p.sendMessage("§7[§eINFO§7] §cEl equipo azul esta lleno!");
				return;
			}
			removeMemberOfRedTeam(playername);
			removeMemberOfSpectatorTeam(playername);
			addMemberToBlueTeam(playername);
			team.setPrefix("§9");
			team.addPlayer(p);
			MatchStart.getMannager().StartGame(p);
			return;
		}
		if(teamname == "r")
		{
			if(TeamRed.size() == 16)
			{
				p.sendMessage("§7[§eINFO§7] §cEl equipo rojo esta lleno!");
				return;
			}
			removeMemberOfBlueTeam(playername);
			removeMemberOfSpectatorTeam(playername);
			addMemberToRedTeam(playername);
			team.setPrefix("§4");
			team.addPlayer(p);
			MatchStart.getMannager().StartGame(p);
			return;
		}
	}

	public String getMyTeam(String PlayerName)
	{
		if(TeamBlue.containsKey(PlayerName))
			return "Blue";
		if(TeamRed.containsKey(PlayerName))
			return "Red";
		if(TeamSpectator.containsKey(PlayerName))
			return "Spectator";

		return "none";
	}

	public String getArenaName(Player p)
	{
		return p.getWorld().getName();
	}

	public int getGameCountdown()
	{
		return getGameCountdown();
	}

	public int getGameChronometer()
	{
		return gameChronometerCount;
	}

	public void setGameStart(boolean value)
	{
		gameStart = value;
	}

	public void setGameCountdownStart(boolean value)
	{
		gameCountdownStart = value;
	}

	public void setChronometer(boolean value)
	{
		gameChronometerStart = value;
	}

	public boolean getGameStart()
	{
		return gameStart;
	}

	public boolean getIfStartGameCountdown()
	{
		return gameCountdownStart;
	}

	public boolean getGameChronometerStart()
	{
		return gameChronometerStart;
	}

	public void removeMemberOfSpectatorTeam(String PlayerName)
	{
		TeamSpectator.remove(PlayerName);
	}

	public void addMemberToSpectatorTeam(String PlayerName)
	{
		TeamSpectator.put(PlayerName, PlayerName);
	}

	public void removeMemberOfRedTeam(String PlayerName)
	{
		TeamRed.remove(PlayerName);
	}

	public void addMemberToRedTeam(String PlayerName)
	{
		TeamRed.put(PlayerName, PlayerName);
	}

	public void removeMemberOfBlueTeam(String PlayerName)
	{
		TeamBlue.remove(PlayerName);
	}

	public String getTeamRedMembers()
	{
		return TeamRed.toString();
	}

	public void addMemberToBlueTeam(String PlayerName)
	{
		TeamBlue.put(PlayerName, PlayerName);
	}

	public String getTeamBlueMembers()
	{
		return TeamBlue.toString();
	}

	public boolean getTower1BlueTeam()
	{
		return Tower1b;
	}

	public boolean getTower2BlueTeam()
	{
		return Tower2b;
	}

	public boolean getTower1RedTeam()
	{
		return Tower1r;
	}

	public boolean getTower2RedTeam()
	{
		return Tower1r;
	}

	/*
	 * GANADORES
	 */

	public boolean getIfBlueWin()
	{
		return blueWin;
	}

	public boolean getIfRedWin()
	{
		return redWin;
	}

	public void setWinner(String teamname, boolean value)
	{
		if(teamname == "blue")
		{
			blueWin = value;
			return;
		}
		if(teamname == "red")
		{
			redWin = value;
			return;
		}
	}

	public String getWinner()
	{
		String winner = "";

		if(redWin && blueWin)
			winner = "Empate de equipos";

		if(redWin == false && blueWin == false)
			winner = "Aún no hay ganadores";

		if(blueWin)
			winner = "Equipo Azul";
		if(redWin)
			winner = "Equipo Rojo";

		return winner;
	}

	/*
	 * CUENTA PARA INICIAR JUEGO
	 */

	public int getCountForStart() {
		return CountForStart;
	}

	public void setCountForStart(int countForStart) {
		CountForStart = countForStart;
	}
	
	public void setTaskIDforCycle(int cycleTask)
	{
		TaskCycle = cycleTask;
	}

	public int getTaskCycle()
	{
		return TaskCycle;
	}
	/*
	 * CRONOMETRO
	 */

	public int getGameChronometerCount()
	{
		return gameChronometerCount;
	}

	public void setGameChronometerCount(int ChronometerCount)
	{
		gameChronometerCount = ChronometerCount;
	}

	public void cancelChronometer()
	{
		Bukkit.getScheduler().cancelTask(getGameChronometerCount());
	}

	public boolean isInWallArea(Player player)
	{
		Location WallMinB = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+currentMap+".Teams.Blue.Wall.min");
		WallMinB.setX(WallMinB.getX() + 2);
		WallMinB.setY(WallMinB.getY() + 2);
		WallMinB.setZ(WallMinB.getZ() + 2);
		Location WallMaxB = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+currentMap+".Teams.Blue.Wall.max");
		WallMaxB.setX(WallMaxB.getX() + 2);
		WallMaxB.setY(WallMaxB.getY() + 2);
		WallMaxB.setZ(WallMaxB.getZ() + 2);

		Location WallMinR = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+currentMap+".Teams.Red.Wall.min");
		WallMinR.setX(WallMinR.getX() + 2);
		WallMinR.setY(WallMinR.getY() + 2);
		WallMinR.setZ(WallMinR.getZ() + 2);
		Location WallMaxR = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+currentMap+".Teams.Red.Wall.max");
		WallMaxR.setX(WallMaxR.getX() + 2);
		WallMaxR.setY(WallMaxR.getY() + 2);
		WallMaxR.setZ(WallMaxR.getZ() + 2);

		if(isInArena(player, WallMinB, WallMaxB) || isInArena(player, WallMinR, WallMaxR))
			return true;

		return false;
	}

	public boolean isInArena(Player player, Location loc1, Location loc2)
	{
		double[] dim = new double[2];

		dim[0] = loc1.getX();
		dim[1] = loc2.getX();
		Arrays.sort(dim);
		if(player.getLocation().getX() > dim[1] || player.getLocation().getX() < dim[0])
			return false;

		dim[0] = loc1.getZ();
		dim[1] = loc2.getZ();
		Arrays.sort(dim);
		if(player.getLocation().getZ() > dim[1] || player.getLocation().getZ() < dim[0])
			return false;

		dim[0] = loc1.getY();
		dim[1] = loc2.getY();
		Arrays.sort(dim);
		if(player.getLocation().getY() > dim[1] || player.getLocation().getY() < dim[0])
			return false;

		return true;
	}

	public boolean isInCore(Player player)
	{
		Location CoreBlueMin = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+currentMap+".Teams.Blue.Core.min");
		Location CoreBlueMax = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+currentMap+".Teams.Blue.Core.max");

		Location CoreRedMin = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+currentMap+".Teams.Red.Core.min");
		Location CoreRedMax = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+currentMap+".Teams.Red.Core.max");

		if(isInArena(player, CoreRedMin, CoreRedMax) || isInArena(player, CoreBlueMin, CoreBlueMax))
			return true;
		return false;
	}

	public boolean isGameOver() {
		if(gameOver == true)
			return true;
		return false;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public void sendCantBreakInactiveCore(Player p, String color)
	{
		p.sendMessage("§7[§eINFO§7] §aEl "+color+"Core §ano esta activo!");
	}

}
