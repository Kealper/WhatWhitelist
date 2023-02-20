package com.kealper.WhatWhitelist;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.server.ServerCommandEvent;

import net.milkbowl.vault.permission.Permission;

import com.kealper.WhatWhitelist.ChatListener;

public class WhatWhitelist extends JavaPlugin {

	public static Plugin plugin;
	public static String version;
	public static List<String> authors;
	public static Logger logger;
	public static Boolean whitelistingDisabled;
	private PluginDescriptionFile info;
	private final String pluginPrefix = ChatColor.GRAY + "[" + ChatColor.AQUA + "WhatWhitelist" + ChatColor.GRAY + "]" + ChatColor.RESET;
	private static Permission permission;
	private FileConfiguration config;
	private final ChatListener cl = new ChatListener();
	private Boolean permissionsEnabled = true;

	public void onEnable() {
		info = this.getDescription();
		version = info.getVersion();
		authors = info.getAuthors();
		plugin = this;
		logger = getLogger();
		getServer().getPluginManager().registerEvents(cl, this);
		saveDefaultConfig();
		setConfig();
		Plugin vault = this.getServer().getPluginManager().getPlugin("Vault");
		if (vault == null) {
			logger.severe("Vault was not found! Plugin will require op to use in-game commands.");
			permissionsEnabled = false;
		} else {
			permission = this.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class).getProvider();
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("whatwhitelist")) {
			if (args.length < 1) {
				sender.sendMessage(pluginPrefix + " " + ChatColor.GREEN + "Version " + version + " by " + authors);
				sender.sendMessage(ChatColor.GREEN + "Usage:");
				sender.sendMessage(ChatColor.YELLOW + "/whatwhitelist status" + ChatColor.GREEN + " - Shows the current status of the whitelist");
				sender.sendMessage(ChatColor.YELLOW + "/whatwhitelist toggle" + ChatColor.GREEN + " - Temporarily toggle the blocking of whitelisting");
				sender.sendMessage(ChatColor.YELLOW + "/whatwhitelist reload" + ChatColor.GREEN + " - Reloads the configuration");
				return true;
			} else {
				if (args[0].equalsIgnoreCase("reload")) {
					if (hasPermission(sender, "whatwhitelist.reload")) {
						reloadConfig();
						setConfig();
						sender.sendMessage(pluginPrefix + " " + ChatColor.GREEN + "WhatWhitelist configuration has been reloaded!");
					} else {
						sender.sendMessage(pluginPrefix + " " + ChatColor.RED + "You do not have permission to run this command!");
					}
					return true;
				}

				if (args[0].equalsIgnoreCase("status")) {
					if (hasPermission(sender, "whatwhitelist.status")) {
						if (this.getServer().hasWhitelist()) {
							sender.sendMessage(pluginPrefix + " " + ChatColor.YELLOW + "Whitelist status: " + ChatColor.GREEN + "Enabled");
						} else {
							sender.sendMessage(pluginPrefix + " " + ChatColor.YELLOW + "Whitelist status: " + ChatColor.RED + "Disabled");
						}
					} else {
						sender.sendMessage(pluginPrefix + " " + ChatColor.RED + "You do not have permission to run this command!");
					}
					return true;
				}

				if (args[0].equalsIgnoreCase("toggle")) {
					if (hasPermission(sender, "whatwhitelist.toggle")) {
						whitelistingDisabled = !whitelistingDisabled;
						if (!whitelistingDisabled) {
							sender.sendMessage(pluginPrefix + " " + ChatColor.YELLOW + "Whitelist toggling is now " + ChatColor.GREEN + "Enabled");
						} else {
							sender.sendMessage(pluginPrefix + " " + ChatColor.YELLOW + "Whitelist toggling is now " + ChatColor.RED + "Disabled");
							if (this.getServer().hasWhitelist()) {
								this.getServer().getScheduler().runTask(WhatWhitelist.plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "whitelist off"));
							}
						}
					} else {
						sender.sendMessage(pluginPrefix + " " + ChatColor.RED + "You do not have permission to run this command!");
					}
					return true;
				}
			}
		}
		return false;
	}

	public boolean hasPermission(CommandSender sender, String perm) {
		if (sender instanceof Player) {
			return hasPermission((Player) sender, perm);
		} else {
			return sender.isOp();
		}
	}

	public boolean hasPermission(Player player, String perm) {
		if (!permissionsEnabled) {
			if (player.isOp()) {
				return true;
			} else {
				return false;
			}
		}

		if (permission.has(player, perm)) {
			return true;
		}
		return false;
	}

	public void onDisable() {

	}

	private void setConfig() {
		config = getConfig();
		whitelistingDisabled = config.getBoolean("whitelistingDisabled");
	}

}