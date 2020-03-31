package me.alexbanper.project.top.Managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.alexbanper.project.top.TowerPowerMain;

public class TeamManager implements Listener {

	
	@EventHandler
	public void disableFriendlyFire(EntityDamageByEntityEvent e)
	{
		if(e.getEntity() instanceof Player && e.getDamager() instanceof Player)
		{
			Player damaged = (Player)e.getEntity();
			Player damager = (Player)e.getDamager();
			if(GameManager.getManager().ifTeamBlueContains(damaged.getName()) && GameManager.getManager().ifTeamBlueContains(damager.getName()))
				e.setCancelled(true);
			
			if(GameManager.getManager().ifTeamRedContains(damaged.getName()) && GameManager.getManager().ifTeamRedContains(damager.getName()))
				e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{
		if(GameManager.getManager().isGameOver() || (GameManager.getManager().getGameStart() == false))
		{
			Location toLobby = new Location(Bukkit.getWorld("Lobby"), 0.434, 65.79427, 0.302, -178, 5);
			e.getPlayer().teleport(toLobby);
		}
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TowerPowerMain.instance, new Runnable() {
			
			@Override
			public void run() {
				e.getPlayer().chat("/join s");	
			}
		}, 3L);
		
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e){
		Player player = e.getPlayer();
		
		if(GameManager.getManager().isGameOver())
		{
			Location toLobby = new Location(Bukkit.getWorld("Lobby"), 0.434, 65.79427, 0.302, -178, 5);
			player.teleport(toLobby);
			return;
		}
		
		if(GameManager.getManager().getGameStart())
		{
			if(GameManager.getManager().ifTeamBlueContains(player.getName()))
			{
				Location spectatorSpawn = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+player.getWorld().getName()+".Teams.Blue.Spawn");
				e.setRespawnLocation(spectatorSpawn);
				return;
			}
			if(GameManager.getManager().ifTeamRedContains(player.getName()))
			{
				Location spectatorSpawn = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+player.getWorld().getName()+".Teams.Red.Spawn");
				e.setRespawnLocation(spectatorSpawn);
				return;
			}
		}
		
		if(GameManager.getManager().ifTeamSpectatorContains(player.getName()))
		{
			if(GameManager.getManager().getGameStart())
			{
				Location spectatorSpawn = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+player.getWorld().getName()+".Teams.Spectator.Spawn");
				player.teleport(spectatorSpawn);
				return;
			}else{
				Location toLobby = new Location(Bukkit.getWorld("Lobby"), 0.434, 65.79427, 0.302, -178, 5);
				player.teleport(toLobby);
				return;
			}
			
		}

	}
	
}
