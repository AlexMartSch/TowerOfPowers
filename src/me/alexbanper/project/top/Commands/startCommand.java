package me.alexbanper.project.top.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.alexbanper.project.top.Events.MatchStart;

public class startCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player)
		{
			Player player = (Player) sender;
			if(command.getName().equalsIgnoreCase("start"))
			{
				if(args.length == 0)
				{
					MatchStart.getMannager().StartGame(player);
					player.sendMessage("§eIntentando iniciar la partida...!");
				}else{
					if(args[0].equalsIgnoreCase("-f"))
					{
						MatchStart.getMannager().forceStartGame(player);
						Bukkit.broadcastMessage("§a§lSe ha forzado el inicio de la partida!");
						return true;
					}
				}
			}
		}
		return false;
	}

}
