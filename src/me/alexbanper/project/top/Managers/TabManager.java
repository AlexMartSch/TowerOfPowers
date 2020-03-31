package me.alexbanper.project.top.Managers;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.alexbanper.project.top.TowerPowerMain;
import me.alexbanper.project.top.Logic.GameLogic;
import pl.kacperduras.protocoltab.ProtocolTabAPI;
import pl.kacperduras.protocoltab.manager.ProtocolTab;

public class TabManager {
	
	public static TabManager tm = new TabManager();
	
	public static TabManager getManager(){
		return tm;
	}
	
	private final ThreadLocal<SimpleDateFormat> dateFormat = ThreadLocal.withInitial(
			() -> new SimpleDateFormat("HH:mm:ss a"));

	
	
	public void setTabList()
	{
		Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(TowerPowerMain.instance, new Runnable() {
			@Override
			public void run() {

				for(Player online : Bukkit.getOnlinePlayers())
				{

					if(GameManager.getManager().ifTeamSpectatorContains(online.getName()))
					{
						for (int i = 0; i < 80; i++) {
							ProtocolTabAPI.getTablist(online).setSlot(i, ProtocolTab.BLANK_TEXT);
						}

						ProtocolTabAPI.getTablist(online).setHeader("&6&lTower Of Powers &b&lv1.0");
						ProtocolTabAPI.getTablist(online).setSlot(0 ,"&bSpectators: &7"+GameManager.getManager().getTeamSpectatorSize());

						int slot = 1;
						for (String key : GameManager.getManager().TeamSpectatorKeySet()) {
							Player player = Bukkit.getPlayer(key);

							ProtocolTabAPI.getTablist(online).setSlot(slot, player.getDisplayName());
							slot += 1;
						}

						int slot1 = 41;
						ProtocolTabAPI.getTablist(online).setSlot(40 ,"&9Blue: &7"+GameManager.getManager().getTeamBlueSize());
						for(String playerName : GameManager.getManager().TeamBlueKeySet())
						{
							Player player = Bukkit.getPlayer(playerName);
							ProtocolTabAPI.getTablist(online).setSlot(slot1 ,player.getDisplayName());
							slot1 += 1;
						}

						int slot2 = 61;
						ProtocolTabAPI.getTablist(online).setSlot(60 ,"&4Red: &7"+GameManager.getManager().getTeamRedSize());
						for(String playerName : GameManager.getManager().TeamRedKeySet())
						{

							Player player = Bukkit.getPlayer(playerName);
							ProtocolTabAPI.getTablist(online).setSlot(slot2 ,player.getDisplayName());
							slot2 += 1;
						}
						if(GameManager.getManager().getGameStart() == true)
						{
							ProtocolTabAPI.getTablist(online).setFooter("§bUse §7/join <Team> §bfor join the match!");
						}else{
							ProtocolTabAPI.getTablist(online).setFooter("§aWaiting for players... §eHour: §6" + dateFormat.get().format(
									new Date(System.currentTimeMillis())));
						}

					}

					if(GameManager.getManager().ifTeamBlueContains(online.getName()) || GameManager.getManager().ifTeamRedContains(online.getName()))
					{
						for (int i = 0; i < 80; i++) {
							ProtocolTabAPI.getTablist(online).setSlot(i, ProtocolTab.BLANK_TEXT);
						}
						int slot = 1;

						ProtocolTabAPI.getTablist(online).setHeader("&6&lTower Of Powers &b&lv1.0 \n &bYou are on: &7"+GameManager.getManager().getMyTeam(online.getName() + " team"));
						ProtocolTabAPI.getTablist(online).setSlot(0 ,"&9Blue: &7"+GameManager.getManager().getTeamBlueSize());
						for(String playerName : GameManager.getManager().TeamBlueKeySet())
						{

							Player player = Bukkit.getPlayer(playerName);
							ProtocolTabAPI.getTablist(online).setSlot(slot ,player.getDisplayName());
							slot += 1;
						}

						int slot2 = 41;
						ProtocolTabAPI.getTablist(online).setSlot(40 ,"&4Red: &7"+GameManager.getManager().getTeamRedSize());
						for(String playerName : GameManager.getManager().TeamRedKeySet())
						{

							Player player = Bukkit.getPlayer(playerName);
							ProtocolTabAPI.getTablist(online).setSlot(slot2 ,player.getDisplayName());
							slot2 += 1;
						}
						if((!GameManager.getManager().getGameStart()) && (GameManager.getManager().getIfStartGameCountdown() == true))
						{
							int count = GameLogic.counter;
							int realcount = count+1;
							if(realcount >= 2)
							{
								ProtocolTabAPI.getTablist(online).setFooter("§aLa Partida comienza en §6"+realcount+" §asegundos!");
							}

							if(realcount == 1)
							{
								ProtocolTabAPI.getTablist(online).setFooter("§aLa Partida comienza en §6"+realcount+" §asegundo!");
							}
						}else{
							if((GameManager.getManager().getGameStart()) && (GameManager.getManager().getIfStartGameCountdown() == false))
							{
								ProtocolTabAPI.getTablist(online).setFooter("§aMatch start soon...");
							}else{
								if(GameManager.getManager().getGameStart() && GameManager.getManager().getIfStartGameCountdown())
								{
									ProtocolTabAPI.getTablist(online).setFooter("§a&l¡In Game! &7&lMap Name: &e"+online.getWorld().getName());
								}else{
									ProtocolTabAPI.getTablist(online).setFooter("§aWaiting players for start match...");
								}

							}
						}
					}

					ProtocolTabAPI.getTablist(online).update();
				}
			}
		}, 20L, 20L);
	}
}
