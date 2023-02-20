package com.kealper.WhatWhitelist;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;


import com.kealper.WhatWhitelist.WhatWhitelist;

public class ChatListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void handleCommand(PlayerCommandPreprocessEvent event) {
		if (event.getMessage().toLowerCase().startsWith("/whitelist on") || event.getMessage().toLowerCase().startsWith("whitelist on")) {
			if (!WhatWhitelist.whitelistingDisabled) {
				return;
			}
			WhatWhitelist.plugin.getServer().getScheduler().runTask(WhatWhitelist.plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "whitelist off"));
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void handleServerCommand(ServerCommandEvent event) {
		if (event.getCommand().toLowerCase().startsWith("/whitelist on") || event.getCommand().toLowerCase().startsWith("whitelist on")) {
			if (!WhatWhitelist.whitelistingDisabled) {
				return;
			}
			WhatWhitelist.plugin.getServer().getScheduler().runTask(WhatWhitelist.plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "whitelist off"));
		}
	}

}