package me.test.testplugin;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	
	private Logger logger;
	@Override 
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);
		this.logger = (Logger) this.getLogger();

		// load all chunks from file
	}
	
	@Override
	public void onDisable() {
		// save loaded chunks to file
		// unload all chunks
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("logger")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				this.logger.info("logger it works!!! kinda?");
				
				World w = p.getWorld();
				
				
				for (StorageMinecart cart : w.getEntitiesByClass(StorageMinecart.class)) {
					Location cartloc = cart.getLocation();
					Chunk c = w.getChunkAt(cartloc);
					this.logger.info(cartloc.toString() + " -- loaded logger: " + c.isLoaded());
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
		
		if(label.equalsIgnoreCase("del")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				
				World w = p.getWorld();
				for (Chunk c : w.getForceLoadedChunks()) {
					p.sendMessage(c.toString());
					Bukkit.getWorld("world").setChunkForceLoaded(c.getX(), c.getZ(), false);
				}
				return true;
			}
		}
			
		return false;
		
		
	}
	
//	@EventHandler
//	public void onPlaceCart(VehicleCreateEvent event) {
//		
//		if(event.getVehicle().getName().equalsIgnoreCase("Minecart With Chest")) {
//			Chunk c = event.getVehicle().getLocation().getChunk();
//			Bukkit.broadcastMessage(c.toString());
//			
//			Bukkit.getWorld("world").setChunkForceLoaded(c.getX(), c.getZ(), true);
//			
//			// Load chunks around cart
//		}			
//	}
	
	@EventHandler
	public void onMoveCart(VehicleMoveEvent event) {
		if(event.getVehicle() instanceof StorageMinecart) {
			Chunk cFrom = Bukkit.getWorld("world").getChunkAt(event.getFrom());
			Chunk cTo   = Bukkit.getWorld("world").getChunkAt(event.getTo());
			Bukkit.getWorld("world").setChunkForceLoaded(cFrom.getX(), cFrom.getZ(), false);
			Bukkit.getWorld("world").setChunkForceLoaded(cTo.getX(), cTo.getZ(), true);
		}			
	}
	
	@EventHandler
	public void onBreakCart(VehicleDestroyEvent event) {
		if(event.getVehicle() instanceof StorageMinecart) {
			Chunk c = event.getVehicle().getLocation().getChunk();
			Bukkit.getWorld("world").setChunkForceLoaded(c.getX(), c.getZ(), false);
		}	
	}
}
