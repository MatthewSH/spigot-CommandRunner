package com.matthewhatcher.commandrunner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class CR extends JavaPlugin {
	private ArrayList<String> commands;
	private HashMap<String, String> placeholders;
	
	@Override
	public void onEnable() {
		this.makeConfig();
		
		if(this.getConfig().getBoolean("use-placeholders")) {
			this.getLogger().info("Loading placeholders.");
			if(!this.loadPlaceholders()) {
				this.getLogger().severe("Something happened when loading the placeholders. Disabling...");
				this.disable();
			}
		}
		
		if(this.loadCommands()) {
			if(this.getConfig().getBoolean("use-placeholders"))
				this.makeReplacements();
			
			this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

				@Override
				public void run() {
					CR.this.runCommands();					
				}
				
			}, 20 * 5);
		}
		
		super.onEnable();
	}
	
	public void onDisable() {
		super.onDisable();
	}
	
	private void makeConfig() {
		try {
			if(!this.getDataFolder().exists())
				this.getDataFolder().mkdirs();
			
			File configFile = new File(this.getDataFolder(), "config.yml");
			File commandFile = new File(this.getDataFolder(), "commands");
			
			if(!configFile.exists())
				this.saveDefaultConfig();
			
			if(!commandFile.exists())
				commandFile.createNewFile();
			
		} catch (Exception e) {
			this.getLogger().severe("Can not read/write to the data folder. Shutting down...");
			this.disable();
			e.printStackTrace();
			
		}
	}
	
	private boolean loadPlaceholders() {
		this.placeholders = new HashMap<String, String>();
		
		for(String item : this.getConfig().getStringList("placeholders")) {
			if(!item.contains(":")) {
				this.placeholders.clear();
				return false;
			}
			
			String items[] = item.split(":", 2);
			
			this.placeholders.put(items[0].trim(), items[1].trim());
		}
		
		return true;
	}
	
	private boolean loadCommands() {
		this.commands = new ArrayList<String>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(new File(this.getDataFolder(), "commands")))) {
			String line;
			
			while((line = br.readLine()) != null) {
				this.commands.add(line);
			}
			
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			this.disable();
			
			return false;
		}
	}
	
	private void runCommands() {
		int threshold = this.getConfig().getInt("threshold");
		CommandSender sender = this.setSender();
		
		if(this.commands.size() > 0) {
			for(String command : this.commands) {
				for(int i = 0; i < threshold; i++) {
					if(this.getServer().dispatchCommand(sender, command)) {
						break;
					}
					
					if(i == threshold) {
						this.getLogger().severe("The following command has hit the threshold of attempts:");
						this.getLogger().severe(command);
					}
				}
			}
			
			if(this.getConfig().getBoolean("clear-on-finish")) {
				this.getLogger().info("Clearing out the commands file since I'm done running the commands.");
				this.clearCommandsFile();
			}
			
			this.getLogger().info("I'm all finished. I'm going to shutdown now! See you next time!");
			this.disable();
		} else {
			this.getLogger().info("No commands to run. See you next time!");
			this.disable();
		}
	}
	
	private CommandSender setSender() {
		CommandSender sender = this.getServer().getConsoleSender();
		String dispatchAs = this.getConfig().getString("dispatch-as");
		
		if(!dispatchAs.equalsIgnoreCase("console")) {
			if(this.getServer().getPlayer(dispatchAs) != null) {
				sender = (CommandSender)this.getServer().getPlayer(dispatchAs);
			}
		}
		
		return sender;
	}
	
	private boolean makeReplacements() {
		if(this.placeholders.size() > 0) {
			for(String placeholder : this.placeholders.keySet()) {
				String replaceWith = this.placeholders.get(placeholder);
				for(String command : this.commands) {
					if(command.contains(placeholder)) {
						this.commands.set(this.commands.indexOf(command), command.replaceAll(placeholder, replaceWith));
					}
				}
			}
			
			return true;
		}
		
		return true;
	}
	
	private void clearCommandsFile() {
		File file = new File(this.getDataFolder(), "commands");
		
		if(file.delete()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void disable() {
		this.getPluginLoader().disablePlugin(this);
	}
}
