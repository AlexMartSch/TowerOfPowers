package me.alexbanper.project.top.Commands;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.alexbanper.project.top.Managers.GameManager;

public class leaveCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player))
		{
			sender.sendMessage("Solo jugadores pueden interactuar.");
			return true;
		}

		Player player = (Player) sender;

		if(command.getName().equalsIgnoreCase("leave"))
		{
			if(GameManager.getManager().isInGameTeam(player.getName()))
			{
				GameManager.getManager().changeTeam(player, player.getName(), "s");
				player.sendMessage("Ahora eres espectador!");
				player.setGameMode(GameMode.SPECTATOR);
			}
		}
		return false;
	}
}
