package me.alexbanper.project.top.Commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.alexbanper.project.top.TowerPowerMain;
import me.alexbanper.project.top.Managers.CycleManager;
import me.alexbanper.project.top.Managers.GameManager;

public class joinCommand implements CommandExecutor{

	private void teleportToArena(Player player)
	{
		if(player.getWorld() == Bukkit.getServer().getWorld("Lobby"))
		{
			Location toSpectatorSpawn = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+CycleManager.getManager().getCurrentMap()+".Teams.Spectator.Spawn");
			player.teleport(toSpectatorSpawn);
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TowerPowerMain.instance, new Runnable() {

				@Override
				public void run() {
					player.setGameMode(GameMode.SPECTATOR);
				}
			}, 5L);
		}
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(!(sender instanceof Player))
		{
			sender.sendMessage("Solo jugadores pueden interactuar.");
			return true;
		}

		Player player = (Player) sender;

		if(command.getName().equalsIgnoreCase("join"))
		{	
			if(args.length == 0)
			{
				if(GameManager.getManager().isGameOver() == true)
				{
					player.sendMessage("§c¡El juego acaba de terminar, aun no puedes unirte!");
					return true;
				}
				if(GameManager.getManager().getTeamBlueSize() <= GameManager.getManager().getTeamRedSize())
				{
					teleportToArena(player);
					GameManager.getManager().changeTeam(player, player.getName(), "b");
					player.sendMessage("§9Ahora eres del equipo Azul!");
					return true;
				}else{
					teleportToArena(player);
					GameManager.getManager().changeTeam(player, player.getName(), "r");
					player.sendMessage("§cAhora eres del equipo Rojo!!");
					return true;
				}
			}else{
				if(args[0].equalsIgnoreCase("myteam"))
				{
					player.sendMessage("Eres: " + GameManager.getManager().getMyTeam(player.getName()));
					return true;
				}
				if(args[0].equalsIgnoreCase("cm"))
				{
					player.sendMessage("Current Map: " + CycleManager.getManager().getCurrentMap());
					player.sendMessage("Next Map: " + CycleManager.getManager().getNextMap());
					return true;
				}
				if(args[0].equalsIgnoreCase("b") || args[0].equalsIgnoreCase("blue"))
				{
					if(GameManager.getManager().isGameOver() == true)
					{
						player.sendMessage("§c¡El juego acaba de terminar, aun no puedes unirte!");
						return true;
					}
					if(GameManager.getManager().isInTeam(player.getName(), "b"))
					{
						player.sendMessage("§cYa estas en el equipo azul!");
						return true;
					}
					player.sendMessage("§9Ahora eres del equipo Azul!");
					teleportToArena(player);
					GameManager.getManager().changeTeam(player, player.getName(), "b");
					return true;
				}
				if(args[0].equalsIgnoreCase("r") || args[0].equalsIgnoreCase("red"))
				{
					if(GameManager.getManager().isGameOver() == true)
					{
						player.sendMessage("§c¡El juego acaba de terminar, aun no puedes unirte!");
						return true;
					}
					if(GameManager.getManager().isInTeam(player.getName(), "r"))
					{
						player.sendMessage("§cYa estas en el equipo rojo!");
						return true;
					}
					player.sendMessage("§4Ahora eres del equipo Rojo!");
					teleportToArena(player);
					GameManager.getManager().changeTeam(player, player.getName(), "r");
					return true;
				}
				if(args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("spectator"))
				{
					if(GameManager.getManager().isInTeam(player.getName(), "s"))
					{
						player.sendMessage("§cYa eres espectador!");
						return true;
					}
					player.sendMessage("§bAhora eres espectador!");
					GameManager.getManager().changeTeam(player, player.getName(), "s");
					player.setGameMode(GameMode.SPECTATOR);
					return true;
				}
				player.sendMessage("§cUsa /join <team> [Blue, Red]");
				return true;
			}
		}
		return false;
	}
}
