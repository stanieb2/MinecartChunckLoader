package me.test.testplugin;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleCreateEvent;

public class Main extends JavaPlugin implements Listener {
	
	@Override 
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);
		// load all chunks from file
	}
	
	@Override
	public void onDisable() {
		// save loaded chunks to file
		// unload all chunks
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("test")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				p.sendMessage("it works!!! kinda?");
				
				World w = p.getWorld();
				
				for (StorageMinecart cart : w.getEntitiesByClass(StorageMinecart.class)) {
					Location cartloc = cart.getLocation();
					Chunk c = w.getChunkAt(cartloc);
					p.sendMessage(cartloc.toString() + " -- loaded: " + c.isLoaded());
				}
				
				return true;
			}
		}
		
		if(label.equalsIgnoreCase("show")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				
				World w = p.getWorld();
				for (Chunk c : w.getForceLoadedChunks()) {
					p.sendMessage(c.toString());
				}
				
				
				return true;
			}
		}
			
		return false;
		
		
	}
	
	@EventHandler
	public void onPlaceCart(VehicleCreateEvent event) {
		
		if(event.getVehicle().getName().equalsIgnoreCase("Minecart With Chest")) {
			Chunk c = event.getVehicle().getLocation().getChunk();
			Bukkit.broadcastMessage(c.toString());
			
			Bukkit.getWorld("world").setChunkForceLoaded(c.getX(), c.getZ(), true);
			
			// Load chunks around cart
		}			
	}
	
	public void onBreakCart() {
		// Unload carts around cart
	}
	
	public void onCartMove() {
		// load new chunks when cart enters new chunk
		// unload old chunks
	}
}
