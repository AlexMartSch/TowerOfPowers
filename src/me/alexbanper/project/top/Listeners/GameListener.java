package me.alexbanper.project.top.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import me.alexbanper.project.top.TowerPowerMain;
import me.alexbanper.project.top.Events.MatchStop;
import me.alexbanper.project.top.Managers.ArenaManager;
import me.alexbanper.project.top.Managers.CycleManager;
import me.alexbanper.project.top.Managers.GameManager;

public class GameListener implements Listener{

	public static TowerPowerMain plugin;

	public GameListener(TowerPowerMain plugin){
		GameListener.plugin = plugin;
	}

	@EventHandler
	public void onWallDestroyByExplosion(EntityExplodeEvent e)
	{
		for(Block b : e.blockList())
		{
			if(b.getType() == Material.PRISMARINE || b.getType() == Material.RED_NETHER_BRICK)
			{
				ArenaManager.getManager().regenerateWalls(b);
			}
		}
	}
	
    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        event.setCancelled(true);
    }
	
	@EventHandler
	public void onDamange(EntityDamageByEntityEvent e){
		if(e.getEntity() instanceof Player && GameManager.getManager().ifTeamSpectatorContains(((Player) e.getEntity()).getName())){
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onLavaFlow(BlockFromToEvent event) {
		Block lavaBlock = event.getBlock();

		if(GameManager.getManager().getGameStart())
		{
			if(!GameManager.getManager().ifCoresIsActive())
				return;

			if(lavaBlock.getType().equals(Material.LAVA))
			{
				World worldName = Bukkit.getServer().getWorld(CycleManager.getManager().getCurrentMap());
				
				Location northLoc = new Location(worldName, (lavaBlock.getX() + 1), lavaBlock.getY(), lavaBlock.getZ());
				Location southLoc = new Location(worldName, (lavaBlock.getX() - 1), lavaBlock.getY(), lavaBlock.getZ());
				Location eastLoc = new Location(worldName, (lavaBlock.getX() -1), lavaBlock.getY(), (lavaBlock.getZ() - 1));
				Location westLoc = new Location(worldName, lavaBlock.getX(), lavaBlock.getY(), (lavaBlock.getZ() + 1));


				Block north = worldName.getBlockAt(northLoc);
				Block south = worldName.getBlockAt(southLoc);
				Block east = worldName.getBlockAt(eastLoc);
				Block west = worldName.getBlockAt(westLoc);

				if (north.getType() == Material.LAPIS_BLOCK
						|| south.getType() == Material.LAPIS_BLOCK
						|| east.getType() == Material.LAPIS_BLOCK
						|| west.getType() == Material.LAPIS_BLOCK)
				{
					GameManager.getManager().setWinner("red", true);
					if(GameManager.getManager().getIfRedWin())
					{
						//Bukkit.broadcastMessage("ROJOS GANAN!");

						MatchStop.getManager().StopGame();
						return;
					}

				}

				if(north.getType() == Material.REDSTONE_BLOCK
						|| south.getType() == Material.REDSTONE_BLOCK
						|| east.getType() == Material.REDSTONE_BLOCK
						|| west.getType() == Material.REDSTONE_BLOCK)
				{
					GameManager.getManager().setWinner("blue", true);
					if(GameManager.getManager().getIfBlueWin())
					{
						//Bukkit.broadcastMessage("AZULES GANAN!");
						MatchStop.getManager().StopGame();	
						return;
					}
				}
			}
		}
	}


	@EventHandler
	public void onBlockBreak(BlockBreakEvent e)
	{
		Player p = (Player) e.getPlayer();
		Block b = (Block) e.getBlock();

		if(GameManager.getManager().getGameStart())
		{
			e.setCancelled(false);
			if(GameManager.getManager().ifTeamBlueContains(p.getName()) || GameManager.getManager().ifTeamRedContains(p.getName()))
			{	
				if(b.getType() == Material.RED_NETHER_BRICK)
				{
					Block block = e.getBlock();
					ArenaManager.getManager().regenerateWalls(block);
					b.setType(Material.AIR);
					e.setCancelled(true);
					return;
				}

				if(b.getType() == Material.PRISMARINE)
				{
					Block block = e.getBlock();
					ArenaManager.getManager().regenerateWalls(block);
					b.setType(Material.AIR);
					e.setCancelled(true);
					return;
				}

				if(b.getType() == Material.OBSIDIAN)
				{
					if(GameManager.getManager().isInCore(p))
					{
						String color = "";
						if(ArenaManager.getManager().canBreakMyOwnTower(e.getPlayer(), "core"))
						{
							if(GameManager.getManager().getCoreActiveBlue() == true)
							{

								if(ArenaManager.getManager().TwoTowersFall("b"))
								{
									if(GameManager.getManager().ifTeamRedContains(p.getName()))
									{
										b.setType(Material.AIR);
										e.setCancelled(true);
										return;	
									}else{
										color = "§9";
										e.setCancelled(true);
									}
								}else{
									color = "§9";
									e.setCancelled(true);
								}
							}else{
								color = "§9";
								e.setCancelled(true);
							}

							if(GameManager.getManager().getCoreActiveRed() == true)
							{
								if(ArenaManager.getManager().TwoTowersFall("r"))
								{
										b.setType(Material.AIR);
										e.setCancelled(true);
										return;
								}else{
									color = "§4";
									e.setCancelled(true);
								}
							}else{
								color = "§4";
								e.setCancelled(true);
							}
							
							GameManager.getManager().sendCantBreakInactiveCore(p, color);
							e.setCancelled(true);
							Bukkit.getScheduler().scheduleSyncDelayedTask(TowerPowerMain.instance, new Runnable() {
								public void run() {
									e.setCancelled(false);
								}
							}, 5L);
							
							return;
						}
						e.setCancelled(true);
						Bukkit.getScheduler().scheduleSyncDelayedTask(TowerPowerMain.instance, new Runnable() {
							public void run() {
								e.setCancelled(false);
							}
						}, 5L);
						return;
					}
				}


				if(b.getType() == Material.MAGMA)
				{
					if(!ArenaManager.getManager().canBreakMyOwnTower(e.getPlayer(), "tower"))
					{

						e.setCancelled(true);
						Bukkit.getScheduler().scheduleSyncDelayedTask(TowerPowerMain.instance, new Runnable() {
							public void run() {
								e.setCancelled(false);
							}
						}, 5L);
						return;
					}else{
						b.setType(Material.AIR);
						e.setCancelled(true);
						GameManager.getManager().setPlayerBreakTower(e.getPlayer().getName());
					}
				}


				if ((b.getType() == Material.REDSTONE_BLOCK) ||
						(b.getType() == Material.LAPIS_BLOCK)) {
					e.getPlayer().sendMessage("§7[§eINFO§7] §cNo puedes romper estos bloques!");

					e.setCancelled(true);
					return;
				}
				e.setCancelled(false);
				return;
			}
		}else{
			e.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent e)
	{
		if(GameManager.getManager().ifTeamBlueContains(e.getPlayer().getName()))
		{
			GameManager.getManager().removeMemberOfBlueTeam(e.getPlayer().getName());
			return;
		}
		if(GameManager.getManager().ifTeamRedContains(e.getPlayer().getName()))
		{
			GameManager.getManager().removeMemberOfRedTeam(e.getPlayer().getName());
			return;
		}
		if(GameManager.getManager().ifTeamSpectatorContains(e.getPlayer().getName()))
		{
			GameManager.getManager().removeMemberOfSpectatorTeam(e.getPlayer().getName());
			return;
		}
	}
}
