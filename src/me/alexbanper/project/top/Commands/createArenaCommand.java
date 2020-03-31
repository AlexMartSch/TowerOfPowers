package me.alexbanper.project.top.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;

import me.alexbanper.project.top.TowerPowerMain;
import me.alexbanper.project.top.Managers.GameManager;
import me.alexbanper.project.top.Utilies.LocationSerialize;

public class createArenaCommand implements CommandExecutor{

	String arenaName = null;
	Location min = null;
	Location max = null;

	public void setLocationsArena(String arenaName, String teamName, String type, Location min, Location max, boolean calculateMiddle)
	{
		TowerPowerMain.instance.getConfig().set("Arenas."+arenaName+".Teams."+teamName+"."+type+".min", new LocationSerialize(min));
		TowerPowerMain.instance.getConfig().set("Arenas."+arenaName+".Teams."+teamName+"."+type+".max", new LocationSerialize(max));

		if(calculateMiddle)
		{
			if(teamName == "Blue")
				GameManager.getManager().calculateMiddle(arenaName, "coreblue", min, max);
			if(teamName == "Red")
				GameManager.getManager().calculateMiddle(arenaName, "corered", min, max);	
		}

		TowerPowerMain.instance.saveConfig();
	}
	
	public void setTowerLocations(String arenaName, String teamName, String middleFor ,String towerNumber, Location min, Location max)
	{
		TowerPowerMain.instance.getConfig().set("Arenas."+arenaName+".Teams."+teamName+".Tower."+towerNumber+".min", new LocationSerialize(min));
		TowerPowerMain.instance.getConfig().set("Arenas."+arenaName+".Teams."+teamName+".Tower."+towerNumber+".max", new LocationSerialize(max));
		
		GameManager.getManager().calculateMiddle(arenaName, middleFor, min, max);
		
		TowerPowerMain.instance.saveConfig();
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(sender instanceof Player)
		{
			Player player = (Player) sender;

			if(!player.hasPermission("pgn.commands.creation"))
				return true;

			WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
			Selection selection = worldEdit.getSelection(player);

			if(command.getName().equalsIgnoreCase("createarena"))
			{
				if(selection instanceof CuboidSelection)
				{
					if(args.length == 0)
					{
						if(TowerPowerMain.instance.getConfig().contains("Arenas."+player.getWorld().getName()))
						{
							player.sendMessage("La arena ya existe!");
							return true;
						}
						player.sendMessage("corecto!");
						arenaName = player.getWorld().getName();
						CuboidSelection cuboid = (CuboidSelection) selection;
						min = cuboid.getMinimumPoint();
						max = cuboid.getMaximumPoint();

						//TowerPowerMain.instance.getConfig().set("Arenas."+player.getWorld().getName()+".cuboid.min", min);


						TowerPowerMain.instance.getConfig().set("Arenas."+player.getWorld().getName()+".cuboid.min", new LocationSerialize(min));

						TowerPowerMain.instance.getConfig().set("Arenas."+player.getWorld().getName()+".cuboid.max", max);
						TowerPowerMain.instance.saveConfig();
						return true;
					}else{
						if(args[0].equalsIgnoreCase("re-select"))
						{
							if(TowerPowerMain.instance.getConfig().contains("Arenas."+player.getWorld().getName()))
							{
								CuboidSelection cuboid = (CuboidSelection) selection;
								min = cuboid.getMinimumPoint();
								max = cuboid.getMaximumPoint();

								TowerPowerMain.instance.getConfig().set("Arenas."+player.getWorld().getName()+".cuboid.min", min);
								TowerPowerMain.instance.getConfig().set("Arenas."+player.getWorld().getName()+".cuboid.max", max);
								TowerPowerMain.instance.saveConfig();
								player.sendMessage("Edicion realizada para "+player.getWorld().getName()+"!");
							}else{
								player.sendMessage("La arena no existe!");
							}

							return true;
						}
					}

				}else{
					player.sendMessage("No selection cuboid");
					return true;
				}
				return true;
			}
			
			if(command.getName().equalsIgnoreCase("setcore"))
			{
				if(args.length == 0)
				{
					player.sendMessage("§7[§eINFO§7] §c/setcore <arenaName> <Team>");
					return true;
				}else{		
					if(TowerPowerMain.instance.getConfig().contains("Arenas."+args[0]))
					{
						CuboidSelection cuboid = (CuboidSelection) selection;
						min = cuboid.getMinimumPoint();
						max = cuboid.getMaximumPoint();

						if(args[1].equalsIgnoreCase("b") || args[1].equalsIgnoreCase("blue"))
						{
							/*TowerPowerMain.instance.getConfig().set("Arenas."+args[0]+".Teams.Blue.Core.min", min);
							TowerPowerMain.instance.getConfig().set("Arenas."+args[0]+".Teams.Blue.Core.max", max);
							GameManager.getManager().calculateMiddle(args[0], "coreblue", min, max);
							TowerPowerMain.instance.saveConfig();*/

							setLocationsArena(args[0], "Blue", "Core" ,min, max, true);
							player.sendMessage("§7[§eINFO§7] §bCore establecida para el equipo azul!");
							return true;
						}
						if(args[1].equalsIgnoreCase("r") || args[1].equalsIgnoreCase("red"))
						{
							setLocationsArena(args[0], "Red", "Core", min, max, true);
							player.sendMessage("§7[§eINFO§7] §bCore establecida para el equipo rojo!");
							return true;
						}
						player.sendMessage("§7[§eINFO§7] §c/setcore <ArenaName> <Team>");
						return true;
					}else{
						player.sendMessage("§7[§eINFO§7] §cLa arena no existe!");
						return true;
					}
				}
			}

			if(command.getName().equalsIgnoreCase("setarea"))
			{
				if(args.length == 0)
				{
					player.sendMessage("§7[§eINFO§7] §c/setarea <arenaName> <Team>");
					return true;
				}else{
					if(TowerPowerMain.instance.getConfig().contains("Arenas."+args[0]))
					{
						CuboidSelection cuboid = (CuboidSelection) selection;
						min = cuboid.getMinimumPoint();
						max = cuboid.getMaximumPoint();

						if(args[1].equalsIgnoreCase("b") || args[1].equalsIgnoreCase("blue"))
						{	
							setLocationsArena(args[0], "Blue", "Area", min, max, false);
							player.sendMessage("§7[§eINFO§7] §bArea establecida para el equipo azul!");
							return true;
						}
						if(args[1].equalsIgnoreCase("r") || args[1].equalsIgnoreCase("red"))
						{
							setLocationsArena(args[0], "Red", "Area", min, max, false);
							player.sendMessage("§7[§eINFO§7] §bArea establecida para el equipo rojo!");
							return true;
						}
						player.sendMessage("§7[§eINFO§7] §c/setarea <ArenaName> <Team>");
						return true;
					}else{
						player.sendMessage("§7[§eINFO§7] §cLa arena no existe!");
						return true;
					}
				}
			}

			if(command.getName().equalsIgnoreCase("settower"))
			{
				if(args.length == 0)
				{
					player.sendMessage("§7[§eINFO§7] §c/setTower <arena> <int> <team>");
					return true;
				}else{
					if(!TowerPowerMain.instance.getConfig().contains("Arenas."+args[0]))
					{
						player.sendMessage("§7[§eINFO§7] §cLa arena no existe!");
						return true;
					}
					if(args.length == 1)
					{
						player.sendMessage("§7[§eINFO§7] §eSelecciona el ID de Torre");
						return true;
					}else{
						if(args[1].equalsIgnoreCase("1"))
						{
							if(args.length == 2)
							{
								player.sendMessage("§7[§eINFO§7] §eSelecciona el TEAM");
								return true;
							}else{
								Location arenaMin = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+args[0]+".cuboid.min");
								Location arenaMax = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+args[0]+".cuboid.max");

								CuboidSelection cuboid = (CuboidSelection) selection;
								min = cuboid.getMinimumPoint();
								max = cuboid.getMaximumPoint();

								if(GameManager.getManager().isInArena(player, arenaMin, arenaMax))
								{
									if(args[2].equalsIgnoreCase("b") || args[2].equalsIgnoreCase("blue"))
									{
										
										/*TowerPowerMain.instance.getConfig().set("Arenas."+args[0]+".Teams.Blue.Tower.1.min", min);
										TowerPowerMain.instance.getConfig().set("Arenas."+args[0]+".Teams.Blue.Tower.1.max", max);
										TowerPowerMain.instance.saveConfig();
										GameManager.getManager().calculateMiddle(args[0], "1b", min, max);*/
										
										setTowerLocations(args[0], "Blue", "1b", args[1], min, max);
										player.sendMessage("§7[§eINFO§7] §aTorre "+args[1]+" establecida equipo azul!");
										
										return true;
									}
									if(args[2].equalsIgnoreCase("r") || args[2].equalsIgnoreCase("red"))
									{
										setTowerLocations(args[0], "Red", "1r", args[1], min, max);
										player.sendMessage("§7[§eINFO§7] §aTorre "+args[1]+" para equipo rojo establecido!");
										return true;
									}
								}else{
									player.sendMessage("§7[§eINFO§7] §cNo estas en la arena!");
									return true;
								}
							}
							return true;
						}
						if(args[1].equalsIgnoreCase("2"))
						{
							if(args.length == 2)
							{
								player.sendMessage("Selecciona el TEAM");
								return true;
							}else{
								Location arenaMin = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+args[0]+".cuboid.min");
								Location arenaMax = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+args[0]+".cuboid.max");

								CuboidSelection cuboid = (CuboidSelection) selection;
								min = cuboid.getMinimumPoint();
								max = cuboid.getMaximumPoint();

								if(GameManager.getManager().isInArena(player, arenaMin, arenaMax))
								{
									if(args[2].equalsIgnoreCase("b") || args[2].equalsIgnoreCase("blue"))
									{
										setTowerLocations(args[0], "Blue", "2b", args[1], min, max);
										player.sendMessage("§7[§eINFO§7] §aTorre "+args[1]+" establecida equipo azul!");
										return true;
									}
									if(args[2].equalsIgnoreCase("r") || args[2].equalsIgnoreCase("red"))
									{
										setTowerLocations(args[0], "Red", "2r", args[1], min, max);
										player.sendMessage("§7[§eINFO§7] §aTorre "+args[1]+" para equipo rojo establecido!");
										return true;
									}
								}else{
									player.sendMessage("§7[§eINFO§7] §cNo estas en la arena!");
									return true;
								}
							}
							return true;
						}
						player.sendMessage("§7[§eINFO§7] §eNo es un ID valido!");
						return true;
					}
				}
			}

			if(command.getName().equalsIgnoreCase("setwall"))
			{
				if(args.length == 0)
				{
					player.sendMessage("/setWall <arena> <team>");
					return true;
				}else{
					if(!TowerPowerMain.instance.getConfig().contains("Arenas."+args[0]))
					{
						player.sendMessage("La arena no existe!");
						return true;
					}
					if(args.length == 1)
					{
						player.sendMessage("Selecciona el TEAM");
						return true;
					}else{
						Location arenaMin = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+args[0]+".cuboid.min");
						Location arenaMax = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+args[0]+".cuboid.max");

						CuboidSelection cuboid = (CuboidSelection) selection;
						min = cuboid.getMinimumPoint();
						max = cuboid.getMaximumPoint();

						if(GameManager.getManager().isInArena(player, arenaMin, arenaMax))
						{
							if(args[1].equalsIgnoreCase("b") || args[1].equalsIgnoreCase("blue"))
							{
								player.sendMessage("Muralla establecida equipo azul!");
								TowerPowerMain.instance.getConfig().set("Arenas."+args[0]+".Teams.Blue.Wall.min", min);
								TowerPowerMain.instance.getConfig().set("Arenas."+args[0]+".Teams.Blue.Wall.max", max);
								TowerPowerMain.instance.saveConfig();
								return true;
							}
							if(args[1].equalsIgnoreCase("r") || args[1].equalsIgnoreCase("red"))
							{
								player.sendMessage("Muralla para equipo rojo establecido!");
								TowerPowerMain.instance.getConfig().set("Arenas."+args[0]+".Teams.Red.Wall.min", min);
								TowerPowerMain.instance.getConfig().set("Arenas."+args[0]+".Teams.Red.Wall.max", max);
								TowerPowerMain.instance.saveConfig();
								return true;
							}
						}else{
							player.sendMessage("No estas en la arena!");
							return true;
						}
					}
					return true;
				}
			}

			if(command.getName().equalsIgnoreCase("setarenaspawn"))
			{
				if(args.length == 0)
				{
					player.sendMessage("/setArenaSpawn <arenaName> <team>");
					return true;
				}else{
					if(!TowerPowerMain.instance.getConfig().contains("Arenas."+args[0]))
					{
						player.sendMessage("La arena no existe!");
						return true;
					}

					if(args.length == 1)
					{
						player.sendMessage("Selecciona el TEAM");
						return true;
					}else{
						Location spawneable = player.getLocation();
						Location arenaMin = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+args[0]+".cuboid.min");
						Location arenaMax = (Location) TowerPowerMain.instance.getConfig().get("Arenas."+args[0]+".cuboid.max");

						if(args[1].equalsIgnoreCase("s") || args[1].equalsIgnoreCase("spectator"))
						{
							player.sendMessage("Spawn para equipo espectador establecido!");
							TowerPowerMain.instance.getConfig().set("Arenas."+args[0]+".Teams.Spectator.Spawn", spawneable);
							TowerPowerMain.instance.saveConfig();
							return true;
						}

						if(GameManager.getManager().isInArena(player, arenaMin, arenaMax))
						{
							if(args[1].equalsIgnoreCase("b") || args[1].equalsIgnoreCase("blue"))
							{
								player.sendMessage("Spawn para equipo azul establecido!");
								TowerPowerMain.instance.getConfig().set("Arenas."+args[0]+".Teams.Blue.Spawn", spawneable);
								TowerPowerMain.instance.saveConfig();
								return true;
							}
							if(args[1].equalsIgnoreCase("r") || args[1].equalsIgnoreCase("red"))
							{
								player.sendMessage("Spawn para equipo rojo establecido!");
								TowerPowerMain.instance.getConfig().set("Arenas."+args[0]+".Teams.Red.Spawn", spawneable);
								TowerPowerMain.instance.saveConfig();
								return true;
							}
						}else{
							player.sendMessage("No estas en la arena!");
							return true;
						}
					}
				}
				return true;
			}
			if(command.getName().equalsIgnoreCase("removearena"))
			{
				if(args.length == 0)
				{
					player.sendMessage("/removearena confirm");
					return true;
				}else{
					if(args[0].equalsIgnoreCase("confirm"))
					{
						if(TowerPowerMain.instance.getConfig().contains("Arenas."+player.getWorld().getName()))
						{
							TowerPowerMain.instance.getConfig().set("Arenas."+player.getWorld().getName(), null);
							TowerPowerMain.instance.saveConfig();
							player.sendMessage("Arena borrada!");
							return true;
						}else{
							player.sendMessage("La arena no existe!");
							return true;
						}
					}
					player.sendMessage("use /removearena confirm");
				}
			}
		}
		return true;
	}

}
