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
import org.bukkit.event.vehicle.VehicleCreateEvent;
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
//		Player p = (Player) sender;
//		p.sendMessage(label);
		if (label.equalsIgnoreCase("test")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				p.sendMessage("it works!!! kinda? of niet?");
				
				World w = p.getWorld();
				
				
				for (StorageMinecart cart : w.getEntitiesByClass(StorageMinecart.class)) {
					Location cartloc = cart.getLocation();
					Chunk c = w.getChunkAt(cartloc);
					p.sendMessage(cartloc.toString() + " -- loaded: " + c.isLoaded());
				}
				
				return true;
			}
		}
		
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
	
	@EventHandler
	public void onPlaceCart(VehicleCreateEvent event) {
		
		if(event.getVehicle().getName().equalsIgnoreCase("Minecart With Chest")) {
			Chunk c = event.getVehicle().getLocation().getChunk();
			Bukkit.broadcastMessage(c.toString());
			
			Bukkit.getWorld("world").setChunkForceLoaded(c.getX(), c.getZ(), true);
			
			// Load chunks around cart
		}			
	}
	
//	@EventHandler
//	public void onPlaceCart( event) {
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
			Chunk c = event.getVehicle().getLocation().getChunk();
//			Bukkit.broadcastMessage(c.toString());
			
			Bukkit.getWorld("world").setChunkForceLoaded(c.getX(), c.getZ(), true);
			for (int x=c.getX()-1; x == c.getX()+1; x++) {
				for (int z=c.getZ()-1; z == c.getZ()+1; z++) {
					if (x != c.getX() && z != c.getZ())
						Bukkit.getWorld("world").setChunkForceLoaded(x, z, false);
				}
			}
//			if (Bukkit.getWorld("world").getForceLoadedChunks().contains(Bukkit.getWorld("world").getChunkAt(c.getX()-1, c.getZ()))) {
//				Bukkit.getWorld("world").setChunkForceLoaded(c.getX() -1, c.getZ(), false);
//			}
//			if (Bukkit.getWorld("world").getForceLoadedChunks().contains(Bukkit.getWorld("world").getChunkAt(c.getX(), c.getZ()-1))) {
//				Bukkit.getWorld("world").setChunkForceLoaded(c.getX(), c.getZ()-1, false);
//			}
//	
			
			
			// Load chunks around cart
		}			
	}
	
	@EventHandler
	public void onBreakCart(VehicleDestroyEvent event) {
		if(event.getVehicle() instanceof StorageMinecart) {
			Chunk c = event.getVehicle().getLocation().getChunk();
			Bukkit.broadcastMessage(c.toString());
			
			Bukkit.getWorld("world").setChunkForceLoaded(c.getX(), c.getZ(), false);
			
			// Load chunks around cart
		}	
	}
	
//	@EventHandler
//	public void onUpdateCart(VehicleUpdateEvent event) {
//		if(event.getVehicle() instanceof StorageMinecart) {
//			Chunk c = event.getVehicle().getLocation().getChunk();
//			Bukkit.broadcastMessage(c.toString());
//			
////			Bukkit.getWorld("world").setChunkForceLoaded(c.getX(), c.getZ(), false);
//			
//			// Load chunks around cart
//		}	
//	}
	
	
//	@EventHandler
//	public void onStopCart(VehicleBlockCollisionEvent event) {
//		if(event.getVehicle() instanceof StorageMinecart) {
//			Chunk c = event.getVehicle().getLocation().getChunk();
//			Bukkit.broadcastMessage(c.toString());
//			
//			Bukkit.getWorld("world").setChunkForceLoaded(c.getX(), c.getZ(), false);
//			
//			// Load chunks around cart
//		}
//
//	}
}
