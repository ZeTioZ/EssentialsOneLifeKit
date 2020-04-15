package fr.zetioz.essentialsonelifekit;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import fr.zetioz.essentialsonelifekit.utils.FilesManager;

public class EOLKMain extends JavaPlugin
{
	private Plugin plugin;
	private FilesManager filesManager;
	private EOLKCommand eolkCommand;
	private EOLKDeathHandler eolkEvent;
	
	@Override
	public void onEnable()
	{
		this.plugin = this;
		this.filesManager = new FilesManager(this);
		filesManager.createConfigsFile();
		filesManager.createMessagesFile();
		filesManager.createDatabaseFile();
		
		this.eolkCommand = new EOLKCommand(this);
		this.eolkEvent = new EOLKDeathHandler(this);
		
		registerEvents(this, eolkCommand, eolkEvent);
		getCommand("essentialsonelifekit").setExecutor(eolkCommand);
		
		filesManager.loadDatabase();
	}

	@Override
	public void onDisable()
	{
		this.plugin = null;
	}
	
	public Plugin getPlugin()
	{
		return this.plugin;
	}
	
	public FilesManager getFilesManager()
	{
		return this.filesManager;
	}
	
	private void registerEvents(Plugin plugin, Listener... listeners)
	{
		for(Listener listener : listeners)
		{
			Bukkit.getPluginManager().registerEvents(listener, plugin);
		}
	}

	public EOLKCommand getEOLKCommand()
	{
		return this.eolkCommand;
	}
	
	public EOLKDeathHandler getEOLKEvent()
	{
		return this.eolkEvent;
	}
}
