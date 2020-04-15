package fr.zetioz.essentialsonelifekit;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class EOLKDeathHandler implements Listener
{
	private EOLKMain main;
	private List<String> playerUUID;
	
	public EOLKDeathHandler(EOLKMain main)
	{
		this.main = main;
		this.playerUUID = new ArrayList<>();
	}
	
	public List<String> getPlayerUUID()
	{
		return this.playerUUID;
	}
	
	public void setPlayerUUID(List<String> playerUUID)
	{
		this.playerUUID = playerUUID;
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e)
	{
		if(playerUUID.contains(e.getEntity().getUniqueId().toString()))
		{
			playerUUID.remove(e.getEntity().getUniqueId().toString());
			main.getEOLKCommand().setPlayerUUID(playerUUID);
			main.getFilesManager().saveDatabase();
		}
	}
}
