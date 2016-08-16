package com.kealper.WhatWhitelist;

import java.util.List;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.server.ServerCommandEvent;

import com.kealper.WhatWhitelist.ChatListener;

public class WhatWhitelist extends JavaPlugin {

	public static Plugin plugin;
	public static String version;
	public static List<String> authors;
	private PluginDescriptionFile info;
	private ChatListener cl = new ChatListener();

	public void onEnable() {
		//~ info = this.getDescription();
		//~ version = info.getVersion();
		//~ authors = info.getAuthors();
		plugin = this;
		getServer().getPluginManager().registerEvents(cl, this);
	}

	public void onDisable() {

	}

}