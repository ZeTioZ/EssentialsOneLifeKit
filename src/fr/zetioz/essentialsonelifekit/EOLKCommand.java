package fr.zetioz.essentialsonelifekit;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class EOLKCommand implements Listener, CommandExecutor
{
	private EOLKMain main;
	private YamlConfiguration messagesFile;
	private YamlConfiguration configsFile;
	private String prefix;
	private List<String> playerUUID;
	
	public EOLKCommand(EOLKMain main)
	{
		this.main = main;
		this.messagesFile = main.getFilesManager().getMessagesFile();
		this.configsFile = main.getFilesManager().getConfigsFile();
		this.prefix = ChatColor.translateAlternateColorCodes('&', messagesFile.getString("prefix"));
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
	
	//region Sub command processor
	@SuppressWarnings("unchecked")
	@EventHandler
	private void onFWarpCommand(PlayerCommandPreprocessEvent e)
	{
		String[] args = e.getMessage().split(" ");
		if(args.length == 2 && configsFile.getList("kits-commands").contains(args[0]))
		{
			List<String> lowerKitsList = new ArrayList<>();
			for(String kit : (List<String>) configsFile.getList("kits-list"))
			{
				lowerKitsList.add(kit.toLowerCase());
			}
			if(lowerKitsList.contains(args[1].toLowerCase()))
			{
				if(playerUUID.contains(e.getPlayer().getUniqueId().toString()) && !e.getPlayer().hasPermission("eolk.bypass"))
				{
					e.setCancelled(true);
					for(String line : messagesFile.getStringList("errors.already-got-a-kit"))
					{
						line = ChatColor.translateAlternateColorCodes('&', line);
						e.getPlayer().sendMessage(prefix + line);
					}
				}
				else
				{
					if(!e.getPlayer().hasPermission("eolk.bypass"))
					{						
						playerUUID.add(e.getPlayer().getUniqueId().toString());
						main.getEOLKEvent().setPlayerUUID(playerUUID);
						main.getFilesManager().saveDatabase();
						for(String line : messagesFile.getStringList("got-a-kit"))
						{
							line = line.replace("{kit}", args[1]);
							line = ChatColor.translateAlternateColorCodes('&', line);
							e.getPlayer().sendMessage(prefix + line);
						}
					}
				}
			}
		}
	}
	//endregion
		
	@SuppressWarnings("deprecation")
	//region Main Plugin Command
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args)
	{
		if(cmd.getName().equalsIgnoreCase("essentialsonelifekit"))
		{
			if(args.length == 0)
			{
				sendHelpPage(sender);
			}
			else if(args.length == 1)
			{
				if(args[0].equalsIgnoreCase("help"))
				{
					sendHelpPage(sender);
				}
				else if(args[0].equalsIgnoreCase("reload"))
				{
					if(sender.hasPermission("eolk.reload"))
					{
						Bukkit.getPluginManager().disablePlugin(main);
						Bukkit.getPluginManager().enablePlugin(main);
						for(String line : messagesFile.getStringList("reload-command"))
						{
							line = ChatColor.translateAlternateColorCodes('&', line);
							sender.sendMessage(prefix + line);
						}
					}
					else
					{
						for(String line : messagesFile.getStringList("errors.not-enough-permission"))
						{
							line = ChatColor.translateAlternateColorCodes('&', line);
							sender.sendMessage(prefix + line);
						}
					}
				}
				else
				{
					sendHelpPage(sender);
				}
			}
			else if(args.length == 2)
			{
				if(args[0].equalsIgnoreCase("add"))
				{
					if(sender.hasPermission("eolk.add"))
					{
						//Add a player to the uuid list
						OfflinePlayer playerToCheck = Bukkit.getOfflinePlayer(args[1]);
						if(!playerUUID.contains(playerToCheck.getUniqueId().toString()))
						{
							playerUUID.add(playerToCheck.getUniqueId().toString());
							main.getEOLKEvent().setPlayerUUID(playerUUID);
							main.getFilesManager().saveDatabase();
							for(String line : messagesFile.getStringList("player-added"))
							{
								line = line.replace("{player}", args[1]);
								line = ChatColor.translateAlternateColorCodes('&', line);
								sender.sendMessage(prefix + line);
							}
						}
						else
						{
							for(String line : messagesFile.getStringList("errors.already-in-the-list"))
							{
								line = line.replace("{player}", args[1]);
								line = ChatColor.translateAlternateColorCodes('&', line);
								sender.sendMessage(prefix + line);
							}
						}
					}
					else
					{
						for(String line : messagesFile.getStringList("errors.not-enough-permission"))
						{
							line = ChatColor.translateAlternateColorCodes('&', line);
							sender.sendMessage(prefix + line);
						}
					}
				}
				else if(args[0].equalsIgnoreCase("remove"))
				{
					if(sender.hasPermission("eolk.remove"))
					{
						OfflinePlayer playerToCheck = Bukkit.getOfflinePlayer(args[1]);
						if(playerUUID.contains(playerToCheck.getUniqueId().toString()))
						{
							playerUUID.remove(playerToCheck.getUniqueId().toString());
							main.getEOLKEvent().setPlayerUUID(playerUUID);
							main.getFilesManager().saveDatabase();
							for(String line : messagesFile.getStringList("player-removed"))
							{
								line = line.replace("{player}", args[1]);
								line = ChatColor.translateAlternateColorCodes('&', line);
								sender.sendMessage(prefix + line);
							}
						}
						else
						{
							for(String line : messagesFile.getStringList("errors.not-in-the-list"))
							{
								line = line.replace("{player}", args[1]);
								line = ChatColor.translateAlternateColorCodes('&', line);
								sender.sendMessage(prefix + line);
							}
						}
					}
					else
					{
						for(String line : messagesFile.getStringList("errors.not-enough-permission"))
						{
							line = ChatColor.translateAlternateColorCodes('&', line);
							sender.sendMessage(prefix + line);
						}
					}
				}
				else
				{
					sendHelpPage(sender);
				}
			}
			else
			{
				sendHelpPage(sender);
			}
		}
		return false;
	}
	//endregion

	private void sendHelpPage(CommandSender sender)
	{
		if(sender.hasPermission("eolk.help"))
		{
			for(String line : messagesFile.getStringList("help-command"))
			{
				line = ChatColor.translateAlternateColorCodes('&', line);
				sender.sendMessage(prefix + line);
			}
		}
		else
		{
			for(String line : messagesFile.getStringList("errors.not-enough-permission"))
			{
				line = ChatColor.translateAlternateColorCodes('&', line);
				sender.sendMessage(prefix + line);
			}
		}
	}
}
