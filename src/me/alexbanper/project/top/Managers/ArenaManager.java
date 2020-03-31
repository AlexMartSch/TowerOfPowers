package me.alexbanper.project.top.Managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.alexbanper.project.top.TowerPowerMain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArenaManager{

	public static ArenaManager am = new ArenaManager();
	static TowerPowerMain plugin;

	//a few other fields
	Map<String, ItemStack[]> inv = new HashMap<String, ItemStack[]>();
	Map<String, ItemStack[]> armor = new HashMap<String, ItemStack[]>();

	/*
	 * LOCATIONS FOR TOWERS
	 */
	Location Tower1Redmin = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+CycleManager.getManager().getCurrentMap()+".Teams.Red.Tower.1.min");
	Location Tower1Redmax = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+CycleManager.getManager().getCurrentMap()+".Teams.Red.Tower.1.max");

	Location Tower2Redmin = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+CycleManager.getManager().getCurrentMap()+".Teams.Red.Tower.2.min");
	Location Tower2Redmax = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+CycleManager.getManager().getCurrentMap()+".Teams.Red.Tower.2.max");

	Location Tower2Bluemin = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+CycleManager.getManager().getCurrentMap()+".Teams.Blue.Tower.1.min");
	Location Tower2Bluemax = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+CycleManager.getManager().getCurrentMap()+".Teams.Blue.Tower.1.max");

	Location Tower1Bluemin = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+CycleManager.getManager().getCurrentMap()+".Teams.Blue.Tower.2.min");
	Location Tower1Bluemax = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+CycleManager.getManager().getCurrentMap()+".Teams.Blue.Tower.2.max");

	/*
	 * NO REPETIR MENSAJES DE CONSTRUCCIÓN DE MURALLAS
	 */
	boolean alreadyBlue = false;
	boolean alreadyRed = false;


	public static ArenaManager getManager(){
		return am;
	}

	public boolean canBreakMyOwnTower(Player p, String type)
	{
		Location BlueAreaMin = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+CycleManager.getManager().getCurrentMap()+".Teams.Blue.Area.min");
		Location BlueAreaMax = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+CycleManager.getManager().getCurrentMap()+".Teams.Blue.Area.max");

		Location RedAreaMin = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+CycleManager.getManager().getCurrentMap()+".Teams.Red.Area.min");
		Location RedAreaMax = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+CycleManager.getManager().getCurrentMap()+".Teams.Red.Area.max");

		Location BlueCoreMin = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+CycleManager.getManager().getCurrentMap()+".Teams.Blue.Core.min");
		Location BlueCoreMax = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+CycleManager.getManager().getCurrentMap()+".Teams.Blue.Core.max");

		Location RedCoreMin = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+CycleManager.getManager().getCurrentMap()+".Teams.Red.Core.min");
		Location RedCoreMax = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+CycleManager.getManager().getCurrentMap()+".Teams.Red.Core.max");


		if(type == "tower")
		{
			if(GameManager.getManager().ifTeamBlueContains(p.getName()))
			{
				if(blocksFromTwoPoints(Tower1Bluemin, Tower1Bluemax).toString().contains("type=MAGMA,data=0") ||
						blocksFromTwoPoints(Tower2Bluemin, Tower2Bluemax).toString().contains("type=MAGMA,data=0"))
				{
					if(GameManager.getManager().getMyTeam(p.getName()) == "Blue")
					{
						if(GameManager.getManager().isInArena(p, BlueAreaMin, BlueAreaMax))
						{
							p.sendMessage("§7[§eINFO§7] §cNo puedes romper tu propia torre!");
							return false;
						}
					}
				}
			}
			if(GameManager.getManager().ifTeamRedContains(p.getName()))
			{
				if(blocksFromTwoPoints(Tower1Redmin, Tower1Redmax).toString().contains("type=MAGMA,data=0") ||
						blocksFromTwoPoints(Tower2Redmin, Tower2Redmax).toString().contains("type=MAGMA,data=0"))
				{
					if(GameManager.getManager().getMyTeam(p.getName()) == "Red")
					{
						if(GameManager.getManager().isInArena(p, RedAreaMin, RedAreaMax))
						{
							p.sendMessage("§7[§eINFO§7] §cNo puedes romper tu propia torre!");
							return false;
						}
					}
				}
			}
			return true;
		}

		if(type == "core")
		{
			// CAN BREAK CORE

			if(GameManager.getManager().ifTeamBlueContains(p.getName()))
			{
				if(blocksFromTwoPoints(BlueCoreMin, BlueCoreMax).toString().contains("type=OBSIDIAN,data=0"))
				{
					if(GameManager.getManager().isInArena(p, BlueCoreMin, BlueCoreMax))
					{
						p.sendMessage("§7[§eINFO§7] §cNo puedes romper tu propio CORE!");
						return false;
					}
				}
			}
			if(GameManager.getManager().ifTeamRedContains(p.getName()))
			{
				if(blocksFromTwoPoints(RedCoreMin, RedCoreMax).toString().contains("type=OBSIDIAN,data=0"))
				{
					if(GameManager.getManager().isInArena(p, RedCoreMin, RedCoreMax))
					{
						p.sendMessage("§7[§eINFO§7] §cNo puedes romper tu propio CORE!");
						return false;
					}
				}
			}
		}
		return true;
	}

	@SuppressWarnings("deprecation")
	public void regenerateWalls(Block block)
	{
		final int b4id = block.getTypeId();
		final byte b4data = block.getData();

		if(block.getType() == Material.RED_NETHER_BRICK)
		{
			if(GameManager.getManager().getTower1r() && GameManager.getManager().getTower2r())
			{

				Bukkit.getScheduler().runTaskLater(TowerPowerMain.instance, new Runnable() {

					@Override
					public void run() {
						if(!alreadyRed)
						{
							Bukkit.broadcastMessage("[Muralla] -> §4Poder de Regeneración al §a100%");
							alreadyRed = true;
						}	

						block.setTypeId(b4id);
						block.setData(b4data);
					}
				}, 20L*10);

				Bukkit.getScheduler().runTaskLater(TowerPowerMain.instance, new Runnable() {
					@Override
					public void run() {
						alreadyRed = false;	
					}
				}, 20L*30);

			}
			else if(GameManager.getManager().getTower1r() || GameManager.getManager().getTower2r())
			{
				Bukkit.getScheduler().runTaskLater(TowerPowerMain.instance, new Runnable() {
					@Override
					public void run() {
						if(!alreadyRed)
						{
							Bukkit.broadcastMessage("[Muralla] -> §4Poder de Regeneración al §e50%");
							alreadyRed = true;
						}
						
						block.setTypeId(b4id);
						block.setData(b4data);
					}
				}, 20L*20);
				
				Bukkit.getScheduler().runTaskLater(TowerPowerMain.instance, new Runnable() {
					@Override
					public void run() {
						alreadyRed = false;	
					}
				}, 20L*30);
			}
			return;
		}

		if(block.getType() == Material.PRISMARINE)
		{
			if(GameManager.getManager().getTower1b() && GameManager.getManager().getTower2b())
			{
				Bukkit.getScheduler().runTaskLater(TowerPowerMain.instance, new Runnable() {
					@Override
					public void run() {
						if(!alreadyBlue)
						{
							Bukkit.broadcastMessage("[Muralla] -> §9Poder de Regeneración al §a100%");
							alreadyBlue = true;
						}
						
						block.setTypeId(b4id);
						block.setData(b4data);
					}
				}, 20L*10);
				
				Bukkit.getScheduler().runTaskLater(TowerPowerMain.instance, new Runnable() {
					@Override
					public void run() {
						alreadyBlue = false;	
					}
				}, 20L*30);
			}
			else if(GameManager.getManager().getTower1b() || GameManager.getManager().getTower2b())
			{
				Bukkit.getScheduler().runTaskLater(TowerPowerMain.instance, new Runnable() {
					@Override
					public void run() {
						if(!alreadyBlue)
						{
							Bukkit.broadcastMessage("[Muralla] -> §9Poder de Regeneración al §e50%");
							alreadyBlue = true;
						}
						
						block.setTypeId(b4id);
						block.setData(b4data);
					}
				}, 20L*20);
				
				Bukkit.getScheduler().runTaskLater(TowerPowerMain.instance, new Runnable() {
					@Override
					public void run() {
						alreadyBlue = false;	
					}
				}, 20L*30);
			}
			return;
		}
	}

	public void reapairWallSound()
	{
		for(Player online : Bukkit.getOnlinePlayers())
		{
			if(GameManager.getManager().isInWallArea(online))
				online.playSound(online.getLocation(), Sound.BLOCK_WOOD_PLACE, 1, 1);
		}
	}

	public void strikeLightning(String Tower)
	{
		Location strikeLocation = null;
		if(Tower == "1b")
			strikeLocation = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+CycleManager.getManager().getCurrentMap()+".Teams.Blue.Tower.2.Middle");

		if(Tower == "2b")
			strikeLocation = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+CycleManager.getManager().getCurrentMap()+".Teams.Blue.Tower.1.Middle");

		if(Tower == "1r")
			strikeLocation = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+CycleManager.getManager().getCurrentMap()+".Teams.Red.Tower.1.Middle");

		if(Tower == "2r")
			strikeLocation = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+CycleManager.getManager().getCurrentMap()+".Teams.Red.Tower.2.Middle");

		for (int x = 0; x < 3; x++){
			Bukkit.getWorld(strikeLocation.getWorld().getName()).strikeLightning(strikeLocation);
		}
	}

	public void checkForTowers()
	{	
		if(GameManager.getManager().getTower1r())
		{
			if(!blocksFromTwoPoints(Tower1Redmin, Tower1Redmax).toString().contains("type=MAGMA,data=0"))
			{
				GameManager.getManager().setTower1r(false);
				Bukkit.broadcastMessage("§7[§dTORRE§7] §a¡La §eTorre #1 §adel equipo §4rojo §afue destruida por §9"
						+ GameManager.getManager().getPlayerBreakTower() + "§a!");

				for(Player online : Bukkit.getOnlinePlayers())
				{
					online.playSound(online.getLocation(), Sound.ENTITY_BLAZE_DEATH, 1, 1);
				}
				strikeLightning("1r");

				Bukkit.getScheduler().runTaskLater(TowerPowerMain.instance, new Runnable() {
					@Override
					public void run() {
						announceTowersFall("Rojo");
					}
				}, 20L*1);
			}
		}

		if(GameManager.getManager().getTower2r())
		{
			if(!blocksFromTwoPoints(Tower2Redmin, Tower2Redmax).toString().contains("type=MAGMA,data=0"))
			{
				GameManager.getManager().setTower2r(false);
				Bukkit.broadcastMessage("§7[§dTORRE§7] §a¡La §eTorre #2 §adel equipo §4rojo §afue destruida por §9"
						+ GameManager.getManager().getPlayerBreakTower() + "§a!");
				for(Player online : Bukkit.getOnlinePlayers())
				{
					online.playSound(online.getLocation(), Sound.ENTITY_BLAZE_DEATH, 1, 1);
				}
				strikeLightning("2r");
				Bukkit.getScheduler().runTaskLater(TowerPowerMain.instance, new Runnable() {
					@Override
					public void run() {
						announceTowersFall("Rojo");
					}
				}, 20L*1);
			}
		}

		if(GameManager.getManager().getTower1b())
		{
			if(!blocksFromTwoPoints(Tower1Bluemin, Tower1Bluemax).toString().contains("type=MAGMA,data=0"))
			{
				GameManager.getManager().setTower1b(false);
				Bukkit.broadcastMessage("§7[§dTORRE§7] §a¡La §eTorre #1 del equipo §9azul §afue destruida por §4"
						+ GameManager.getManager().getPlayerBreakTower() + "§a!");
				for(Player online : Bukkit.getOnlinePlayers())
				{
					online.playSound(online.getLocation(), Sound.ENTITY_BLAZE_DEATH, 1, 1);
				}
				strikeLightning("1b");
				Bukkit.getScheduler().runTaskLater(TowerPowerMain.instance, new Runnable() {
					@Override
					public void run() {
						announceTowersFall("Azul");
					}
				}, 20L*1);
			}
		}

		if(GameManager.getManager().getTower2b())
		{
			if(!blocksFromTwoPoints(Tower2Bluemin, Tower2Bluemax).toString().contains("type=MAGMA,data=0"))
			{
				GameManager.getManager().setTower2b(false);
				Bukkit.broadcastMessage("§7[§dTORRE§7] §a¡La §eTorre #2 del equipo §9azul §afue destruida por §4"
						+ GameManager.getManager().getPlayerBreakTower() + "§a!");
				for(Player online : Bukkit.getOnlinePlayers())
				{
					online.playSound(online.getLocation(), Sound.ENTITY_BLAZE_DEATH, 1, 1);
				}
				strikeLightning("2b");
				Bukkit.getScheduler().runTaskLater(TowerPowerMain.instance, new Runnable() {
					@Override
					public void run() {
						announceTowersFall("Azul");
					}
				}, 20L*1);
			}

		}

	}

	public void announceTowersFall(String teamname)
	{
		Location explosionLoc;

		if(TwoTowersFall(teamname))
		{
			Bukkit.broadcastMessage("§7[§dTORRE§7] §e¡El equipo §d"+teamname+ " §eha perdido sus dos torres!");
			for(Player online : Bukkit.getOnlinePlayers())
			{
				online.playSound(online.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1, 1);
			}
			setActiveCore(teamname);

			if(teamname == "Rojo")
			{
				explosionLoc = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+CycleManager.getManager().getCurrentMap()+".Teams.Red.Core.Middle");
				Bukkit.getServer().getWorld(CycleManager.getManager().getCurrentMap()).createExplosion(explosionLoc, 0.0f, false);
				return;
			}

			if(teamname == "Azul")
			{
				explosionLoc = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+CycleManager.getManager().getCurrentMap()+".Teams.Blue.Core.Middle");
				Bukkit.getServer().getWorld(CycleManager.getManager().getCurrentMap()).createExplosion(explosionLoc, 0.0f, false);
				return;
			}

		}
	}

	public void setActiveCore(String teamname)
	{
		if(teamname == "Rojo")
		{
			GameManager.getManager().setCoreActiveRed(true);
			Bukkit.broadcastMessage("§7[§dTORRE§7] §a¡El CORE del equipo §4ROJO §aesta activado!");
			for(Player online : Bukkit.getOnlinePlayers())
			{
				online.playSound(online.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 1, 1);
			}
			return;
		}
		if(teamname == "Azul")
		{
			GameManager.getManager().setCoreActiveBlue(true);
			Bukkit.broadcastMessage("§7[§dTORRE§7] §a¡El CORE del equipo §9AZUL §aesta activado!");
			for(Player online : Bukkit.getOnlinePlayers())
			{
				online.playSound(online.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 1, 1);
			}
			return;
		}
	}

	public boolean TwoTowersFall(String teamname)
	{
		if(teamname == "b" || teamname == "Azul")
		{
			if((GameManager.getManager().getTower1b() == false) && (GameManager.getManager().getTower2b() == false))
			{
				return true;
			}
		}

		if(teamname == "r" || teamname == "Rojo")
		{
			if((GameManager.getManager().getTower1r() == false) && (GameManager.getManager().getTower2r() == false))
			{
				return true;
			}
		}
		return false;
	}

	public static List<Block> blocksFromTwoPoints(Location loc1, Location loc2)
	{
		List<Block> blocks = new ArrayList<Block>();

		int topBlockX = (loc1.getBlockX() < loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());
		int bottomBlockX = (loc1.getBlockX() > loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());

		int topBlockY = (loc1.getBlockY() < loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());
		int bottomBlockY = (loc1.getBlockY() > loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());

		int topBlockZ = (loc1.getBlockZ() < loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());
		int bottomBlockZ = (loc1.getBlockZ() > loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());

		for(int x = bottomBlockX; x <= topBlockX; x++)
		{
			for(int z = bottomBlockZ; z <= topBlockZ; z++)
			{
				for(int y = bottomBlockY; y <= topBlockY; y++)
				{
					Block block = loc1.getWorld().getBlockAt(x, y, z);
					if(block.getType() != Material.AIR)
						blocks.add(block);
				}
			}
		}

		return blocks;
	}

	//add players to the arena, save their inventory
	/*public void addPlayer(Player p, String team){
		inv.put(p.getName(), p.getInventory().getContents());//save inventory
		armor.put(p.getName(), p.getInventory().getArmorContents());

		p.getInventory().setArmorContents(null);
		p.getInventory().clear();

		locs.put(p.getName(), p.getLocation());
	}

	public boolean isInGame(Player p){
		if(TeamBlue.containsKey(p.getName()))
			return true;
		if(TeamRed.containsKey(p.getName()))
			return true;
		return false;
	}*/
}
