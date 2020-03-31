package me.alexbanper.project.top.Managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.World;

import me.alexbanper.project.top.TowerPowerMain;


public class WorldManager{

	public static WorldManager wm = new WorldManager();

	public static WorldManager getManager()
	{
		return wm;
	}

	boolean announce = true;
	public void resetMap(String WorldName)
	{

		WorldManager WM = new WorldManager();
		WM.reset(Bukkit.getWorld(WorldName));
		if(announce)
		{
			Bukkit.broadcastMessage("§c¡Se estan reiniciando las arenas...!");
			announce = false;
		}


		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TowerPowerMain.instance, new Runnable() {

			@Override
			public void run() {
				announce = true;
			}
		}, 40L);
		return;
	}

	public void resetMaps()
	{
		for(String mapList : CycleManager.getManager().getCycleList())
		{
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TowerPowerMain.instance, new Runnable() {
				@Override
				public void run() {
					resetMap(mapList);
				}
			}, 40L);
		}
	}

	int taskID = 0;
	
	public void reset(World world) {
		Bukkit.getServer().unloadWorld(world, false);
		Bukkit.getServer().getScheduler().runTaskLater(TowerPowerMain.instance, new Runnable(){
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				final File srcWorldFolder = new File("plugins/TowerOfPowers/Maps/" + world.getName());
				final File worldFolder = new File(world.getName());
				deleteFolder(worldFolder); // Delete old folder
				Bukkit.broadcastMessage("§cIniciando arena...");
				copyWorldFolder(srcWorldFolder, worldFolder); 

				taskID = Bukkit.getServer().getScheduler().scheduleAsyncRepeatingTask(TowerPowerMain.instance, new Runnable(){
					@Override
					public void run() {
						Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "mv import "+world.getName()+" normal CleanRoomGenerator");
					}
				}, 40, 1);
				Bukkit.broadcastMessage("§cArena lista... §aUsa /join");

				Bukkit.getServer().getScheduler().runTaskLater(TowerPowerMain.instance, new Runnable(){
					@Override
					public void run() {
						Bukkit.getScheduler().cancelTask(taskID);
					}
				}, 100);
			}
		}, 60);
	}

	private void deleteFolder(File folder) {
		File[] files = folder.listFiles();
		if(files != null) {
			for(File file : files) {
				if(file.isDirectory()) {
					deleteFolder(file);
				} else {
					file.delete();
				}
			}
		}
		folder.delete();
	}
	private void copyWorldFolder(File from, File to) {
		try {
			ArrayList<String> ignore = new ArrayList<String>(Arrays.asList(new String[] { "uid.dat", "session.dat" }));
			if(!ignore.contains(from.getName())) {
				if(from.isDirectory()) {
					if(!to.exists()) {
						to.mkdirs();
					}
					String[] files = from.list();
					for(String file : files) {
						File srcFile = new File(from, file);
						File destFile = new File(to, file);
						copyWorldFolder(srcFile, destFile);
					}
				} else {
					InputStream in = new FileInputStream(from);
					OutputStream out = new FileOutputStream(to);
					byte[] buffer = new byte[1024];
					int length;
					while ((length = in.read(buffer)) > 0) {
						out.write(buffer, 0, length);
					}
					in.close();
					out.close();
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
