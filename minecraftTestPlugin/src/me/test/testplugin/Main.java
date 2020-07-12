package me.test.testplugin;

import java.text.MessageFormat;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
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
	
//	private Logger logger;
	@Override 
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);
//		this.logger = (Logger) this.getLogger();

	}
	
	@Override
	public void onDisable() {

		// unload all chunks
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if (!p.isOp()) {
				p.sendMessage("You do not have permission to use this command!");
				return true;
			}
				
		}
		
		if(label.equalsIgnoreCase("show")) {
			List<World> worlds = sender.getServer().getWorlds();
			String msg = "";
			boolean chunksLoaded = false;
			for (World w : worlds) { 
				chunksLoaded = false;
//				msg += "\n  " + w.getName() + " at:";
				for (Chunk c : w.getForceLoadedChunks()) { 
					msg += "\n    x: " +  String.valueOf(c.getX()) + ", z: "+  String.valueOf(c.getZ());
					chunksLoaded = true;
				}
				if (chunksLoaded)
					msg = "\n  " + w.getName() + " at:" + msg;
			}
			
			if (msg != "")
				sender.sendMessage("Chunk(s) force loaded in:" + msg);
			else 
				sender.sendMessage("No chunks are force loaded");
			
			return true;

		}
		
		if(label.equalsIgnoreCase("del")) {
			if (args.length != 2) {
				sender.sendMessage("Please give the x and z coordinate of the chunk.");
				return true;
			}
			Bukkit.getWorld("world").setChunkForceLoaded(Integer.parseInt(args[0]), Integer.parseInt(args[1]), false);
			sender.sendMessage((String) MessageFormat.format("Unloaded chunk at coordinates: x:{0}, z:{1}", args[0], args[1]));
			return true;
		}
		
		if(label.equalsIgnoreCase("delAll")) {
			for (Chunk c : Bukkit.getWorld("world").getForceLoadedChunks())
				Bukkit.getWorld("world").setChunkForceLoaded(c.getX(), c.getZ(), false);
			sender.sendMessage("No more chunks are force loaded");
			return true;
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
