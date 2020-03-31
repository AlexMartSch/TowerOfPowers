package me.alexbanper.project.top.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.alexbanper.project.top.Managers.GameManager;

public class ChatListener implements org.bukkit.event.Listener{

	/*
	 * PLAYER CHAT
	 * TEAM CHAT - GLOBAL CHAT
	 * 
	 */
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e)
	{
		e.setCancelled(true);
		Player player = e.getPlayer();

		if (e.getMessage().startsWith("!")){
			Bukkit.broadcastMessage("§7(Global) §r"+e.getPlayer().getDisplayName()+"§f: " + e.getMessage().replaceFirst("!", ""));
			return;
		}

		for(Player online : Bukkit.getOnlinePlayers())
		{
			if(GameManager.getManager().ifTeamSpectatorContains(player.getName()) && !(e.getMessage().startsWith("!")))
			{	

				for(String teamchat : GameManager.getManager().TeamSpectatorKeySet())
				{
					Player stp = Bukkit.getPlayer(teamchat);
					stp.sendMessage("§7(§bESPECTADOR§7) §b"+e.getPlayer().getName() + "§f: " + e.getMessage());
				}
				return;
			}
			else
			{
				e.getRecipients().remove(online);
			}
			if(GameManager.getManager().ifTeamBlueContains(player.getName()) && !(e.getMessage().startsWith("!")))
			{
				for(String teamchat : GameManager.getManager().TeamBlueKeySet())
				{
					Player stp = Bukkit.getPlayer(teamchat);
					stp.sendMessage("§7(§9AZUL§7) §9"+e.getPlayer().getName() + "§f: " + e.getMessage());
				}
				return;
			}else
			{
				e.getRecipients().remove(online);
			}
			if(GameManager.getManager().ifTeamRedContains(player.getName()) && !(e.getMessage().startsWith("!")))
			{
				for(String teamchat : GameManager.getManager().TeamRedKeySet())
				{
					Player stp = Bukkit.getPlayer(teamchat);
					stp.sendMessage("§7(§4ROJO§7) §4"+e.getPlayer().getName() + "§f: " + e.getMessage());
				}
				return;
			}
			else
			{
				e.getRecipients().remove(online);
			}
		}
		e.setCancelled(true);
	}
}
