package me.alexbanper.project.top.Commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.domedd.developerapi.messagebuilder.TitleBuilder;
import me.alexbanper.project.top.TowerPowerMain;
import me.alexbanper.project.top.Managers.GameManager;
import me.confuser.barapi.BarAPI;

public class stopCommand implements CommandExecutor{

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(sender instanceof Player)
		{
			Player player = (Player) sender;
			if(command.getName().equalsIgnoreCase("endgame"))
			{
				if(args.length == 0)
				{
					if((!GameManager.getManager().getGameStart()) && (GameManager.getManager().getIfStartGameCountdown() == true))
					{

						Bukkit.getScheduler().cancelTask(GameManager.getManager().getCountForStart());
						Bukkit.broadcastMessage("§cSe ha detenido la partida por §6"+player.getName());
						for(Player online : Bukkit.getOnlinePlayers())
						{
							BarAPI.setMessage("Game has been aborted...");

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
									online.getPlayer().setGameMode(GameMode.SPECTATOR);
								}
							}, 40L);

						}
						GameManager.getManager().stopGameBooleans();
						return true;
					}
					if((GameManager.getManager().getGameStart() == true))
					{
						Bukkit.broadcastMessage("§cSe ha detenido la partida por "+player.getName());
						GameManager.getManager().cancelChronometer();
						for(Player online : Bukkit.getOnlinePlayers())
						{
							online.playSound(online.getLocation(), Sound.ENTITY_WITHER_DEATH, 1, 1);

							TitleBuilder tb = new TitleBuilder("Match stopped by "+player.getName(), "¡Thanks for playing!")
									.setTimings(3, 75, 3, 4, 64, 4);
							tb.send(online);

							BarAPI.setMessage("Game has been aborted...");

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
									Location toLobby = new Location(Bukkit.getWorld("Lobby"), 0.434, 65.79427, 0.302, -178, 5);
									online.getPlayer().teleport(toLobby);

									Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TowerPowerMain.instance, new Runnable() {

										@Override
										public void run() {
											online.getPlayer().setGameMode(GameMode.SPECTATOR);
										}
									}, 5L);

									online.getPlayer().setGameMode(GameMode.SPECTATOR);

								}
							}, 160L);

						}
						GameManager.getManager().stopGameBooleans();
						return true;
					}
					player.sendMessage("§cNo hay una partida iniciada!");
					return true;
				}
			}
		}else{
			sender.sendMessage("Solo jugadores pueden interactuar!");
		}
		return true;
	}
}
