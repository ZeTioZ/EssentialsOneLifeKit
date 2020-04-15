package fr.zetioz.essentialsonelifekit.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import fr.zetioz.essentialsonelifekit.EOLKMain;

public class FilesManager
{
	
	private EOLKMain main;
	private Plugin plugin;
	
	public FilesManager(EOLKMain main)
	{
		this.main = main;
		this.plugin = this.main.getPlugin();
	}
	
	//region Configs File (Creator/Getter)
    private YamlConfiguration configsFileConfig;
    
    public YamlConfiguration getConfigsFile()
    {
        return this.configsFileConfig;
    }

    public void createConfigsFile()
    {
    	File configsFile = new File(plugin.getDataFolder(), "configs.yml");
        if (!configsFile.exists())
        {
        	configsFile.getParentFile().mkdirs();
        	plugin.saveResource("configs.yml", false);
        }

        configsFileConfig = new YamlConfiguration();
        try
        {
        	configsFileConfig.load(configsFile);
        }
        catch (IOException | InvalidConfigurationException e)
        {
        	plugin.getLogger().severe("An error occured while loading the configs file!");
        	e.printStackTrace();
        }
    }
    
    //endregion
    
    //region Message File (Creator/Getter)
    private YamlConfiguration messagesFileConfig;

    public YamlConfiguration getMessagesFile()
    {
        return this.messagesFileConfig;
    }
    
	public void createMessagesFile()
	{
		File messagesFile = new File(plugin.getDataFolder(), "messages.yml");
		if(!messagesFile.exists())
		{
			messagesFile.getParentFile().mkdir();
			plugin.saveResource("messages.yml", false);
		}
		
		messagesFileConfig = new YamlConfiguration();
		try
	    {
			messagesFileConfig.load(messagesFile);
	    }
	    catch (IOException | InvalidConfigurationException e) {
	    	plugin.getLogger().severe("An error occured while loading the messages file!");
	    	e.printStackTrace();
	    }
	}
	//endregion

	//region Database File (Creator/Getter/Saver/Loader)
    private File databaseFile;
    private YamlConfiguration databaseFileConfig;
    
    //region Database Getter
    public YamlConfiguration getDatabaseFile()
    {
        return this.databaseFileConfig;
    }
    //endregion

    //region Database Creator
    public void createDatabaseFile()
    {
    	File databaseFolder = new File(plugin.getDataFolder() + File.separator + "database");
    	databaseFile = new File(databaseFolder, "eolk-list.yml");
        if (!databaseFile.exists())
        {
        	databaseFile.getParentFile().mkdirs();
        	try
        	{
				databaseFile.createNewFile();
			}
        	catch (IOException e)
        	{
				plugin.getLogger().severe("An error occured while creating the database file!");
			}
         }

        databaseFileConfig = new YamlConfiguration();
        try
        {
        	databaseFileConfig.load(databaseFile);
        }
        catch (IOException | InvalidConfigurationException e)
        {
        	plugin.getLogger().severe("An error occured while loading the database file!");
        }
    }
    //endregion
    
    //region Database Loader
  	public void loadDatabase()
  	{
  		List<String> playerUUID = !databaseFileConfig.getStringList("players-uuid").isEmpty() ? databaseFileConfig.getStringList("players-uuid") : new ArrayList<>();
  		main.getEOLKCommand().setPlayerUUID(playerUUID);
  		main.getEOLKEvent().setPlayerUUID(playerUUID);
  	}
  	//endregion
  	
  	//region Database Saver
  	public void saveDatabase()
  	{
  		this.databaseFileConfig.set("players-uuid", main.getEOLKCommand().getPlayerUUID());
  		try
    	{
			this.databaseFileConfig.save(this.databaseFile);
		}
    	catch (IOException e)
    	{
    		plugin.getLogger().severe("An error occured while saving the database!\nPlease contact the plugin creator!");
		}
  	}
  	//endregion
  	
    //endregion
	
}